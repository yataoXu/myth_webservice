package com.zdmoney.service.subject;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.agreement.*;
import com.zdmoney.assets.api.dto.match.MatchAddInvestOrderDto;
import com.zdmoney.assets.api.dto.partner.PartnerAgreementContentReqDto;
import com.zdmoney.assets.api.dto.partner.PartnerAgreementContentResDto;
import com.zdmoney.assets.api.dto.signature.*;
import com.zdmoney.assets.api.dto.signature.enums.ContractType;
import com.zdmoney.assets.api.dto.subject.SubjectAddInvestOrderDto;
import com.zdmoney.assets.api.dto.subject.SubjectCollectResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectOrderItemDto;
import com.zdmoney.assets.api.dto.subject.borrow.*;
import com.zdmoney.assets.api.dto.subject.enums.ProductType;
import com.zdmoney.assets.api.dto.transfer.*;
import com.zdmoney.assets.api.dto.transfer.enums.TransfersType;
import com.zdmoney.assets.api.facade.match.IMatchInvestOrderFacadeService;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.assets.api.facade.subject.ILcbFinancialPlanFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payment.BusiSubjectPayPlan;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.vo.trade.TransferVo;
import com.zdmoney.web.dto.EstimateDto;
import com.zdmoney.web.dto.ProtocolDTO;
import com.zdmoney.web.dto.TransferDTO;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wu.hg on 2016/6/13.
 */
@Service
@Slf4j
@EnableAsync
public class SubjectService {

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    CustomerMainInfoService customerMainInfoService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private BusiProductSubMapper busiProductSubMapper;

