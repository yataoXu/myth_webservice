package com.zdmoney.service.impl;

import com.alibaba.fastjson.JSON;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectCollectResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectOrderItemDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.BusinessOperation;
import com.zdmoney.common.ResultDto;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.enm.OrderGenerateType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.helper.SerialNumberGenerator;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTenderDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTenderResultDto;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.match.dto.MatchDetail;
import com.zdmoney.match.dto.ResourceMatchResult;
import com.zdmoney.models.OperStateRecord;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.service.*;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.vo.trade.TenderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by user on 2019/3/4.
 */
@Service
public class MatchResultServiceImpl implements MatchResultService {

    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchResultServiceImpl.class);

    @Autowired
    private BusiProductService busiProductService;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @Autowired
    private IOperStateRecordService operStateRecordService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiProductContractService productContractService;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private TradeService tradeService;

    public void processMatchResult(List<ResourceMatchResult> list, boolean ensureProcessedOnce) {
        LOGGER.info("接收到匹配结果：{}", JSON.toJSONString(list));
        //查询 子产品、标的信息、循环 创建子订单并落地 投标 满标通知
        for(ResourceMatchResult matchResult:list){
            this.processMatchResult(matchResult, ensureProcessedOnce);
        }

    }

    @Override
    public void processMatchResult(ResourceMatchResult matchResult, boolean ensureProcessedOnce){
        String subjectNo = matchResult.getResourcePartId();
        if(ensureProcessedOnce && !ensureNotProcessed(subjectNo)) return;
        try {
            BusiProductSub productSub = getProductSub(subjectNo);
            BusiProductContract subjectInfo = productContractService.selectBySubjectNo(subjectNo);
            CustomerMainInfo debtor = getDebtor(subjectInfo);
            List<OrderInfoWrapper> orderSubs = createOrders(matchResult.getMatchPartResults(), productSub, debtor, subjectInfo);
            saveOrders(orderSubs);
            List<ResultDto<BusiOrderSub>> tenderResults = tender(orderSubs);
            handleTenderResult(tenderResults, productSub, subjectInfo);
        }catch (Exception e){
            LOGGER.error("标的"+subjectNo+"处理撮合结果发生异常："+e.getMessage(), e);
            MailUtil.sendMail("标的"+subjectNo+"处理撮合结果发生异常", e.getMessage());
        }
    }

    private boolean ensureNotProcessed(String subjectNo){
        boolean notProcessed = false;
        OperStateRecord record = new OperStateRecord();
        record.setKeyword(subjectNo);
        record.setOperType(BusinessOperation.MATCHING_RESULT_CALLBACK.getOperType());
        try{
            operStateRecordService.save(record);
            notProcessed = true;
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        return notProcessed;
    }

    private BusiProductSub getProductSub(String subjectNo){
        return busiProductService.findProductSubBySubjectNo(subjectNo);
    }

    private CustomerMainInfo getDebtor(BusiProductContract productContract){
        return customerMainInfoService.findOneByCmNumber(productContract.getCmNumber());
    }

    private List<OrderInfoWrapper> createOrders(List<MatchDetail> matchParts, BusiProductSub productSub,
                                                CustomerMainInfo debtor, BusiProductContract subjectInfo){
        List<OrderInfoWrapper> orders = new ArrayList<>(matchParts.size());
        for(MatchDetail matchPart : matchParts){
            orders.add(createOrderWrapper(matchPart, productSub, debtor, subjectInfo));
        }
        return orders;
    }

    private OrderInfoWrapper createOrderWrapper(MatchDetail matchResult, BusiProductSub productSub,
                                                                    CustomerMainInfo debtor, BusiProductContract subjectInfo){
        String orderId = matchResult.getMatchPartId();
        BusiOrder busiOrder = busiOrderService.selectByOrderNo(orderId);
        CustomerMainInfo customerInfo = customerMainInfoService.findOne(busiOrder.getCustomerId());
        BusiOrderSub orderSub = buildOrder(matchResult, productSub, debtor, subjectInfo, busiOrder, customerInfo);
        return new OrderInfoWrapper(orderSub, productSub, customerInfo, subjectInfo);
    }

    private BusiOrderSub buildOrder(MatchDetail matchResult, BusiProductSub productSub,
                                    CustomerMainInfo debtor, BusiProductContract subjectInfo,
                                    BusiOrder busiOrder, CustomerMainInfo customerInfo){
        String orderNum = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.SUB_ORDER,customerInfo.getId());
        Date now = new Date();
        BusiOrderSub orderSub = new BusiOrderSub();
        BeanUtils.copyProperties(busiOrder, orderSub);
        orderSub.setPaySerNum(null);
        orderSub.setDebtorNum(debtor.getCmNumber());
        orderSub.setDebtorName(debtor.getCmRealName());
        orderSub.setBorrowerDate(subjectInfo.getLoanDate());
        orderSub.setSubjectNo(subjectInfo.getSubjectNo());

        orderSub.setParentId(busiOrder.getId());
        orderSub.setParentNo(busiOrder.getOrderId());
        orderSub.setPlanId(busiOrder.getProductId());
        orderSub.setProductName(productSub.getProductName());
        orderSub.setProductId(productSub.getId());
        orderSub.setProductType(AppConstants.OrderProductType.FINANCE_PLAN_SUB);

        orderSub.setLoginId(customerInfo.getFuiouLoginId());
        orderSub.setCmNumber(customerInfo.getCmNumber());

        orderSub.setOrderId(orderNum);
        orderSub.setOrderAmt(matchResult.getMatchDetailValue());
        orderSub.setCashAmount(matchResult.getMatchDetailValue());
        BigDecimal orderInterest = calculateOrderInterest(matchResult.getMatchDetailValue(), productSub);
        orderSub.setPrincipalinterest(matchResult.getMatchDetailValue().add(orderInterest));
        orderSub.setYearRate(productSub.getYearRate());
        orderSub.setYearRateStr(productSub.getYearRate().toString());
        orderSub.setOrderTime(now);
        orderSub.setConfirmPaymentDate(now);
        orderSub.setInterestStartDate(productSub.getInterestStartDate());
        orderSub.setInterestEndDate(productSub.getInterestEndDate());
        orderSub.setIntegralAmount(BigDecimal.ZERO);
        orderSub.setCouponAmount(BigDecimal.ZERO);
        orderSub.setRaiseRateIncome(BigDecimal.ZERO);
        orderSub.setRaiseDays(0l);
        orderSub.setDebtType(AppConstants.FinancePlan.DEBT_TYPE1);
        orderSub.setPayType(productSub.getRepayType());
        orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
        orderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
        return orderSub;
    }

    private BigDecimal calculateOrderInterest(BigDecimal orderAmt, BusiProductSub productSub){
        BigDecimal interest = orderAmt.multiply(productSub.getProductInterest()).divide(productSub.getProductPrincipal(), 2, BigDecimal.ROUND_DOWN);
        return interest;
    }


    private void saveOrders(List<OrderInfoWrapper> orders){
        try {
            List<BusiOrderSub> records = new ArrayList<>(orders.size());
            for (OrderInfoWrapper orderInfoWrapper : orders) {
                records.add(orderInfoWrapper.getOrder());
            }
            busiOrderService.saveSubOrders(records);
        }catch (Exception e){
            throw new BusinessException("报错子单失败："+e.getMessage());
        }
    }

    private List<ResultDto<BusiOrderSub>> tender(List<OrderInfoWrapper> orders){
        List<ResultDto<BusiOrderSub>> results = new ArrayList<>();
        for(OrderInfoWrapper orderinfo : orders) {
            results.add(doTender(orderinfo.getOrder(), orderinfo.getCustomerInfo(), orderinfo.getSubjectInfo()));
        }
        return results;
    }

    public ResultDto<BusiOrderSub> doTender(BusiOrderSub orderSub, CustomerMainInfo customer, BusiProductContract subjectInfo){
        ResultDto<BusiOrderSub> resultDto = new ResultDto<>();
        TenderVo tenderVo = new TenderVo();
        tenderVo.setCustomerMainInfo(customer);
        tenderVo.setBidAmount(subjectInfo.getCollectAmount());
        try {
            // 投标参数拼接
            BorrowTenderDto borrowTenderDto = tradeService.commonTenderDto(tenderVo, orderSub);
            // 调用华瑞投标
            BorrowTenderResultDto tenderResultDto = tradeService.commonBidTender(borrowTenderDto);
            if (tenderResultDto != null) {
                // 得到华润付款流水
                orderSub.setPaySerNum(tenderResultDto.getCashSerialNo());
                updateOrder(orderSub, AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
                resultDto.setData(orderSub);
            }else{
                resultDto = new ResultDto<>("投标发生异常",false);
            }
        }catch (Exception e){
            String msg = String.format("撮合子单【%s】落地后,投标或更新子单发生问题：%s", orderSub.getOrderId(),e.getMessage());
            LOGGER.error(msg, e);
            resultDto = new ResultDto<>(msg,false);
        }
        return resultDto;
    }

    private void handleTenderResult(List<ResultDto<BusiOrderSub>> tenderResults, BusiProductSub productSub, BusiProductContract subjectInfo){
        updateProductIfNecessary(tenderResults, productSub);
        sendGoalReachedNoticeIfNecessary(tenderResults, productSub, subjectInfo);
        sendAlertEmailIfNecessary(tenderResults);
    }

    private void sendAlertEmailIfNecessary(List<ResultDto<BusiOrderSub>> tenderResults){
        StringBuilder errorMsg = new StringBuilder();
        for (ResultDto<BusiOrderSub> resultDto : tenderResults) {
            if (!resultDto.isSuccess()) errorMsg.append(resultDto.getMsg()).append("\r\n");
        }
        if(errorMsg.length() > 0) MailUtil.sendMail("撮合后进行子单投标发生问题", errorMsg.toString());
    }

    private void sendGoalReachedNoticeIfNecessary(List<ResultDto<BusiOrderSub>> tenderResults, BusiProductSub productSub, BusiProductContract subjectInfo){
        try {
            int actualCollectDay = DateUtil.getDaysBetweenTwoDate(productSub.getSaleStartDate(), productSub.getSaleEndDate()) + 1;
            SubjectCollectResultDto collectResultDto =
                    new SubjectCollectResultDto(AppConstants.PARTNER_NO, subjectInfo.getSubjectNo(), productSub.getProductPrincipal(), tenderResults.size(), actualCollectDay);
            boolean loanAmountReached = true;
            for (ResultDto<BusiOrderSub> tenderResult : tenderResults) {
                if (!tenderResult.isSuccess()) {
                    loanAmountReached = false;
                    break;
                }
                BusiOrderSub orderSub = tenderResult.getData();
                collectResultDto.addOrderItem(new SubjectOrderItemDto(orderSub.getOrderId(), orderSub.getOrderAmt()));
            }
            if (!loanAmountReached) return;
            collectResultDto.setProductNo(subjectInfo.getSubjectNo());
            collectResultDto.setProductName(productSub.getProductName());
            collectResultDto.setProductInterest(productSub.getProductInterest());//产品总利息
            AssetsResultDto<String> resultDto = lcbSubjectFacadeService.collectResultNotify(collectResultDto);
            if (resultDto == null || !resultDto.isSuccess()) {
                MailUtil.sendMail("满标通知失败",
                        resultDto == null ? subjectInfo.getSubjectNo() + "调用标的失败" : subjectInfo.getSubjectNo() + "通知标的发生异常：" + resultDto.getMsg());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            MailUtil.sendMail("挖财撮合后尝试通知标的满标报错", subjectInfo.getSubjectNo()+":"+e.getMessage());
        }
    }

    private void updateOrder(BusiOrderSub orderSub, String status){
        LOGGER.info("投标后更新子单信息【{}】：status->{},pay_ser_num->{}", orderSub.getOrderId(), status, orderSub.getPaySerNum());
        BusiOrderSub template = new BusiOrderSub();
        template.setOrderId(orderSub.getOrderId());
        template.setPaySerNum(orderSub.getPaySerNum());
        template.setStatus(status);
        busiOrderSubMapper.updateByPrimaryKeySelectiveByOrderNum(template);
    }

    private void updateProductIfNecessary(List<ResultDto<BusiOrderSub>> tenderResults, BusiProductSub productSub){
        BigDecimal totalInvestAmt = BigDecimal.ZERO;
        int totalInvestLenderCount = 0;
        for(ResultDto<BusiOrderSub> tenderResult : tenderResults){
            if(tenderResult.isSuccess()){
                BusiOrderSub orderSub = tenderResult.getData();
                totalInvestAmt = totalInvestAmt.add(orderSub.getOrderAmt());
                totalInvestLenderCount++;
            }
        }
        if(totalInvestLenderCount == 0) return;
        BusiProductSub productTemplate = new BusiProductSub();
        productTemplate.setId(productSub.getId());
        productTemplate.setTotalInvestAmt(totalInvestAmt);
        productTemplate.setTotalInvestPerson(totalInvestLenderCount);
        try {
            busiProductService.updateSubProduct(productTemplate);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            MailUtil.sendMail("挖财撮合后更新子产品报错", productSub.getId()+":"+e.getMessage());
        }
    }

    static class OrderInfoWrapper{
        BusiOrderSub order;
        BusiProductSub productSub;
        CustomerMainInfo customerInfo;
        BusiProductContract subjectInfo;

        OrderInfoWrapper(){}

        OrderInfoWrapper(BusiOrderSub order, BusiProductSub productSub, CustomerMainInfo customerInfo, BusiProductContract subjectInfo) {
            this.order = order;
            this.productSub = productSub;
            this.customerInfo = customerInfo;
            this.subjectInfo = subjectInfo;
        }

        public BusiOrderSub getOrder() {
            return order;
        }

        public void setOrder(BusiOrderSub order) {
            this.order = order;
        }

        public BusiProductSub getProductSub() {
            return productSub;
        }

        public void setProductSub(BusiProductSub productSub) {
            this.productSub = productSub;
        }

        public CustomerMainInfo getCustomerInfo() {
            return customerInfo;
        }

        public void setCustomerInfo(CustomerMainInfo customerInfo) {
            this.customerInfo = customerInfo;
        }

        public BusiProductContract getSubjectInfo() {
            return subjectInfo;
        }

        public void setSubjectInfo(BusiProductContract subjectInfo) {
            this.subjectInfo = subjectInfo;
        }
    }
}
