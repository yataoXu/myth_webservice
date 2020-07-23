package com.zdmoney.facade;

import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.BusinessOperation;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.enm.OrderGenerateType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.helper.SerialNumberGenerator;
import com.zdmoney.integral.api.dto.lcbaccount.AccountOprResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTenderDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTenderResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTransferDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTransferResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.enm.*;
import com.zdmoney.integral.api.facade.IBidFacadeService;
import com.zdmoney.life.api.utils.JsonUtils;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.transfer.BusiDebtTransferMapper;
import com.zdmoney.models.OperStateRecord;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.*;
import com.zdmoney.service.payment.PaymentPlanService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.session.SessionManager;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.JackJsonUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.vo.trade.TenderVo;
import com.zdmoney.web.TransferResultDTO;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.UnpaidDebtToTransferDto;
import com.zdmoney.webservice.api.facade.ISpecialOperFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2018/8/16.
 */
@Component
@Slf4j
public class SpecialOperFacadeService implements ISpecialOperFacadeService {

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    BusiProductService busiProductService;

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;


    @Autowired
    private BusiDebtTransferMapper debtTransferMapper;

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private IBidFacadeService bidFacadeService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private IOperStateRecordService operStateRecordService;

    @Autowired
    private SubjectService subjectService;

    static final String OPER_TYEP = BusinessOperation.UNPAID_DEBT_TRANSFER.getOperType();

    ThreadLocal<OperStateRecord> operStateStore = new ThreadLocal<>();

    @Autowired
    private  TradeService tradeService;

    private void ensureNoMultiProcessing(String orderNo){
        OperStateRecord record = operStateRecordService.selectByTypeAndKeyword(OPER_TYEP, orderNo);
        if(record != null) throw new RuntimeException(orderNo + "已发起过转让，请走修复流程");
    }
    @Override
    public ResultDto<List<Integer>> transferUnpaidDebt(UnpaidDebtToTransferDto dto) {
        ensureNoMultiProcessing(dto.getOrderNo());
        String key = OPER_TYEP + dto.getOrderNo();
        boolean set = sessionManager.setNX(key, dto.getOrderNo());
        try {
            sessionManager.expire(key,5, TimeUnit.MINUTES);
            CustomerMainInfo mainInfo = findFort();
            BusiOrderSub initOrder = busiOrderSubMapper.queryBusiOrderSubInfo(dto.getOrderNo());
            BusiOrderSub transferOrder = createTransferOrder(initOrder, dto.getOrderAmt(), mainInfo,dto.getPeriods());//构造并保存受让人个贷订单
            tender(transferOrder, mainInfo, initOrder.getOrderId(),dto.getDebtWorth());//投标
            BusiOrderSub originOrder = getOriginOrder(dto.getOrderNo());
            if (originOrder == null) {
                originOrder = initOrder;
            }
            String recordNum = bidTransfer(transferOrder, initOrder, originOrder, mainInfo, BigDecimal.ZERO, dto.getDebtWorth());//转让债权
            transferOrder.setTransferSerialNo(recordNum);//更新下家转让流水号
            BusiDebtTransfer debtTransfer = recordDebtTransferation(transferOrder, initOrder, originOrder, BigDecimal.ZERO);// 生成转让记录，已成功，已交割
            finishTransferation(initOrder, transferOrder, mainInfo, dto.getPeriods(), debtTransfer.getTransferNo());//更新上家、下家订单，更新上家回款计划
        }finally {
            if(set) sessionManager.remove(key);
            clearThreadBindOperState();
        }
        if(!set) return ResultDto.FAIL("有相关操作在执行，请稍后再试");
        return new ResultDto(dto.getPeriods());
    }

