package websvc.servant.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import com.zdmoney.assets.api.dto.signature.ProductAgreementObtainResDto;
import com.zdmoney.assets.api.dto.subject.SubjectBorrowerDetailReqDto;
import com.zdmoney.assets.api.dto.subject.agreement.SubjectAgreementContentReqDto;
import com.zdmoney.assets.api.dto.subject.agreement.SubjectAgreementContentResDto;
import com.zdmoney.assets.api.dto.subject.enums.ProductType;
import com.zdmoney.assets.api.dto.subject.invest.InvestAgreementObtainDto;
import com.zdmoney.assets.api.dto.subject.invest.InvestAgreementObtainResDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.common.util.StringUtil;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.facade.InnerEmployeeService;
import com.zdmoney.facade.LcbGatewayFacadeImpl;
import com.zdmoney.mapper.BusiAgreementMapper;
import com.zdmoney.mapper.CustomerBorrowInfoMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.payment.PaymentCalendarMapper;
import com.zdmoney.mapper.product.BusiProductRuleMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.CustomerBorrowInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payChannel.BusiPayChannel;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductRule;
import com.zdmoney.service.*;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.service.payChannel.BusiPayChannelService;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.subject.SubjectService;
 import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.*;
import com.zdmoney.vo.*;
import com.zdmoney.web.dto.HistorySaleDTO;
import com.zdmoney.web.dto.PacketDTO;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.facade.ICreditFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.TradeFunctionService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 00225181 on 2016/3/26.
 */
@Service
@Slf4j
public class TradeFunctionServiceImpl implements TradeFunctionService {
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;
    @Autowired
    private AccountOverview520003Service accountOverview520003Service;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayInfo520002Service pay520002Service;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private UserAssetService userAsset520004Service;
    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;
    @Autowired
    private BusiAgreementMapper busiAgreementMapper;
    @Autowired
    private BusiOrderTempService busiOrderTempService;
    @Autowired
    private BusiProductService busiProductService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private PaymentCalendarMapper paymentCalendarMapper;
    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;
    @Autowired
    private BusiOrderService busiOrderService;
    @Autowired
    private WelfareService welfareService;
    @Autowired
    private RedisSessionManager redisSessionManager;
    @Autowired
    private BusiPayChannelService busiPayChannelService;
    @Autowired
    private CustomerBorrowInfoMapper customerBorrowInfoMapper;
    @Autowired
    BusiProductRuleMapper busiProductRuleMapper;
    @Autowired
    LcbGatewayFacadeImpl lcbGatewayFacadeImpl;
    @Autowired
    ICreditFacadeService creditFacadeService;
    @Autowired
    BusiFinancePlanService busiFinancePlanService;
    @Autowired
    InnerEmployeeService innerEmployeeService;

    @Autowired
    private ConfigParamBean configParamBean;


    @FunctionId("500000")
    public Result platformHistorySale(ReqMain reqMain) throws Exception {
        return Result.success(getHistorySale());
    }

