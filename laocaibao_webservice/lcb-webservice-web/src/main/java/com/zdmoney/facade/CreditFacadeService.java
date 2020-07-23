package com.zdmoney.facade;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.dto.lcbaccount.BankCardBindDto;
import com.zdmoney.integral.api.dto.lcbaccount.BankCardBindResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.enm.CustomerType;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerAuthorizeInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.*;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.CopyUtil;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.utils.PropertiesUtil;
import com.zdmoney.web.dto.BankCardInfoDTO;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.BankCardInfoListDTO;
import com.zdmoney.webservice.api.dto.Asset.BusiTradeFlowDTO;
import com.zdmoney.webservice.api.dto.Asset.UserBindCardDTO;
import com.zdmoney.webservice.api.dto.credit.GettingVCRequestDto;
import com.zdmoney.webservice.api.dto.credit.RegisteringRequestDto;
import com.zdmoney.webservice.api.dto.customer.CustomerMainInfoVo;
import com.zdmoney.webservice.api.facade.ICreditFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import websvc.models.Model_430008;
import websvc.models.Model_430009;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.servant.FuiouFunctionService;
import websvc.servant.impl.UserFunctionServiceImpl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2017/10/19.
 */
@Component
@Slf4j
@SuppressWarnings("Duplicates")
public class CreditFacadeService extends CreditProcessor implements ICreditFacadeService {

    //private static final log log = logFactory.getlog(CreditFacadeService.class);

    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiBankLimitService busiBankLimitService;


    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @Autowired
    private UserFunctionServiceImpl userFunctionService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private FuiouFunctionService fuiouFunctionService;

    private static final String PLATFORM = "credit";
    private static final String SYSTEM = "credit";
    private static final String MECHANISM = "证大";
    private static final String IP = "0.0.0.0";
    private static final String VERSION = "10.0.0";
    private static final String CHANNEL = "credit";

    private static final int REGISTER_VERIFICATION_CODE_TYPE = 0;

    private ReqHeadParam createDefaultReqHeadParam(){
        ReqHeadParam reqHeadParam = new ReqHeadParam();
        reqHeadParam.setUserAgent(PLATFORM);
        reqHeadParam.setVersion(VERSION);
        reqHeadParam.setMechanism(MECHANISM);
        reqHeadParam.setPlatform(PLATFORM);
        reqHeadParam.setTogatherType(PLATFORM);
        reqHeadParam.setOpenchannel(PLATFORM);
        reqHeadParam.setSystem(SYSTEM);
        reqHeadParam.setOpenchannel(CHANNEL);
        return reqHeadParam;
    }
    @Override
    public ResultDto<String> register(RegisteringRequestDto requestDto) {
        ResultDto<String> responseDto = null;
        try{
            preProcess(requestDto);
            ReqHeadParam reqHeadParam = createDefaultReqHeadParam();
            String cmPassword = "888888";
            String cmCellphone = requestDto.getPhone();
            String validateCode = requestDto.getVerifyCode();
            String cmRealName = requestDto.getName();
            String cmIdnum = requestDto.getIdNo();
            Long customerId = customerMainInfoService.doRegister(cmCellphone,cmPassword,validateCode,cmRealName,cmIdnum,IP,reqHeadParam);
            responseDto = new ResultDto(String.valueOf(customerId));
            afterProcess(requestDto);
        }catch(BusinessException e){
            log.error(e.getMessage(),e);
            responseDto = ResultDto.FAIL(e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage(),e);
            responseDto = ResultDto.FAIL("系统内部错误");
        }
        return responseDto;
    }

    @Override
    public ResultDto<String> getVerificationCode(GettingVCRequestDto requestDto) {
        ResultDto<String> responseDto = null;
        try{
            preProcess(requestDto);
            String code = customerValidateCodeService.generateOrSearchVerificationCode(
                    REGISTER_VERIFICATION_CODE_TYPE, requestDto.getPhone(),requestDto.getChannelCode());
            responseDto =  new ResultDto(code);
            afterProcess(requestDto);
        }catch(BusinessException e){
            log.error(e.getMessage(),e);
            responseDto = ResultDto.FAIL(e.getMessage());
        }catch(Exception e){
            log.error(e.getMessage(),e);
            responseDto = ResultDto.FAIL("系统内部错误");
        }
        return responseDto;
    }