    @Autowired
    private ILcbFinancialPlanFacadeService lcbFinancialPlanFacadeService;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    /**
     * 支付普通产品通知
     */
    @Async("payThreadExecutor")
    public void notifyCommonProductSubject(BusiOrderSub orderTemp, CustomerMainInfo mainInfo) {
        log.info("notify to subject system common product start: orderId=" + orderTemp.getOrderId());
        try {
            SubjectAddInvestOrderDto dto = new SubjectAddInvestOrderDto();
            orderTemp.setSubjectStatus(AppConstants.SubjectStatus.NOTIFY_FAILURE);
            BusiProductSub product = busiProductSubMapper.selectByPrimaryKey(orderTemp.getProductId());
            if (StringUtils.isNotBlank(orderTemp.getSubjectNo())) {
                dto.setSubjectNo(orderTemp.getSubjectNo());
            } else {
                dto.setSubjectNo(product.getSubjectNo());
            }
            dto.setCustomerNo(mainInfo.getCmNumber());
            dto.setCustomerIdNo(mainInfo.getCmIdnum());
            dto.setCustomerName(mainInfo.getCmRealName());
            dto.setCustomerPhone(mainInfo.getCmCellphone());
            dto.setOrderAmount(orderTemp.getOrderAmt());
            dto.setOrderTime(orderTemp.getOrderTime());
            dto.setOrderNo(orderTemp.getOrderId());
            dto.setPrincipal(orderTemp.getOrderAmt());
            dto.setYearRate(orderTemp.getYearRate());
            dto.setInterestStartDate(orderTemp.getInterestStartDate());
            dto.setInterestEndDate(orderTemp.getInterestEndDate());
            dto.setProductNo(orderTemp.getProductId().toString());
            dto.setProductName(product.getProductName());
            dto.setPartnerNo(AppConstants.PARTNER_NO);
            AssetsResultDto resultDto = lcbSubjectFacadeService.addSubjectInvestOrder(dto);
            if (resultDto.isSuccess()) {
//                busiOrderTempService.updateNotifyFlag(orderTemp.getId());
//                orderTemp.setSubjectStatus(AppConstants.SubjectStatus.NOTIFY_SUCCESS);
            } else {
                log.error("notify to subject system exception :" + resultDto.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("notify to subject system exception :" + e.getMessage());
        }
//        busiOrderTempService.updateByPrimaryKeySelective(orderTemp);
        log.info("notify to subject system common product end: orderId=" + orderTemp.getOrderId());
    }

    /**
     * 支付理财计划产品通知
     */
    @Async
    public void notifyCommonProductSubject( List<BusiOrderSub> orderSubList, Map<Long,CustomerMainInfo> mainInfoMap) {
        for(BusiOrderSub orderTemp:orderSubList){
            try {
                log.info("notify to subject system common product start: orderId=" + orderTemp.getOrderId());
                SubjectAddInvestOrderDto dto = new SubjectAddInvestOrderDto();
                CustomerMainInfo mainInfo = mainInfoMap.get(orderTemp.getParentId());
                dto.setSubjectNo(orderTemp.getSubjectNo());
                dto.setCustomerNo(mainInfo.getCmNumber());
                dto.setCustomerIdNo(mainInfo.getCmIdnum());
                dto.setCustomerName(mainInfo.getCmRealName());
                dto.setCustomerPhone(mainInfo.getCmCellphone());
                dto.setOrderAmount(orderTemp.getOrderAmt());
                dto.setOrderTime(orderTemp.getOrderTime());
                dto.setOrderNo(orderTemp.getOrderId());
                dto.setPrincipal(orderTemp.getOrderAmt());
                dto.setYearRate(orderTemp.getYearRate());
                dto.setInterestStartDate(orderTemp.getInterestStartDate());
                dto.setInterestEndDate(orderTemp.getInterestEndDate());
                dto.setProductNo(orderTemp.getProductId().toString());
                dto.setProductName(orderTemp.getProductName());
                dto.setPartnerNo("LCB");
                AssetsResultDto resultDto = lcbSubjectFacadeService.addSubjectInvestOrder(dto);
                if (!resultDto.isSuccess()) {
                    log.error("notify to subject system exception :" + resultDto.getMsg());
                }
                log.info("notify to subject system common product end: orderId=" + orderTemp.getOrderId());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("notify to subject system exception :" + e.getMessage());
            }
        }

//        busiOrderTempService.updateByPrimaryKeySelective(orderTemp);
    }

    /**
     * 支付转让产品通知
     */
    @Async("payThreadExecutor")
    public void notifyTransferProductSubject(String transferNo, BusiOrderSub order, BusiProductSub product, CustomerMainInfo mainInfo,TransferVo transferVo) {
            try {
                TransferInvestOrderReqDto transferOrderReqDto = new TransferInvestOrderReqDto();
                transferOrderReqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
                transferOrderReqDto.setOrderTime(order.getOrderTime());
                transferOrderReqDto.setOrderNo(order.getOrderId());
                transferOrderReqDto.setYearRate(order.getYearRate());
                transferOrderReqDto.setOrderAmount(order.getOrderAmt());
                transferOrderReqDto.setProductNo(order.getProductId().toString());
                transferOrderReqDto.setInterestStartDate(order.getInterestStartDate());
                transferOrderReqDto.setInterestEndDate(order.getInterestEndDate());
                transferOrderReqDto.setCustomerIdNo(mainInfo.getCmIdnum());
                transferOrderReqDto.setCustomerName(mainInfo.getCmRealName());
                transferOrderReqDto.setCustomerNo(mainInfo.getCmNumber());
                transferOrderReqDto.setCustomerPhone(mainInfo.getCmCellphone());
                transferOrderReqDto.setPrincipal(order.getOrderAmt());
                transferOrderReqDto.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
                transferOrderReqDto.setTransferNo(transferNo);
                transferOrderReqDto.setProductName(product == null ? "" : product.getProductName());
                if (transferVo!=null) {
                    //理财计划订单转让，增加上家信息
                    transferOrderReqDto.setTransfersType(TransfersType.TRANSFER_PLAN);
                    transferOrderReqDto.setFinanceId(order.getDebtNo());
                    TransferInvestOrderReqDto.Transfer transfer = new TransferInvestOrderReqDto.Transfer();//上家
                    transfer.setCustomerIdNo(transferVo.getCmIdnum());
                    transfer.setCustomerName(transferVo.getCustomerName());
                    transfer.setCustomerNo(transferVo.getCustomerNo());
                    transfer.setCustomerPhone(transferVo.getCustomerPhone());
                    transfer.setDeliveryDate(new Date());
                    transfer.setOrderNo(transferVo.getOrderNo());
                    transfer.setSubjectNo(transferVo.getSubjectNo());
                    transfer.setFinanceId(transferVo.getFinanceId());
                    transferOrderReqDto.setTransfer(transfer);
                }
                AssetsResultDto resultDto = lcbSubjectFacadeService.addTransferInvestOrder(transferOrderReqDto);
                if (resultDto.isSuccess()) {
                    log.info("通知标的系统转让下单结果，转让订单编号【{}】", order.getOrderId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("通知标的系统转让下单结果异常，转让订单编号【{}】", order.getOrderId());
            }
    }
    /**
     * 支付转让产品通知
     */
    @Async
    public void notifyTransferProductSubject(List<BusiOrderSub> orderSubList, Map<Long,CustomerMainInfo> mainInfoMap, Map<String, BusiOrderSub> transferMap) {
        for(BusiOrderSub order:orderSubList) {
            try {
                log.info("notify to subject system common product start:" + JSONObject.fromObject(order));
                TransferInvestOrderReqDto transferOrderReqDto = new TransferInvestOrderReqDto();
                CustomerMainInfo mainInfo = mainInfoMap.get(order.getParentId());
                transferOrderReqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
                transferOrderReqDto.setOrderTime(order.getOrderTime());
                transferOrderReqDto.setOrderNo(order.getOrderId());
                transferOrderReqDto.setYearRate(order.getYearRate());
                transferOrderReqDto.setOrderAmount(order.getOrderAmt());
                transferOrderReqDto.setProductNo(order.getProductId().toString());
                transferOrderReqDto.setInterestStartDate(order.getInterestStartDate());
                transferOrderReqDto.setInterestEndDate(order.getInterestEndDate());
                transferOrderReqDto.setCustomerIdNo(mainInfo.getCmIdnum());
                transferOrderReqDto.setCustomerName(mainInfo.getCmRealName());
                transferOrderReqDto.setCustomerNo(mainInfo.getCmNumber());
                transferOrderReqDto.setCustomerPhone(mainInfo.getCmCellphone());
                transferOrderReqDto.setPrincipal(order.getOrderAmt());
                transferOrderReqDto.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
                transferOrderReqDto.setTransferNo(order.getTransferNo());
                transferOrderReqDto.setProductName(StringUtil.parseString(order.getProductName(),""));
                transferOrderReqDto.setFinanceId(order.getDebtNo());
                BusiOrderSub initOrderSub = transferMap.get(order.getTransferNo());
                CustomerMainInfo transferInfo = customerMainInfoService.findOne(initOrderSub.getCustomerId());
                //理财计划订单转让，增加上家信息
                transferOrderReqDto.setTransfersType(TransfersType.TRANSFER_PLAN);
                TransferInvestOrderReqDto.Transfer transfer = new TransferInvestOrderReqDto.Transfer();//上家
                transfer.setCustomerIdNo(transferInfo.getCmIdnum());
                transfer.setCustomerName(transferInfo.getCmRealName());
                transfer.setCustomerNo(transferInfo.getCmNumber());
                transfer.setCustomerPhone(transferInfo.getCmCellphone());
                transfer.setDeliveryDate(new Date());
                transfer.setOrderNo(initOrderSub.getOrderId());
                transfer.setSubjectNo(initOrderSub.getSubjectNo());
                transfer.setFinanceId(initOrderSub.getDebtNo());
                transferOrderReqDto.setTransfer(transfer);
                AssetsResultDto resultDto = lcbSubjectFacadeService.addTransferInvestOrder(transferOrderReqDto);
                if (resultDto.isSuccess()) {
                    log.info("通知标的系统转让下单结果，转让订单编号【{}】", order.getOrderId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("通知标的系统转让下单结果异常，转让订单编号【{}】", order.getOrderId());
            }
         }
    }

    /**
     * 查询转让前协议内容
     */
    @Deprecated
    public String queryBeforeTransferProtocal(String protocolNo) {
        String protocolContent = "";
        try {
            PartnerAgreementContentReqDto reqDto = new PartnerAgreementContentReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setNo(protocolNo);
            AssetsResultDto<PartnerAgreementContentResDto> result = lcbSubjectFacadeService.getPartnerAgreementContentByNo(reqDto);
            if (result.isSuccess()) {
                protocolContent = result.getData().getAgreement();
            } else {
                throw new BusinessException("查询协议失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询协议异常");
        }
        return protocolContent;
    }

    /**
     * 查询转让后协议内容
     */
    public Map queryAfterTransferProtocal(String transferNo) {
//        String protocolContent = "";
        HashMap<String, Object> returnMaps = Maps.newHashMap();
        try {
            TransferServiceAgreementReqDto reqDto = new TransferServiceAgreementReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setTransferNo(transferNo);
            AssetsResultDto<TransferServiceAgreementResDto> result = lcbSubjectFacadeService.getTransferServiceAgreement(reqDto);
            if (result.isSuccess()) {
                returnMaps.put("agreementStr", "");
                returnMaps.put("isElecAgreement", true);
                returnMaps.put("viewUrl", result.getData().getViewUrl());
                returnMaps.put("downloadUrl", result.getData().getDownloadUrl());
            } else {
                log.error("queryAfterTransferProtocal failed ", result.getMsg());
                throw new BusinessException("查询协议失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询协议异常");
        }
        return returnMaps;
    }

    /**
     * 转让成功通知标的系统获取回款计划
     */
    public List<BusiSubjectPayPlan> notifyTransferToSubject(TransferDTO resultDto, List<TransferBorrowersReqDto> borrowList, BigDecimal basicRate, List<TransferRepayPlanReqDto> planList, BigDecimal estimatePrice, CustomerMainInfo customerMainInfo, BusiDebtTransfer transfer, BusiProduct newProduct, BusiOrderTemp oldOrder) {
        log.info("notifyTransferToSubject begin : transfer no is {}", transfer.getTransferNo());
        List<BusiSubjectPayPlan> resultList = Lists.newArrayList();
        try {
            TransferApplyReqDto reqDto = new TransferApplyReqDto();
            reqDto.setTransferNo(transfer.getTransferNo());
            reqDto.setTransferName(newProduct.getProductName());
            reqDto.setInterestStartDate(oldOrder.getInterestStartDate());//2017-1-12 修改 ，原始取的是新产品的起息日。
            reqDto.setInterestEndDate(newProduct.getInterestEndDate());
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setDeliveryDate(transfer.getTransferDate());
            reqDto.setPv(estimatePrice);
            reqDto.setServiceChargeRate(basicRate);
            reqDto.setServiceCharge(transfer.getTransferCharge());
            reqDto.setOriginalOrderNo(oldOrder.getOrderId());
            reqDto.setOriginalPrincipal(oldOrder.getOrderAmt());
            reqDto.setOriginalInterest(oldOrder.getPrincipalinterest().subtract(oldOrder.getOrderAmt()));
            reqDto.setOriginalPrincipalInterest(oldOrder.getPrincipalinterest());
            reqDto.setTransferCustomerNo(customerMainInfo.getCmNumber());
            reqDto.setTransferCustomerName(customerMainInfo.getCmRealName());
            reqDto.setTransferOrderNo(transfer.getInitOrderNo());
            reqDto.setTransferAmount(transfer.getTransferPrice());
            reqDto.setServiceCharge(transfer.getTransferCharge());
            reqDto.setTransferCustomerIdNo(customerMainInfo.getCmIdnum());
            reqDto.setTransferCustomerPhone(customerMainInfo.getCmCellphone());
            reqDto.setPlanReqDtos(planList);
            reqDto.setSubjectNo(newProduct.getSubjectNo());
            reqDto.setBorrowerList(borrowList);
            reqDto.setOriginalYearRate(oldOrder.getYearRate());
            AssetsResultDto<TransferApplyResDto> result = lcbSubjectFacadeService.transferApply(reqDto);
            if (result.isSuccess()) {
                List<TransfersRepayPlanResDto> list = result.getData().getTransfersPlanList();
                resultDto.setProtocolName(result.getData().getTransferAgreementName());
                resultDto.setProtocolNo(result.getData().getTransferAgreementNo());
                resultDto.setProtocolUrl(configParamBean.getProductDetailUrl() + "/commonDispatchRequest?method=500038&resultPage=front/transferServicesAgree&transferNo=" + transfer.getTransferNo());
                if (!CollectionUtils.isEmpty(list)) {
                    int curTerm = 0;
                    for (TransfersRepayPlanResDto dto : list) {
                        BusiSubjectPayPlan plan = new BusiSubjectPayPlan();
                        if (StringUtils.isBlank(newProduct.getSubjectNo())) {
                            plan.setNo(transfer.getTransferNo());
                            plan.setSubjectNo(transfer.getTransferNo());
                        } else {
                            plan.setNo(dto.getPlanNo());
                            plan.setSubjectNo(dto.getSubjectNo());
                        }
                        plan.setPrincipal(dto.getPrincipal());
                        plan.setInterest(dto.getInterest());
                        plan.setPrincipalInterest(dto.getPrincipalInterest());
                        plan.setTerm(dto.getTerm());
                        plan.setBorrowingDays(dto.getBorrowingDays());
                        plan.setDayInterest(dto.getDayInterest());
                        plan.setRepayDay(dto.getRepayDay());
                        plan.setRepayStatus("1");
                        plan.setCreateTime(new Date());
                        plan.setCurrTerm(++curTerm);
                        plan.setProductId(newProduct.getId());
                        resultList.add(plan);
                    }
                }
            } else {
                log.error("notifyTransferToSubject failed :{}", result.getMsg());
                throw new BusinessException("转让失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("notifyTransferToSubject exception :{}", e.getMessage());
            throw new BusinessException("转让失败");
        }
        if (CollectionUtils.isEmpty(resultList)) {
            log.error("notifyTransferToSubject failed :{}", "paymentList is null");
            throw new BusinessException("转让失败");
        }
        log.info("notifyTransferToSubject end : transfer no is {}", transfer.getTransferNo());
        return resultList;
    }

    /**
     * 计算转让利率
     */
    public BigDecimal queryXirrValue(Date transferDate, BigDecimal transferPrice, BusiOrderTemp order, List<TransferRepayPlanReqDto> planList) {
        BigDecimal rate = null;
        try {
            RateValuationReqDto valuateDto = new RateValuationReqDto();
            valuateDto.setOriginalPrincipal(order.getOrderAmt());
            valuateDto.setInterestStartDate(order.getInterestStartDate());
            valuateDto.setInterestEndDate(order.getInterestEndDate());
            valuateDto.setPartnerNo("LCB");
            valuateDto.setDeliveryDate(transferDate);
            valuateDto.setTransferAmount(transferPrice);
            valuateDto.setPlanReqDtos(planList);
            valuateDto.setOriginalYearRate(order.getYearRate());
            AssetsResultDto<RateValuationResDto> result = lcbSubjectFacadeService.xirrValuate(valuateDto);
            if (result.isSuccess()) {
                rate = result.getData().getRate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("计算利率失败");
        }
        return rate.setScale(3,BigDecimal.ROUND_DOWN);
    }

    /**
     * 转让初始化（协议模板）
     * @return
     */
    public List<ProtocolDTO> queryProtocalName() {
        List<ProtocolDTO> protocolList = Lists.newArrayList();
        try {
            AgreementNamesReqDto reqDto = new AgreementNamesReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setProductType(ProductType.TRANSFER_PRODUCT);
            AssetsResultDto<AgreementNamesResDto> resultDto = lcbSubjectFacadeService.getProductApplyAgreements(reqDto);
            if (resultDto.isSuccess()) {
                List<AgreementNameDto> resultList = resultDto.getData().getAgreementNameDtos();
                for (AgreementNameDto resDto : resultList) {
                    ProtocolDTO protocolDTO = new ProtocolDTO();
                    protocolDTO.setProtocolNo(resDto.getNo());
                    protocolDTO.setProtocolName(resDto.getName());
                    protocolDTO.setProtocolUrl(resDto.getUrl());
                    protocolList.add(protocolDTO);
                }
            }
            else {
                log.error("查询协议失败:{}" , resultDto.getMsg());
                throw new BusinessException("查询协议失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询协议异常:{}" , e.getMessage());
            throw new BusinessException("查询协议异常");
        }
        if (CollectionUtils.isEmpty(protocolList)) {
            log.error("查询协议失败:无协议");
            throw new BusinessException("查询协议失败");
        }
        return protocolList;
    }

    /**
     * 查询估值 （标的）
     */
    public List<EstimateDto> queryEstimatePrice(BusiOrderTemp order, List<Date> deliveryDates, List<TransferRepayPlanReqDto> planList) {
        List<EstimateDto> protocolList = Lists.newArrayList();
        try {
            PvValuateV2ReqDto valuateDto = new PvValuateV2ReqDto();
            valuateDto.setOriginalPrincipal(order.getOrderAmt());
            valuateDto.setInterestStartDate(order.getInterestStartDate());
            valuateDto.setInterestEndDate(order.getInterestEndDate());
            valuateDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            valuateDto.setDeliveryDates(deliveryDates);
            valuateDto.setPlanReqDtos(planList);
            valuateDto.setOriginalYearRate(order.getYearRate());
            valuateDto.setMinRate(Convert.toBigDecimal(configParamBean.getTransfer_min_rate()));
            valuateDto.setMaxRate(Convert.toBigDecimal(configParamBean.getTransfer_max_rate()));
            AssetsResultDto<PvValuateV2ResDto> result = lcbSubjectFacadeService.xnpvValuateV2(valuateDto);
            if (result.isSuccess()) {
                PvValuateV2ResDto resDto = result.getData();
                List<PvResultV2Dto> list = resDto.getPvResultV2Dtos();
                for (PvResultV2Dto dto : list) {
                    EstimateDto estimateDto = new EstimateDto();
                    estimateDto.setDate(dto.getDeliveryDate());
                    estimateDto.setEstimatePrice(Convert.toStr(dto.getPv().setScale(2,BigDecimal.ROUND_DOWN)));
                    estimateDto.setMinpv(Convert.toStr(dto.getMinpv().setScale(2,BigDecimal.ROUND_DOWN)));
                    estimateDto.setMaxpv(Convert.toStr(dto.getMaxpv().setScale(2,BigDecimal.ROUND_DOWN)));
                    protocolList.add(estimateDto);
                }
            } else {
                log.error("error:" + result.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询估值异常");
        }
        if (CollectionUtils.isEmpty(protocolList)) {
            throw new BusinessException("查询估值失败");
        }
        return protocolList;
    }

    /**
     * 查询估值 （标的）v4.6
     */
    public List<TransferPvResDto.DeliveryDateIrrPvDto> transferEstimatePrice(BusiOrderTemp order, List<Date> deliveryDates, List<TransferRepayPlanReqDto> planList, BusiProduct busiProduct,BigDecimal feeRate) {
        TransferPvReqDto pvReqDto = new TransferPvReqDto();
        pvReqDto.setDeliveryDates(deliveryDates);
        pvReqDto.setTransferRate(feeRate);
        pvReqDto.setInterestStartDate(order.getInterestStartDate());
        pvReqDto.setInterestEndDate(order.getInterestEndDate());
        pvReqDto.setOriginalPrincipal(order.getOrderAmt());
        pvReqDto.setOriginalYearRate(order.getYearRate());
        pvReqDto.setPlanReqDtos(planList);
        pvReqDto.setMinRate(busiProduct.getMinRate());
        pvReqDto.setMaxRate(busiProduct.getMaxRate());
        pvReqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());

        List<TransferPvResDto.DeliveryDateIrrPvDto> deliveryPvList = null;
        try {
            AssetsResultDto<TransferPvResDto> resultDto = lcbSubjectFacadeService.xirrPvValuate(pvReqDto);
            if (resultDto.isSuccess()) {
                TransferPvResDto resDto = resultDto.getData();
                deliveryPvList = resDto.getDeliveryPvList();
            }

        } catch (Exception e) {
            log.error("调用标的系统，查询估值异常",e);
            throw new BusinessException("查询估值异常");
        }
        if (CollectionUtils.isEmpty(deliveryPvList)) {
            throw new BusinessException("查询估值失败");
        }
        return deliveryPvList;
    }

    /**
     * 申请法大大签名及存管银行开户协议
     * @param mainInfo 用户信息
     * 4.8改动 增加富友存管ID,改为异步调用
     */
    @Async
    public void applySignature(CustomerMainInfo mainInfo) {
        try {
            SignApplyReqDto reqDto = new SignApplyReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setCustomerId(mainInfo.getId().toString());
            reqDto.setCustomerNo(mainInfo.getCmNumber());
            reqDto.setCustomerName(mainInfo.getCmRealName());
            reqDto.setCustomerIdNo(mainInfo.getCmIdnum());
            reqDto.setCustomerPhone(mainInfo.getCmCellphone());
            reqDto.setLoginId(mainInfo.getFuiouLoginId());
            AssetsResultDto<SignApplyResDto> result = lcbSubjectFacadeService.applySignature(reqDto);
            if (!result.isSuccess()) {
                log.error("customer【{}】 applySignature failed ",mainInfo.getId(), result.getMsg());
            }
        } catch (Exception e) {
            log.error("customer【{}】 applySignature exception ",mainInfo.getId(), e);
        }
    }

    /**
     * 普通产品通知
     */
    @Async("payThreadExecutor")
    public void signProductAgreement(BusiOrderSub order, BusiProductSub product, CustomerMainInfo customerMainInfo) {
        try {
            ProductInvestReqDto reqDto = new ProductInvestReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setCustomerNo(customerMainInfo.getCmNumber());
            reqDto.setCustomerIdNo(customerMainInfo.getCmIdnum());
            reqDto.setCustomerName(customerMainInfo.getCmRealName());
            reqDto.setCustomerPhone(customerMainInfo.getCmCellphone());
            reqDto.setOrderNo(order.getOrderId());
            reqDto.setOrderAmount(order.getOrderAmt());
            reqDto.setOrderTime(order.getOrderTime());
            reqDto.setYearRate(order.getOriginalRate());
            reqDto.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
            reqDto.setPrincipal(order.getOrderAmt());
            reqDto.setPrincipalInterest(order.getPrincipalinterest());
            reqDto.setInterestStartDate(order.getInterestStartDate());
            reqDto.setInterestEndDate(order.getInterestEndDate());
            reqDto.setProductName(product.getProductName());
            reqDto.setProductNo(product.getId().toString());
            reqDto.setBorrowTerm(order.getCloseDays().intValue());
            reqDto.setSubjectNo(product.getSubjectNo());
            AssetsResultDto resultDto = lcbSubjectFacadeService.signInvestAgreement(reqDto);
            if (!resultDto.isSuccess()) {
                log.error("fail:" + resultDto.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:" + e.getMessage());
        }
    }

    /**
     * 电子签章-查询投资协议
     */
    public ProductAgreementObtainResDto queryInvestAgreement(String orderNum) {
        ProductAgreementObtainResDto resDto = null;
        try {
            ProductAgreementObtainReqDto investReqDto = new ProductAgreementObtainReqDto();
            investReqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            investReqDto.setOrderNo(orderNum);
            AssetsResultDto<ProductAgreementObtainResDto> resultDto = lcbSubjectFacadeService.getProductInvestAgreement(investReqDto);
            if (resultDto.isSuccess()) {
                resDto = resultDto.getData();
            } else {
                log.error("fail:" + resultDto.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:" + e.getMessage());
        }
        return resDto;
    }

    /**
     * 电子签章-查询债权转让协议
     */
    public ProductAgreementObtainResDto queryProductLoanAgreement(String orderNum) {
        ProductAgreementObtainResDto resDto = null;
        try {
            ProductAgreementObtainReqDto investReqDto = new ProductAgreementObtainReqDto();
            investReqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            investReqDto.setOrderNo(orderNum);
            AssetsResultDto<ProductAgreementObtainResDto> resultDto = lcbSubjectFacadeService.getProductLoanAgreement(investReqDto);
            if (resultDto.isSuccess()) {
                resDto = resultDto.getData();
            } else {
                log.error("fail:" + resultDto.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:" + e.getMessage());
        }
        return resDto;
    }

    /**
     * 支付模板列表
     */
    public List<AgreementNameDto> gainPayAgreementsTemplete(BusiProduct busiProduct) {
        List<AgreementNameDto> resDto = null;
        try {
            AgreementNamesReqDto reqDto;
            //转让产品
            if (busiProduct.getIsTransfer().equals(AppConstants.ProductTransferStatus.TRANSFER_PRODUCT)) {
                reqDto = AgreementNamesReqDto.buildTransProduct();
            } else {
                //标的产品
//                if (busiProduct.getContractId() > 0) {
//                    if(AppConstants.PersonalLoan.PERSONAL_LOAN == busiProduct.getPersonLoan()){
//                        reqDto = AgreementNamesReqDto.buildPersonLoanProduct(busiProduct.getSubjectNo());
//                    }
//                    else{
//                        reqDto = AgreementNamesReqDto.buildSubjectProduct(busiProduct.getSubjectNo());
//                    }
//                }
//                //优选产品
//                else {
//                    reqDto = AgreementNamesReqDto.buildOptionProduct();
//                }
                if(AppConstants.ProductSubjectType.SUBJECT_GD.equals(busiProduct.getSubjectType())){
                    reqDto = AgreementNamesReqDto.buildPersonLoanProduct(busiProduct.getSubjectNo());
                }
                else if (AppConstants.ProductSubjectType.SUBJECT_GD.equals(busiProduct.getSubjectType())){
                    reqDto = AgreementNamesReqDto.buildSubjectProduct(busiProduct.getSubjectNo());
                    //理财计划
                }else if (AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())){
                   reqDto = AgreementNamesReqDto.buildPlanProduct();
                }
                else{
                    reqDto = AgreementNamesReqDto.buildOptionProduct();
                }

            }
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<AgreementNamesResDto> resultDto = lcbSubjectFacadeService.getAgreementsByProduct(reqDto);
            if (resultDto.isSuccess()) {
                resDto = resultDto.getData().getAgreementNameDtos();
            } else {
                log.error("fail:" + resultDto.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询支付模板失败:" + e.getMessage());
//            throw new BusinessException("查询模板异常");
        }
        return resDto;
    }



    /**
     * 查询转让前协议内容
     */
    public String queryAgreements(String productType,String agreementNo) {
        String content = "";
        try {
            AgreementContentReqDto reqDto = new AgreementContentReqDto();
            reqDto.setAgreementNo(agreementNo);
            //优选
            if(productType.equals("0")){
                reqDto.setProductType(ProductType.OPTION_PRODUCT);
            }
            //标的
            if(productType.equals("1")){
                reqDto.setProductType(ProductType.SUBJECT_PRODUCT);
            }
            //转让
            if(productType.equals("2")){
                reqDto.setProductType(ProductType.TRANSFER_PRODUCT);
            }

            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<AgreementContentResDto> result = lcbSubjectFacadeService.getAgreementContent(reqDto);
            if (result.isSuccess()) {
                content = result.getData().getContent();
            } else {
                throw new BusinessException("查询协议失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询协议异常");
        }
        return content;
    }

    /**
     * 借款--首页初始化
     * @param idNum
     * @param cellphone
     * @return
     */
    public MyBorrowResDto queryMyBorrows(String idNum, String cellphone) {
        MyBorrowResDto resDto = null;
        try {
            MyBorrowReqDto reqDto = new MyBorrowReqDto();
            reqDto.setBorrowerPhone(cellphone);
            reqDto.setIdNo(idNum);
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<MyBorrowResDto> resultDto = lcbSubjectFacadeService.myBorrows(reqDto);
            if (resultDto.isSuccess()) {
                resDto = resultDto.getData();
            } else {
                log.error("借款首页初始化失败:" + resultDto.getMsg());
                throw new BusinessException("借款首页初始化");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("借款首页初始化失败:" + e.getMessage());
            throw new BusinessException("借款首页初始化");
        }
        return resDto;
    }

    /**
     * 查询借款详情
     * @param idNum
     * @param cellPhone
     * @param subjectNo
     * @return
     */
    public BorrowDetailDto queryBorrowDetail(String idNum, String cellPhone, String subjectNo) {
        BorrowDetailDto result = null;
        try {
            BorrowDetailReqDto reqDto = new BorrowDetailReqDto();
            reqDto.setIdNo(idNum);
            reqDto.setBorrowerPhone(cellPhone);
            reqDto.setSubjectNo(subjectNo);
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<BorrowDetailDto> resultDto = lcbSubjectFacadeService.borrowDetail(reqDto);
            if (resultDto.isSuccess()) {
                result = resultDto.getData();
            } else {
                log.error("查询借款详情失败:" + resultDto.getMsg());
                throw new BusinessException("查询借款详情");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询借款详情失败:" + e.getMessage());
            throw new BusinessException("查询借款详情");
        }
        return result;
    }

    /**
     * 个贷-还款详情
     */
    public BorrowPlanDetailDto borrowRepayPlanDetail(String cellphone, String idNo, String subjectNo) {
        BorrowPlanDetailDto result  = null;
        try {
            BorrowPlanDetailReqDto reqDto = new BorrowPlanDetailReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setBorrowerPhone(cellphone);
            reqDto.setIdNo(idNo);
            reqDto.setSubjectNo(subjectNo);
            AssetsResultDto<BorrowPlanDetailDto> resultDto = lcbSubjectFacadeService.borrowRepayPlanDetail(reqDto);
            if (resultDto.isSuccess()) {
                result = resultDto.getData();
            } else {
                log.error("查询还款产品-失败:" + resultDto.getMsg());
                throw new BusinessException("查询还款产品");
            }
        } catch (Exception e) {
            log.error("查询还款产品-异常:" + e.getMessage());
            throw new BusinessException("查询还款产品");
        }
        return result;
    }


    /**
     * 个贷-签约
     */
    public String signBorrow(String cellphone,String idNo,String subjectNo) {
        log.info("签约开始-手机号：{}",cellphone);
        String url = "";
        try {
            BorrowSignReqDto reqDto = new BorrowSignReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setBorrowerPhone(cellphone);
            reqDto.setIdNo(idNo);
            reqDto.setSubjectNo(subjectNo);
            AssetsResultDto<BorrowSignResDto> resultDto = lcbSubjectFacadeService.borrowerSign(reqDto);
            if (resultDto.isSuccess()) {
                url = resultDto.getData().getSignUrl();
            } else {
                log.error("签约结束-失败:" + resultDto.getMsg());
                throw new BusinessException("签约失败");
            }
        } catch (Exception e) {
            log.error("签约结束-异常:" + e.getMessage());
            throw new BusinessException("签约失败");
        }
        log.info("签约结束-成功");
        return url;
    }

    /**
     * 个贷-查询借款协议
     */
    public AgreementUrlResDto queryBorrowAgreement(String cellphone, String idNo, String subjectNo) {
        AgreementUrlResDto  result= null;
        try {
            BorrowAgreementReqDto reqDto = new BorrowAgreementReqDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            reqDto.setBorrowerPhone(cellphone);
            reqDto.setIdNo(idNo);
            reqDto.setSubjectNo(subjectNo);
            AssetsResultDto<BorrowAgreementResDto> resultDto = lcbSubjectFacadeService.getBorrowAgreement(reqDto);
            if (resultDto.isSuccess()) {
                result = resultDto.getData();
            } else {
                log.error("查询借款协议-失败:" + resultDto.getMsg());
                throw new BusinessException("查询借款协议");
            }
        } catch (Exception e) {
            log.error("查询借款协议-异常:" + e.getMessage());
            throw new BusinessException("查询借款协议");
        }
        return result;
    }

    /**
     * 查询订单协议
     */
    public AgreementUrlResDto queryOrderAgreementByType(String orderNo,ContractType contractType) {
        AgreementUrlResDto  result;
        try {
            OrderTypeAgreementReqDto reqDto = new OrderTypeAgreementReqDto();
            reqDto.setContractType(contractType);
            reqDto.setOrderNo(orderNo);
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<AgreementUrlResDto> resultDto = lcbSubjectFacadeService.getOrderAgreementByType(reqDto);
            if (resultDto.isSuccess()) {
                result = resultDto.getData();
            } else {
                log.error("查询协议-失败:" + resultDto.getMsg());
                throw new BusinessException("查询协议失败");
            }
        } catch (Exception e) {
            log.error("查询协议-异常:" + e.getMessage());
            throw new BusinessException("查询协议异常");
        }
        return result;
    }

    /**
     * 标的系统-查询产品详情模板
     */
    public List<AgreementNameDto> queryProductAgreementsByType(String subjectNo, ProductType productType) {
        List<AgreementNameDto>  result;
        try {
            AgreementNamesReqDto reqDto = new AgreementNamesReqDto();
            reqDto.setProductType(productType);
            reqDto.setSubjectNo(subjectNo);
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<AgreementNamesResDto> resultDto = lcbSubjectFacadeService.getProductDetailAgreements(reqDto);
            if (resultDto.isSuccess()) {
                result = resultDto.getData().getAgreementNameDtos();
            } else {
                log.error("查询协议-失败:" + resultDto.getMsg());
                throw new BusinessException("查询协议失败");
            }
        } catch (Exception e) {
            log.error("查询协议-异常:" + e.getMessage());
            throw new BusinessException("查询协议异常");
        }
        return result;
    }

    /**
     * 生成开户协议
     */
    @Async
    public void openAccountAgreement(CustomerMainInfo customerMainInfo) {
        try {
            OpenAccountAgreementDto oprDto = new OpenAccountAgreementDto();
            oprDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            oprDto.setCustomerNo(customerMainInfo.getCmNumber());
            oprDto.setIdNo(customerMainInfo.getCmIdnum());
            oprDto.setPhone(customerMainInfo.getCmCellphone());
            AssetsResultDto<String>  resultDto = lcbSubjectFacadeService.openAccountAgreement(oprDto);
            if (!resultDto.isSuccess()) {
                log.error("调用标的系统，生成开户协议失败，用户ID【{}】，失败原因【{}】",customerMainInfo.getId(),resultDto.getMsg());
            }
        } catch (Exception e) {
            log.error("调用标的系统，生成开户协议异常，用户ID【{}】",customerMainInfo.getId(),e);
        }
    }
    /**
     * 理财计划产品通知
     */
    @Async("payThreadExecutor")
    public void addMatchInvestOrder(BusiOrderSub order, BusiProductSub product, CustomerMainInfo customerMainInfo) {
        try {
            MatchAddInvestOrderDto reqDto = new MatchAddInvestOrderDto();
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            BigDecimal couponAmount = order.getCouponAmount()==null?BigDecimal.ZERO :order.getCouponAmount();
            BigDecimal integralAmount = order.getIntegralAmount()==null?BigDecimal.ZERO :order.getIntegralAmount();
            reqDto.setDeductibleAmount(couponAmount.add(integralAmount));
            reqDto.setAccountNo(customerMainInfo.getCmNumber());
            reqDto.setAccountName(customerMainInfo.getCmRealName());
            reqDto.setInvestType(ContractType.PLAN_INVEST.name());
            reqDto.setIdNo(customerMainInfo.getCmIdnum());
            reqDto.setInvestRate(order.getOriginalRate());
            reqDto.setInvestAmount(order.getOrderAmt());
            reqDto.setInvestOrderNo(order.getOrderId());
            reqDto.setPlanNo(product.getId().toString());
            reqDto.setPlanName(product.getProductName());
            reqDto.setPhone(customerMainInfo.getCmCellphone());
            reqDto.setInvestTime(order.getOrderTime());
            reqDto.setPartnerName("捞财宝");
            reqDto.setActualAmount(order.getCashAmount());
            reqDto.setProductName(product.getProductName());
            reqDto.setCloseDay(product.getCloseDay());
            AssetsResultDto resultDto = lcbFinancialPlanFacadeService.addMatchInvestOrder(reqDto);
            if (!resultDto.isSuccess()) {
                log.error("理财计划通知标的失败:订单编号【{}】", order.getOrderId());
            }
            else {
                log.info("理财计划通知标的成功:订单编号【{}】", order.getOrderId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail:" + e.getMessage());
            log.error("理财计划通知标的异常:订单编号【{}】", order.getOrderId(),e);
        }
    }

    @Async
    public void notifyTransferProductSubject(BusiOrderSub order, BusiOrderSub initOrderSub,CustomerMainInfo mainInfo,List<Integer> periods) {
        try {
            log.info("notify subject system transfer order start:" + JSONObject.fromObject(order));
            TransferInvestOrderReqDto transferOrderReqDto = new TransferInvestOrderReqDto();
            transferOrderReqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            transferOrderReqDto.setOrderTime(order.getOrderTime());
            transferOrderReqDto.setOrderNo(order.getOrderId());
            transferOrderReqDto.setYearRate(order.getYearRate());
            transferOrderReqDto.setOrderAmount(order.getOrderAmt());
            transferOrderReqDto.setProductNo(order.getProductId().toString());
            transferOrderReqDto.setInterestStartDate(order.getInterestStartDate());
            transferOrderReqDto.setInterestEndDate(order.getInterestEndDate());
            transferOrderReqDto.setCustomerIdNo(mainInfo.getCmIdnum());
            transferOrderReqDto.setCustomerName(mainInfo.getCmRealName());
            transferOrderReqDto.setCustomerNo(mainInfo.getCmNumber());
            transferOrderReqDto.setCustomerPhone(mainInfo.getCmCellphone());
            transferOrderReqDto.setPrincipal(order.getOrderAmt());
            transferOrderReqDto.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
            transferOrderReqDto.setTransferNo(order.getTransferNo());
            transferOrderReqDto.setProductName(StringUtil.parseString(order.getProductName(),""));
            transferOrderReqDto.setFinanceId(order.getDebtNo());
            transferOrderReqDto.setTransTermList(periods);
            CustomerMainInfo transferInfo = customerMainInfoService.findOne(initOrderSub.getCustomerId());
            //理财计划订单转让，增加上家信息
            transferOrderReqDto.setTransfersType(TransfersType.TRANSFER_ORG);
            TransferInvestOrderReqDto.Transfer transfer = new TransferInvestOrderReqDto.Transfer();//上家
            transfer.setCustomerIdNo(transferInfo.getCmIdnum());
            transfer.setCustomerName(transferInfo.getCmRealName());
            transfer.setCustomerNo(transferInfo.getCmNumber());
            transfer.setCustomerPhone(transferInfo.getCmCellphone());
            transfer.setDeliveryDate(new Date());
            transfer.setOrderNo(initOrderSub.getOrderId());
            transfer.setSubjectNo(initOrderSub.getSubjectNo());
            transfer.setFinanceId(initOrderSub.getDebtNo());
            transferOrderReqDto.setTransfer(transfer);
            AssetsResultDto resultDto = lcbSubjectFacadeService.addTransferInvestOrder(transferOrderReqDto);
            if (resultDto.isSuccess()) {
                log.info("通知标的系统转让下单结果，转让订单编号【{}】", order.getOrderId());
            }
        } catch (Exception e) {
            log.error("通知标的系统转让下单结果异常，转让订单编号【{}】", order.getOrderId());
            log.error(e.getMessage(),e);
        }
    }

    public ResultDto sendGoalReachedNoticeIfNecessary(String subjectNo, boolean sendFailureEmail){

        BusiProductSub productSub = busiProductSubMapper.findProductSubBySubjectNo(subjectNo);
        if(productSub.getTotalInvestAmt().compareTo(productSub.getProductPrincipal()) == 0){
            int actualCollectDay = DateUtil.getDaysBetweenTwoDate(productSub.getSaleStartDate(), productSub.getSaleEndDate()) + 1;
            SubjectCollectResultDto collectResultDto =
                    new SubjectCollectResultDto(AppConstants.PARTNER_NO, subjectNo,
                            productSub.getProductPrincipal(), productSub.getTotalInvestPerson(), actualCollectDay);
            Map<String,Object> conditions = new HashMap<>();
            conditions.put("status",AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
            conditions.put("subjectNo",subjectNo);
            List<BusiOrderSub> orderSubs = busiOrderSubMapper.selectByMap(conditions);
            for (BusiOrderSub order : orderSubs) {
                collectResultDto.addOrderItem(new SubjectOrderItemDto(order.getOrderId(), order.getOrderAmt()));
            }
            collectResultDto.setProductNo(subjectNo);
            collectResultDto.setProductName(productSub.getProductName());
            collectResultDto.setProductInterest(productSub.getProductInterest());//产品总利息
            ResultDto result = new ResultDto();
            try {
                AssetsResultDto<String> resultDto = lcbSubjectFacadeService.collectResultNotify(collectResultDto);
                if (resultDto == null || !resultDto.isSuccess()) {
                    String msg = resultDto == null ? subjectNo + "调用标的失败" : subjectNo + "通知标的发生异常：" + resultDto.getMsg();
                    result = new ResultDto(msg,false);
                    if(sendFailureEmail) MailUtil.sendMail("满标通知失败", msg);
                }
            }catch (Exception e){
                log.error(e.getMessage(), e );
                result = new ResultDto(e.getMessage(), false);
                if(sendFailureEmail) MailUtil.sendMail("满标通知失败", subjectNo + "通知标的发生异常：" + e.getMessage());
            }
            return result;
        }else{
            return new ResultDto("标的尚未满标",false);
        }
    }
}
