package com.zdmoney.service.transfer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.transfer.TransferBorrowersReqDto;
import com.zdmoney.assets.api.dto.transfer.TransferPvResDto;
import com.zdmoney.assets.api.dto.transfer.TransferRepayPlanReqDto;
import com.zdmoney.assets.api.dto.transfer.TransferRevokeReqDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.assets.api.utils.DateUtils;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.dto.voucher.VoucherDto;
import com.zdmoney.integral.api.dto.voucher.VoucherSearchDto;
import com.zdmoney.integral.api.facade.IVoucherFacadeService;
import com.zdmoney.mapper.AtChMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.mapper.transfer.BusiDebtTransferMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.message.ContentType;
import com.zdmoney.message.api.dto.message.MsgMessageSendDto;
import com.zdmoney.message.api.dto.sm.SendSmNotifyReqDto;
import com.zdmoney.message.api.facade.IMsgMessageFacadeService;
import com.zdmoney.message.api.facade.ISmFacadeService;
import com.zdmoney.models.AtCh;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderIntegral;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payment.BusiSubjectPayPlan;
import com.zdmoney.models.payment.PaymentPlan;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.models.transfer.DebtTransferDetail;
import com.zdmoney.models.zdpay.UserGrantBO;
import com.zdmoney.service.*;
import com.zdmoney.service.payment.BusiSubjectPayPlanService;
import com.zdmoney.service.payment.PaymentPlanService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.vo.RepayPlanVo;
import com.zdmoney.web.dto.*;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.plan.DebtQueueDTO;
import com.zdmoney.webservice.api.facade.IFinancePlanFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import websvc.models.*;
import websvc.req.ReqMain;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class BusiDebtTransferService extends BaseService<BusiDebtTransfer, Long> {

    @Autowired
    private BusiOrderTempMapper busiOrderTempMapper;

    @Autowired
    private BusiProductMapper busiProductMapper;

    @Autowired
    private BusiProductSubMapper busiProductSubMapper;

    @Autowired
    private AtChMapper atChMapper;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private BusiSubjectPayPlanService busiSubjectPayPlanService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;

    @Autowired
    private IFinancePlanFacadeService financePlanFacadeService;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @Autowired
    RedisSessionManager redisSessionManager;

    @Autowired
    private IVoucherFacadeService voucherFacadeService;

    @Autowired
    private BusiOrderIntegralService busiOrderIntegralService;

    @Autowired
    private  BusiOrderService busiOrderService;

    @Autowired
    private CustomerGrantInfoMapper customerGrantInfoMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ISmFacadeService iSmFacadeService;

    @Autowired
    private IMsgMessageFacadeService iMsgMessageFacadeService;



    private BusiDebtTransferMapper getBusiDebtTransferMapper() {
        return (BusiDebtTransferMapper) baseMapper;
    }

    public BusiDebtTransfer getTransferByProductId(Long productId) {
        BusiDebtTransfer query = new BusiDebtTransfer();
        query.setProductId(productId);
        return getBusiDebtTransferMapper().selectOne(query);
    }

    public Result transferInit(ReqMain reqMain) throws Exception {
        TransferInitDTO initDTO = new TransferInitDTO();
        Model_500031 cdtModel = (Model_500031) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp order = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        if (!checkCanTransfer(order,reqMain)) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }

        Date currentDate = new Date();
        int leftDays = DateUtil.getIntervalDays2(order.getInterestEndDate(), currentDate);
        initDTO.setLeftDays(leftDays);
        int dayRange = 5;//转让日期区间 默认5天
        SysParameter parameter = sysParameterService.findOneByPrTypeWithoutCache("transferRange");
        if (parameter != null) {
            try {
                dayRange = Integer.parseInt(parameter.getPrValue());
            } catch (Exception e) {
                dayRange = 5;
            }
        }
        //当前转让时间之前有待还资金，不可转让
        List<PaymentPlan> paymentPlans =  paymentPlanService.selectPayPlansByCurrentTime(order.getOrderId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (!CollectionUtils.isEmpty(paymentPlans)){
            initDTO.setAlertType(1);
            initDTO.setAlertInfo("该产品您有一笔待还资金，尚不可发起转让,\n待该笔还款到账后方可发起转让");
        }

        List<Date> transferDateList = new ArrayList<>();
        for (int i = 1; i <= dayRange; i++) {
            Date nextDate = DateUtil.plusDay(currentDate, i);
            if (DateUtil.compareStringDate(nextDate, order.getInterestEndDate()) >= 0) {
                break;
            }
            //正常发起转让，显示还款日之前日期
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("orderNum", order.getOrderId());
            paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd").format(nextDate));
            List<PaymentPlan> paymentPlanList =paymentPlanService.selectPayPlansByDate(paramMap);
            if (!CollectionUtils.isEmpty(paymentPlanList)){
                break;
            }
            transferDateList.add(nextDate);
        }

        //查询最原始的订单
        BusiOrderTemp originOrder = getOriginOrder(order.getOrderId());
        if (originOrder == null) {
            originOrder = order;
        }
        List<TransferRepayPlanReqDto> planList = getPlanList(originOrder);
        //计算剩余本息-普通产品
        if (order.getProductType().equals(AppConstants.OrderProductType.COMMON)) {
            this.getCommonExpectPI(initDTO, order);
        }
        //计算剩余本息-标的产品
        else {
            this.getSubjectExpectPI(initDTO, order);
        }
        if (!CollectionUtils.isEmpty(transferDateList)){
            //查询转让日期 估值
            List<EstimateDto> estimateDtoList = subjectService.queryEstimatePrice(originOrder, transferDateList, planList);
            initDTO.setEstimateList(estimateDtoList);
        }else{
            initDTO.setAlertType(1);
            initDTO.setAlertInfo("该产品您有一笔待还资金，尚不可发起转让,\n待该笔还款到账后方可发起转让");
        }

        //查询协议
        List<ProtocolDTO> protocolList = subjectService.queryProtocalName();
        initDTO.setProtocolList(protocolList);

        return Result.success(initDTO);
    }


    /**
     * 计算普通产品待收本息
     */
    private void getCommonExpectPI(TransferInitDTO initDTO, BusiOrderTemp order) {
        initDTO.setPrincipal(CoreUtil.BigDecimalAccurate(order.getOrderAmt()));
        initDTO.setInterest(CoreUtil.BigDecimalAccurate(order.getPrincipalinterest().subtract(order.getOrderAmt())));
    }

    /**
     * 计算标的产品待收本息
     */
    private void getSubjectExpectPI(TransferInitDTO initDTO, BusiOrderTemp order) {
        List<String> status = Lists.newArrayList();
        status.add(AppConstants.PaymentPlanStatus.UNRETURN);
        List<PaymentPlan> list = paymentPlanService.selectPaymentPlans(order.getOrderId(), status);
        BigDecimal expectPrincipal = BigDecimal.ZERO;
        BigDecimal expectInterest = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            for (PaymentPlan paymentPlan : list) {
                expectPrincipal = expectPrincipal.add(paymentPlan.getPrincipal());
                expectInterest = expectInterest.add(paymentPlan.getInterest());
            }
        }
        initDTO.setPrincipal(CoreUtil.BigDecimalAccurate(expectPrincipal));
        initDTO.setInterest(CoreUtil.BigDecimalAccurate(expectInterest));
    }

    /**
     * 标的产品待收本息 v4.8
     */
    private TransferInitDTO getSubjectExpectPI(BusiOrderTemp order) {
        List<String> status = Lists.newArrayList();
        status.add(AppConstants.PaymentPlanStatus.UNRETURN);
        List<PaymentPlan> list = paymentPlanService.selectPaymentPlans(order.getOrderId(), status);
        BigDecimal expectPrincipal = BigDecimal.ZERO;
        BigDecimal expectInterest = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            for (PaymentPlan paymentPlan : list) {
                expectPrincipal = expectPrincipal.add(paymentPlan.getPrincipal());
                expectInterest = expectInterest.add(paymentPlan.getInterest());
            }
        }
        TransferInitDTO transferDto = new TransferInitDTO();
        transferDto.setPrincipal(CoreUtil.BigDecimalAccurate(expectPrincipal));
        transferDto.setInterest(CoreUtil.BigDecimalAccurate(expectInterest));

        return transferDto;
    }

    private TransferInitDTO getTransferInitDTO(BusiOrderTemp order){
        TransferInitDTO initDTO = new TransferInitDTO();
        if (order.getProductType().equals(AppConstants.OrderProductType.COMMON)) {
            initDTO.setPrincipal(CoreUtil.BigDecimalAccurate(order.getOrderAmt()));
            initDTO.setInterest(CoreUtil.BigDecimalAccurate(order.getPrincipalinterest().subtract(order.getOrderAmt())));
        }else{
            initDTO = getSubjectExpectPI(order);
        }
        return initDTO;
    }

    /**
     * 检查该笔订单是否可转让
     */
    private boolean checkCanTransfer(BusiOrderTemp order,ReqMain reqMain) {
        //默认不可转让
        boolean canTransfer = false;
        if (order == null) {
            log.error("can not transfer , reason : order not exist");
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        //加息订单不能转让  v11注释掉
        /*if (order.getYearRate().compareTo(order.getOriginalRate()) != 0) {
            log.error("can not transfer , reason : order rate not equal");
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }*/
        //转让订单判断
        if (AppConstants.OrderTransferStatus.ORDER_TRANSFER.equals(order.getTransferType())) {
            BusiDebtTransfer transferee = getBusiDebtTransferByOrderId(order.getCustomerId(), order.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE);
            if (transferee == null) {
                throw new BusinessException("该笔订单不能转让，转让记录不存在");
            }
            if (!transferee.getIsSettle().equals(AppConstants.TransferDebtStatus.TRANSFER_SETTLE_YES)) {
                throw new BusinessException("该笔订单不能转让，未交割");
            }
        }
        if (!(order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0) || order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14))) {
            log.error("can not transfer , orderId {} ,reason : status {}", order.getId(), order.getStatus());
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }
        //判断最后一期回款状态
        boolean lastFlag = this.judgeLastPlan(order.getOrderId());
        if (lastFlag) {
            log.error("can not transfer , orderId {} ,reason : 最后一期回款状态非待回款", order.getId());
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }
        BusiProduct product = busiProductMapper.selectByPrimaryKey(order.getProductId());
        BusiDebtTransfer transfer = getBusiDebtTransferByOrderId(order.getCustomerId(), order.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFER);
        if (transfer != null) {
            log.error("can not transfer , orderId {} ,reason : exist transfer record", order.getId());
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }
        if (product != null) {
            BusiProductContract busiProductContract = busiProductContractMapper.selectByPrimaryKey(product.getContractId());
            if (busiProductContract == null || "1".equals(busiProductContract.getStatus())) {
                if (product.getTransferStatus().equals(AppConstants.ProductTransferProperty.CAN_TRANSFER)) {
                    Date currentDate = new Date() ;
                    Date canTransferDate = currentDate;
                    //特殊理财人忽略转让天数
                    if (BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(reqMain.getReqHeadParam().getPlatform())) {
                        canTransfer = true;
                        log.info("特殊理财人转让,订单{}",order.getOrderId());
                    }else{
                        //普通人转让
                        if (product.getTransferDay() != null) {
                            canTransferDate = DateUtil.plusDay(order.getInterestStartDate(), product.getTransferDay());
                        }
                        if (DateUtil.compareStringDate(currentDate, canTransferDate) >= 0 && DateUtil.compareStringDate(DateUtil.plusDay(order.getInterestEndDate(), -1), currentDate) > 0) {
                            canTransfer = true;
                        }
                    }
                    //不包含起息日
                    if (DateUtil.compareStringDate(currentDate, order.getInterestStartDate()) <= 0) {
                        log.error("can not transfer , orderId {} ,reason :can not include interestStartDate", order.getId());
                        throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
                    }

                }
            }
        }
        return canTransfer;
    }


    public Result queryTransferRate(ReqMain reqMain) throws Exception {
        Model_500032 cdtModel = (Model_500032) reqMain.getReqParam();
        TransferRateDTO dto = new TransferRateDTO();
        Long orderId = cdtModel.getOrderId();
        Date transferDate = cdtModel.getTransferDate();
        BigDecimal transferPrice = cdtModel.getTransferPrice();
        BigDecimal estimatePrice = cdtModel.getEstimatePrice();
        BusiOrderTemp order = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        if (order == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(order.getProductId());
        if (busiProduct == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
        }
        //查询最原始的订单
        BusiOrderTemp originOrder = getOriginOrder(order.getOrderId());
        if (originOrder == null) {
            originOrder = order;
        }
        List<TransferRepayPlanReqDto> planList = getPlanList(originOrder);
        BigDecimal rate = subjectService.queryXirrValue(transferDate, transferPrice, originOrder, planList);
        if (rate == null) {
            throw new BusinessException("查询利率失败");
        }
        dto.setRate(rate);
        BigDecimal basicFeeRate = queryBasicFeeRate();
        BigDecimal benefitFeeRate = queryBenefitFeeRate();
        BigDecimal feeRate = basicFeeRate;
        if (benefitFeeRate != null && benefitFeeRate.compareTo(basicFeeRate) < 0) {
            dto.setDiscount(benefitFeeRate.divide(basicFeeRate, 2, RoundingMode.DOWN));
            feeRate = benefitFeeRate;
        }
        dto.setTransferFeeRateStr("转让价格的" + new DecimalFormat("0.0%").format(feeRate));
        BigDecimal transferFee = transferPrice.multiply(feeRate).setScale(2, RoundingMode.DOWN);
        dto.setTransferFee(transferFee);
        dto.setExceptReceivedAmount(transferPrice.subtract(transferFee).setScale(2, RoundingMode.DOWN));
        String tips = "";
        String tipsType = "";
        BigDecimal minTransferRate = busiProduct.getMinRate();
        BigDecimal maxTransferRate = busiProduct.getMaxRate();
        if (minTransferRate == null || maxTransferRate == null) {
            throw new BusinessException("转让利率范围不能为空");
        }
        if (rate.compareTo(minTransferRate) >= 0 && rate.compareTo(maxTransferRate) <= 0) {
            if (transferPrice.compareTo(estimatePrice) > 0) {
                tipsType = "3";
                tips = "价格较高，转让成功机率越小";
            } else if (transferPrice.compareTo(estimatePrice) == 0) {
                tipsType = "2";
                tips = "价格适中";
            } else {
                tipsType = "1";
                tips = "价格较低，转让成功机率越大";
            }
        } else {
            tipsType = "4";
            tips = "价格超出合理范围，建议转让利率区间为" + new DecimalFormat("0.0%").format(minTransferRate) + "-" + new DecimalFormat("0.0%").format(maxTransferRate);
        }
        dto.setTips(tips);
        dto.setType(tipsType);
        return Result.success(dto);
    }

    @Transactional
    public Result doTransfer(ReqMain reqMain,Model_500033 cdtModel)throws Exception{
        TransferDTO resultDto = new TransferDTO();
        Long orderId = cdtModel.getOrderId();
        Long customerId = cdtModel.getCustomerId();
        BigDecimal transferPrice = cdtModel.getTransferPrice();
        Date transferDate = cdtModel.getTransferDate();

        if (DateUtil.compareStringDate(new Date(), transferDate) >= 0) {
            throw new BusinessException("非法的转让日期");
        }
        //验证用户信息
        CustomerMainInfo customerMainInfo = customerMainInfoService.validateCustomerInfo(customerId);
        BusiOrderTemp busiOrder = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        if (busiOrder == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        if (!busiOrder.getCustomerId().equals(customerId)) {
            throw new BusinessException("订单信息与用户信息不符");
        }
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(busiOrder.getProductId());
        if (busiProduct == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
        }
        //验证是否可转让
        if (!checkCanTransfer(busiOrder,reqMain)) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }
        //当前转让时间之前有待还资金，不可转让
        List<PaymentPlan> paymentPlans =  paymentPlanService.selectPayPlansByCurrentTime(busiOrder.getOrderId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (!CollectionUtils.isEmpty(paymentPlans)){
            throw new BusinessException("该产品您有一笔待还资金，尚不可发起转让,\n待该笔还款到账后方可发起转让");
        }
        //选择转让日期如果是还款日，不可转让
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderNum", busiOrder.getOrderId());
        paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd").format(transferDate));
        List<PaymentPlan> paymentPlanList =paymentPlanService.selectPayPlansByDate(paramMap);
        if (!CollectionUtils.isEmpty(paymentPlanList)){
            throw new BusinessException("转让日期和还款日相同，不符合转让条件.");
        }

        //查询最原始的订单
        BusiOrderTemp originOrder = getOriginOrder(busiOrder.getOrderId());
        if (originOrder == null) {
            originOrder = busiOrder;
        }
        List<TransferRepayPlanReqDto> planList = getPlanList(originOrder);
        //计算利率
        BigDecimal rate = subjectService.queryXirrValue(transferDate, transferPrice, originOrder, planList);
        if (rate == null) {
            throw new BusinessException("查询利率失败");
        }
        if (busiProduct.getMinRate() == null || busiProduct.getMaxRate() == null) {
            throw new BusinessException("转让利率范围不能为空");
        }
        if (rate.compareTo(busiProduct.getMinRate()) < 0 || rate.compareTo(busiProduct.getMaxRate()) > 0) {
            throw new BusinessException("转让利率超出合理范围");
        }
        //转让手续费
        BigDecimal basicFeeRate = queryBasicFeeRate();
        BigDecimal benefitFeeRate = queryBenefitFeeRate();
        BigDecimal feeRate = basicFeeRate;
        if (benefitFeeRate != null && benefitFeeRate.compareTo(basicFeeRate) < 0) {
            feeRate = benefitFeeRate;
        }
        BigDecimal transferFee = transferPrice.multiply(feeRate).setScale(2, RoundingMode.DOWN);

        //判断授权是否充足
        checkTransferAuth(customerId,transferFee);

       //计算产品利息
        BigDecimal productInterest = getInterest(busiOrder, transferPrice, transferDate);
        //生成转让产品
        String transferProductName = createTransferProductName(originOrder);
        //折扣比例 （产品估值-转让价格）/产品估值
        BigDecimal discountRate = (busiOrder.getOrderAmt().subtract(transferPrice)).divide(busiOrder.getOrderAmt(),18,BigDecimal.ROUND_DOWN).setScale(4,RoundingMode.DOWN);
        /*if(discountRate.compareTo(BigDecimal.ZERO)<0){
            throw new BusinessException("折让比例不能小于0");
        }*/
        BusiProduct newProduct = createTransferProduct(transferProductName, productInterest, busiOrder, busiProduct, transferPrice, transferDate, rate,originOrder,discountRate,null);
        //生成转让单
        BusiDebtTransfer transfer = createDebtTransfer(customerId, newProduct.getId(), busiProduct, busiOrder, originOrder, transferPrice, transferDate, transferFee, rate, feeRate);
        resultDto.setTransferFeeRate(feeRate);
        //通知标的系统，获取回款计划
        List<Date> transferDateList = new ArrayList<>();
        transferDateList.add(transferDate);
        List<EstimateDto> estimateDtoList = subjectService.queryEstimatePrice(originOrder, transferDateList, planList);
        if (CollectionUtils.isEmpty(estimateDtoList)) {
            throw new BusinessException("查询估值失败");
        }
        BigDecimal estimatePrice = new BigDecimal(estimateDtoList.get(0).getEstimatePrice());
        //封装原始订单借款人信息
        AtCh atchQuery = new AtCh();
        atchQuery.setOrderId(BigDecimal.valueOf(originOrder.getId()));
        List<AtCh> atchList = atChMapper.select(atchQuery);
        List<TransferBorrowersReqDto> reqList = Lists.newArrayList();
        for (AtCh atch : atchList) {
            TransferBorrowersReqDto reqDto = new TransferBorrowersReqDto();
            reqDto.setTransferNo(transfer.getTransferNo());
            reqDto.setBorrowerIdNo(atch.getLenderIdno());
            reqDto.setBorrowerName(atch.getLenderName());
            reqDto.setBorrowPurpose(atch.getYt());
            reqList.add(reqDto);
        }
        List<BusiSubjectPayPlan> payPlanList = subjectService.notifyTransferToSubject(resultDto, reqList, feeRate, planList, estimatePrice, customerMainInfo, transfer, newProduct, originOrder);
        //新增回款计划
        for (BusiSubjectPayPlan payPlan : payPlanList) {
            busiSubjectPayPlanService.save(payPlan);
        }
        resultDto.setTransferDate(transferDate);
        resultDto.setApplyDate(new Date());

        return Result.success(resultDto);
    }

    //转让授权判断
    private void checkTransferAuth(Long customerId,BigDecimal transferFee){
        //授权是否充足
        Map  authInfos = busiOrderService.userGrantFlag(customerId,1,transferFee);// 0:充足 1：不足
        String grantFlag = authInfos.get("grantFlag").toString();
        if ("1".equals(grantFlag)){
            throw new BusinessException("授权不足，无法转让");
        }

    }

    @Transactional
    public Result transfer(ReqMain reqMain) throws Exception {
        Model_500033 cdtModel = (Model_500033) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        String key = "T_" + orderId + "_lock";
        boolean success = redisSessionManager.setNX(key, "1");
        Result result = Result.fail("已发起转让，请稍作等待");
        if(success){
            try{
                result = doTransfer(reqMain,cdtModel);
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw e;
            }finally {
                redisSessionManager.remove(key);
            }
        }
        return result;
    }

    @Transactional
    public Result transfers(ReqMain reqMain) throws Exception {
        Model_500044 cdtModel = (Model_500044) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        String key = "T_" + orderId + "_lock";
        boolean success = redisSessionManager.setNX(key, "1");
        Result result = Result.fail("已发起转让，请稍作等待");
        if(success){
            try{
                result = doTransfers(reqMain,cdtModel);
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw e;
            }finally {
                redisSessionManager.remove(key);
            }
        }
        return result;
    }
    @Transactional
    public Result doTransfers(ReqMain reqMain,Model_500044 cdtModel)throws Exception{
        TransferDTO resultDto = new TransferDTO();
        Long orderId = cdtModel.getOrderId();
        Long customerId = cdtModel.getCustomerId();
        BigDecimal transferPrice = cdtModel.getTransferPrice();
        Date transferDate = cdtModel.getTransferDate();


        if (DateUtil.compareStringDate(new Date(), transferDate) >= 0) {
            throw new BusinessException("非法的转让日期");
        }
        //验证用户信息
        CustomerMainInfo customerMainInfo = customerMainInfoService.validateCustomerInfo(customerId);
        BusiOrderTemp busiOrder = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        if (busiOrder == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        if (!busiOrder.getCustomerId().equals(customerId)) {
            throw new BusinessException("订单信息与用户信息不符");
        }
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(busiOrder.getProductId());
        if (busiProduct == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
        }
        //验证是否可转让
        if (!checkCanTransfer(busiOrder,reqMain)) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }
        //当前转让时间之前有待还资金，不可转让
        List<PaymentPlan> paymentPlans =  paymentPlanService.selectPayPlansByCurrentTime(busiOrder.getOrderId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (!CollectionUtils.isEmpty(paymentPlans)){
            throw new BusinessException("该产品您有一笔待还资金，尚不可发起转让,\n待该笔还款到账后方可发起转让");
        }
        //选择转让日期如果是还款日，不可转让
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderNum", busiOrder.getOrderId());
        paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd").format(transferDate));
        List<PaymentPlan> paymentPlanList =paymentPlanService.selectPayPlansByDate(paramMap);
        if (!CollectionUtils.isEmpty(paymentPlanList)){
            throw new BusinessException("转让日期和还款日相同，不符合转让条件.");
        }
        //转让价格超出上限提示
        TransferInitDTO transferInitDTO = getTransferInitDTO(busiOrder);
        BigDecimal maxAmt = new BigDecimal(transferInitDTO.getPrincipal()).add(new BigDecimal(transferInitDTO.getInterest()));
        if (transferPrice.compareTo(maxAmt)>=0){
            throw new BusinessException("转让价格不能超出剩余本息和.");
        }

        //查询最原始的订单
        BusiOrderTemp originOrder = getOriginOrder(busiOrder.getOrderId());
        if (originOrder == null) {
            originOrder = busiOrder;
        }
        //查询最原始产品
        BusiProduct originProduct = busiProductMapper.selectByPrimaryKey(originOrder.getProductId());

        List<TransferRepayPlanReqDto> planList = getPlanList(originOrder);

        //通知标的系统，获取回款计划
        List<Date> transferDateList = new ArrayList<>();
        transferDateList.add(transferDate);
        List<EstimateDto> estimateDtoList = subjectService.queryEstimatePrice(originOrder, transferDateList, planList);
        if (CollectionUtils.isEmpty(estimateDtoList)) {
            throw new BusinessException("查询估值失败");
        }

        //产品估值
        BigDecimal estimatePrice = new BigDecimal(BigInteger.ZERO);
        for (EstimateDto estimate : estimateDtoList) {
            if (DateUtils.isSameDay(estimate.getDate(), transferDate)) {
                estimatePrice = Convert.toBigDecimal(estimate.getEstimatePrice());
                BigDecimal maxpv = Convert.toBigDecimal(estimate.getMaxpv());
                BigDecimal mixpv = Convert.toBigDecimal(estimate.getMinpv());
                boolean flag = estimatePrice.compareTo(mixpv) >= 0 && estimatePrice.compareTo(maxpv) <= 0;
                if (!flag) {
                    throw new BusinessException("当前转让价格不在允许的转让价格范围内");
                }
            }
        }

        BigDecimal rate = subjectService.queryXirrValue(cdtModel.getTransferDate(), transferPrice, originOrder, planList);

        //转让手续费
        BigDecimal basicFeeRate = queryBasicFeeRate();
        BigDecimal benefitFeeRate = queryBenefitFeeRate();
        BigDecimal feeRate = basicFeeRate;
        if (benefitFeeRate != null && benefitFeeRate.compareTo(basicFeeRate) < 0) {
            feeRate = benefitFeeRate;
        }
        BigDecimal transferFee = transferPrice.multiply(feeRate).setScale(2, RoundingMode.DOWN);

        //判断授权是否充足
        checkTransferAuth(customerId,transferFee);

        //判断授权是否充足
        checkTransferAuth(customerId,transferFee);

        //计算产品利息
        BigDecimal productInterest = getInterest(busiOrder, transferPrice, transferDate);
        //生成转让产品
        String transferProductName = createTransferProductName(originOrder);
        //折扣比例 （产品估值-转让价格）/产品估值
        BigDecimal discountRate = (estimatePrice.subtract(transferPrice)).divide(estimatePrice,18,BigDecimal.ROUND_DOWN).setScale(4,RoundingMode.DOWN);
        /*if(discountRate.compareTo(BigDecimal.ZERO)<0){
            throw new BusinessException("折让比例不能小于0");
        }*/
        BusiProduct newProduct = createTransferProduct(transferProductName, productInterest, busiOrder, busiProduct, transferPrice, transferDate, rate,originOrder,discountRate,estimatePrice);
        //生成转让单
        BusiDebtTransfer transfer = createDebtTransfer(customerId, newProduct.getId(), busiProduct, busiOrder, originOrder, transferPrice, transferDate, transferFee, rate, feeRate);
        resultDto.setTransferFeeRate(feeRate);

        //封装原始订单借款人信息
        AtCh atchQuery = new AtCh();
        atchQuery.setOrderId(BigDecimal.valueOf(originOrder.getId()));
        List<AtCh> atchList = atChMapper.select(atchQuery);
        List<TransferBorrowersReqDto> reqList = Lists.newArrayList();
        for (AtCh atch : atchList) {
            TransferBorrowersReqDto reqDto = new TransferBorrowersReqDto();
            reqDto.setTransferNo(transfer.getTransferNo());
            reqDto.setBorrowerIdNo(atch.getLenderIdno());
            reqDto.setBorrowerName(atch.getLenderName());
            reqDto.setBorrowPurpose(atch.getYt());
            reqList.add(reqDto);
        }
        List<BusiSubjectPayPlan> payPlanList = subjectService.notifyTransferToSubject(resultDto, reqList, feeRate, planList, estimatePrice, customerMainInfo, transfer, newProduct, originOrder);
        //新增回款计划
        for (BusiSubjectPayPlan payPlan : payPlanList) {
            busiSubjectPayPlanService.save(payPlan);
        }
        //更新用户转让授权缴费额度
        updateUserGrant(customerId,transferFee);

        resultDto.setTransferDate(transferDate);
        resultDto.setApplyDate(new Date());
        return Result.success(resultDto);

    }
    /**
     * 撤销转让
     */
    public Result cancelTransfer(ReqMain reqMain) throws Exception {
        TransferRevokeReqDto reqDto = new TransferRevokeReqDto();
        Model_500035 model_500035 = (Model_500035) reqMain.getReqParam();
        String orderNo = model_500035.getOrderId();
        String customerId = model_500035.getCustomerId();
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
        if (customerMainInfo == null) {
            throw new BusinessException("查询不到用户信息！");
        }
        BusiOrderTemp order = busiOrderTempMapper.selectViewByPrimaryKey(Long.valueOf(orderNo));
        if (order == null) {
            throw new BusinessException("该订单信息不存在！");
        }
        //        BusiDebtTransfer query = new BusiDebtTransfer();
//        query.setInitOrderNo(order.getOrderId());//orderNum
//        BusiDebtTransfer busiDebtTransfer = getBusiDebtTransferMapper().selectOne(query);

        BusiDebtTransfer busiDebtTransfer = new BusiDebtTransfer();
        Example example = new Example(BusiDebtTransfer.class);
        example.createCriteria().andEqualTo("initOrderNo", order.getOrderId());
        example.setOrderByClause("ID desc");
        List<BusiDebtTransfer> busiDebtTransfers = getBusiDebtTransferMapper().selectByExample(example);

        if (CollectionUtils.isEmpty(busiDebtTransfers)) {
            throw new BusinessException("该订单转让记录不存在！");
        }
        if (!CollectionUtils.isEmpty(busiDebtTransfers)) {
            busiDebtTransfer = busiDebtTransfers.get(0);
        }

        if (!busiDebtTransfer.getTransferStatus().equals("0")) {
            throw new BusinessException("该状态不可撤销转让");
        }

        String transferNo = busiDebtTransfer.getTransferNo();
        if (StringUtils.isNotBlank(transferNo)) {
            reqDto.setPartnerNo("LCB");
            reqDto.setOperator(customerId);
            reqDto.setTransferNo(transferNo);

            AssetsResultDto resultDto = lcbSubjectFacadeService.transferRevoke(reqDto);
            if (resultDto.isSuccess()) {

                updateBusiStatus(busiDebtTransfer);
            } else {
                log.warn("customerId={} revoke transfer no={} msg={} error.", customerId, transferNo, resultDto.getMsg());
                throw new BusinessException("撤销转让失败！");
            }
        }
        return Result.success();
    }


    @Transactional
    private void updateBusiStatus(BusiDebtTransfer busiDebtTransfer) {
        try {
            //更新产品表
            BusiProduct product = new BusiProduct();
            product.setId(busiDebtTransfer.getProductId());
            product.setUpLowFlag("0");
            product.setProductMemo("撤销转让");
            busiProductMapper.updateByPrimaryKeySelective(product);

            BusiProductSub productSub = new BusiProductSub();
            PropertyUtils.copyProperties(productSub, product);
            busiProductSubMapper.updateByPrimaryKeySelective(productSub);

            log.info("撤销转让更新产品ProductId " + busiDebtTransfer.getProductId() + " 成功   ");
            //更新转让表
            BusiDebtTransfer bufer = new BusiDebtTransfer();
            bufer.setTransferStatus("4");
            bufer.setId(busiDebtTransfer.getId());
            bufer.setUpdateDate(new Date());
            getBusiDebtTransferMapper().updateByPrimaryKeySelective(bufer);
            log.info("撤销转让更新转让Id " + busiDebtTransfer.getId() + " 成功:   ");
        } catch (Exception e) {
            log.info("撤销转让更新产品订单信息失败");
            throw new BusinessException("撤销转让失败！");
        }
    }


    /**
     * 生成转让产品
     */
    private BusiProduct createTransferProduct(String productName, BigDecimal interest, BusiOrderTemp order, BusiProduct busiProduct, BigDecimal transferPrice, Date transferDate, BigDecimal transferRate,BusiOrderTemp originOrder,BigDecimal discountRate,BigDecimal estimatePrice) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        BusiProduct product = new BusiProduct();
        Date date = new Date();
        product.setProductName(productName);
        product.setYearRate(transferRate);
        product.setProductDesc(busiProduct.getProductDesc());
        product.setProductFeature(busiProduct.getProductFeature());
        product.setInvestLower(transferPrice);
        product.setInvestUpper(transferPrice);
        product.setInterestStartDate(transferDate);
        product.setInterestEndDate(order.getInterestEndDate());
        product.setSaleStartDate(date);
        product.setSaleEndDate(DateUtil.getDateEndTime(DateUtil.getDateBefore(transferDate, 1)));
        product.setRepayType(busiProduct.getRepayType());
        product.setFundArrivalDesc(busiProduct.getRepayType());
        product.setRiskMeasures(busiProduct.getRiskMeasures());
        product.setIncrementAmount(BigDecimal.ZERO);
        product.setProductPrincipal(transferPrice);
        product.setInterestRule(busiProduct.getInterestRule());
        product.setUpLowFlag(AppConstants.ProductUpLowStatus.PRODUCT_UP);
        product.setTopFlag("0");//未置顶
        product.setHotSellFlag("0");//未热销
        product.setAuditFlag(AppConstants.ProductAuditStatus.PRODUCT_AUDIT_PASS);
        product.setInvitationCodeFlag("0");//不需要邀请码
        product.setTotalInvestPerson(0);
        product.setTotalInvestAmt(BigDecimal.ZERO);
        product.setIsNewHand("0");
        product.setVmTotalInvestPersonNumber(0L);
        product.setVmTotalInvestAmt(BigDecimal.ZERO);

        product.setAddInterest(BigDecimal.ZERO);
        product.setShowStartDate(DateUtil.addMinutes(date, 30));
        product.setContractId(busiProduct.getContractId());
        product.setContractType(busiProduct.getContractType());
        product.setProductMemo(busiProduct.getProductMemo());
        product.setMarketing(busiProduct.getMarketing());
        product.setProductUeditor(busiProduct.getProductUeditor());
        product.setFundType(busiProduct.getFundType());
        product.setRepaySource(busiProduct.getRepaySource());
        product.setScurityMode(busiProduct.getScurityMode());
        product.setProductChannel(busiProduct.getProductChannel());
        product.setSubjectNo(busiProduct.getSubjectNo());
        product.setProductInterest(interest);
        product.setIsTransfer(AppConstants.ProductTransferStatus.TRANSFER_PRODUCT);
        //剩余可转让次数
        Integer transferTimes = busiProduct.getTransferTimes() - 1;
        Integer days = DateUtil.getIntervalDays2(busiProduct.getInterestEndDate(), date);
        if (transferTimes > 0 && days > busiProduct.getTransferDay()) {
            product.setTransferStatus(AppConstants.ProductTransferProperty.CAN_TRANSFER);
        } else {
            product.setTransferStatus(AppConstants.ProductTransferProperty.CAN_NOT_TRANSFER);
        }
        product.setTransferTimes(transferTimes);
        product.setTransferDay(busiProduct.getTransferDay());
        product.setLimitType(0L);
        product.setMinRate(busiProduct.getMinRate());
        product.setMaxRate(busiProduct.getMaxRate());
        product.setPcTop("0");
        product.setPcSort(0);
        product.setIsRecommend("0");
        product.setProductType(busiProduct.getProductType());
        product.setCooperativeDesc(busiProduct.getCooperativeDesc());
        product.setIsArea("0");
        product.setPersonLoan(busiProduct.getPersonLoan());
        product.setSubjectType(busiProduct.getSubjectType());

        //v4.3 新增
        BusiProduct orginProduct=getOriginProduct(originOrder);
        product.setProductRank(orginProduct.getProductRank()==null?0:orginProduct.getProductRank());
        product.setPurchaseCondition(covertStrIsNull(orginProduct.getPurchaseCondition()));
        product.setRepaymentGuarantee(covertStrIsNull(orginProduct.getRepaymentGuarantee()));
        product.setItemInfo(covertStrIsNull(orginProduct.getItemInfo()));
        product.setBorrowUse(covertStrIsNull(orginProduct.getBorrowUse()));
        product.setTargetConsumer(orginProduct.getTargetConsumer());

        //v4.8
        product.setDiscountRate(discountRate);
        product.setEstimatePrice(estimatePrice);
        product.setOriginYearRate(orginProduct.getYearRate());
        product.setYearRateInit(orginProduct.getYearRate());


        int result = busiProductMapper.insert(product);
        if (result != 1) {
            throw new BusinessException("创建转让产品失败！");
        }
        BusiProductSub productSub = new BusiProductSub();
        PropertyUtils.copyProperties(productSub, product);
        int res = busiProductSubMapper.saveProductSub(productSub);
        if (res != 1) {
            throw new BusinessException("创建转让子产品备份失败！");
        }
        return product;
    }
    private String covertStrIsNull(String str){
        if (str==null){
            str="";
        }
        return str;
    }
    /**
     * 生成转让单
     */
    private BusiDebtTransfer createDebtTransfer(Long customerId, Long newProductId, BusiProduct busiProduct, BusiOrderTemp order, BusiOrderTemp originOrder, BigDecimal transferPrice, Date transferDay, BigDecimal transferFee, BigDecimal transferRate, BigDecimal feeRate) throws BusinessException {
        BusiDebtTransfer transfer = new BusiDebtTransfer();
        Date date = new Date();
        transfer.setTransferNo(SerialNoGeneratorService.generatorTransferNo(customerId));
        transfer.setTransferId(customerId);
        transfer.setInitOrderNo(order.getOrderId());
        transfer.setPubDate(date);
        transfer.setTransferStatus(AppConstants.DebtTransferStatus.TRANSFER_INIT);
        transfer.setTransferPrice(transferPrice);
        transfer.setTransferDate(transferDay);
        transfer.setRealTransferDate(transfer.getTransferDate());
        transfer.setTransferCharge(transferFee);
        //计算剩余天数
        transfer.setLeftDay(DateUtil.getIntervalDays2(order.getInterestEndDate(), transferDay) + 1);
        transfer.setTransferRate(transferRate);
        transfer.setIsSettle(AppConstants.TransferDebtStatus.TRANSFER_SETTLE_NO);
        transfer.setProductId(newProductId);
        transfer.setInitProductId(busiProduct.getId());
        transfer.setProductType(busiProduct.getSubjectType());
        transfer.setUpdateDate(date);
        transfer.setServiceRate(feeRate);
        //存储最原始的订单号
        transfer.setOriginOrderNo(originOrder.getOrderId());
        transfer.setOriginProductId(originOrder.getProductId());

        RepayPlanVo repayPlanVo = calcuteRepayPlanAmt(order.getOrderId());
        //用户待收本金
        transfer.setInitPrincipal(repayPlanVo.getTotalInitPrincipal());
        //用户待收利息
        transfer.setInitInterest(repayPlanVo.getTotalInitInterest());
        int i = getBusiDebtTransferMapper().insert(transfer);
        if (i != 1) {
            throw new BusinessException("生成转让单失败");
        }
        return transfer;
    }

    //计算回款计划待还本金
    public BigDecimal calcuteCrediAmount(String orderNum){
        BigDecimal totalLeftPropical = BigDecimal.ZERO;
        List<String> status = Lists.newArrayList();
        status.add(AppConstants.PaymentPlanStatus.UNRETURN);
        List<PaymentPlan> paymentPlans = paymentPlanService.selectPaymentPlans(orderNum, status);
        if (!CollectionUtils.isEmpty(paymentPlans)) {
            for(PaymentPlan paymentPlan : paymentPlans){
                totalLeftPropical = totalLeftPropical.add(paymentPlan.getPrincipal());
            }
        }
        return totalLeftPropical;
    }
    //计算回款计划待还信息
    public RepayPlanVo calcuteRepayPlanAmt(String orderNum){
        RepayPlanVo repayPlanVo = new RepayPlanVo();
        BigDecimal totalInitPrincipal = BigDecimal.ZERO;
        BigDecimal totalInitInterest = BigDecimal.ZERO;
        List<String> status = Lists.newArrayList();
        status.add(AppConstants.PaymentPlanStatus.UNRETURN);
        List<PaymentPlan> paymentPlans = paymentPlanService.selectPaymentPlans(orderNum, status);
        if (!CollectionUtils.isEmpty(paymentPlans)) {
            for(PaymentPlan paymentPlan : paymentPlans){
                totalInitPrincipal = totalInitPrincipal.add(paymentPlan.getPrincipal());
                totalInitInterest = totalInitInterest.add(paymentPlan.getInterest());
            }
        }
        repayPlanVo.setTotalInitPrincipal(totalInitPrincipal);
        repayPlanVo.setTotalInitInterest(totalInitInterest);

        return repayPlanVo;
    }

    /**
     * 查询还款计划列表
     */
    private List<TransferRepayPlanReqDto> getPlanList(BusiOrderTemp order) {
        List<TransferRepayPlanReqDto> planList = Lists.newArrayList();
        if (order.getProductType().equals(AppConstants.OrderProductType.COMMON)) {
            TransferRepayPlanReqDto reqDto = new TransferRepayPlanReqDto();
            reqDto.setPlanNo(order.getId().toString() + DateUtil.getDateFormatString(new Date(), DateUtil.dateFormat));
            reqDto.setPrincipalInterest(order.getPrincipalinterest());
            reqDto.setPrincipal(order.getOrderAmt());
            reqDto.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
            reqDto.setTerm(1);
            reqDto.setRepayDay(DateUtil.plusDay(order.getInterestEndDate(), 1));
            planList.add(reqDto);
        } else {
//            PaymentPlan queryPlan = new PaymentPlan();
//            queryPlan.setOrderNum(order.getOrderId());
            List<PaymentPlan> paymentPlanList = paymentPlanService.selectPaymentPlans(order.getOrderId(), null);
            for (PaymentPlan paymentPlan : paymentPlanList) {
                TransferRepayPlanReqDto reqDto = new TransferRepayPlanReqDto();
                reqDto.setPlanNo(paymentPlan.getNo());
                reqDto.setRepayDay(paymentPlan.getRepayDay());
                reqDto.setTerm(paymentPlan.getCurrTerm());
                reqDto.setInterest(paymentPlan.getInterest());
                reqDto.setPrincipal(paymentPlan.getPrincipal());
                reqDto.setPrincipalInterest(paymentPlan.getPrincipalInterest());
                planList.add(reqDto);
            }
        }
        if (CollectionUtils.isEmpty(planList)) {
            throw new BusinessException("查询还款计划失败");
        }
        return planList;
    }

    /**
     * 计算利息
     */
    private BigDecimal getInterest(BusiOrderTemp order, BigDecimal transferPirce, Date transferDate) {
        BigDecimal interest = BigDecimal.ZERO;
        //一次性还本付息
        if (order.getPayType().equals("1")) {
            interest = order.getPrincipalinterest().subtract(transferPirce);
        }
        //等额本息或者先息后本
        else {
            BigDecimal principalAndInterest = BigDecimal.ZERO;
//            PaymentPlan queryPlan = new PaymentPlan();
//            queryPlan.setOrderNum(order.getOrderId());
            List<String> status = Lists.newArrayList();
            status.add(AppConstants.PaymentPlanStatus.UNRETURN);
            List<PaymentPlan> paymentPlanList = paymentPlanService.selectPaymentPlans(order.getOrderId(), status);
            for (PaymentPlan paymentPlan : paymentPlanList) {
                if (DateUtil.compareStringDate(paymentPlan.getRepayDay(), transferDate) >= 0) {
                    principalAndInterest = principalAndInterest.add(paymentPlan.getPrincipalInterest());
                }
            }
            interest = principalAndInterest.subtract(transferPirce);
        }
        return interest;
    }

    /**
     * 查询基础手续费率
     */
    private BigDecimal queryBasicFeeRate() {
        BigDecimal rate = null;
        try {
            SysParameter parameter = sysParameterService.findOneByPrTypeWithoutCache("transferFeeRate");
            if (parameter != null) {
                rate = new BigDecimal(parameter.getPrValue());
            }
        } catch (Exception e) {
            throw new BusinessException("查询手续费比例失败");
        }
        if (rate == null) {
            throw new BusinessException("查询手续费比例失败");
        }
        return rate;
    }

    /**
     * 查询优惠手续费率
     */
    private BigDecimal queryBenefitFeeRate() {
        BigDecimal rate = null;
        try {
            SysParameter parameter = sysParameterService.findOneByPrTypeWithoutCache("benefitFeeRate");
            if (parameter != null) {
                rate = new BigDecimal(parameter.getPrValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }


    /**
     * 查询转让前协议内容
     */
    public Result queryBeforeTransferProtocol(ReqMain reqMain) {
        BeforeTransferProtocolDTO dto = new BeforeTransferProtocolDTO();
        Model_500037 cdtModel = (Model_500037) reqMain.getReqParam();
        String protocolNo = cdtModel.getProtocolNo();
        String content = subjectService.queryBeforeTransferProtocal(protocolNo);
        dto.setContent(content);
        return Result.success(dto);
    }

    /**
     * 查询转让后协议内容
     */
    public Result queryAfterTransferProtocol(ReqMain reqMain) {
        Model_500038 cdtModel = (Model_500038) reqMain.getReqParam();
        String transferNo = cdtModel.getTransferNo();
        Map map = subjectService.queryAfterTransferProtocal(transferNo);
        return Result.success(map);
    }

    /**
     * 生成转让编号
     */
    private String buildTransferNo(Long customerId) {
        String transferNo = "T" + customerId;
        SimpleDateFormat timeStrFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        transferNo += timeStrFormat.format(new Date());
        transferNo += (int) (Math.random() * (100));
        return transferNo;
    }


    public BusiDebtTransfer getBusiDebtTransferByOrderId(Long customerId, String orderId, String transferDirect) {
        BusiDebtTransfer busiDebtTransfer = null;
        Map map = Maps.newTreeMap();
        if (AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFER.equals(transferDirect)) {
            map.put("transferId", customerId);
            map.put("initOrderNo", orderId);
        } else if (AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE.equals(transferDirect)) {
            map.put("buyId", customerId);
            map.put("newOrderNo", orderId);
        } else {
            return null;
        }
        List<BusiDebtTransfer> list = getBusiDebtTransferMapper().getTransfersByParam(map);
        if (!CollectionUtils.isEmpty(list)) {
            busiDebtTransfer = list.get(0);
        }
        return busiDebtTransfer;
    }

    public BusiDebtTransfer getByProductIdAndCmId(Long customerId, Long productId) {
        BusiDebtTransfer busiDebtTransfer = null;
        Example example = new Example(BusiDebtTransfer.class);
        example.createCriteria().andEqualTo("transferId", customerId).andEqualTo("productId", productId);
        example.setOrderByClause("update_date desc");
        List<BusiDebtTransfer> list = getBusiDebtTransferMapper().selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            busiDebtTransfer = list.get(0);
        }
        return busiDebtTransfer;
    }


    /**
     * 获取最原始订单信息
     */
    private BusiOrderTemp getOriginOrder(String orderId) {
        BusiOrderTemp orderTemp = null;
        Example example = new Example(BusiDebtTransfer.class);
        example.createCriteria().andEqualTo("newOrderNo", orderId).andEqualTo("transferStatus", AppConstants.DebtTransferStatus.TRANSFER_FINISH);
        List<BusiDebtTransfer> list = getBusiDebtTransferMapper().selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            BusiDebtTransfer transfer = list.get(0);
            if (StringUtils.isNotBlank(transfer.getOriginOrderNo())) {
                orderTemp = busiOrderTempMapper.selectViewByOrderNo(transfer.getOriginOrderNo());
            }
        }
        return orderTemp;
    }

    public BusiDebtTransfer getSuccessBusiDebtTransfer(Long customerId, String orderId, String transferDirect) {
        BusiDebtTransfer busiDebtTransfer = null;
        Example example = new Example(BusiDebtTransfer.class);
        List<String> status = Lists.newArrayList();
        status.add(AppConstants.DebtTransferStatus.TRANSFER_FINISH);
        status.add(AppConstants.DebtTransferStatus.TRANSFER_SUCCESS);
        if (AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFER.equals(transferDirect)) {
            example.createCriteria().andEqualTo("transferId", customerId)
                    .andEqualTo("initOrderNo", orderId).andIn("transferStatus", status);
        } else if (AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE.equals(transferDirect)) {
            example.createCriteria().andEqualTo("buyId", customerId)
                    .andEqualTo("newOrderNo", orderId).andIn("transferStatus", status);
        } else {
            return null;
        }
        example.setOrderByClause("update_date desc");
        List<BusiDebtTransfer> list = getBusiDebtTransferMapper().selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            busiDebtTransfer = list.get(0);
        }
        return busiDebtTransfer;
    }

    //生成转让产品名称
    private String createTransferProductName(BusiOrderTemp originOrder) {
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(originOrder.getProductId());
        if (busiProduct == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
        }
        int id = getBusiDebtTransferMapper().getNewNo(busiProduct.getId());
        String transferProductName = busiProduct.getProductName() + "-" + id;
        return transferProductName;

    }
   //根据最原始订单信息获取关联产品信息
   private BusiProduct getOriginProduct(BusiOrderTemp originOrder) {
       BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(originOrder.getProductId());
       if (busiProduct == null) {
           throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
       }
       return busiProduct;
   }

    /**
     * 判断回款计划最后一期状态是否可转让
     *
     * @param orderNum
     * @return
     */
    private boolean judgeLastPlan(String orderNum) {
        boolean flag = false;
        PaymentPlan paymentPlan = paymentPlanService.selectLastPaymentPlans(orderNum);
        if (paymentPlan != null) {
            if (!AppConstants.PaymentPlanStatus.UNRETURN.equals(paymentPlan.getRepayStatus())) {
                flag = true;
            }
        }
        return flag;
    }

    public void updateTransferStatus(BusiDebtTransfer transfer, String originStatus, String needStatus, Long buyId, String newOrderNo, Long orderId) {
        log.info(">>>>>>>>>>更新转让单【{}】开始，受让人订单ID【{}】,起始状态【{}】,最终状态【{}】", transfer.getId(), orderId, originStatus, needStatus);
        Map map = Maps.newTreeMap();
        map.put("id", transfer.getId());
        map.put("originStatus", originStatus);
        map.put("needStatus", needStatus);
        if (buyId != null) {
            map.put("buyId", buyId);
        }
        if (StringUtils.isNotBlank(newOrderNo)) {
            map.put("newOrderNo", newOrderNo);
        }
        int num = getBusiDebtTransferMapper().updateTransferDebtStatus(map);
        if (num == 1) {
            log.info(">>>>>>>>>>更新转让单【{}】结束，受让人订单ID【{}】,起始状态【{}】,最终状态【{}】", transfer.getId(), orderId, originStatus, needStatus);
        } else {
            log.info(">>>>>>>>>>更新转让单【{}】状态失败，受让人订单ID【{}】，起始状态【{}】,最终状态【{}】", transfer.getId(), orderId, originStatus, needStatus);
            throw new BusinessException("更新转让单状态失败!");
        }
    }

    /**
     * 理财计划订单提前退出初始化
     */
    public Result initExitFinPlanOrder(ReqMain reqMain) throws Exception{
        Model_500804 cdtModel = (Model_500804) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp order = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(order.getCustomerId());
        if (customerMainInfo == null) {
            throw new BusinessException("该用户不存在");
        }
        if (order == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        //计算从起息到当前时间相隔天数
        int days = DateUtil.getIntervalDays2(new Date(),order.getInterestStartDate())+1;
        //计算利息 ： 本金*年利率*持有天数/365
        BigDecimal actualInterest = order.getOrderAmt().multiply(order.getOriginalRate()).multiply(BigDecimal.valueOf(days)).
                divide(BigDecimal.valueOf(365l),2, BigDecimal.ROUND_DOWN);
        // 计算实际本息和
        BigDecimal principalinterest = order.getOrderAmt().add(actualInterest).setScale(2,BigDecimal.ROUND_DOWN);
        BigDecimal voucherAmt = new BigDecimal(0);
        VoucherSearchDto dto = new VoucherSearchDto();
        if(order.getRaiseDays().intValue() > days){
            Map<String, String> param = Maps.newTreeMap();
            param.put("orderNo", order.getId().toString());
            BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(param);
            dto.setNo(busiOrderIntegral.getVoucherId());//加息券编号
            dto.setOrderNo(order.getOrderId());//对应order_num
            dto.setAccountNo(customerMainInfo.getCmNumber());
            //查询加息券
            VoucherDto voucherDto = null;
            PageResultDto<VoucherDto> resultDto = voucherFacadeService.searchVouchers(dto);
            if (resultDto != null) {
                if (resultDto.isSuccess() && !resultDto.getDataList().isEmpty()) {
                    voucherDto = resultDto.getDataList().get(0);
                } else {
                    throw new RuntimeException(resultDto.getMsg());
                }
            } else {
                throw new RuntimeException("查询可用加息券异常");
            }
            voucherAmt=order.getOrderAmt().multiply(voucherDto.getRate()).multiply(BigDecimal.valueOf(days))
                    .divide(BigDecimal.valueOf(365l),2, BigDecimal.ROUND_DOWN);
        }
        //实际本息和
        principalinterest =principalinterest.add(voucherAmt);
        FinPlanExitDTO finPlanExitDTO=new FinPlanExitDTO();
        //提前退出手续费
        Double percentFee=Double.parseDouble(getExitFeeRate());
        String exitFeePercent = (subZeroAndDot(percentFee*100+""))+"%";
        //手续费=本金*退出费率
        BigDecimal exitFeeRateAmt= order.getOrderAmt().multiply(new BigDecimal(getExitFeeRate()));//退出手续费比例
        BigDecimal totalAmt=principalinterest.subtract(exitFeeRateAmt).setScale(2,BigDecimal.ROUND_DOWN);//扣除手续费后金额
        finPlanExitDTO.setExitFeePercent(exitFeePercent);//退出手续费比例
        finPlanExitDTO.setExitFeeRateAmt(exitFeeRateAmt);
        finPlanExitDTO.setTotalAmt(totalAmt);

        return Result.success(finPlanExitDTO);
    }
    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    private String getExitFeeRate(){
        String exitFeeRate = "0.02";
        SysParameter parameter = sysParameterService.findOneByPrTypeWithoutCache("exitFeeRate");
        if (null != parameter && StringUtils.isNotBlank(parameter.getPrValue())) {
            exitFeeRate = parameter.getPrValue();
        }
        return exitFeeRate;
    }

    /**
     * 理财计划订单提前退出
     */
    @Transactional
    public Result exitFinPlanOrder(ReqMain reqMain) throws Exception {
        Model_500805 cdtModel = (Model_500805) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        Long customerId = cdtModel.getCustomerId();
        //验证用户信息
        CustomerMainInfo customerMainInfo = customerMainInfoService.validateCustomerInfo(customerId);
        BusiOrderTemp busiOrder = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        checkExitFinplan(customerId,customerMainInfo,busiOrder);

        List<BusiOrderSub> busiOrderSubs= busiOrderSubMapper.queryOrderByParentId(orderId);
        List<DebtQueueDTO> debtQueueList = new ArrayList<>();
        if (CollectionUtils.isEmpty(busiOrderSubs)){
            throw new BusinessException("该笔订单无法提前退出");
        }
        for(BusiOrderSub busiOrderSub : busiOrderSubs){
            DebtQueueDTO debtQueue = new DebtQueueDTO();
            debtQueue.setOrderNo(busiOrderSub.getOrderId());
            debtQueue.setDebtType(AppConstants.FinanceOrderPushType.ORDER_PUSH_TYPE_2);//提前申请退出
            debtQueueList.add(debtQueue);
        }

        ResultDto resultDto = financePlanFacadeService.sendDebtInfo(debtQueueList);
        if (!resultDto.isSuccess()){
            return Result.fail("生成提前申请退出失败:",resultDto.getMsg());
    }
        //记录用户提前申请时间
        Date date = new Date();
        BusiOrderTemp busiOrderTemp=new BusiOrderTemp();
        busiOrderTemp.setExitClickDate(date);
        busiOrderTemp.setId(orderId);
       // BigDecimal exitFeeRateAmt= busiOrder.getOrderAmt().multiply(new BigDecimal(getExitFeeRate()));
       // busiOrderTemp.setExitChargeFee(exitFeeRateAmt);
        busiOrderTempMapper.updateByPrimaryKeySelective(busiOrderTemp);

        BusiOrderSub busiOrderSub=new BusiOrderSub();
        busiOrderSub.setExitClickDate(date);
        busiOrderSub.setId(orderId);
        //busiOrderSub.setExitChargeFee(exitFeeRateAmt);
        busiOrderSubMapper.updateByPrimaryKeySelective(busiOrderSub);
        return Result.success(resultDto.getMsg());
    }
    private void checkExitFinplan(Long customerId,CustomerMainInfo customerMainInfo,BusiOrderTemp busiOrder){
        if (customerMainInfo == null) {
            throw new BusinessException("查询不到用户信息！");
        }
        if (busiOrder == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        if (!busiOrder.getCustomerId().equals(customerId)) {
            throw new BusinessException("订单信息与用户信息不符");
        }
        if (!AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17.equals(busiOrder.getStatus())) {
            throw new BusinessException("该笔订单状态,不符合提前退出条件");
        }
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(busiOrder.getProductId());
        if (busiProduct == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
        }
        if (!AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiOrder.getProductType().toString())){
            throw new BusinessException("该笔订单不是理财计划产品，不符合提前退出条件");
        }
        long n = busiOrderSubMapper.queryFundByParentId(busiOrder.getId());
        if (n > 0) {
            throw new BusinessException("该笔订单有回款资金在复投中");
        }
    }


    public Result initTransfer(ReqMain reqMain) throws Exception {
        TransferInitDTO initDTO = new TransferInitDTO();
        Model_500041 cdtModel = (Model_500041) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp order = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        if (!checkCanTransfer(order,reqMain)) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_22);
        }
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(order.getProductId());
        if (busiProduct == null) {
            throw new BusinessException("订单关联的产品不存在");
        }
        Date currentDate = new Date();
        int leftDays = DateUtil.getIntervalDays2(order.getInterestEndDate(), currentDate);
        initDTO.setLeftDays(leftDays);
        int dayRange = 5;//转让日期区间 默认5天
        SysParameter parameter = sysParameterService.findOneByPrTypeWithoutCache("transferRange");
        if (parameter != null) {
            try {
                dayRange = Integer.parseInt(parameter.getPrValue());
            } catch (Exception e) {
                dayRange = 5;
            }
        }
        //当前转让时间之前有待还资金，不可转让
        List<PaymentPlan> paymentPlans =  paymentPlanService.selectPayPlansByCurrentTime(order.getOrderId(),new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (!CollectionUtils.isEmpty(paymentPlans)){
            initDTO.setAlertType(1);
            initDTO.setAlertInfo("该产品您有一笔待还资金，尚不可发起转让,\n待该笔还款到账后方可发起转让");
        }

        List<Date> transferDateList = new ArrayList<>();
        for (int i = 1; i <= dayRange; i++) {
            Date nextDate = DateUtil.plusDay(currentDate, i);
            if (DateUtil.compareStringDate(nextDate, order.getInterestEndDate()) >= 0) {
                break;
            }
            //正常发起转让，显示还款日之前日期
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("orderNum", order.getOrderId());
            paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd").format(nextDate));
            List<PaymentPlan> paymentPlanList =paymentPlanService.selectPayPlansByDate(paramMap);
            if (!CollectionUtils.isEmpty(paymentPlanList)){
                break;
            }
            transferDateList.add(nextDate);
        }

        //查询最原始的订单
        BusiOrderTemp originOrder = getOriginOrder(order.getOrderId());
        if (originOrder == null) {
            originOrder = order;
        }
        List<TransferRepayPlanReqDto> planList = getPlanList(originOrder);
        //计算剩余本息-普通产品
        if (order.getProductType().equals(AppConstants.OrderProductType.COMMON)) {
            this.getCommonExpectPI(initDTO, order);
        }
        //计算剩余本息-标的产品
        else {
            this.getSubjectExpectPI(initDTO, order);
        }
        BigDecimal basicFeeRate = queryBasicFeeRate();
        BigDecimal benefitFeeRate = queryBenefitFeeRate();
        BigDecimal feeRate = basicFeeRate;
        if (benefitFeeRate != null && benefitFeeRate.compareTo(basicFeeRate) < 0) {
            initDTO.setDiscount(benefitFeeRate.divide(basicFeeRate, 2, RoundingMode.DOWN));
            feeRate = benefitFeeRate;
        }
        initDTO.setTransferFeeRateStr("转让价格的" + new DecimalFormat("0.0%").format(feeRate));
        if (!CollectionUtils.isEmpty(transferDateList)){
            //查询转让日期 估值
            List<TransferPvResDto.DeliveryDateIrrPvDto> estimateDtoList = subjectService.transferEstimatePrice(originOrder, transferDateList, planList, busiProduct,feeRate);
            initDTO.setTransferEstimateList(estimateDtoList);
        }else{
            initDTO.setAlertType(1);
            initDTO.setAlertInfo("该产品您有一笔待还资金，尚不可发起转让,\n待该笔还款到账后方可发起转让");
        }

        //查询协议
        List<ProtocolDTO> protocolList = subjectService.queryProtocalName();
        initDTO.setProtocolList(protocolList);

        return Result.success(initDTO);
    }


    public Result transferAuth(ReqMain reqMain) throws Exception {
        Model_500042 cdtModel = (Model_500042) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        BigDecimal transferPrice = cdtModel.getTransferPrice();
        Long orderId = cdtModel.getOrderId();

        CustomerMainInfo customerMainInfo = customerMainInfoService.validateCustomerInfo(customerId);
        if (customerMainInfo == null) {
            throw new BusinessException("查询不到用户信息！");
        }
        BusiOrderTemp busiOrder = busiOrderTempMapper.selectViewByPrimaryKey(orderId);
        if (busiOrder == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        BigDecimal basicFeeRate = queryBasicFeeRate();
        BigDecimal benefitFeeRate = queryBenefitFeeRate();
        BigDecimal feeRate = basicFeeRate;
        if (benefitFeeRate != null && benefitFeeRate.compareTo(basicFeeRate) < 0) {
            feeRate = benefitFeeRate;
        }
        //转让手续费
        BigDecimal transferFee = transferPrice.multiply(feeRate).setScale(2, RoundingMode.DOWN);
        Map authFlag = busiOrderService.userGrantFlag(customerId,1,transferFee);

        return Result.success(authFlag);
    }


    //更新转让时用户授权信息
    @Transactional
    private void updateUserGrant(Long customerId, BigDecimal transferFee) {
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(customerId);
        if (userGrant ==null){
            throw new BusinessException("查询不到用户授权信息！");
        }
        BigDecimal authFee = transferFee.multiply(new BigDecimal(100));
         //缴费剩余授权额度
        BigDecimal feeAmt= new BigDecimal(userGrant.getAutoFeeAmt()).subtract(authFee);//分为单位

        userGrant.setCustomerId(customerId);
        userGrant.setAutoFeeAmt(feeAmt.longValue());

        customerGrantInfoMapper.updateCustomerGrantInfo(userGrant);
    }

    public List<BusiDebtTransfer> selectDebtTransfer(Map<String,Object> params){
        return getBusiDebtTransferMapper().selectDebtTransferByCondition(params);
    }

    public int updateByMap(Map<String,Object> params){
       return getBusiDebtTransferMapper().updateByMap(params);
    }


    public Result transferRateInfo(ReqMain reqMain) throws Exception {
        Model_500055 cdtModel = (Model_500055) reqMain.getReqParam();
        TransferRateDTO dto = new TransferRateDTO();
        BigDecimal transferPrice = cdtModel.getTransferPrice();
        BigDecimal estimatePrice = cdtModel.getEstimatePrice();
        if (transferPrice.compareTo(new BigDecimal(0))<=0){
            throw new BusinessException("转让价格不能为0");
        }

        BusiOrderTemp order = busiOrderTempMapper.selectViewByPrimaryKey(cdtModel.getOrderId());
        if (order == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }

        BigDecimal basicFeeRate = queryBasicFeeRate();
        BigDecimal benefitFeeRate = queryBenefitFeeRate();
        BigDecimal feeRate = basicFeeRate;
        if (benefitFeeRate != null && benefitFeeRate.compareTo(basicFeeRate) < 0) {
            dto.setDiscount(benefitFeeRate.divide(basicFeeRate, 2, RoundingMode.DOWN));
            feeRate = benefitFeeRate;
        }
        dto.setTransferFeeRateStr("转让价格的" + new DecimalFormat("0.0%").format(feeRate));
        BigDecimal transferFee = transferPrice.multiply(feeRate).setScale(2, RoundingMode.DOWN);
        dto.setTransferFee(transferFee);
        dto.setExceptReceivedAmount(transferPrice.subtract(transferFee).setScale(2, RoundingMode.DOWN));
        //折扣比例 （产品估值-转让价格）/产品估值
        String discountRate = (estimatePrice.subtract(transferPrice)).divide(estimatePrice,18,BigDecimal.ROUND_DOWN).setScale(4,RoundingMode.DOWN).toPlainString();
        dto.setDiscountRate(discountRate);

        //查询最原始的订单
        BusiOrderTemp originOrder = getOriginOrder(order.getOrderId());
        if (originOrder == null) {
            originOrder = order;
        }
        List<TransferRepayPlanReqDto> planList = getPlanList(originOrder);
        BigDecimal rate = subjectService.queryXirrValue(cdtModel.getTransferDate(), transferPrice, originOrder, planList);
        if (rate == null) {
            throw new BusinessException("查询利率失败");
        }
        dto.setTransferRate(Convert.toStr(rate));
        return Result.success(dto);
    }

    public void sendNotifyDeptTradeSMS() {

        Map<String, Object> params = Maps.newTreeMap();
        Date date = cn.hutool.core.date.DateUtil.date();
        DateTime beginOfDay = cn.hutool.core.date.DateUtil.beginOfDay(date);
        int dayOfMonth = beginOfDay.dayOfMonth();
        if (dayOfMonth == 14){
            Date endDate = cn.hutool.core.date.DateUtil.offsetHour(beginOfDay, 12);
            Date tmp = cn.hutool.core.date.DateUtil.offsetMonth(endDate,-1);
            Date startDate = cn.hutool.core.date.DateUtil.offsetDay(tmp,14);
            params.put("startDate",DateUtil.getDateFormatString(startDate,DateUtil.fullFormat));
            params.put("endDate", DateUtil.getDateFormatString(endDate,DateUtil.fullFormat));
        }else if (dayOfMonth == 28){
            Date endDate = cn.hutool.core.date.DateUtil.offsetHour(beginOfDay, 12);
            Date startDate = cn.hutool.core.date.DateUtil.offsetDay(endDate, -14);
            params.put("startDate",DateUtil.getDateFormatString(startDate,DateUtil.fullFormat));
            params.put("endDate", DateUtil.getDateFormatString(endDate,DateUtil.fullFormat));
        }else{
            log.error("该定时任务,在每个月的14,28日时间点执行！");
            throw new BusinessException("该定时任务,在每个月的14,28日时间点执行！");
        }
        List<CustomerMainInfo> customerMainInfoList = customerMainInfoService.getCustomerForDebtTransfer(params);
        if (!customerMainInfoList.isEmpty()) {
            for (CustomerMainInfo customerMainInfo : customerMainInfoList) {
                pushDebtTransferSms(customerMainInfo);
            }
        }
    }


    private void pushDebtTransferSms(CustomerMainInfo customerMainInfo) {
        try {
            SendSmNotifyReqDto reqDto = new SendSmNotifyReqDto();
            //短信内容
            reqDto.setMobile(customerMainInfo.getCmCellphone());
            String msgContent = configParamBean.getDebtTransferSmMsg();
            reqDto.setSendMsg(msgContent);
            MessageResultDto resultDto = iSmFacadeService.sendNotifyMsg(reqDto);
            if (resultDto.isSuccess()) {
                log.info("LCB债权转让通知--->发送短信成功，用户编号:{}", customerMainInfo.getCmNumber());
            } else {
                log.error("LCB债权转让通知--->发送短信失败，用户编号:{}", customerMainInfo.getCmNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("LCB债权转让通知--->发送短信异常,用户编号:{},原因:{}", customerMainInfo.getCmNumber(), e.getMessage());
        }
    }

    /**
     * 发送消息中心消息
     * @param debtTransferDetail
     */
    public void sendMsgMessage(DebtTransferDetail debtTransferDetail){
        if(debtTransferDetail == null) {
            log.info("LCB债权转让通知--->没有查询到债券转让信息");
            return;
        }
        MsgMessageSendDto sendDto = new MsgMessageSendDto();
        sendDto.setTitle("债权转让通知书");
        sendDto.setMerchantSerialNo(debtTransferDetail.getDebtorName());
        sendDto.setSummary("债权转让通知书");
        String msgContent = createMsgContent(debtTransferDetail);
        sendDto.setContent(msgContent);
        sendDto.setContentType(ContentType.OTHER);
        sendDto.setInscriber(debtTransferDetail.getDebtorName());
        sendDto.setCallbackUrl(configParamBean.getWsRootUrl() + configParamBean.getMessageCallBack());
        List<MsgMessageSendDto.UserDto> users = new ArrayList<>();
        MsgMessageSendDto.UserDto userDto = new MsgMessageSendDto.UserDto(String.valueOf(debtTransferDetail.getDebtorId()), debtTransferDetail.getDebtorNum(),
                debtTransferDetail.getDebtorName(), debtTransferDetail.getDebtorPhone());
        users.add(userDto);
        sendDto.setUsers(users);
        try {
            MessageResultDto resultDto = iMsgMessageFacadeService.send(sendDto);
            if (resultDto.isSuccess()) {
                log.info("LCB债权转让通知--->发送消息成功,用户编号：{}",debtTransferDetail.getDebtorNum());
            } else {
                log.info("LCB债权转让通知--->发送消息失败,失败码:{},失败原因{}", resultDto.getCode(), resultDto.getMsg());
            }
        } catch (Exception e) {
            log.error("LCB债权转让通知--->发送消息异常,异常信息:{}", e.getMessage());
        }
    }

    private String createMsgContent(DebtTransferDetail debtTransferDetail){
        String deptTransferTempl = configParamBean.getDeptTransferMsg();
        String[][] ddss = CoreUtil.getAmtPerWord(Double.valueOf(debtTransferDetail.getTransferPrice()));
        StringBuffer sbf = new StringBuffer();
        for(int i=0;i<ddss.length;i++){
            sbf.append(ddss[i][1]+ddss[i][0]);
        }
        String transferPriceStr = sbf.toString();
        String [] valueList = {debtTransferDetail.getDebtorName(),
                debtTransferDetail.getTransferName(),debtTransferDetail.getTransferIdNum(),
                debtTransferDetail.getBuyName(),debtTransferDetail.getBuyIdNum(),
                debtTransferDetail.getTransferPrice(),transferPriceStr,
                cn.hutool.core.date.DateUtil.format(debtTransferDetail.getTradeDate(), "yyyy 年 MM 月 dd 日"), cn.hutool.core.date.DateUtil.format(new Date(), "yyyy 年 MM 月 dd 日")};
        return MessageFormat.format(deptTransferTempl, valueList);
    }

}