    @Override
    public ResultDto BindCard(UserBindCardDTO userBindCardDTO){
        Long customerId = userBindCardDTO.getCustomerId();
        String bankCode = userBindCardDTO.getBankCode();
        String bankCard = userBindCardDTO.getBankCard();
        String cellphone = userBindCardDTO.getCellphone();

        boolean lock = false;
        String LOCK = "CREDIT-BIND-CARD" + customerId;
        String KEY = "credit_" + customerId;
        try {
            // 获取锁
            lock = redisSessionManager.setNX(KEY, LOCK);
            if (lock) {
                // 1分钟后,key值失效,自动释放锁
                redisSessionManager.expire(KEY, 1, TimeUnit.MINUTES);

                CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(customerId);
                String key = "CREDIT_" + mainInfo.getCmCellphone();
                String obj = redisSessionManager.get(key);
                if (StringUtils.isBlank(obj)) {
                    return new ResultDto("300001", "请重新登录");
                }

                CustomerBankAccount bankAccount = customerBankAccountMapper.selectBankAccountByBankCode(bankCard);
                if (bankAccount != null) {
                    if (customerId.longValue() != bankAccount.getCustomerId().longValue()) {
                        return new ResultDto("该卡已被他人绑定", false);
                    }
                    return new ResultDto("绑卡成功", true);
                }

                //校验银行卡bin信息
                customerMainInfoService.checkCardBinInfo(bankCode, bankCard);
                //校验银行信息
                BusiBankLimit busiBankLimit = busiBankLimitService.validateBankLimit(AppConstants.PayChannelCodeContants.HUARUI_BANK, bankCode);

                //调用华瑞验卡绑卡接口
                BankCardBindDto bankCardBindDto = new BankCardBindDto();
                bankCardBindDto.setBankId(busiBankLimit.getBankCode());
                bankCardBindDto.setBankCode(busiBankLimit.getBankCode());
                bankCardBindDto.setBankNo(bankCard);
                bankCardBindDto.setBankName(busiBankLimit.getBankName());
                bankCardBindDto.setTelNo(cellphone);
                bankCardBindDto.setIDcard(mainInfo.getCmIdnum());
                bankCardBindDto.setUserName(mainInfo.getCmRealName());
                bankCardBindDto.setUserNo(mainInfo.getCmNumber());
                bankCardBindDto.setTransNo(SerialNoGeneratorService.generateBindSerialNo(mainInfo.getId()));
                bankCardBindDto.setCustomerType(CustomerType.ORGANIZATION);
                com.zdmoney.integral.api.common.dto.ResultDto<BankCardBindResultDto> result = null;//accountFacadeService.bankCardBind(bankCardBindDto);
                if (!result.isSuccess()) {
                    return new ResultDto("绑定银行卡失败:" + result.getMsg(), false);
                } else {
                    Date date = new Date();
                    CustomerBankAccount customerBankAccount = new CustomerBankAccount();
                    customerBankAccount.setCbAccount(bankCard);
                    customerBankAccount.setCbAccountName(mainInfo.getCmRealName());
                    customerBankAccount.setCbBankCode(busiBankLimit.getBankCode());
                    customerBankAccount.setCbBankName(busiBankLimit.getBankName());
                    customerBankAccount.setCustomerId(mainInfo.getId());
                    customerBankAccount.setCbInputDate(date);
                    customerBankAccount.setCbModifyDate(date);
                    customerBankAccount.setCbBindPhone(cellphone);
                    customerBankAccount.setCbValid((short) 0);
                    int num = customerBankAccountMapper.insert(customerBankAccount);
                    if (num != 1) {
                        throw new BusinessException("新增绑卡流水失败");
                    }
                    CustomerMainInfo customerMainInfo = new CustomerMainInfo();
                    customerMainInfo.setId(mainInfo.getId());
                    customerMainInfo.setBankAccountId(String.valueOf(customerBankAccount.getId()));
                    customerMainInfoService.updateNotNull(customerMainInfo);
                }
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService绑卡失败:" + msg);
            return new ResultDto("绑卡失败:" + msg, false);
        } finally {
            if (lock) {
                // 如果获取了锁，则释放锁
                redisSessionManager.remove(KEY);
            }
        }
        return new ResultDto("绑卡成功", true);
    }

    @Override
    public ResultDto<BankCardInfoListDTO> getCustomerBankInfo(Long customerId) {
        BankCardInfoListDTO bankCardInfoListDTO = new BankCardInfoListDTO();
        try {
            CustomerMainInfo mainInfo = customerMainInfoService.findOpenCustomerById(customerId);
            if (mainInfo.getPayErrorTime() != null || mainInfo.getPayLockTime() != null) {
                String lockHours = sysParameterService.findOneByPrType("resetLockHour").getPrValue();
                String errorTimes = sysParameterService.findOneByPrType("resetErrorTimes").getPrValue();
                bankCardInfoListDTO.setLockPay(customerMainInfoService.isLocked(
                        mainInfo.getPayLockTime(),
                        Integer.parseInt(lockHours)));
                if (bankCardInfoListDTO.getLockPay()) {
                    String errorMsg = PropertiesUtil.getDescrptionProperties().get("withdraw.pay_password.lock");
                    errorMsg = MessageFormat.format(errorMsg, errorTimes, lockHours);
                    bankCardInfoListDTO.setLockDesc(errorMsg);
                }
            } else {
                bankCardInfoListDTO.setLockPay(false);
            }
            String withdrawtimes = sysParameterService.findOneByPrType("withdrawtimes").getPrValue();
            int cnt = busiTradeFlowMapper.selectWithdrawTimesByCustomerId(mainInfo.getId());
            if (cnt >= Integer.parseInt(withdrawtimes)) {
                bankCardInfoListDTO.setLockPay(true);
                bankCardInfoListDTO.setLockDesc("今日已提现" + withdrawtimes + "次，不能再次提现，请明日再试！");
            }
            BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(mainInfo);
            bankCardInfoListDTO.setAccountBalance(CoreUtil.BigDecimalAccurate(accountBalance));
            BigDecimal extractAmount = accountOverview520003Service.extractAllBalance(mainInfo.getCmNumber());
            bankCardInfoListDTO.setExtractAmount(CoreUtil.BigDecimalAccurate(extractAmount));

            List<BankCardInfoDTO> cardInfoList = userFunctionService.getBindCardInfoByList(customerId);
            List<com.zdmoney.webservice.api.dto.Asset.BankCardInfoDTO> bankList = new ArrayList<>();
            BeanUtils.copyProperties(bankList, cardInfoList);
            bankCardInfoListDTO.setBankCardInfoList(bankList);
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService获取银行卡信息失败:" + msg);
            return new ResultDto("获取银行卡信息失败:" + msg, false);
        }
        return new ResultDto(bankCardInfoListDTO);
    }

    @Override
    public ResultDto realNameAuth(Long customerId, String idNum, String realName) {
        try {
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCustomerId(customerId);
            String key = "CREDIT_" + customerMainInfo.getCmCellphone();
            String obj = redisSessionManager.get(key);
            if (StringUtils.isBlank(obj)){
                return new ResultDto("300001", "请重新登录");
            }
            if(3 == customerMainInfo.getCmStatus()){
                if (!idNum.equalsIgnoreCase(customerMainInfo.getCmIdnum())){
                    return new ResultDto("身份信息不一致", false);
                }
                if (!realName.equals(customerMainInfo.getCmRealName())){
                    return new ResultDto("姓名不一致", false);
                }
                return new ResultDto("实名认证成功", true);
            }
            customerMainInfoService.realNameAuth(customerId, realName, idNum, createDefaultReqHeadParam());
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService实名认证失败:" + msg);
            return new ResultDto("实名认证失败:" + msg, false);
        }
        return new ResultDto("实名认证成功", true);
    }

    @Override
    public ResultDto login(String cellphone, String validateCode) {
        try {
            CustomerMainInfo customerMainInfo = customerMainInfoService.validCodeLogin(cellphone, validateCode);
            String key = "CREDIT_" + cellphone;
            redisSessionManager.put(key, customerMainInfo.getCmCellphone(), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            if ("validateCode.error".equals(e.getMessage())) {
                return new ResultDto("2001", "验证码错误");
            }
            if ("validateCode.invalid".equals(e.getMessage())) {
                return new ResultDto("2002", "验证码已失效");
            }
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService登录失败:" + msg);
            return new ResultDto("登录失败:" + msg, false);
        }
        return new ResultDto();
    }

    @Override
    public ResultDto<String> getUserInfoByIdNum(String idNum) {
        CustomerMainInfo customerMainInfo = null;
        try {
            customerMainInfo = customerMainInfoService.selectByIdNum(idNum,AppConstants.USER_TYPE_NORMAL);
            if (customerMainInfo == null) {
                return new ResultDto("没有该用户信息", false);
            }
        }catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService获取用户信息失败:" + msg);
            return new ResultDto("获取用户信息失败:" + msg, false);
        }
        return new ResultDto(customerMainInfo.getCmCellphone());
    }

    @Override
    public ResultDto<CustomerMainInfoVo> getCustomerInfoByIdNum(String idNum) {
        CustomerMainInfoVo mainInfoVo = null;
        CustomerMainInfo customerMainInfo = null;
        try {
            customerMainInfo = customerMainInfoService.selectByIdNum(idNum,AppConstants.USER_TYPE_NORMAL);
            if (customerMainInfo == null) {
                return new ResultDto("没有该用户信息", false);
            }
        }catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService获取用户信息失败:" + msg);
            return new ResultDto("获取用户信息失败:" + msg, false);
        }
        CopyUtil.copyProperties(customerMainInfo, mainInfoVo);
        return new ResultDto(mainInfoVo);
    }