    public ResultDto correctFailedOpers(UnpaidDebtToTransferDto dto){
        String key = OPER_TYEP + dto.getOrderNo();
        boolean set = sessionManager.setNX(key, dto.getOrderNo());
        try {
            sessionManager.expire(key,5, TimeUnit.MINUTES);
            OperStateRecord record = operStateRecordService.selectByTypeAndKeyword(OPER_TYEP, dto.getOrderNo());
            if(record == null) return ResultDto.FAIL(dto.getOrderNo()+"投标可能未成功，请确认后再处理");
            int state = Integer.parseInt(record.getCurrentState());
            setOperState(record);
            CustomerMainInfo mainInfo = findFort();
            BusiOrderSub initOrder = busiOrderSubMapper.queryBusiOrderSubInfo(dto.getOrderNo());
            BusiOrderSub transferOrder = busiOrderSubMapper.queryBusiOrderSubInfo(record.getExtraInfo());
            BusiOrderSub originOrder = getOriginOrder(dto.getOrderNo());
            if (originOrder == null) {
                originOrder = initOrder;
            }
            if(state == 1){//已投标，未转让
                String recordNum = bidTransfer(transferOrder, initOrder, originOrder, mainInfo, BigDecimal.ZERO,dto.getDebtWorth());//转让债权
                transferOrder.setTransferSerialNo(recordNum);//更新下家转让流水号
                state = 2;
            }
            BusiDebtTransfer debtTransfer = null;
            if(state == 2){//已调标的接口转让，落地转让记录失败
                debtTransfer = recordDebtTransferation(transferOrder, initOrder, originOrder,BigDecimal.ZERO);// 生成转让记录，已成功，已交割
                state = 3;
            }
            if(state == 3){//已转让，需更新下家订单转让交易流水号等，并更新上家回款计划，通知标的转让订单
                if(debtTransfer == null){//如果转让记录不为空，说明修复记录上一步骤做的转让，不需要到数据库查询转让记录，否则通过下家订单记录的转让编号查询转让记录
                    Map<String,Object> map = new HashMap<>();
                    map.put("transferNo",transferOrder.getTransferNo());
                    List<BusiDebtTransfer> list = debtTransferMapper.getTransfersByParam(map);
                    if(list.isEmpty()) throw new BusinessException("不存在对应转让记录，下家订单："+transferOrder.getOrderId());
                    else debtTransfer = list.get(0);
                }
                finishTransferation(initOrder,transferOrder,mainInfo,dto.getPeriods(),debtTransfer.getTransferNo());//更新上家、下家订单，更新上家回款计划
            }
        }finally {
            if(set) sessionManager.remove(key);
            clearThreadBindOperState();
        }
        if(!set) return ResultDto.FAIL("有相关操作在执行，请稍后再试");
        return ResultDto.SUCCESS();
    }

    @Override
    public ResultDto checkOperResult(String initOrderNo) {
        OperStateRecord record = operStateRecordService.selectByTypeAndKeyword(OPER_TYEP, initOrderNo);
        if(record == null) return ResultDto.FAIL(initOrderNo + "投标可能未成功，请确认");
        int state = Integer.parseInt(record.getCurrentState());
        String errMsg = null;
        if(state == 1) errMsg = "已投标，未转让";
        if(state == 2) errMsg = "已转让，落地转让记录失败";
        if(state == 3) errMsg = "已转让，更新上家回款计划、通知标的转让订单等失败";
        if(errMsg != null) return ResultDto.FAIL(errMsg);
        ResultDto resultDto = ResultDto.SUCCESS();
        try {
            resultDto.setData(JackJsonUtil.strToList(record.getSpareField(),Integer.class));
        } catch (IOException e) {
            log.error("转换回款计划期数失败:"+record.getId(),e);
        }
        return resultDto;
    }

    /**
     * 查找堡垒户
     * @return
     */
    private CustomerMainInfo findFort(){
        SysParameter sysParameter = sysParameterService.findOneByPrTypeDefaultWithoutCache(AppConstants.SYSPARAMS.FORT_ACCOUNT);
        if(sysParameter == null || StringUtils.isBlank(sysParameter.getPrValue()))
            throw new BusinessException("不存在有效堡垒户");
        String cmNumber = sysParameter.getPrValue();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
        if(mainInfo == null) throw new BusinessException("不存在有效堡垒户");
        return mainInfo;
    }

