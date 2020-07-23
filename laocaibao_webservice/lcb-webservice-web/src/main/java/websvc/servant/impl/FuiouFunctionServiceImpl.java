package websvc.servant.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.component.redis.KeyGenerator;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.CreditConstant;
import com.zdmoney.constant.InterfaceCode;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.AccountDto;
import com.zdmoney.integral.api.dto.lcbaccount.AccountOprDto;
import com.zdmoney.integral.api.dto.lcbaccount.AccountOprResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.UserDrawModelDto;
import com.zdmoney.integral.api.dto.lcbaccount.enm.AccountWholeType;
import com.zdmoney.integral.api.dto.lcbaccount.enm.WithdrawModel;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.integral.api.facade.IDepositFacadeService;
import com.zdmoney.mapper.bank.BusiBankLimitMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerLimitInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.models.zdpay.UserAuthDto;
import com.zdmoney.models.zdpay.UserGrantBO;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.BusiOrderService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.JSONUtils;
import com.zdmoney.utils.MD5Utils;
import com.zdmoney.vo.UserAuthOrderDto;
import com.zdmoney.web.dto.BankCardDTO;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.servant.FuiouFunctionService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Service
@Slf4j
public class FuiouFunctionServiceImpl implements FuiouFunctionService {

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private CustomerGrantInfoMapper customerGrantInfoMapper;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private BusiBankLimitMapper busiBankLimitMapper;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @Autowired
    private IAccountFacadeService accountFacadeService;

    @Autowired
    private ILCBSubjectFacadeService subjectFacadeService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private IDepositFacadeService depositFacadeService;


    /**
     * 获取客户端类型
     * 0：PC，1：APP
     *
     * @param reqHeadParam
     * @return
     */
    private int getPlatform(ReqHeadParam reqHeadParam) {
        int clientType = 0;
        if ("APP".equals(reqHeadParam.getPlatform().toUpperCase()) ||
                "TOUCH".equals(reqHeadParam.getPlatform().toUpperCase())) {
            clientType = 1;
        }
        return clientType;
    }

    /**
     * 用户属性：1.出借人2.借款人3.营销户 4.手续费户5.代偿户6.消费金融商家7.平台自有资金账户 9.担保方手续费户
     * @param accountType
     * @return
     */
    private Integer getUserAttr(String accountType) {
        if (CustomerAccountType.LENDER.getValue().equals(accountType)) {
            return 1;
        } else if (CustomerAccountType.BORROWER.getValue().equals(accountType)) {
            return 2;
        } else if (CustomerAccountType.MARKETING.getValue().equals(accountType)) {
            return 3;
        } else if (CustomerAccountType.SERVICE_CHARGE_ACCOUNT.getValue().equals(accountType)) {
            return 4;
        } else if (CustomerAccountType.COMPENSATORY_ACCOUNT.getValue().equals(accountType)) {
            return 5;
        } else if (CustomerAccountType.CONSUME_ACCOUNT.getValue().equals(accountType)) {
            return 6;
        } else if (CustomerAccountType.PLATFORM_ACCOUNT.getValue().equals(accountType)) {
            return 7;
        } else if (CustomerAccountType.GUARANTEE_ACCOUNT.getValue().equals(accountType)) {
            return 9;
        }
        return 0;
    }

    @FunctionId("430001")
    @Override
    public Result openAccount(ReqMain reqMain) throws Exception {
        Model_430001 model = (Model_430001) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findAuthCustomerById(model.getCustomerId());
        if (StringUtils.isNotEmpty(mainInfo.getFuiouLoginId())) {
            return Result.fail("您已开户, 请勿重复开户");
        }
        int userAttr = getUserAttr(mainInfo.getAccountType());
        int clientType = getPlatform(reqMain.getReqHeadParam());
        /**
         * 0个人,1法人
         */
        String openType = "0";
        if (!"0".equals(mainInfo.getCustomerType())){
            openType = "1";
        }
        Long autoFeeAmt = 0L;
        String autoFeeTerm = null;

        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        String custName = !"0".equals(openType)?mainInfo.getEnterpriseName(): mainInfo.getCmRealName();
        map.put("custName", custName);
        map.put("openType", openType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("userAttr", userAttr);
        map.put("userId", mainInfo.getId());
        map.put("artifName", mainInfo.getCmRealName());
        map.put("pageUrl", model.getPageUrl());
        map.put("idCardNo", mainInfo.getCmIdnum());

        if (!"2".equals(mainInfo.getCustomerType())) {
            if (userAttr == 1) {
                autoFeeAmt = Convert.toLong(configParamBean.getFuiouLenderAutoFeeAmt()) * 100;
                map.put("autoFeeAmt", autoFeeAmt);
                map.put("autoLendAmt", Convert.toLong(configParamBean.getFuiouLenderAutoLendAmt())*100);
                autoFeeTerm = DateUtil.getFuiouTerm(new Date(), 10);
                map.put("autoFeeTerm", autoFeeTerm);
                map.put("autoLendTerm", autoFeeTerm);
            } else {
                if(CreditConstant.WACAI_CHANNEL.getCode().equals(mainInfo.getChannelCode())){
                    log.info("挖财渠道缴费额度：5000000(分)");
                    autoFeeAmt = 5000000L; //分
                }else {
                    autoFeeAmt = 0L;
                    AssetsResultDto<BigDecimal> resultDto = subjectFacadeService.getNewBorrowTotalAmount(mainInfo.getCmIdnum());
                    log.info("标的--查询新建标的借款金额:" + JSON.toJSONString(resultDto));
                    if (resultDto.isSuccess() && resultDto.getData().compareTo(new BigDecimal(0)) == 1) {
                        BigDecimal amt = resultDto.getData().divide(new BigDecimal(2));
                        autoFeeAmt = Convert.toLong(amt.multiply(new BigDecimal(100))) ;
                    }
                }
                map.put("autoFeeAmt", autoFeeAmt);
                map.put("autoFeeTerm", DateUtil.getFuiouTerm(new Date(), 10));
                map.put("autoRepayTerm", DateUtil.getFuiouTerm(new Date(), 10));
                map.put("autoRepayAmt", 0);
            }
        }
        if (1 == userAttr) {
            map.put("authStatus", "100100000000");
        } else if (2 == userAttr) {
            map.put("authStatus", "010100000000");
        } else {
            map.put("authStatus", "000000000000");
        }
        if (StringUtils.isNotEmpty(model.getBackUrl())) {
            map.put("backUrl", model.getBackUrl());
        }
        StringBuilder url = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_OPEN.getUrl()).
                append("?merchantCode=").append(configParamBean.getFuiouMerchantCode());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ("merchantCode".equals(entry.getKey())) continue;
            url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        url.append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", url);
        return Result.success(resMap);
    }