    @Override
    public ResultDto<Long> validateCodeRegister(String cellphone, String validateCode) {
        Long customerId = null;
        CustomerMainInfo customerMainInfo;
        String key = "CREDIT_" + cellphone;
        try {
            customerMainInfo = customerMainInfoService.findOneByPhone(cellphone);
            if (customerMainInfo == null) {
                // 手机号后4位作为默认密码
                String password = cellphone.substring(cellphone.length() - 4, cellphone.length());
                ReqHeadParam reqHeadParam = createDefaultReqHeadParam();
                customerMainInfo = customerMainInfoService.registerWithValidateCode(StringUtils.trim(cellphone), password, validateCode, null, null, null, reqHeadParam,null, null);
            }else if(CustomerAccountType.LENDER.getValue().equals(customerMainInfo.getAccountType())){
                redisSessionManager.put(key, customerMainInfo.getCmCellphone(), 1, TimeUnit.HOURS);
                return new ResultDto("用户角色异常，无法执行此操作", false);
            }
            customerId = customerMainInfo.getId();
            redisSessionManager.put(key, customerMainInfo.getCmCellphone(), 1, TimeUnit.HOURS);
        } catch (Exception e) {
            if ("validateCode.error".equals(e.getMessage())) {
                return new ResultDto("2001", "验证码错误");
            }
            if ("validateCode.invalid".equals(e.getMessage())) {
                return new ResultDto("2002", "验证码已失效");
            }
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService注册失败:" + msg);
            return new ResultDto("注册失败", msg, false);
        }
        return new ResultDto(customerId);
    }