    /**
     * 保存受让人个贷订单
     * @param initOrder
     * @param orderAmt
     * @param mainInfo
     * @return
     */
    private BusiOrderSub createTransferOrder(BusiOrderSub initOrder,BigDecimal orderAmt,CustomerMainInfo mainInfo,List<Integer> periods){
        BusiOrder transferOrder = formTransferOrder(initOrder, mainInfo);
        transferOrder.setCashAmount(orderAmt);
        transferOrder.setOrderAmt(orderAmt);
        transferOrder.setPrincipalinterest(orderAmt);
        BusiOrderSub orderSub = new BusiOrderSub();
        BeanUtils.copyProperties(transferOrder,orderSub);
        orderSub.setSubjectNo(initOrder.getSubjectNo());
        orderSub.setDebtType(AppConstants.FinancePlan.DEBT_TYPE2);//转让债权
        orderSub.setCmNumber(mainInfo.getCmNumber());
        orderSub.setDebtorName(initOrder.getDebtorName());//借款人姓名
        orderSub.setDebtorNum(initOrder.getDebtorNum());//借款人用户编号
        saveOrders(transferOrder,orderSub);//保存订单
        initOperState(initOrder.getOrderId(),orderSub.getOrderId(),periods);//保存操作状态
        return orderSub;
    }

    private void initOperState(String initOrderNo,String newOrderNo,List<Integer> periods){//保存操作状态
        OperStateRecord record = new OperStateRecord();
        record.setCreateTime(new Date());
        record.setCurrentState("0");
        record.setKeyword(initOrderNo);
        record.setExtraInfo(newOrderNo);
        record.setOperType(OPER_TYEP);
        try {
            record.setSpareField(JackJsonUtil.objToStr(periods));
        } catch (IOException e) {
            log.error("数据转换失败",e);
        }
        operStateStore.set(record);
    }

    private void saveOperStateRecord(){
        operStateRecordService.save(operStateStore.get());
    }

    private void upgradeOperState(String currentState){//更新操作状态，仅限内存里的对象
        OperStateRecord record = getOperState();
        record.setCurrentState(currentState);
        operStateStore.set(record);
    }

    private void updateOperStateRecord(String currentState){//更新操作状态到数据库
        OperStateRecord record = getOperState();
        record.setCurrentState(currentState);
        operStateRecordService.updateByCondition(record);
    }

    private void setOperState(OperStateRecord record){
        operStateStore.set(record);
    }

    private OperStateRecord getOperState(){
        return operStateStore.get();
    }

    private void clearThreadBindOperState(){
        operStateStore.remove();
    }

    private void saveOrders(BusiOrder order,BusiOrderSub orderSub){
        busiOrderService.insert(order);
        orderSub.setId(order.getId());
        busiOrderSubMapper.insertWithId(orderSub);
    }