    public HistorySaleDTO getHistorySale() {
        String saleKey = "5.0:PLATFORM_SALE" + DateTime.now().toString("yyyyMMdd");
        String saleDTO = redisSessionManager.get(saleKey);

        HistorySaleDTO dto = new HistorySaleDTO();
        if (StringUtils.isNotBlank(saleDTO)) {
            dto = JSONObject.toJavaObject(JSONObject.parseObject(saleDTO), HistorySaleDTO.class);
            log.info("缓存公共数据key:{}  value:{}",saleKey,JSONObject.parseObject(saleDTO));
        } else {
            HistorySaleVo historySaleVo = busiOrderService.selectHistorySale();
            BigDecimal yestodaySale = busiOrderService.selectYestodaySale(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
            dto.setTotalInterest(historySaleVo.getTotalInterest());
            dto.setTotalOrderAmt(historySaleVo.getTotalOrderAmt());
            dto.setYestodaySale(yestodaySale);
            int days = DateUtil.getIntervalDays(new Date(), Convert.toDate(configParamBean.getLcbReleaseDate()));
            dto.setDays(days);
            log.info("缓存公共数据key:{}  value:{}",saleKey,JSON.toJSONString(dto));
            redisSessionManager.put(saleKey, JSON.toJSONString(dto), 1, TimeUnit.DAYS);
        }
        return dto;
    }


    /*
     * 500003 下单接口
     */
    @FunctionId("500003")
    public Result order(ReqMain reqMain) throws Exception {
        Model_500003 cdtModel = (Model_500003) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        BigDecimal orderAmt = cdtModel.getOrderAmt();
        String inviteCode = StringUtils.trim(cdtModel.getInviteCode());
        return orderService.order(customerId, productId, orderAmt, inviteCode, reqMain);
    }

    @FunctionId("500009")
    public Result rechargeInfo(ReqMain reqMain) throws Exception {
        Model_500009 model = (Model_500009) reqMain.getReqParam();
        String flowNum = model.getFlowNum();

        String status = busiTradeFlowMapper.getRechargeStatusByFlowNum(flowNum);
        String statusMsg = "";
        String infos = "";
        if (StringUtil.isEmpty(status)) {
            statusMsg = "查询参数不存在";
            infos = "-1";
        } else if (AppConstants.RechargeStatus.Recharge_STATUS_1.equals(status)) {
            statusMsg = "充值成功";
            infos = "0";
        } else if (AppConstants.RechargeStatus.Recharge_STATUS_0.equals(status) || AppConstants.RechargeStatus.Recharge_STATUS_2.equals(status)) {
            statusMsg = "充值中";
            infos = "1";
        } else if (AppConstants.RechargeStatus.Recharge_STATUS_3.equals(status)) {
            statusMsg = "充值失败";
            infos = "2";
        }
        return Result.success(statusMsg, infos);
    }

    @FunctionId("500012")
    public Result assetList(ReqMain reqMain) throws Exception {
        return null;
    }

    /**
     * 项目简介
     *
     * @param subjctNo
     * @return
     */
    private String getIteamInfo(String subjctNo) {
        String itemInfo = "";
        SubjectBorrowerDetailReqDto reqDto = new SubjectBorrowerDetailReqDto();
        reqDto.setPartnerNo("LCB");
        reqDto.setSubjectNo(subjctNo);
        try {
            AssetsResultDto<String> resultDto = lcbSubjectFacadeService.getDiscloseItemInfo(reqDto);
            log.info("调用标的系统查询产品项目简介信息：" + JSONUtils.toJSON(resultDto));
            if (resultDto.isSuccess()) itemInfo = resultDto.getData();
        } catch (Exception e) {
            log.error("调用标的系统查询产品项目简介信息失败,标的编号:" + subjctNo);
        }
        return itemInfo;
    }

    private String getUseWelfareInfo(BusiProduct busiProduct) {
        String welfareInfo = AppConstants.PRODUCT_NO_WELFARE;
        StringBuffer welfareStr = new StringBuffer();
        BusiProductRule busiProductRule = busiProductRuleMapper.selectByPrimaryKey(busiProduct.getRuleId());
        if (busiProductRule != null && StringUtils.isNotBlank(busiProductRule.getWelfare())) {
            String[] statusList = busiProductRule.getWelfare().split(",");
            if (statusList != null && statusList.length > 0) {
                for (String status : statusList) {
                    if ("1".equals(status)) {
                        welfareStr.append("积分,");
                    }
                    if ("2".equals(status)) {
                        welfareStr.append("红包,");
                    }
                    if ("3".equals(status)) {
                        welfareStr.append("加息券,");
                    }
                }
                welfareInfo = welfareStr.toString().substring(0, welfareStr.length() - 1);
            }

        }
        return welfareInfo;
    }


    //    @FunctionId("501016")
    public Result productDetailPC(ReqMain reqMain) throws Exception {
        Model_501016 cdtModel = (Model_501016) reqMain.getReqParam();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("productId", cdtModel.getProductId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("sDate", sdf.format(new Date()));
        BusiProductVO busiProduct = busiProductService.getProductDetailPC(map);
        if (busiProduct != null) {
            if (busiProduct.getShowStartDate() != null) {
                boolean flag = busiProduct.getShowStartDate().getTime() > new Date().getTime();
                if (flag || "0".equals(busiProduct.getAuditFlag())) {
                    return Result.fail("产品不存在");
                }
            }
//            if (busiProduct.getItemInfo()!=null){
//                busiProduct.setProductDesc(busiProduct.getItemInfo());
//            }
            if ("1".equals(busiProduct.getIsTransfer())) {//转让产品
                busiProduct.setWelfare(AppConstants.PRODUCT_NO_WELFARE);
            } else {
                busiProduct.setWelfare(getUseWelfareInfo(busiProduct));
            }

            busiProduct.setProductDesc(getIteamInfo(busiProduct.getSubjectNo()));

            return Result.success(busiProduct);
        } else {
            return Result.fail("产品不存在");
        }
    }

    @FunctionId("500017")
    public Result investAgreement(ReqMain reqMain) throws Exception {
        Model_500017 cdtModel = (Model_500017) reqMain.getReqParam();

        InvestAgreementVo investAgreementVo = busiAgreementMapper.selectInvestAgreemenet(cdtModel.getOrderId());
        if (investAgreementVo == null) {
            return Result.fail("投资协议无效!");
        }
        if (StringUtils.isNotBlank(investAgreementVo.getIdNum())) {
            investAgreementVo.setIdNum(investAgreementVo.getIdNumStr());
        }
        return Result.success(investAgreementVo);
    }

    @FunctionId("500018")
    public Result loanAgreement(ReqMain reqMain) throws Exception {
        Model_500018 cdtModel = (Model_500018) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
        if (busiOrderTemp == null) {
            throw new BusinessException("订单不存在！");
        }
        Long productId = busiOrderTemp.getProductId();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
        paramsMap.put("productId", productId);
        BusiProduct busiProduct = busiProductService.findProductById(paramsMap);
        if (busiProduct == null) {
            throw new BusinessException("产品不存在！");
        }
        //债权信息列表
        HashMap<String, Object> returnMaps = Maps.newHashMap();
        if (AppConstants.ProductSubjectType.SUBJECT_ZX.equals(busiProduct.getSubjectType())
                || AppConstants.ProductSubjectType.SUBJECT_GD.equals(busiProduct.getSubjectType())
                || AppConstants.OrderTransferStatus.ORDER_TRANSFER.equals(busiOrderTemp.getTransferType())) {
            try {
                InvestAgreementObtainDto dto = new InvestAgreementObtainDto();
                dto.setPartnerNo("LCB");
                dto.setOrderNo(busiOrderTemp.getOrderId());
                AssetsResultDto<InvestAgreementObtainResDto> resultDto = lcbSubjectFacadeService.getInvestOrderElecAgreement(dto);
                if (resultDto.isSuccess()) {
                    returnMaps.put("agreementStr", resultDto.getData().getContent());
                    returnMaps.put("viewUrl", resultDto.getData().getViewUrl());
                    returnMaps.put("downloadUrl", resultDto.getData().getDownloadUrl());
                    returnMaps.put("isElecAgreement", resultDto.getData().isElecAgreement());
                } else {
                    return Result.fail("债权协议不存在！");
                }
            } catch (Exception e) {
                {

                }
                e.printStackTrace();
                log.error(e.getMessage());
                return Result.fail("债权协议不存在！");
            }
            return Result.success(returnMaps);
        }
        //捞财宝债权信息列表
        ProductAgreementObtainResDto loanDto = subjectService.queryProductLoanAgreement(busiOrderTemp.getOrderId());
        if (loanDto == null) {
            return Result.fail("查询协议出错！");
        }
        if (StringUtils.isNotBlank(loanDto.getViewUrl())) {
            returnMaps.put("intercreditorAgreementViewUrl", loanDto.getViewUrl());
            returnMaps.put("intercreditorAgreementDownloadUrl", loanDto.getDownloadUrl());
            returnMaps.put("isElecIntercreditorAgreement", true);
        } else {
            List<IntercreditorAgreementVo> intercreditorAgreementVoList = busiAgreementMapper.selectIntercreditorAgreement(cdtModel.getOrderId());
            if (CollectionUtils.isNotEmpty(intercreditorAgreementVoList)) {
                for (IntercreditorAgreementVo vo : intercreditorAgreementVoList) {
                    vo.setBorrower(vo.getBorrower());
                    vo.setBorrowAmount(vo.getBorrowAmount());
                    vo.setPayAmount(vo.getPayAmount());
                    vo.setBorrowByUse(vo.getBorrowByUse());
                    String idNum = vo.getBorrowerIdNum();
                    if (idNum.length() == 18) {
                        idNum = idNum.substring(0, 4) + "**********" + idNum.substring(14, 18);
                        vo.setBorrowerIdNum(idNum);
                    }
                    if (idNum.length() == 15) {
                        idNum = idNum.substring(0, 4) + "**********" + idNum.substring(11, 15);
                        vo.setBorrowerIdNum(idNum);
                    }
                }
            }
            returnMaps.put("intercreditorAgreement", intercreditorAgreementVoList);
            returnMaps.put("isElecIntercreditorAgreement", false);
        }

        //捞财宝投资协议
        ProductAgreementObtainResDto resDto = subjectService.queryInvestAgreement(busiOrderTemp.getOrderId());
        if (resDto == null) {
            return Result.fail("查询协议出错！");
        }
        InvestAgreementVo investAgreementVo = busiAgreementMapper.selectInvestAgreemenet(cdtModel.getOrderId());
        if (investAgreementVo == null) {
            return Result.fail("债权协议不存在！");
        }
        if (investAgreementVo.getIdNum() != null) {
            investAgreementVo.setIdNum(investAgreementVo.getIdNumStr());
        }
        returnMaps.put("isElecInvestAgreement", false);
        returnMaps.put("investAgreement", investAgreementVo);
        if (StringUtils.isNotBlank(resDto.getViewUrl())) {
            returnMaps.put("investAgreementViewUrl", resDto.getViewUrl());
            returnMaps.put("investAgreementDownloadUrl", resDto.getDownloadUrl());
            returnMaps.put("isElecInvestAgreement", true);
        }
        return Result.success(returnMaps);
    }

    @FunctionId("500022")
    public Result getPaymentCalendar(ReqMain reqMain) throws Exception {
        Model_500022 model_500022 = (Model_500022) reqMain.getReqParam();
        List<PaymentCalendar> paymentReceives = paymentCalendarMapper.selectPaymentCalendar(model_500022.getCustomerId());
        return Result.success(paymentReceives);
    }


    @FunctionId("500023")
    public Result loanAgreementTemplate(ReqMain reqMain) throws Exception {
        Model_500023 model_500023 = (Model_500023) reqMain.getReqParam();
        String subjectNo = model_500023.getSubjectNo();
        SubjectAgreementContentReqDto reqDto = new SubjectAgreementContentReqDto("LCB", subjectNo);
        AssetsResultDto<SubjectAgreementContentResDto> resultDto = lcbSubjectFacadeService.getSubjectAgreementContentByNo(reqDto);
        if (resultDto.isSuccess()) {
            return Result.success(resultDto.getData().getAgreement());
        } else {
            log.error(resultDto.getMsg());
            return Result.fail();
        }
    }

    @FunctionId("500040")
    public Result checkBespeakTicket(ReqMain reqMain) throws Exception {
        Model_500040 model_500040 = (Model_500040) reqMain.getReqParam();
        Long customerId = model_500040.getCustomerId();
        Long productId = model_500040.getProductId();
        BigDecimal orderAmt = model_500040.getOrderAmt();
        return orderService.checkBespeakTicket(LaocaiUtil.getLoginType(reqMain.getReqHeadParam()), customerId, productId, orderAmt);
    }

    /*
     * 520001 支付接口2.0
     */
    @FunctionId("520001")
    public Result pay(ReqMain reqMain) throws Exception {
        Model_520001 cdtModel = (Model_520001) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long orderId = cdtModel.getOrderId();
        String integralAmt = cdtModel.getIntegralAmt();
        String redId = cdtModel.getRedId();
        String voucherId = cdtModel.getVoucherId();
        String payPassword = cdtModel.getPayPassword();
        boolean isNeedPassword = false;
        return tradeService.pay(customerId, orderId, integralAmt, redId, voucherId, payPassword, isNeedPassword, reqMain);
    }

    /*
     * 520002 获取支付信息接口2.0
     */
    @FunctionId("520002")
    public Result payInfo(ReqMain reqMain) throws Exception {
        return pay520002Service.getPayInfo(reqMain);
    }

    @FunctionId("520003")
    public Result assetOverview(ReqMain reqMain) throws Exception {
        return accountOverview520003Service.getOverview(reqMain);
    }

    @FunctionId("520004")
    public Result userAsset(ReqMain reqMain) throws Exception {
        return userAsset520004Service.getUserAsset(reqMain);
    }

    @FunctionId("520044")
    public Result userAssets(ReqMain reqMain) throws Exception {
        return userAsset520004Service.getUserAssets(reqMain);
    }

    @FunctionId("520005")
    public Result availableBalance(ReqMain reqMain) throws Exception {
        return accountOverview520003Service.getAccountBalance(reqMain);
    }


    /*
     * 520077 充值绑卡接口3.0
     *
     */
    @FunctionId("520077")
    public Result rechargeAmt(ReqMain reqMain) throws Exception {
        return bankCardService.rechargeAmt(reqMain);
    }

    @FunctionId("520009")
    public Result withDraw(ReqMain reqMain) throws Exception {
        return tradeService.withDraw(reqMain);
    }

    @Deprecated
    @FunctionId("530002")
    public Result tppPayValidateCode(ReqMain reqMain) throws Exception {
        return Result.success();
    }

    @FunctionId("710004")
    @Override
    public Result orderPay(ReqMain reqMain) throws Exception {
        Model_710004 cdtModel = (Model_710004) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        BigDecimal orderAmt = cdtModel.getOrderAmt();
        String inviteCode = StringUtils.trim(cdtModel.getInviteCode());
        //判断是否重复下单
        Map queryMap = Maps.newTreeMap();
        queryMap.put("customerId", customerId);
        queryMap.put("productId", productId);
        List<BusiOrder> orderList = busiOrderService.selectProductOrders(queryMap);
        if (CollectionUtils.isNotEmpty(orderList)) {
            return Result.fail("您已购买过此产品");
        }
        Result orderResult = orderService.order(customerId, productId, orderAmt, null, reqMain);
        if (orderResult.getSuccess()) {
            Long orderId = (Long) ((Map<String, Object>) orderResult.getData()).get("orderId");
            return tradeService.pay(customerId, orderId, null, null, null, null, false, reqMain);
        } else {
            return orderResult;
        }
    }

    @FunctionId("720002")
    @Override
    public Result orderPayTencent(ReqMain reqMain) throws Exception {
        Model_720002 cdtModel = (Model_720002) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        BigDecimal orderAmt = cdtModel.getOrderAmt();
        String couponId = cdtModel.getCouponId();
        //判断是否重复下单某个产品（腾讯活动）
        Map queryMap = Maps.newTreeMap();
        queryMap.put("customerId", customerId);
        queryMap.put("productId", productId);
        List<BusiOrder> orderList = busiOrderService.selectProductOrders(queryMap);
        if (CollectionUtils.isNotEmpty(orderList)) {
            return Result.fail("您已购买过此产品");
        }
        Result orderResult = orderService.order(customerId, productId, orderAmt, null, reqMain);
        if (orderResult.getSuccess()) {
            Long orderId = (Long) ((Map<String, Object>) orderResult.getData()).get("orderId");
            return tradeService.pay(customerId, orderId, null, couponId, null, null, false, reqMain);
        } else {
            return orderResult;
        }
    }

    @FunctionId("720003")
    public Result gainAvailCoupon(ReqMain reqMain) throws Exception {
        Model_720003 model_720003 = (Model_720003) reqMain.getReqParam();
        String sessionToken = model_720003.getSessionToken();
        Integer investPeriod = model_720003.getInvestPeriod();
        List<PacketDTO> packets = welfareService.gainRedPacket(sessionToken, investPeriod);
        return Result.success(packets);
    }

    @Override
    @FunctionId("500050")
    public Result queryAgreement(ReqMain reqMain) throws Exception {
        Model_500050 model_500050 = (Model_500050) reqMain.getReqParam();
        String productType = model_500050.getProductType();
        String agreementNo = model_500050.getAgreementNo();
        String content = subjectService.queryAgreements(productType, agreementNo);
        return Result.success(content);
    }

    /*
     * 800005 用户余额查询
     */
    @FunctionId("800005")
    public Result accountBalance(ReqMain reqMain) throws Exception {
        Model_800005 cdtModel = (Model_800005) reqMain.getReqParam();
        Long logId = 0L;
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(cdtModel.getCustomerId()));
        if (customerMainInfo == null) {
            return Result.fail("user.notExist");
        }
        //账户余额
        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(customerMainInfo);
        Map<String, String> userAccount = Maps.newTreeMap();
        userAccount.put("accountBalance", CoreUtil.BigDecimalAccurate(accountBalance));
        userAccount.put("handNew", customerMainInfo.getIsConsumed() == 0 ? "y" : "n");
        userAccount.put("buyingPermited", StringUtils.isEmpty(customerMainInfo.getAccountType()) || CustomerAccountType.LENDER.getValue().equals(customerMainInfo.getAccountType()) ? "y" : "n");
        Date riskExpireTime = customerMainInfo.getRiskExpireTime();
        String riskFlag = "0", riskExpire = "0", riskType = "0", signContract = "0", bankAccountId = "0", borrowInfoFlag = "0";
        if (customerMainInfo.getRiskTestType() != null) {
            riskFlag = "1";
            riskType = customerMainInfo.getRiskTestType();
        }
        if (riskExpireTime != null && DateUtil.getIntervalDays2(new Date(), riskExpireTime) > 0) {
            riskExpire = "1";
        }
        if (customerMainInfo.getSignContract() != null) {
            signContract = customerMainInfo.getSignContract().toString();
        }
        if (customerMainInfo.getBankAccountId() != null) {
            bankAccountId = "1";
        }
        CustomerBorrowInfo borrowInfo = new CustomerBorrowInfo();
        borrowInfo.setCmId(Long.parseLong(cdtModel.getCustomerId()));
        CustomerBorrowInfo customerBorrowInfo = customerBorrowInfoMapper.selectOne(borrowInfo);
        if (customerBorrowInfo != null) {
            borrowInfoFlag = "1";
        }
        userAccount.put("riskFlag", riskFlag);// 1:已经评测 0：未评测
        userAccount.put("riskExpire", riskExpire);// 1:已经测评过期  0：未过期
        userAccount.put("cmStatus", customerMainInfo.getCmStatus().toString());//用户状态  3  实名认证通过
        userAccount.put("riskType", riskType);//  0：默认无评测类型
        userAccount.put("signContract", signContract);//  电子合同签约 0-未签约  1-已签约
        userAccount.put("cmNumber", customerMainInfo.getCmNumber());//
        userAccount.put("bankAccountId", bankAccountId);// 是否绑卡 ： 0未绑 1：已绑定
        userAccount.put("borrowInfoFlag", borrowInfoFlag);// 借款采集意向 ： 0:未收集 1：已收集
        userAccount.put("memberLevel", Convert.toStr(customerMainInfo.getMemberLevel()));
        return Result.success(userAccount);
    }

    @Override
    @FunctionId("540009")
    public Result queryProductAgreements(ReqMain reqMain) throws Exception {
        Model_540009 model_540009 = (Model_540009) reqMain.getReqParam();
        Long productId = model_540009.getProductId();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
        paramsMap.put("productId", productId);
        BusiProduct busiProduct = busiProductService.findProductById(paramsMap);

        if (busiProduct == null) {
            throw new BusinessException("产品不存在！");
        }
        String subjectNo = "";
        ProductType productType = ProductType.OPTION_PRODUCT;
        if (AppConstants.ProductTransferStatus.TRANSFER_PRODUCT.equals(busiProduct.getIsTransfer())) {
            productType = ProductType.TRANSFER_PRODUCT;
        } else {
            if (AppConstants.ProductSubjectType.SUBJECT_ZX.equals(busiProduct.getSubjectType())) {
                productType = ProductType.SUBJECT_PRODUCT;
            }
            if (AppConstants.ProductSubjectType.SUBJECT_GD.equals(busiProduct.getSubjectType())) {
                productType = ProductType.PERSON_LOAN_PRODUCT;
            }
        }
        List<AgreementNameDto> content = subjectService.queryProductAgreementsByType(subjectNo, productType);
        return Result.success(content);

    }

    @FunctionId("500060")
    @Override
    public Result queryInvestRecord(ReqMain reqMain) throws Exception {
        Model_500060 model = (Model_500060) reqMain.getReqParam();
        Long productId = model.getProductId();
        int pageNo = ObjectUtils.defaultIfNull(model.getPageNo(), 1);
        int pageSize = ObjectUtils.defaultIfNull(model.getPageSize(), ParamConstant.PAGESIZE);

        Page<InvestRecordVO> page = productService.queryInvestRecord(productId, pageNo, pageSize);
        return Result.success(page);
    }

    @FunctionId("520018")
    @Override
    public Result getCurrentPayChannel(ReqMain reqMain) throws Exception {
        BusiPayChannel payChannel = busiPayChannelService.getCurrentPayChannel();
        Map map = Maps.newTreeMap();
        map.put("channelCode", payChannel.getPayPlatformCode());
        map.put("channelName", payChannel.getChannelName());
        return Result.success(map);
    }

    @FunctionId("520020")
    @Override
    public Result findOperationDataStatistics(ReqMain reqMain) throws Exception {
        return tradeService.findOperationDataStatistics(reqMain);
    }

    @FunctionId("520021")
    @Override
    public Result extractAllBalance(ReqMain reqMain) throws Exception {
        Model_520021 model = (Model_520021) reqMain.getReqParam();
        BigDecimal allBalance = accountOverview520003Service.extractAllBalance(model.getCmNumber());
        return Result.success(allBalance);
    }

    @FunctionId("521000")
    public Result cashierPay(ReqMain reqMain) throws Exception {
        return tradeService.cashierPay(reqMain);
    }

    @FunctionId("521001")
    public Result cashierPayInit(ReqMain reqMain) throws Exception {
        return tradeService.cashierPayInit(reqMain);
    }

    @FunctionId("521002")
    public Result vouchers(ReqMain reqMain) throws Exception {
        return pay520002Service.vouchers(reqMain);
    }


    @FunctionId("521005")
    public Result continuedInverstment(ReqMain reqMain) throws Exception {
        Model_521005 model = (Model_521005) reqMain.getReqParam();
        return tradeService.reinvestment(model.getCustomerId(), model.getOrderId(), model.getReinvestType());
    }


    @FunctionId("521003")
    public Result unableOrderContinuedList(ReqMain reqMain) throws Exception {
        return userAsset520004Service.unableOrderContinuedList(reqMain);
    }

    @FunctionId("521004")
    public Result orderContinuedInit(ReqMain reqMain) throws Exception {
        return userAsset520004Service.orderContinuedInit(reqMain);
    }

    /**
     * ip验证
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("521006")
    @Override
    public Result innerIPAddres(ReqMain reqMain) throws Exception {
        String IPStr = reqMain.getReqHeadParam().getIp();
        Model_521006 model = (Model_521006) reqMain.getReqParam();
        Long customerId = model.getCustomerId();
        return innerEmployeeService.staffAuth(customerId,IPStr);
    }

    /**
     * 专区产品
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("510003")
    @Override
    public Result staffProductList(ReqMain reqMain) throws Exception {
        return innerEmployeeService.staffProductList(reqMain);
    }

}