    @Override
    public ResultDto getLoginValidCode(String cellPhone) {
        String code = null;
        try {
            code = customerValidateCodeService.generateOrSearchVerificationCode(AppConstants.ValidateCode.CREDIT, cellPhone, "credit");
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
            log.error("CreditFacadeService发送验证码失败:" + msg);
            return new ResultDto("发送验证码失败:" + msg, false);
        }
        return new ResultDto("发送验证码成功:" + code, true);
    }

    public ResultDto customerAuthorize(long customerId) {
        try {
            boolean isAuthorize = customerMainInfoService.isAuthorize(customerId);
            if (isAuthorize) {
                return new ResultDto("授权成功", true);
            }

            CustomerAuthorizeInfo customerAuthorizeInfo = new CustomerAuthorizeInfo();
            customerAuthorizeInfo.setCustomerId(customerId);
            customerAuthorizeInfo.setAuthorizeStatus(1);
            customerAuthorizeInfo.setAuthorizeDate(new Date());
            long res = customerMainInfoService.saveCustomerAuthorize(customerAuthorizeInfo);
            if (res <= 0){
                return new ResultDto("授权失败", true);
            }
        } catch (Exception e){
            log.error("授权失败:" + e);
            return new ResultDto("授权失败:", false);
        }
        return new ResultDto("授权成功", true);
    }

    @Override
    public ResultDto<com.zdmoney.webservice.api.dto.Asset.BankCardInfoDTO> getBankCardInfo(long customerId) {
        if (customerId == 0){
            log.error("customerId不可为空");
            return new ResultDto("customerId不可为空:", false);
        }
        List<CustomerBankAccount> custBankAccounts = customerBankAccountMapper.listCustBankAccount(customerId);
        if (custBankAccounts .isEmpty()){
            return new ResultDto(ResultDto.ERROR_CODE,"没有查询到该用户绑定银行卡信息");
        }
        CustomerBankAccount custBankAccount  = custBankAccounts.get(custBankAccounts.size() - 1);

        com.zdmoney.webservice.api.dto.Asset.BankCardInfoDTO bankCardInfo= new com.zdmoney.webservice.api.dto.Asset.BankCardInfoDTO();
        bankCardInfo.setCellphone(custBankAccount.getCbBindPhone());
        bankCardInfo.setBankName(custBankAccount.getCbBankName());
        bankCardInfo.setBankCard(custBankAccount.getCbAccount());
        return new ResultDto(bankCardInfo);
    }