    public Result openAccountWithParamEncode(ReqMain reqMain) throws Exception {
        Model_430001 model = (Model_430001) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findAuthCustomerById(model.getCustomerId());
        if (StringUtils.isNotEmpty(mainInfo.getFuiouLoginId())) {
            return Result.fail("您已开户, 请勿重复开户");
        }
        int userAttr = getUserAttr(mainInfo.getAccountType());
        int clientType = getPlatform(reqMain.getReqHeadParam());
        /**
         * 0个人,1法人
         */
        String openType = "0";
        if (!"0".equals(mainInfo.getCustomerType())){
            openType = "1";
        }
        Long autoFeeAmt = 0L;
        String autoFeeTerm = null;

        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        String custName = !"0".equals(openType)?mainInfo.getEnterpriseName(): mainInfo.getCmRealName();
        map.put("custName", custName);
        map.put("openType", openType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("userAttr", userAttr);
        map.put("userId", mainInfo.getId());
        map.put("artifName", mainInfo.getCmRealName());
        map.put("pageUrl", model.getPageUrl());
        map.put("idCardNo", mainInfo.getCmIdnum());

        if (!"2".equals(mainInfo.getCustomerType())) {
            if (userAttr == 1) {
                autoFeeAmt = Convert.toLong(configParamBean.getFuiouLenderAutoFeeAmt()) * 100;
                map.put("autoFeeAmt", autoFeeAmt);
                map.put("autoLendAmt", Convert.toLong(configParamBean.getFuiouLenderAutoLendAmt())*100);
                autoFeeTerm = DateUtil.getFuiouTerm(new Date(), 10);
                map.put("autoFeeTerm", autoFeeTerm);
                map.put("autoLendTerm", autoFeeTerm);
            } else {
                if(CreditConstant.WACAI_CHANNEL.getCode().equals(mainInfo.getChannelCode())){
                    log.info("挖财渠道缴费额度：5000000(分)");
                    autoFeeAmt = 5000000L; //分
                }else {
                    autoFeeAmt = 0L;
                    AssetsResultDto<BigDecimal> resultDto = subjectFacadeService.getNewBorrowTotalAmount(mainInfo.getCmIdnum());
                    log.info("标的--查询新建标的借款金额:" + JSON.toJSONString(resultDto));
                    if (resultDto.isSuccess() && resultDto.getData().compareTo(new BigDecimal(0)) == 1) {
                        BigDecimal amt = resultDto.getData().divide(new BigDecimal(2));
                        autoFeeAmt = Convert.toLong(amt.multiply(new BigDecimal(100))) ;
                    }
                }
                map.put("autoFeeAmt", autoFeeAmt);
                map.put("autoFeeTerm", DateUtil.getFuiouTerm(new Date(), 10));
                map.put("autoRepayTerm", DateUtil.getFuiouTerm(new Date(), 10));
                map.put("autoRepayAmt", 0);
            }
        }
        if (1 == userAttr) {
            map.put("authStatus", "100100000000");
        } else if (2 == userAttr) {
            map.put("authStatus", "010100000000");
        } else {
            map.put("authStatus", "000000000000");
        }
        if (StringUtils.isNotEmpty(model.getBackUrl())) {
            map.put("backUrl", model.getBackUrl());
        }

        StringBuilder url = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_OPEN.getUrl()).
                append("?merchantCode=").append(configParamBean.getFuiouMerchantCode());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ("merchantCode".equals(entry.getKey())) continue;
            url.append("&").append(entry.getKey()).append("=").append(entry.getValue() == null?entry.getValue():URLEncoder.encode(entry.getValue().toString(),"utf-8"));
        }
        url.append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", url);
        return Result.success(resMap);
    }

    @FunctionId("430002")
    @Override
    public Result bankCardBind(ReqMain reqMain) throws Exception {
        Model_430002 model = (Model_430002) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        if (StringUtils.isEmpty(mainInfo.getFuiouLoginId())) {
            String msg = "您还未开户,请开户后进行绑卡操作";
            return Result.fail(msg);
        }
        if (mainInfo.getBankAccountId() != null) {
            String msg = "您已有绑卡信息，请勿重复绑卡";
            return Result.fail(msg);
        }
        int clientType = getPlatform(reqMain.getReqHeadParam());
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("userId", mainInfo.getId());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("pageUrl", model.getPageUrl());

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.BANK_BIND.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&clientType=").append(clientType)
                .append("&backNotifyUrl=").append(configParamBean.getZdpayBackNotifyUrl())
                .append("&userId=").append(mainInfo.getId())
                .append("&loginId=").append(mainInfo.getFuiouLoginId())
                .append("&pageUrl=").append(model.getPageUrl())
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", sb);
        return Result.success(resMap);
    }

    @FunctionId("430003")
    @Override
    public Result bankCardUnBind(ReqMain reqMain) throws Exception {
        Model_430003 model = (Model_430003) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        if (mainInfo == null) {
            throw new BusinessException("用户信息不存在，请致电客服！");
        }
        String bankId = mainInfo.getBankAccountId();
        if (org.apache.commons.lang.StringUtils.isEmpty(bankId)) {
            throw new BusinessException("该用户未绑卡，请致电客服！");
        }
        AssetsResultDto<Integer> resultDto;
        if(BusiConstants.LOGIN_TYPE_CREDIT.equalsIgnoreCase(reqMain.getReqHeadParam().getSystem())){
            try {
                resultDto = subjectFacadeService.findExistUnLoan(mainInfo.getCmIdnum());
                if (!resultDto.isSuccess()) {
                    log.error("调用标的系统，查询借款人是否放款信息失败，失败原因: "+ resultDto.getMsg());
                }
            } catch (Exception e) {
                log.info("调用标的系统，查询借款人是否放款信息失败异常：  " + e);
                throw new BusinessException("未放款的借款信息查询异常，请致电客服！");
            }
            if (resultDto != null && resultDto.getData().intValue() == 1) {
                throw new BusinessException("您有未放款的借款，无法解绑银行卡");
            }
        }
        //理财端借款人不能解绑
        if(!BusiConstants.LOGIN_TYPE_CREDIT.equalsIgnoreCase(reqMain.getReqHeadParam().getSystem())){
            if(mainInfo.getAccountType().equals(CustomerAccountType.BORROWER.getValue())){
                throw new BusinessException("解绑失败！您需前往借款端进行相关操作哦~！");
            }
        }
        //机构理财用户不能解绑
        if ("1".equals(mainInfo.getCustomerType())) {
            throw new BusinessException("机构户不支持此功能！");
        }

        int clientType = getPlatform(reqMain.getReqHeadParam());
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("userId", mainInfo.getId());
        map.put("pageUrl", model.getPageUrl());

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.BANK_UN_BIND.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&clientType=").append(clientType)
                .append("&backNotifyUrl=").append(configParamBean.getZdpayBackNotifyUrl())
                .append("&loginId=").append(mainInfo.getFuiouLoginId())
                .append("&userId=").append(mainInfo.getId())
                .append("&pageUrl=").append(model.getPageUrl())
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", sb);
        return Result.success(resMap);
    }

    @FunctionId("430004")
    @Override
    public Result mobileChange(ReqMain reqMain) throws Exception {
        Model_430004 model = (Model_430004) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        int clientType = getPlatform(reqMain.getReqHeadParam());
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("changeType", model.getChangeType());
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("pageUrl", model.getPageUrl());

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.MOBILE_CHANGE.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&changeType=").append(model.getChangeType())
                .append("&clientType=").append(clientType)
                .append("&backNotifyUrl=").append(configParamBean.getZdpayBackNotifyUrl())
                .append("&loginId=").append(mainInfo.getFuiouLoginId())
                .append("&pageUrl=").append(model.getPageUrl())
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", sb);
        return Result.success(resMap);
    }

    @FunctionId("430005")
    @Override
    public Result passwordModify(ReqMain reqMain) throws Exception {
        Model_430005 model = (Model_430005) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        int clientType = getPlatform(reqMain.getReqHeadParam());
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("changeType", model.getChangeType());
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("pageUrl", model.getPageUrl());

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.PASSWORD_MODIFY.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&clientType=").append(clientType)
                .append("&changeType=").append(model.getChangeType())
                .append("&backNotifyUrl=").append(configParamBean.getZdpayBackNotifyUrl())
                .append("&loginId=").append(mainInfo.getFuiouLoginId())
                .append("&pageUrl=").append(model.getPageUrl())
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", sb);
        return Result.success(resMap);
    }

    @FunctionId("430006")
    @Override
    public Result smsNofity(ReqMain reqMain) throws Exception {
        Model_430006 model = (Model_430006) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());

        int clientType = getPlatform(reqMain.getReqHeadParam());
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("pageUrl", model.getPageUrl());

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.SMS_NOFITY.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&clientType=").append(clientType)
                .append("&backNotifyUrl=").append(configParamBean.getZdpayBackNotifyUrl())
                .append("&loginId=").append(mainInfo.getFuiouLoginId())
                .append("&pageUrl=").append(model.getPageUrl())
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", sb);
        return Result.success(resMap);
    }

    @FunctionId("430008")
    @Override
    public Result recharge(ReqMain reqMain) throws Exception {
        Model_430008 model = (Model_430008) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findAuthCustomerById(model.getCustomerId());
        BigDecimal amt = Convert.toBigDecimal(model.getPayAmt());
        if (amt.compareTo(new BigDecimal(5)) == -1) {
            return Result.fail("最少充值金额5元!");
        }
        if (StringUtils.isEmpty(mainInfo.getBankAccountId())) {
            return Result.fail("请先绑卡再充值!");
        }
        int clientType = getPlatform(reqMain.getReqHeadParam());
        String payTime = DateUtil.getDateFormatString(new Date(), "yyyyMMddHHmmsss");
        // 4001（认证支付）|4002(网银支付)
        String productType = (1 == model.getChannelType()) ? AppConstants.PAY_TYPE.EBANK_PAY : AppConstants.PAY_TYPE.QUICK_PAY;
        BigDecimal payAmt = Convert.toBigDecimal(model.getPayAmt()).multiply(Convert.toBigDecimal( "100"));
        String flowNum = busiOrderService.buildCeNumber("R", "8888", mainInfo.getId());
        BusiTradeFlow tradeFlow = new BusiTradeFlow();
        tradeFlow.setFlowNum(flowNum);
        tradeFlow.setTrdDate(new Date());
        tradeFlow.setTrdType(AppConstants.TradeStatusContants.RECHARGEING);
        tradeFlow.setTrdAmt(Convert.toBigDecimal(model.getPayAmt()));
        tradeFlow.setCustomerId(mainInfo.getId());
        tradeFlow.setStatus(AppConstants.TradeStatusContants.INIT);
        tradeFlow.setApplicantType(CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType()) ? "1" : "0");
        CustomerBankAccount account = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(mainInfo.getBankAccountId()));
        if (account != null) {
            tradeFlow.setBankName(account.getCbBankName());
            tradeFlow.setBankCardId(account.getId());
            tradeFlow.setBankCardNum(account.getCbAccount());
        }
        // 101:网银充值  102: 快捷充值
        tradeFlow.setType(model.getChannelType() == 1 ? 101 : 102);
        busiTradeFlowMapper.insert(tradeFlow);

        int businessType = "credit".equals(mainInfo.getCmOpenChannel()) ? 2 : 1;
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("channelOrderNo", flowNum);
        map.put("payTime", payTime);
        map.put("channelType", model.getChannelType());
        map.put("productType", productType);
        map.put("payAmt", payAmt.longValue());
        map.put("businessType", businessType);
        map.put("pageUrl", model.getPageUrl());

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_RECHARGE.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&clientType=").append(clientType)
                .append("&backNotifyUrl=").append(configParamBean.getZdpayBackNotifyUrl())
                .append("&loginId=").append(mainInfo.getFuiouLoginId())
                .append("&channelType=").append(model.getChannelType())
                .append("&payTime=").append(payTime)
                .append("&productType=").append(productType)
                .append("&payAmt=").append(payAmt.longValue()).append("&businessType=").append(businessType)
                .append("&channelOrderNo=").append(flowNum)
                .append("&pageUrl=").append(model.getPageUrl())
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", sb);
        return Result.success(resMap);
    }

    @FunctionId("430010")
    @Override
    public Result userGrant(ReqMain reqMain) throws Exception {
        Model_430010 model = (Model_430010) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        Map<String, Object> map = Maps.newHashMap();
        Long autoLendAmt = Convert.toLong(configParamBean.getFuiouLenderAutoLendAmt()) * 100;// 自动出借授权额度,单位分
        Long autoFeeAmt = Convert.toLong(configParamBean.getFuiouLenderAutoFeeAmt()) * 100;// 缴费授权额度,单位分
        String autoLendTerm = DateUtil.getFuiouTerm(new Date(), 10);
        String autoFeeTerm = DateUtil.getFuiouTerm(new Date(), 10);
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
        int type = 1;
        if (CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
            autoLendTerm = null;
            autoFeeAmt = 0L;
            if (userGrant != null) {
                UserAuthOrderDto authOrder = busiOrderService.borrowerAuthInfo(mainInfo.getId(), new Date(), model.getOrderAmt());
                log.info("-------->busiOrderService.borrowerAuthInfo授权:" + JSON.toJSONString(authOrder));
                if (authOrder.getAuthFee().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) == 1) {
                    autoFeeAmt = Convert.toLong(authOrder.getAuthFee().multiply(new BigDecimal(100)));
                }
            }
            autoFeeTerm = DateUtil.getFuiouTerm(new Date(), 10);
            type = 2;
        } else if (CustomerAccountType.COMPENSATORY_ACCOUNT.getValue().equals(mainInfo.getAccountType())) {
            type = 3;
        } else if (userGrant != null){
            type = 1;
            autoLendAmt = userGrant.getAutoLendAmt();
            autoFeeAmt = userGrant.getAutoFeeAmt();
            if (model.getServiceFee() != null) {
                // 转让加服务费
                UserAuthOrderDto userAuthOrder = busiOrderService.commonUserAuthOrder(mainInfo.getId());
                log.info("---->转让授权计算，所需授权额度: {},当前授权信息: {}",JSON.toJSONString(userAuthOrder), JSON.toJSONString(userGrant));
                if (userAuthOrder.getTotalAmt().compareTo(Convert.toBigDecimal(userGrant.getAutoLendAmt()).divide(new BigDecimal(100))) >= 0) {
                    autoLendAmt = Convert.toLong(userAuthOrder.getTotalAmt().multiply(new BigDecimal(100)).add(new BigDecimal(100)));
                }
                if (userAuthOrder.getAuthFee().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) >= 0) {
                    BigDecimal totalAmt = userAuthOrder.getAuthFee().add(model.getServiceFee()).multiply(new BigDecimal(100).add(new BigDecimal(100)));
                    autoFeeAmt = Convert.toLong(totalAmt);
                }
            } else {
                UserAuthDto authOrder = busiOrderService.getOrderUserAuth(model.getCustomerId(), model.getProductId(), model.getOrderAmt());
                log.info("---->下单授权计算，所需授权额度: {},当前授权信息: {}",JSON.toJSONString(authOrder), JSON.toJSONString(userGrant));
                // 下单
                if (authOrder.getAuthTotalAmt().compareTo(Convert.toBigDecimal(userGrant.getAutoLendAmt()).divide(new BigDecimal(100))) >= 0) {
                    autoLendAmt = Convert.toLong(authOrder.getAuthTotalAmt().multiply(new BigDecimal(100)).add(new BigDecimal(100)));
                }
                if (authOrder.getPayAmt().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) >= 0) {
                    autoFeeAmt = Convert.toLong(authOrder.getPayAmt().multiply(new BigDecimal(100)).add(new BigDecimal(100)));
                }
            }
        }

        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        int clientType = getPlatform(reqMain.getReqHeadParam());
        map.put("clientType", clientType);
        map.put("loginId", mainInfo.getFuiouLoginId());

        map.put("pageUrl", model.getPageUrl());
        map.put("grantType", type);
        int userAttr = getUserAttr(mainInfo.getAccountType());
        if (userAttr == 1) {
            Long feeAmt = Convert.toLong(configParamBean.getFuiouLenderAutoFeeAmt()) * 100;
            map.put("autoFeeAmt", autoFeeAmt > feeAmt ? autoFeeAmt : feeAmt);
            map.put("autoFeeTerm", autoFeeTerm);
            Long lendAmt = Convert.toLong(configParamBean.getFuiouLenderAutoLendAmt()) * 100;
            map.put("autoLendAmt", autoLendAmt > lendAmt ? autoLendAmt : lendAmt);
            map.put("autoLendTerm", autoLendTerm);
            map.put("authStatus", "100100000000");
        } else {
            map.put("autoFeeAmt", autoFeeAmt);
            map.put("autoFeeTerm", autoFeeTerm);
            map.put("autoRepayTerm", DateUtil.getFuiouTerm(new Date(), 10));
            map.put("autoRepayAmt", 0);
            map.put("authStatus", "010100000000");
        }
        if (StringUtils.isNotEmpty(model.getBackUrl())) {
            map.put("backUrl", model.getBackUrl());
        }
        Map<String, Object> temp = Maps.newHashMap();
        temp.put("userId", mainInfo.getId());
        String remarkJson = JSON.toJSONString(temp);
        map.put("remark", remarkJson);
        //先生成签名 再UrlEncode remark
        String signature = MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey());
        map.put("remark", URLUtil.encode(remarkJson,CharsetUtil.UTF_8));

        StringBuilder url = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_GRANT.getUrl()).
                append("?merchantCode=").append(configParamBean.getFuiouMerchantCode());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ("merchantCode".equals(entry.getKey())){
                continue;
            }
            url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        url.append("&signature=").append(signature);
        Map<String, Object> resMap = Maps.newHashMap();
        resMap.put("url", url);
        return Result.success(resMap);
    }

    @Override
    public Result userGrantWithParamEncode(ReqMain reqMain) throws Exception {
        Model_430010 model = (Model_430010) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        Map<String, Object> map = new HashMap<>();
        Long autoLendAmt = Convert.toLong(configParamBean.getFuiouLenderAutoLendAmt()) * 100;// 自动出借授权额度,单位分
        Long autoFeeAmt = Convert.toLong(configParamBean.getFuiouLenderAutoFeeAmt()) * 100;// 缴费授权额度,单位分
        String autoLendTerm = DateUtil.getFuiouTerm(new Date(), 10);
        String autoFeeTerm = DateUtil.getFuiouTerm(new Date(), 10);
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
        int type = 1;
        if (CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
            autoLendTerm = null;
            autoFeeAmt = 0L;
            if (userGrant != null) {
                /**
                 * 重新授权的时候如果订单金额为空则置为5w
                 */
                if(model.getOrderAmt() == null){
                    autoFeeAmt = 5000000L;//分
                }
                UserAuthOrderDto authOrder = busiOrderService.borrowerAuthInfo(mainInfo.getId(), new Date(), model.getOrderAmt());
                log.info("-------->busiOrderService.borrowerAuthInfo授权:" + JSON.toJSONString(authOrder));
                if (authOrder.getAuthFee().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) == 1) {
                    autoFeeAmt = Convert.toLong(authOrder.getAuthFee().multiply(new BigDecimal(100)));
                }
            }else if(CreditConstant.WACAI_CHANNEL.getCode().equals(mainInfo.getChannelCode())){
                autoFeeAmt = 5000000L;//分
            }
            autoFeeTerm = DateUtil.getFuiouTerm(new Date(), 10);
            type = 2;
        } else if (CustomerAccountType.COMPENSATORY_ACCOUNT.getValue().equals(mainInfo.getAccountType())) {
            type = 3;
        } else if (userGrant != null){
            type = 1;
            autoLendAmt = userGrant.getAutoLendAmt();
            autoFeeAmt = userGrant.getAutoFeeAmt();
            if (model.getServiceFee() != null) {
                // 转让加服务费
                UserAuthOrderDto userAuthOrder = busiOrderService.commonUserAuthOrder(mainInfo.getId());
                log.info("---->转让授权计算，所需授权额度: {},当前授权信息: {}",JSON.toJSONString(userAuthOrder), JSON.toJSONString(userGrant));
                if (userAuthOrder.getTotalAmt().compareTo(Convert.toBigDecimal(userGrant.getAutoLendAmt()).divide(new BigDecimal(100))) >= 0) {
                    autoLendAmt = Convert.toLong(userAuthOrder.getTotalAmt().multiply(new BigDecimal(100)).add(new BigDecimal(100)));
                }
                if (userAuthOrder.getAuthFee().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) >= 0) {
                    BigDecimal totalAmt = userAuthOrder.getAuthFee().add(model.getServiceFee()).multiply(new BigDecimal(100).add(new BigDecimal(100)));
                    autoFeeAmt = Convert.toLong(totalAmt);
                }
            } else {
                UserAuthDto authOrder = busiOrderService.getOrderUserAuth(model.getCustomerId(), model.getProductId(), model.getOrderAmt());
                log.info("---->下单授权计算，所需授权额度: {},当前授权信息: {}",JSON.toJSONString(authOrder), JSON.toJSONString(userGrant));
                // 下单
                if (authOrder.getAuthTotalAmt().compareTo(Convert.toBigDecimal(userGrant.getAutoLendAmt()).divide(new BigDecimal(100))) >= 0) {
                    autoLendAmt = Convert.toLong(authOrder.getAuthTotalAmt().multiply(new BigDecimal(100)).add(new BigDecimal(100)));
                }
                if (authOrder.getPayAmt().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) >= 0) {
                    autoFeeAmt = Convert.toLong(authOrder.getPayAmt().multiply(new BigDecimal(100)).add(new BigDecimal(100)));
                }
            }
        }

        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        int clientType = getPlatform(reqMain.getReqHeadParam());
        map.put("clientType", clientType);
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("pageUrl", model.getPageUrl());
        Map<String, Object> temp = new HashMap<>();
        temp.put("userId", mainInfo.getId());
        map.put("remark", JSON.toJSONString(temp));
        map.put("grantType", type);
        int userAttr = getUserAttr(mainInfo.getAccountType());
        if (userAttr == 1) {
            Long feeAmt = Convert.toLong(configParamBean.getFuiouLenderAutoFeeAmt()) * 100;
            map.put("autoFeeAmt", autoFeeAmt > feeAmt ? autoFeeAmt : feeAmt);
            map.put("autoFeeTerm", autoFeeTerm);
            Long lendAmt = Convert.toLong(configParamBean.getFuiouLenderAutoLendAmt()) * 100;
            map.put("autoLendAmt", autoLendAmt > lendAmt ? autoLendAmt : lendAmt);
            map.put("autoLendTerm", autoLendTerm);
            map.put("authStatus", "100100000000");
        } else {
            map.put("autoFeeAmt", autoFeeAmt);
            map.put("autoFeeTerm", autoFeeTerm);
            map.put("autoRepayTerm", DateUtil.getFuiouTerm(new Date(), 10));
            map.put("autoRepayAmt", 0);
            map.put("authStatus", "010100000000");
        }
        if (StringUtils.isNotEmpty(model.getBackUrl())) {
            map.put("backUrl", model.getBackUrl());
        }

        StringBuilder url = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_GRANT.getUrl()).
                append("?merchantCode=").append(configParamBean.getFuiouMerchantCode());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ("merchantCode".equals(entry.getKey())) continue;
            url.append("&").append(entry.getKey()).append("=").append(entry.getValue() == null?entry.getValue():URLEncoder.encode(entry.getValue().toString(),"utf-8"));
        }
        url.append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("url", url);
        return Result.success(resMap);
    }

    @FunctionId("430009")
    @Override
    public Result withdraw(ReqMain reqMain) throws Exception {
        Model_430009 model = (Model_430009) reqMain.getReqParam();

        //校验用户能否提现
        Model_431009 model431009 =  new Model_431009();
        BeanUtils.copyProperties(model,model431009);
        Result checkResult = checkWithdrawFunc(model431009);
        if (!checkResult.getSuccess()){
            return  checkResult;
        }
        Map<String,String> result =new HashMap<>();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());





        int clientType = getPlatform(reqMain.getReqHeadParam());
        String payTime = DateUtil.getDateFormatString(new Date(), "yyyyMMddHHmmsss");
        String flowId = busiOrderService.buildCeNumber("W", "8888", model.getCustomerId());
        // 服务费
        BigDecimal feeAmount = new BigDecimal(0);
        //提现流水对象
        BusiTradeFlow tradeFlow = new BusiTradeFlow();
        try {
            //通知账户冻结该用户提现资金
            tradeFlow.setTrdDate(new Date());
            tradeFlow.setCustomerId(model.getCustomerId());
            tradeFlow.setFlowNum(flowId);
            tradeFlow.setTrdType(AppConstants.TradeStatusContants.WITHDRAWING);
            tradeFlow.setStatus(AppConstants.TradeStatusContants.INIT);
            tradeFlow.setType(model.getType());
            tradeFlow.setJobFlag(0);
            tradeFlow.setTrdAmt(Convert.toBigDecimal(model.getPayAmt()));

            CustomerBankAccount account = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(mainInfo.getBankAccountId()));
            if (account != null) {
                tradeFlow.setBankName(account.getCbBankName());
                tradeFlow.setBankCardId(account.getId());
                tradeFlow.setBankCardNum(account.getCbAccount());
            }
            //设置提现申请人类型
            if (CustomerAccountType.BORROWER.getValue().equalsIgnoreCase(mainInfo.getAccountType())) {
                tradeFlow.setApplicantType(com.zdmoney.constant.Constants.APPLICANT_TYPE_MANAGER);
            } else {
                tradeFlow.setApplicantType(com.zdmoney.constant.Constants.APPLICANT_TYPE_INVESTOR);
            }
            // 提现模式配置接口
            UserDrawModelDto userDrawModelDto = new UserDrawModelDto();
            userDrawModelDto.setLoginId(mainInfo.getFuiouLoginId());
            userDrawModelDto.setWithdrawModel(AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_1 == model.getType() ? WithdrawModel.NEXT_DAY : WithdrawModel.CURRENT_DAY);
            ResultDto resultDto = depositFacadeService.withDrawModelSetting(userDrawModelDto);
            if (!resultDto.isSuccess()) {
                throw new BusinessException("提现模式配置接口错误:" + resultDto.getMsg());
            }

            //仅个人提现需要冻结
            if (CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType()) || CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
                AccountOprDto accountOprDto =new AccountOprDto();
                // 快速提现服务费
                if (AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_2 == model.getType()) {
                    BigDecimal rate = Convert.toBigDecimal(configParamBean.getWithdraw_rate());
                    // 计算手续费, 精确到小数点后两位，计算时小数点第三位截断处理，服务费最小0.01。
                    feeAmount = Convert.toBigDecimal(model.getPayAmt()).multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
                    if (feeAmount.compareTo(new BigDecimal(0)) <= 0) {
                        feeAmount = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_DOWN);
                    }
                    // 扣除手续费后, 到账金额
                    BigDecimal arrivalAmt = Convert.toBigDecimal(model.getPayAmt()).subtract(feeAmount).setScale(2, BigDecimal.ROUND_DOWN);
                    tradeFlow.setTrdAmt(arrivalAmt);
                    tradeFlow.setServiceCharge(feeAmount);
                }
                //提现冻结流水号
                String transNo = "TXDJ"+flowId;
                accountOprDto.setTransNo(transNo);
                accountOprDto.setOrderNo(flowId);
                accountOprDto.setAccountNo(mainInfo.getCmNumber());
                accountOprDto.setAccountType(AccountWholeType.PERSONAL);
                // 积分以元为单位
                accountOprDto.setAmount(tradeFlow.getTrdAmt());
                accountOprDto.setFeeAmount(feeAmount);
                log.info("提现冻结，客户号：{}，提现金额：{}，账户端冻结成功！账户入参内容：{}",mainInfo.getCmNumber(),model.getPayAmt(), JSON.toJSONString(accountOprDto));
                ResultDto<AccountOprResultDto> accountOprResultDtoResultDto = accountFacadeService.withdrawFrozen(accountOprDto);
                log.info("提现冻结成功，客户号：{}，提现金额：{}，账户端冻结成功！账户返回内容：{}",mainInfo.getCmNumber(),model.getPayAmt(),JSON.toJSONString(accountOprResultDtoResultDto));
                tradeFlow.setAccountSeriNo(accountOprResultDtoResultDto.getData().getRecordNum());
            }
            busiTradeFlowMapper.insert(tradeFlow);
        } catch (Exception e) {
            log.error("提现冻结失败，用户ID:{},失败原因:{}", mainInfo.getId(), e);
            result.put("code","0006");
            result.put("msg","提现冻结失败,冻结金额："+model.getPayAmt());
            return Result.fail(result.get("msg"), result);
        }

        // 账户以分为单位
        long payAmt = tradeFlow.getTrdAmt().multiply(Convert.toBigDecimal("100")).longValue();

        Map<String, Object> map = Maps.newHashMap();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("channelOrderNo", flowId);
        map.put("payTime", payTime);
        map.put("productType", "4002");
        map.put("payAmt", payAmt);
        map.put("userName", mainInfo.getCmRealName());
        map.put("pageUrl", model.getPageUrl());

        Map<String, Object> temp = Maps.newHashMap();
        temp.put("withdrawType", model.getType());
        String remarkJson = JSON.toJSONString(temp);
        map.put("remark",remarkJson);
        //先生成签名 再UrlEncode remark
        String signature = MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey());
        map.put("remark", URLUtil.encode(remarkJson, CharsetUtil.UTF_8));

        StringBuilder url = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_WITHDRAW.getUrl()).
                append("?merchantCode=").append(configParamBean.getFuiouMerchantCode());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ("merchantCode".equals(entry.getKey())) {
                continue;
            }
            url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        url.append("&signature=").append(signature);

        result.put("code","0000");
        result.put("msg","操作成功");
        result.put("url",url.toString());
        return Result.success(result.get("msg"), result);
    }

    @Override
    public Result withdrawWithParamEncode(ReqMain reqMain) throws Exception {
        Model_430009 model = (Model_430009) reqMain.getReqParam();

        //校验用户能否提现
        Model_431009 model431009 =  new Model_431009();
        BeanUtils.copyProperties(model,model431009);
        Result checkResult = checkWithdrawFunc(model431009);
        if (!checkResult.getSuccess()){
            return  checkResult;
        }
        Map<String,String> result =new HashMap<>();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());

        int clientType = getPlatform(reqMain.getReqHeadParam());
        String payTime = DateUtil.getDateFormatString(new Date(), "yyyyMMddHHmmsss");
        String flowId = "";
        if (model.getBorrowNo() != null && !"".equals(model.getBorrowNo())) {
            String length = model.getBorrowNo().length()>=10 ? model.getBorrowNo().length()+"" : "0"+ model.getBorrowNo().length();
            flowId += busiOrderService.buildCeNumber("W", length+model.getBorrowNo() + "8888", model.getCustomerId());
        }else {
            flowId += busiOrderService.buildCeNumber("W", "8888", model.getCustomerId());
        }

        // 服务费
        BigDecimal feeAmount = new BigDecimal(0);
        //提现流水对象
        BusiTradeFlow tradeFlow = new BusiTradeFlow();
        try {
            //通知账户冻结该用户提现资金
            tradeFlow.setTrdDate(new Date());
            tradeFlow.setCustomerId(model.getCustomerId());
            tradeFlow.setFlowNum(flowId);
            tradeFlow.setTrdType(AppConstants.TradeStatusContants.WITHDRAWING);
            tradeFlow.setStatus(AppConstants.TradeStatusContants.INIT);
            tradeFlow.setType(model.getType());
            tradeFlow.setJobFlag(0);
            tradeFlow.setTrdAmt(Convert.toBigDecimal(model.getPayAmt()));

            CustomerBankAccount account = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(mainInfo.getBankAccountId()));
            if (account != null) {
                tradeFlow.setBankName(account.getCbBankName());
                tradeFlow.setBankCardId(account.getId());
                tradeFlow.setBankCardNum(account.getCbAccount());
            }
            //设置提现申请人类型
            if (CustomerAccountType.BORROWER.getValue().equalsIgnoreCase(mainInfo.getAccountType())) {
                tradeFlow.setApplicantType(com.zdmoney.constant.Constants.APPLICANT_TYPE_MANAGER);
            } else {
                tradeFlow.setApplicantType(com.zdmoney.constant.Constants.APPLICANT_TYPE_INVESTOR);
            }
            // 提现模式配置接口
            UserDrawModelDto userDrawModelDto = new UserDrawModelDto();
            userDrawModelDto.setLoginId(mainInfo.getFuiouLoginId());
            userDrawModelDto.setWithdrawModel(AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_1 == model.getType() ? WithdrawModel.NEXT_DAY : WithdrawModel.CURRENT_DAY);
            ResultDto resultDto = depositFacadeService.withDrawModelSetting(userDrawModelDto);
            if (!resultDto.isSuccess()) {
                throw new BusinessException("提现模式配置接口错误:" + resultDto.getMsg());
            }

            //仅个人提现需要冻结
            if (CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType()) || CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
                AccountOprDto accountOprDto =new AccountOprDto();
                // 快速提现服务费 --挖财渠道不收取服务费
                if (AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_2 == model.getType() && !CreditConstant.WACAI_CHANNEL.getCode().equals(mainInfo.getChannelCode())) {
                    BigDecimal rate = Convert.toBigDecimal(configParamBean.getWithdraw_rate());
                    // 计算手续费, 精确到小数点后两位，计算时小数点第三位截断处理，服务费最小0.01。
                    feeAmount = Convert.toBigDecimal(model.getPayAmt()).multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
                    if (feeAmount.compareTo(new BigDecimal(0)) <= 0) {
                        feeAmount = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_DOWN);
                    }
                    // 扣除手续费后, 到账金额
                    BigDecimal arrivalAmt = Convert.toBigDecimal(model.getPayAmt()).subtract(feeAmount).setScale(2, BigDecimal.ROUND_DOWN);
                    tradeFlow.setTrdAmt(arrivalAmt);
                    tradeFlow.setServiceCharge(feeAmount);
                }
                //提现冻结流水号
                String transNo = "TXDJ"+flowId;
                accountOprDto.setTransNo(transNo);
                accountOprDto.setOrderNo(flowId);
                accountOprDto.setAccountNo(mainInfo.getCmNumber());
                accountOprDto.setAccountType(AccountWholeType.PERSONAL);
                // 积分以元为单位
                accountOprDto.setAmount(tradeFlow.getTrdAmt());
                accountOprDto.setFeeAmount(feeAmount);
                log.info("提现冻结，客户号：{}，提现金额：{}，账户端冻结成功！账户入参内容：{}",mainInfo.getCmNumber(),model.getPayAmt(), JSON.toJSONString(accountOprDto));
                ResultDto<AccountOprResultDto> accountOprResultDtoResultDto = accountFacadeService.withdrawFrozen(accountOprDto);
                log.info("提现冻结成功，客户号：{}，提现金额：{}，账户端冻结成功！账户返回内容：{}",mainInfo.getCmNumber(),model.getPayAmt(),JSON.toJSONString(accountOprResultDtoResultDto));
                tradeFlow.setAccountSeriNo(accountOprResultDtoResultDto.getData().getRecordNum());
            }
            busiTradeFlowMapper.insert(tradeFlow);
        } catch (Exception e) {
            log.error("提现冻结失败，用户ID:{},失败原因:{}", mainInfo.getId(), e);
            result.put("code","0006");
            result.put("msg","提现冻结失败,冻结金额："+model.getPayAmt());
            return Result.fail(result.get("msg"), result);
        }

        // 账户以分为单位
        long payAmt = tradeFlow.getTrdAmt().multiply(Convert.toBigDecimal("100")).longValue();

        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", clientType);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", mainInfo.getFuiouLoginId());
        map.put("channelOrderNo", flowId);
        map.put("payTime", payTime);
        map.put("productType", "4002");
        map.put("payAmt", payAmt);
        map.put("userName", mainInfo.getCmRealName());
        map.put("pageUrl", model.getPageUrl());

        Map<String, Object> temp = new HashMap<>();
        temp.put("withdrawType", model.getType());
        map.put("remark", JSON.toJSONString(temp));

        StringBuilder sb = new StringBuilder(configParamBean.getZdpayUrl()).append(InterfaceCode.USER_WITHDRAW.getUrl())
                .append("?merchantCode=").append(configParamBean.getFuiouMerchantCode())
                .append("&clientType=").append(clientType)
                .append("&backNotifyUrl=").append(enCodeString(configParamBean.getZdpayBackNotifyUrl()))
                .append("&loginId=").append(enCodeString(mainInfo.getFuiouLoginId()))
                .append("&channelOrderNo=").append(enCodeString(flowId))
                .append("&payTime=").append(enCodeString(payTime))
                .append("&productType=").append(enCodeString("4002"))
                .append("&payAmt=").append(payAmt)
                .append("&userName=").append(enCodeString(mainInfo.getCmRealName()))
                .append("&pageUrl=").append(enCodeString(model.getPageUrl()))
                .append("&remark=").append(enCodeString(JSON.toJSONString(temp)))
                .append("&signature=").append(MD5Utils.MD5Encrypt(map, configParamBean.getFuiouKey()));
        result.put("code","0000");
        result.put("msg","操作成功");
        result.put("url",sb.toString());
        return Result.success(result.get("msg"), result);
    }

    private String enCodeString(String param) throws UnsupportedEncodingException {
        return param==null?null:URLEncoder.encode(param,"utf-8");
    }

    @FunctionId("431009")
    @Override
    public Result checkWithdraw(ReqMain reqMain) throws Exception {
        Model_431009 model = (Model_431009) reqMain.getReqParam();
        return checkWithdrawFunc(model);
    }

    public Result checkWithdrawFunc(Model_431009 model) throws Exception {
        Map<String,String> result =new HashMap<>();
        if (new BigDecimal(model.getPayAmt()).compareTo(new BigDecimal("1")) == -1) {
            result.put("code","0003");
            result.put("msg","最小提现金额1元");
            return Result.fail(result.get("msg"), result);
        }
        if (new BigDecimal(model.getPayAmt()).compareTo(new BigDecimal("1000000")) == 1) {
            result.put("code","0010");
            result.put("msg","单笔最大提现金额100万元");
            return Result.fail(result.get("msg"), result);
        }
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        if (mainInfo == null) {
            result.put("code","0004");
            result.put("msg","用户信息不存在");
            return Result.fail(result.get("msg"), result);
        }
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
        if (userGrant == null && AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_2 == model.getType()) {
            result.put("code","0009");
            result.put("msg","您还未授权，为保障您的正常交易，请尽快前往华瑞银行完成授权。");
            return Result.fail(result.get("msg"), result);
        }


        String CustomerRegisterDate = configParamBean.getQuickWithdrawLimitCustomerRegisterDate();
        Date quickWithdrawLimitCustomerRegisterDate = DateUtil.strFormatToDate(CustomerRegisterDate,"yyyy-MM-dd");
        if (model.getType()==2 && "lender".equals( mainInfo.getAccountType())&& DateUtil.compareStringDate(mainInfo.getCmInputDate(),quickWithdrawLimitCustomerRegisterDate)>=0){
            result.put("code","0003");
            result.put("msg","快速提现暂不能使用，请使用普通提现。");
            return Result.fail(result.get("msg"), result);
        }


        //每天提现限制
        if (configParamBean.getWithdrawalsLimitTimes().equalsIgnoreCase("ON")&&
                (CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType()) || CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType()))) {
            Map<String,Object> parameter =new HashMap<>();
            DateTime trdDateStart = cn.hutool.core.date.DateUtil.beginOfDay(new Date());
            DateTime trdDateEnd = cn.hutool.core.date.DateUtil.endOfDay(new Date());
            parameter.put("trdDateStart",trdDateStart.toJdkDate());
            parameter.put("trdDateEnd",trdDateEnd.toJdkDate());
            parameter.put("customerId",model.getCustomerId());
            parameter.put("trdType",AppConstants.TradeStatusContants.WITHDRAWING);
            List<BusiTradeFlow>  busiTradeFlowList = busiTradeFlowMapper.selectByStatusTrddate(parameter);

            String withdrawStatus ="Normal";
            int withdrawtimes = 0;// 成功提现次数
            BigDecimal withdrawlimit= new BigDecimal("0");
            for (BusiTradeFlow busiTradeFlowItem: busiTradeFlowList){
                if (AppConstants.TradeStatusContants.INIT.equals( busiTradeFlowItem.getStatus())
                        ||AppConstants.TradeStatusContants.PROCESSING.equals( busiTradeFlowItem.getStatus())
                        ||AppConstants.TradeStatusContants.WITHDRAW_REFUND_START.equals( busiTradeFlowItem.getStatus())
                        ||AppConstants.TradeStatusContants.WITHDRAW_REFUND_FAIL.equals( busiTradeFlowItem.getStatus())){
                    //有冻结状态提现申请记录
                    withdrawStatus = "Frozen";
                }else if (AppConstants.TradeStatusContants.PROCESS_SUCCESS.equals( busiTradeFlowItem.getStatus())){
                    withdrawlimit.add(busiTradeFlowItem.getTrdAmt());
                    withdrawtimes++;
                }
            }

            SysParameter sysParameter = sysParameterService.findOneByPrType("withdrawtimes");
            if (sysParameter == null) {
                throw new BusinessException("获取单日提现次数失败！");
            }

            SysParameter sysParameter1 = sysParameterService.findOneByPrType("withdrawlimit");
            if (sysParameter1 == null) {
                throw new BusinessException("获取单日提现上限金额失败！");
            }

            if (new BigDecimal(model.getPayAmt()).compareTo(new BigDecimal(sysParameter1.getPrValue()))>0){
                result.put("code","0007");
                result.put("msg","提现金额超过每天限额："+sysParameter1.getPrValue()+"元。");
                log.error("用户：{} 提现金额超过每天限额："+sysParameter1.getPrValue()+"元。",mainInfo.getCmNumber());
                return Result.fail(result.get("msg"), result);
            }
            else if (withdrawlimit.add(new BigDecimal(model.getPayAmt())).compareTo(new BigDecimal(sysParameter1.getPrValue()))>0){
                result.put("code","0007");
                result.put("msg","您今天已成功提现"+withdrawlimit.doubleValue()+"元，明天再来提现吧。");
                log.error("用户：{} 今天已成功提现"+withdrawlimit.doubleValue()+"元，明天再来提现吧",mainInfo.getCmNumber());
                return Result.fail(result.get("msg"), result);
            }
            else if (withdrawtimes >= Integer.parseInt(sysParameter.getPrValue())){
                result.put("code","0002");
                result.put("msg","您今天已成功提现"+withdrawtimes+"次，明天再来提现吧。");
                log.error("用户：{} 今天已成功提现"+withdrawtimes+"次，明天再来提现吧",mainInfo.getCmNumber());
                return Result.fail(result.get("msg"), result);
            }
            else if ("Frozen".equals(withdrawStatus)){
                result.put("code","0001");
                result.put("msg","您刚刚做过一次提现，资金处于冻结中，请稍后查看交易状态。");
                result.put("remark","每天只能提现"+sysParameter.getPrValue()+"次，如果提现不成功，可稍后重新提现");
                log.error("用户：{} 刚刚做过一次提现，资金处于冻结中",mainInfo.getCmNumber());
                return Result.fail(result.get("msg"), result);
            }
        }

        if (AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_2.equals(model.getType())) {
            //判断是否有快速提现限制
            if (StrUtil.isNotBlank(configParamBean.getQuickWithdrawLimitTime())){
                String[] split = configParamBean.getQuickWithdrawLimitTime().split("\\|");
                if(split.length != 2 || DateUtil.isEffectiveDate(cn.hutool.core.date.DateUtil.date(), cn.hutool.core.date.DateUtil.parse(split[0]),cn.hutool.core.date.DateUtil.parse(split[1]))){
                    result.put("code","0002");
                    result.put("msg",configParamBean.getQuickWithdrawLimitDesc());
                    return Result.fail(result.get("msg"), result);
                }
            }
            BigDecimal rate = Convert.toBigDecimal(configParamBean.getWithdraw_rate());
            // 计算手续费, 精确到小数点后两位，计算时小数点第三位截断处理，服务费最小0.01。--挖财不校验手续费
            BigDecimal feeAmount ;
            if(!CreditConstant.WACAI_CHANNEL.getCode().equals(mainInfo.getChannelCode())){
                feeAmount = Convert.toBigDecimal(model.getPayAmt()).multiply(rate).setScale(2, BigDecimal.ROUND_DOWN);
                if (feeAmount.compareTo(new BigDecimal(0)) <= 0) {
                    feeAmount = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_DOWN);
                }
                Map authMap = busiOrderService.userGrantFlag(model.getCustomerId(),1, feeAmount);
                log.info("快速提现授权校验信息:" + JSON.toJSONString(authMap));
                String grantFlag = authMap.get("grantFlag").toString();
                if ("1".equals(grantFlag)){
                    result.put("code","0008");
                    result.put("msg","授权金额/期限不足，无法提现，请前往华瑞银行重新授权。");
                    return Result.fail(result.get("msg"), result);
                }
            }

        }

        // 查询可提现金额
        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(mainInfo);
        if (accountBalance.compareTo(new BigDecimal(model.getPayAmt())) == -1) {
            log.info("提现，客户号：{}，提现金额：{}， 可提现余额为：{} 余额不足！",model.getCustomerId(),model.getPayAmt(),accountBalance.toString());
            result.put("code","0005");
            result.put("msg","可提现金额不足！");
            return Result.fail(result.get("msg"), result);
        }
        result.put("code","0000");
        result.put("msg","该用户可以提现");
        return Result.success(result);
    }


    @FunctionId("430011")
    @Override
    public Result userStatus(ReqMain reqMain) throws Exception {
        Model_430011 model = (Model_430011) reqMain.getReqParam();
        Map<String, Integer> map = new HashMap<>();

        String key = KeyGenerator.USER_STATUS.getKey() + model.getCustomerId();
        String val = redisSessionManager.get(key);
        if (StringUtils.isNotEmpty(val)) {
            map = JSON.parseObject(val, map.getClass());
        } else {
            CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
            Boolean isAuth = StringUtils.isNotEmpty(mainInfo.getCmIdnum()) && 3 == mainInfo.getCmStatus();
            map.put("isRealProve",  isAuth ? 1 : 0);
            Boolean openAccountFlag = StringUtils.isNotEmpty(mainInfo.getFuiouLoginId());
            map.put("isOpenAccount", openAccountFlag ? 1 : 0);
            Boolean isBindCard = StringUtils.isNotBlank(mainInfo.getBankAccountId());
            map.put("isBindCard", isBindCard ? 1 : 0);
            UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
            map.put("isAuthorize", userGrant != null ? 1 : 0);
            boolean flag = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (1 != entry.getValue()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                redisSessionManager.put(key, JSON.toJSONString(map), KeyGenerator.USER_STATUS.getTime(), KeyGenerator.USER_STATUS.getTimeUnit());
            }
        }
        return Result.success(map);
    }

    @FunctionId("430013")
    @Override
    public Result userAuthJudge(ReqMain reqMain) throws Exception {
        Model_430013 model = (Model_430013) reqMain.getReqParam();
        Map authMap = busiOrderService.userAuthJudge(model.getCustomerId(),model.getProductId(),model.getOrderAmt());
        return Result.success(authMap);
    }

    @FunctionId("430014")
    @Override
    public Result offlineRecharge(ReqMain reqMain) throws Exception {
        Model_430014 model = (Model_430014) reqMain.getReqParam();
        Map<String, Object> map = new HashMap<>();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        if (StringUtils.isNotEmpty(mainInfo.getBankAccountId())) {
            CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(mainInfo.getBankAccountId()));
            map.put("bankName", bankAccount.getCbBankName());
            String bankCard = bankAccount.getCbAccount();
            if (CoreUtil.isDigit(bankAccount.getCbAccount())) {
                bankCard = CoreUtil.strReplace(bankAccount.getCbAccount());
            }
            map.put("bankCardNo", bankCard);
        }
        map.put("depositBank", configParamBean.getOfflineRechargeDepositBank());
        map.put("accountName", configParamBean.getOfflineRechargeAccountName());
        map.put("accountNo", configParamBean.getOfflineRechargeAccountNo());
        map.put("operation", configParamBean.getOfflineRechargeOperation());
        map.put("attions", configParamBean.getOfflineRechargeAttions());
        return Result.success(map);
    }

    @FunctionId("430015")
    @Override
    public Result webAuthInfo(ReqMain reqMain) throws Exception {
        Model_430015 model = (Model_430015) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getUserId());
        Long consumed = mainInfo.getIsConsumed();
        Map<String, Object> map = new HashMap<>();
        map.put("consumed", Convert.toInt(consumed) > 0 ? 1 : 0);
        map.put("riskExpire", mainInfo.getRiskTestType() != null ? 1 : 0);
        // authStatus 0: 成功 1: 开户未授权 2: 开户失败 3:授权失败
        if ("0000".equals(model.getCode())) {
            if (!"100100000000".equals(model.getAuthStatus()) && !"010100000000".equals(model.getAuthStatus())){
                map.put("authRes", 1);
                String tips = "温馨提醒|请务必开通授权，否则无法购买产品。";
                if (!CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType())){
                    tips = "温馨提醒|请务必开通授权，否则无法正常签约, 还款, 缴费。";
                }
                map.put("tips", tips);
            } else {
                map.put("authRes", 0);
            }
        } else {
            if (StringUtils.isEmpty(mainInfo.getFuiouLoginId())) {
                map.put("authRes", 2);
                map.put("tips", "开户失败，为保障正常交易，请重新开户。");
            } else {
                map.put("authRes", 3);
                map.put("tips", "授权失败，为保障正常交易，请重新开户。");
            }
            map.put("errMsg", "银行返回错误提示：" + model.getMsg() + "|详情请咨询客服：|400-096-6588|（周一到周六9:00-18:00）");
        }
        return Result.success(map);
    }

    @FunctionId("430016")
    @Override
    public Result bankCardInfo(ReqMain reqMain) throws Exception {
        Model_430016 model = (Model_430016) reqMain.getReqParam();
        BankCardDTO bankCard = new BankCardDTO();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        if (StringUtils.isEmpty(mainInfo.getBankAccountId())) {
            return Result.fail("没有该用户银行卡信息", bankCard);
        }
        CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Convert.toLong(mainInfo.getBankAccountId()));
        if (bankAccount == null) {
            return Result.fail("没有该用户银行卡信息", bankCard);
        }
        bankCard.setBankName(bankAccount.getCbBankName());
        bankCard.setBankCard(bankAccount.getCbAccount());
        if (CoreUtil.isDigit(bankAccount.getCbAccount())) {
            bankCard.setBankCard(CoreUtil.strReplace(bankAccount.getCbAccount()));
        }
        bankCard.setBankCode(bankAccount.getCbBankCode());

        BusiBankLimit bankLimit = new BusiBankLimit();
        bankLimit.setBankCode(bankAccount.getCbBankCode());
        bankLimit.setBankStatus("1");
        bankLimit.setPayChannel(AppConstants.PayChannelCodeContants.FUIOU);
        List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(bankLimit);
        if (CollectionUtils.isEmpty(bankLimits)) {
            throw new BusinessException("获取银行限额信息失败！");
        }
        bankLimit = bankLimits.get(0);
        String limitDesc = "限额：";
        limitDesc += "单笔" + bankLimit.getSingleAmt() + "元，";
        limitDesc += "单日" + bankLimit.getDayAmt() + "元，";
        limitDesc += "单月" + bankLimit.getMonthAmt() + "元";
        bankCard.setLimitDesc(limitDesc);

        String withdrawDesc = "";
        SysParameter sysParameter = sysParameterService.findOneByPrType("withdrawtimes");
        if (sysParameter == null) {
            throw new BusinessException("获取单日提现次数失败！");
        }
        String value = sysParameter.getPrValue();
        withdrawDesc += "1、普通+快速单日限" + value + "笔，";
        sysParameter = sysParameterService.findOneByPrType("withdrawlimit");
        if (sysParameter == null) {
            throw new BusinessException("获取提现限额信息失败！");
        }
        value = sysParameter.getPrValue();
        NumberFormat nf = new DecimalFormat("###,###.##");
        String valueFmt = nf.format(Long.valueOf(value)/10000);
        withdrawDesc += "单笔上限为" + valueFmt + "万元；\n2、提现到账时间：";

        // String fastDesc = new StringBuilder("1、单日提现不限笔数，单笔上限为").append(valueFmt).append("万元\n2、提现到账时间：预计当日到账").toString();
        bankCard.setWithdrawRate(configParamBean.getWithdraw_rate());
        bankCard.setFastWithdrawDesc(withdrawDesc + "预计当日到账；");
        bankCard.setWithdrawDesc(withdrawDesc + "预计T+1个工作日；");
        bankCard.setIsLockPay(bankLimit.getLlRecharge());
        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(mainInfo);
        bankCard.setAccountBalance(CoreUtil.BigDecimalAccurate(accountBalance));
        bankCard.setLimitAmt(value);

        // 查询冻结金额
        ResultDto<AccountDto> res = accountFacadeService.getAccountBalance(AccountWholeType.PERSONAL_WITHDRAW_FRZ + mainInfo.getCmNumber());
        if (res.isSuccess()) {
            bankCard.setFreezeAmount(res.getData().getBalance());
        }
        return Result.success(bankCard);
    }

    @FunctionId("430017")
    @Override
    public Result contractGrant(ReqMain reqMain) throws Exception {
        Model_430017 model = (Model_430017) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        if (!CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
            return Result.fail("非借款人不可调用签约");
        }
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
        if (userGrant == null) {
            return Result.fail("请授权后再签约");
        }
        Map<String, Object> resMap = new HashMap<>();
        UserAuthOrderDto authOrder = busiOrderService.borrowerAuthInfo(mainInfo.getId(), model.getSignDate(), model.getBorrowAmt());
        log.info("-------->430017:UserAuthOrderDto:" + JSONUtils.toJSON(authOrder));
        boolean amt = authOrder.getAuthFee().compareTo(Convert.toBigDecimal(userGrant.getAutoFeeAmt()).divide(new BigDecimal(100))) > 0;
        boolean term = authOrder.getAuthDate().compareTo(Convert.toDate(userGrant.getAutoFeeTerm())) > 0;
        resMap.put("type", (amt || term) ? 0 : 1);
        return Result.success(resMap);
    }

    @FunctionId("430018")
    @Override
    public Result setHrPwd(ReqMain reqMain) throws Exception {
        Model_430018 model = (Model_430018) reqMain.getReqParam();
        Map<String, Object> resMap = new HashMap<>();
        try {
            CustomerMainInfo mainInfo = customerMainInfoService.queryCustomerInfoByLoginId(model.getLoginId());
            CustomerMainInfo customer = new CustomerMainInfo();
            customer.setId(mainInfo.getId());
            customer.setIsSetPwd(1);
            customerMainInfoService.updateNotNull(customer);
            resMap.put("res", "设置成功");
            return Result.success(resMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("设置失败");
    }

    @FunctionId("430019")
    @Override
    public Result isExceedLimit(ReqMain reqMain) throws Exception {
        Model_430019 model = (Model_430019) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
        BigDecimal totalAmt = busiOrderService.statisticsOrderAmt(mainInfo.getId());

        log.info("430019，用户ID：{} ，再投总金额：{}，本次投资额：{}", mainInfo.getId(), totalAmt, model.getInvestAmt());
        CustomerLimitInfo limitInfo = new CustomerLimitInfo();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("prType", "investing_limit");
        paramsMap.put("prName", mainInfo.getRiskTestType());
        SysParameter sysParameter = sysParameterService.getSysParameterPrs(paramsMap);
        if (sysParameter == null) {
            throw new BusinessException("请先测评风险等级");
        }
        List<SysParameter> parameterList = sysParameterService.findByPrType("investing_limit");

        BigDecimal amt = Convert.toBigDecimal(sysParameter.getPrValue()).multiply(new BigDecimal(10000));
        limitInfo.setRiskType(mainInfo.getRiskTestType());
        limitInfo.setMaxAmt(amt.divide(new BigDecimal(10000)));
        limitInfo.setTotalAmt(totalAmt);
        BigDecimal surplusAmt = amt.subtract(totalAmt);
        limitInfo.setSurplusAmt(surplusAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : surplusAmt);
        limitInfo.setParamsList(parameterList);
        limitInfo.setRiskTestUrl(configParamBean.getRiskTestUrl());
        if (limitInfo.getSurplusAmt().compareTo(model.getInvestAmt()) < 0) {
            limitInfo.setAlert(true);
        }
        return Result.success(limitInfo);
    }

    @FunctionId("430023")
    @Override
    public Result withdrawAuth(ReqMain reqMain) throws Exception {
        Model_430023 model = (Model_430023) reqMain.getReqParam();
        Map authMap = busiOrderService.userGrantFlag(model.getCustomerId(),1,model.getOrderAmt());
        return Result.success(authMap);
    }
}