    /**
     * 构造下家个贷订单
     * @param originalOrder
     * @param mainInfo
     * @return
     */
    private BusiOrder formTransferOrder(BusiOrderSub originalOrder, CustomerMainInfo mainInfo){
        BusiOrder order = new BusiOrder();
        Date date = new Date();
        String orderNum = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.SUB_ORDER,mainInfo.getId());
        order.setOrderId(orderNum);
        order.setCustomerId(mainInfo.getId());
        order.setProductId(0L);
        order.setOrderTime(date);
        order.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
        order.setConfirmPaymentDate(date);
        order.setYearRate(originalOrder.getYearRate());
        order.setInterestStartDate(date);//起息日
        order.setInterestEndDate(originalOrder.getInterestEndDate());//结息日
        order.setCloseDays(Long.valueOf(com.zdmoney.common.util.DateUtil.daysBetween(date,originalOrder.getInterestEndDate())));//封闭期
        order.setCustomerName(mainInfo.getCmRealName());
        order.setInviteCode(mainInfo.getCmInviteCode());
        order.setCmOpenPlatform("");
        order.setOriginalRate(originalOrder.getOriginalRate());
        order.setProductType(3);
        order.setTransferType(AppConstants.OrderTransferStatus.ORDER_TRANSFER);//转让订单
        order.setModifyDate(date);
        order.setRaiseRateIncome(BigDecimal.ZERO);
        order.setHolderType(OrderConstants.OrderHoldType.FORT_HOLDER); // 堡垒户
        order.setPayType(AppConstants.FinancePlan.REPAY_TYPE0);
        order.setIntegralAmount(BigDecimal.ZERO);
        order.setCouponAmount(BigDecimal.ZERO);
        return order;
    }

    /**
     * 投标，并更新订单交易流水和状态
     * @param orderSub
     * @param mainInfo
     */
    private void tender(BusiOrderSub orderSub,CustomerMainInfo mainInfo, String initOrderNo,BigDecimal debtWorth){
        BusiProductContract productContract = null;
        Example example = new Example(BusiProductContract.class);
        example.createCriteria().andEqualTo("subjectNo", orderSub.getSubjectNo());
        List<BusiProductContract> busiProductContracts = busiProductContractMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(busiProductContracts)) {
            productContract = busiProductContracts.get(0);
        }
        TenderVo tenderVo = new TenderVo();
        tenderVo.setBidAmount(productContract.getCollectAmount());
        tenderVo.setCustomerMainInfo(mainInfo);
        tenderVo.setDebtType(orderSub.getDebtType());
        tenderVo.setYearRate(orderSub.getYearRate());
        tenderVo.setSaleEndDate(productContract.getSaleEndDate());
        tenderVo.setSaleStartDate(productContract.getSaleStartDate());
        tenderVo.setInitOrderNum(initOrderNo);
        tenderVo.setDebtWorth(debtWorth);
        String cashSerial = null;
        try {
            BorrowTenderDto tenderDto = tradeService.commonTenderDto(tenderVo, orderSub);
            BorrowTenderResultDto tenderResultDto = tradeService.commonBidTender(tenderDto);
            if(tenderResultDto != null){
                cashSerial = tenderResultDto .getCashSerialNo();
            }
            log.info("堡垒户投标成功，订单编号：{}，交易流水号：{}",orderSub.getOrderId(),cashSerial);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_11);
            busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
            MailUtil.sendMail("堡垒户投标发生异常", "订单编号:" + orderSub.getOrderId() + "，异常原因:" + e);
            throw e;
        }
        BusiOrderSub tmp = new BusiOrderSub();
        tmp.setId(orderSub.getId());
        tmp.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
        tmp.setPaySerNum(cashSerial);
        busiOrderService.updateMainAndSubOrderByCondition(tmp);
        upgradeOperState("1");//更新操作状态
        saveOperStateRecord();
    }

    /**
     * 债权转让结束：更新下家订单转让流水号、转让记录编号；更新上家回款计划状态；通知标的转让完成
     * @param initOrderSub
     * @param orderSub
     * @param mainInfo
     * @param periods
     * @param transferNo
     */
    public void finishTransferation(BusiOrderSub initOrderSub, BusiOrderSub orderSub, CustomerMainInfo mainInfo,List<Integer> periods,String transferNo){
        BusiOrderSub tmp = new BusiOrderSub();
        tmp.setId(orderSub.getId());
        tmp.setTransferSerialNo(orderSub.getTransferSerialNo());
        tmp.setTransferNo(transferNo);
        busiOrderSubMapper.updateByPrimaryKeySelective(tmp);//更新下家转让流水号、转让记录编号
        log.info("更新下家转让流水号、转让记录编号成功：{}",JsonUtils.toJson(tmp));
        Map<String, Object> planMap = new HashMap<>();
        planMap.put("orderNum", initOrderSub.getOrderId());//订单编号
        planMap.put("status", AppConstants.PaymentPlanStatus.RETURNED_TRANSFER);//新状态
        planMap.put("initStatus", AppConstants.PaymentPlanStatus.UNRETURN);//原状态
        planMap.put("currentTerms",periods);
        paymentPlanService.updateByMap(planMap);
        log.info("更新上家回款计划成功：{}",JsonUtils.toJson(planMap));
        //通知标的转让完成
        orderSub = busiOrderSubMapper.selectByPrimaryKey(orderSub.getId());
        subjectService.notifyTransferProductSubject(orderSub,initOrderSub,mainInfo,periods);
        updateOperStateRecord("9");//操作完结
    }

    /**
     * 获取原始 订单
     * @param orderId
     * @return
     */
    private BusiOrderSub getOriginOrder(String orderId) {
        BusiOrderSub orderTemp = null;
        Example example = new Example(BusiDebtTransfer.class);
        example.createCriteria().andEqualTo("newOrderNo", orderId).andEqualTo("transferStatus", AppConstants.DebtTransferStatus.TRANSFER_FINISH);
        List<BusiDebtTransfer> list = debtTransferMapper.selectByExample(example);
        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            BusiDebtTransfer transfer = list.get(0);
            if (org.apache.commons.lang.StringUtils.isNotBlank(transfer.getOriginOrderNo())) {
                orderTemp = busiOrderSubMapper.queryBusiOrderSubInfo(transfer.getOriginOrderNo());
            }
        }
        return orderTemp;
    }

    /**
     * 创建债权转让记录
     * @param busiOrderSub
     * @param initOrderSub
     * @param originOrder
     * @param transferFee
     * @return
     * @throws com.zdmoney.exception.BusinessException
     */
    private BusiDebtTransfer recordDebtTransferation(BusiOrderSub busiOrderSub, BusiOrderSub initOrderSub, BusiOrderSub originOrder, BigDecimal transferFee) throws com.zdmoney.exception.BusinessException {
        BusiDebtTransfer transfer = new BusiDebtTransfer();
        Date date = new Date();
        transfer.setTransferNo(buildTransferNo(initOrderSub.getCustomerId()));
        transfer.setTransferId(initOrderSub.getCustomerId());
        transfer.setInitOrderNo(initOrderSub.getOrderId());
        transfer.setPubDate(date);
        transfer.setIsSettle(AppConstants.TransferDebtStatus.TRANSFER_SETTLE_YES);
        transfer.setTransferStatus(AppConstants.DebtTransferStatus.TRANSFER_FINISH);
        transfer.setNewOrderNo(busiOrderSub.getOrderId());
        transfer.setTransferPrice(busiOrderSub.getOrderAmt());
        transfer.setTransferDate(new Date());
        transfer.setRealTransferDate(transfer.getTransferDate());
        transfer.setTransferCharge(transferFee);
        transfer.setProductId(busiOrderSub.getProductId());
        transfer.setInitProductId(initOrderSub.getProductId());
        transfer.setUpdateDate(date);
        transfer.setProductType(AppConstants.ProductSubjectType.FINANCE_PLAN_SUB);
        transfer.setBuyId(busiOrderSub.getCustomerId());
        //存储最原始的订单号
        transfer.setOriginOrderNo(originOrder.getOrderId());
        transfer.setOriginProductId(originOrder.getProductId());
        long leftDay = DateUtil.getBetweenDays(busiOrderSub.getInterestEndDate(), busiOrderSub.getInterestStartDate())+1;
        transfer.setLeftDay(Integer.valueOf(leftDay+""));
        transfer.setTradeDate(date);
        log.info("堡垒户交割转让记录：{}",JsonUtils.toJson(transfer));
        int i = debtTransferMapper.insert(transfer);
        if (i != 1) {
            throw new com.zdmoney.exception.BusinessException("堡垒户生成转让单失败："+JsonUtils.toJson(transfer));
        }
        updateOperStateRecord("3");//更新操作状态和附件信息
        return transfer;
    }

    /**
     * 生成转让记录编号
     * @param customerId
     * @return
     */
    private String buildTransferNo(Long customerId) {
        String transferNo = "T" + customerId;
        SimpleDateFormat timeStrFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        transferNo += timeStrFormat.format(new Date());
        transferNo += (int) (Math.random() * (100));
        return transferNo;
    }

    /**
     * 转让
     * @param orderSub
     * @param initOrderSub
     * @param originOrder
     * @param mainInfo
     * @param transferFee
     * @param debtAmount
     * @return
     */
    private String bidTransfer(BusiOrderSub orderSub, BusiOrderSub initOrderSub,BusiOrderSub originOrder, CustomerMainInfo mainInfo, BigDecimal transferFee,BigDecimal debtAmount) {
        com.zdmoney.common.ResultDto<TransferResultDTO> transferResult =
                tradeService.doTransferCredit(initOrderSub.getId(), orderSub.getId(), transferFee, debtAmount);
        if(transferResult != null && transferResult.isSuccess()){
            TransferResultDTO transferResultData = transferResult.getData();
            log.info("堡垒户调用标的转让成功：{}，流水号：{}", initOrderSub.getOrderId(), transferResultData.getTransferSerialNo());
            updateOperStateRecord("2");//更新操作状态和附件信息
            return transferResultData.getTransferSerialNo();

        }
        throw new com.zdmoney.exception.BusinessException("转让失败，订单号"+orderSub.getOrderId());
    }

    /**
     * 构造转让接口参数DTO
     * @param orderSub
     * @param initOrderSub
     * @param originOrder
     * @param mainInfo
     * @param transferFee
     * @param debtAmount
     * @return
     */
    private BorrowTransferDto getTransferDto(BusiOrderSub orderSub, BusiOrderSub initOrderSub,BusiOrderSub originOrder, CustomerMainInfo mainInfo, BigDecimal transferFee,BigDecimal debtAmount){
        String initOrderNum = initOrderSub.getOrderId();
        BigDecimal transferPrice = orderSub.getOrderAmt();
        BorrowTransferDto tenderDto =new BorrowTransferDto();
        BorrowTransferDto.BidTransfer bidTransfer = new BorrowTransferDto.BidTransfer();
        tenderDto.setTransNo("ZR"+orderSub.getOrderId());
        bidTransfer.setCustomerNo(orderSub.getCmNumber());

        bidTransfer.setOrderNo(orderSub.getOrderId());
        bidTransfer.setOriginalOrderNo(initOrderSub.getOrderId());

        bidTransfer.setOrderAmount(transferPrice);
        bidTransfer.setCashAmount(transferPrice);
        bidTransfer.setFeeAmount(transferFee);
        bidTransfer.setProductId(initOrderSub.getProductId()+"");
        bidTransfer.setBidNo(originOrder.getSubjectNo());

        bidTransfer.setCustomerTransferNo(initOrderSub.getCmNumber());
        bidTransfer.setCreditAmount(debtAmount);
        bidTransfer.setOriginalPlanOrderNo(initOrderSub.getParentNo());

        bidTransfer.setProductType(ProductType.FINANCE_BID);
        List<BorrowTransferDto.AccountDetail> accounts = new ArrayList<>();
        BorrowTransferDto.AccountDetail personIn =new BorrowTransferDto.AccountDetail();
        personIn.setAccountNo(initOrderSub.getCmNumber());
        personIn.setAccountType(AccountWholeType.PERSONAL);
        personIn.setOrderNo(orderSub.getOrderId());
        personIn.setAccountOprType(AccountOprType.TRANSFER);

        personIn.setAmount(transferPrice);
        personIn.setRelAccountNo(orderSub.getCmNumber());
        personIn.setRelAccountType(AccountWholeType.PERSONAL);
        personIn.setAccountOprDirection(AccountOprDirection.IN);
        personIn.setServiceCode(orderSub.getProductId()+"");
        personIn.setServiceName(orderSub.getProductName());
        accounts.add(personIn);
        tenderDto.setAccountDetails(accounts);
        tenderDto.setBidTransfer(bidTransfer);
        return tenderDto;
    }

}