    @Override
    public ResultDto<List<BusiTradeFlowDTO>> queryRecentTradeFlow(long customerId, String trdDate) {
        if (customerId == 0){
            log.error("customerId不可为空");
            return new ResultDto("customerId不可为空:", false);
        }
        if (StringUtils.isBlank(trdDate)){
            log.info("customerId不可为空");
            return new ResultDto();
        }
        log.info("customerId:"+customerId +"trdDate"+trdDate);
        Map map = Maps.newTreeMap();
        map.put("customerId",customerId);
        map.put("trdDate", trdDate);
        List<BusiTradeFlowDTO>  tradeFlowDtoList = busiTradeFlowMapper.selectRecentTradeFlow(map);
        if (tradeFlowDtoList.isEmpty()){
            return new ResultDto("暂无提现流水:", true);
        }
        return new ResultDto(tradeFlowDtoList);
    }



    @Override
    public ResultDto withdraw(String customerNo, String payAmt, String pageUrl){
        try {
            Model_430009 model_430009 = new Model_430009();
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(customerNo);
            model_430009.setCustomerId(mainInfo.getId());
            model_430009.setPayAmt(payAmt);
            model_430009.setPageUrl(pageUrl);
            // 默认普通提现
            model_430009.setType(1);
            ReqMain reqMain = new ReqMain();
            reqMain.setReqParam(model_430009);
            ReqHeadParam reqHeadParam = new ReqHeadParam();
            reqHeadParam.setPlatform("PC");
            reqMain.setReqHeadParam(reqHeadParam);
            Result result = null;
            result = fuiouFunctionService.withdraw(reqMain);
            return new ResultDto(result.getData());
        } catch (Exception e) {
            log.info("=======标的提现失败，用户编号：{}",customerNo);
            return new ResultDto("操作失败", false);
        }

    }

    @Override
    public ResultDto recharge(String customerNo, Integer channelType, String payAmt, String pageUrl) {

        try {
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(customerNo);
            Model_430008 model_430008 = new Model_430008();
            model_430008.setChannelType(channelType);
            model_430008.setCustomerId(mainInfo.getId());
            model_430008.setPageUrl(pageUrl);
            model_430008.setPayAmt(payAmt);
            ReqMain reqMain = new ReqMain();
            reqMain.setReqParam(model_430008);
            ReqHeadParam reqHeadParam = new ReqHeadParam();
            reqHeadParam.setPlatform("PC");
            reqMain.setReqHeadParam(reqHeadParam);
            Result result = null;
            result = fuiouFunctionService.recharge(reqMain);
            Map<String,String> resultMap =new HashMap<>();
            if(result.getSuccess()){
                resultMap.put("code", "0000");
                resultMap.put("msg","操作成功");
                Map<String,String> urlMap = (Map)result.getData();
                resultMap.put("url",urlMap.get("url"));
            }else {
                resultMap.put("code", "1111");
                resultMap.put("msg",result.getMessage());
            }
            return new ResultDto(resultMap);
        } catch (Exception e) {
            log.info("=======标的充值失败，用户编号：{}",customerNo);
            return new ResultDto("操作失败", false);
        }
    }

    private Map<String, Object> encodeParams(
            String fuiouLoginId, String flowNum, String payTime, String productType) {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantCode", configParamBean.getFuiouMerchantCode());
        map.put("clientType", 0);
        map.put("backNotifyUrl", configParamBean.getZdpayBackNotifyUrl());
        map.put("loginId", fuiouLoginId);
        map.put("channelOrderNo", flowNum);
        map.put("payTime", payTime);
        map.put("productType", productType);
        return map;
    }

    private StringBuilder encodeParamsUrl(Map<String, Object> map, String url, String signature) {
        StringBuilder sbl = new StringBuilder(configParamBean.getZdpayUrl()).append(url);
        int i = 0;
        for(String key : map.keySet()) {
            String linkFlag = (i == 0 ? "?" : "&");
            sbl.append(linkFlag).append(key).append("=").append(map.get(key));
            i = i + 1;
        }
        sbl.append("&signature=").append(signature);
        log.info("####encodeParamsUrl=", sbl.toString());
        return sbl;
    }
}
