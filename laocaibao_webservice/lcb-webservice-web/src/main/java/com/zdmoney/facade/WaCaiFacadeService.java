package com.zdmoney.facade;

import cn.hutool.core.convert.Convert;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.CreditConstant;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.dto.lcbaccount.AccountDto;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysRequestLog;
import com.zdmoney.models.zdpay.UserGrantBO;
import com.zdmoney.service.AsyncService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.utils.ValidatorUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.credit.wacai.*;
import com.zdmoney.webservice.api.facade.IWaCaiFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import websvc.models.*;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.req.ReqParam;
import websvc.servant.FuiouFunctionService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zdmoney.constant.CreditConstant.*;

/**
 * 挖财
 * Created by 46186 on 2019/3/7.
 */
@Component
@Slf4j
public class WaCaiFacadeService implements IWaCaiFacadeService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FuiouFunctionService fuiouFunctionService;

    @Autowired
    private CustomerGrantInfoMapper customerGrantInfoMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private IAccountFacadeService accountFacadeService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;


    private static final String PLATFORM = "credit";
    private static final String SYSTEM = "credit";
    private static final String MECHANISM = "证大";
    private static final String VERSION = "10.0.0";
    private static final String CHANNEL = "credit";
    private static final String OPEN_ACCOUNT = "0";
    private static final String AUTHORIZE = "1";

    private ReqHeadParam createDefaultReqHeadParam() {
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
    public ResultDto<String> openAccount(OpenAuthRequestDto requestDto) {

        log.info("调用开户接口：参数【{}】", requestDto.toString());
        SysRequestLog reqLog = getReqLog("channel_openAccount", requestDto.toString());
        Date startTime = new Date();
        try {
            ResultDto validateParam = validateParam(requestDto);
            if (validateParam != null) {
                assembleReqLog(reqLog, false, startTime, "参数为空");
                return validateParam;
            }

            CreditConstant res = validateUser(requestDto.getPhone(), requestDto.getIdNum(), requestDto.getChannelCode());
            if (res != CreditConstant.SUCCESS) {
                assembleReqLog(reqLog, false, startTime, res.toString());
                return CreditConstant.getResult(res);
            }

            CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(requestDto.getPhone());
            Result result = null;
            if (OPEN_ACCOUNT.equals(requestDto.getReqType())) {
                Model_430001 model = new Model_430001();
                model.setCustomerId(mainInfo.getId());
                model.setPageUrl(requestDto.getPageUrl());
                result = fuiouFunctionService.openAccountWithParamEncode(assembleReqMain(model, requestDto.getClientType()));
            }else {
                Model_430010 model = new Model_430010();
                model.setCustomerId(mainInfo.getId());
                model.setPageUrl(requestDto.getPageUrl());
                result = fuiouFunctionService.userGrantWithParamEncode(assembleReqMain(model, requestDto.getClientType()));
            }
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            log.error("调用开户接口业务异常：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }

    @Override
    public ResultDto<String> bindBankCard(CashierRequestDto requestDto) {

        log.info("调用绑定银行卡接口：参数【{}】", requestDto.toString());
        SysRequestLog reqLog = getReqLog("channel_bindCard", requestDto.toString());
        Date startTime = new Date();
        try {
            ResultDto validateParam = validateParam(requestDto);
            if (validateParam != null) {
                assembleReqLog(reqLog, false, startTime, "参数为空");
                return validateParam;
            }
            CreditConstant res = validateUser(requestDto.getPhone(), requestDto.getIdNum(), requestDto.getChannelCode());
            if (res != CreditConstant.SUCCESS) {
                assembleReqLog(reqLog, false, startTime, res.toString());
                return CreditConstant.getResult(res);
            }
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(requestDto.getPhone());
            Model_430002 model = new Model_430002();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            Result result = null;
            result = fuiouFunctionService.bankCardBind(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            log.error("调用绑卡接口业务异常：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }

    @Override
    public ResultDto<String> unBindBankCard(CashierRequestDto requestDto) {
        log.info("调用解绑银行卡接口：参数【{}】", requestDto.toString());
        SysRequestLog reqLog = getReqLog("channel_unBindCard", requestDto.toString());
        Date startTime = new Date();
        try {
            ResultDto validateParam = validateParam(requestDto);
            if (validateParam != null) {
                assembleReqLog(reqLog, false, startTime, "参数为空");
                return validateParam;
            }
            CreditConstant res = validateUser(requestDto.getPhone(), requestDto.getIdNum(), requestDto.getChannelCode());
            if (res != CreditConstant.SUCCESS) {
                assembleReqLog(reqLog, false, startTime, res.toString());
                return CreditConstant.getResult(res);
            }
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(requestDto.getPhone());
            String bankId = mainInfo.getBankAccountId();
            if (org.apache.commons.lang.StringUtils.isEmpty(bankId)) {
                assembleReqLog(reqLog, false, startTime, "客户未绑卡");
                return CreditConstant.getResult(CreditConstant.UNBIND);
            }
            Model_430003 model = new Model_430003();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            Result result = null;
            result = fuiouFunctionService.bankCardUnBind(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            log.error("调用解卡接口业务异常：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            if ("您有未放款的借款，无法解绑银行卡".equals(e.getMessage())) {
                return CreditConstant.getResult(CreditConstant.NO_LOAN);
            }
            return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }

    @Override
    public ResultDto<String> withdraw(WithdrawRequestDto requestDto) {
        log.info("调用提现接口：参数【{}】", requestDto.toString());
        SysRequestLog reqLog = getReqLog("channel_withdraw", requestDto.toString());
        Date startTime = new Date();
        try {
            ResultDto validateParam = validateParam(requestDto);
            if (validateParam != null) {
                assembleReqLog(reqLog, false, startTime, "参数为空");
                return validateParam;
            }
            CreditConstant res = validateUser(requestDto.getPhone(), requestDto.getIdNum(), requestDto.getChannelCode());
            if (res != CreditConstant.SUCCESS) {
                assembleReqLog(reqLog, false, startTime, res.toString());
                return CreditConstant.getResult(res);
            }
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(requestDto.getPhone());

            Model_430009 model = new Model_430009();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            model.setPayAmt(requestDto.getAmount());
            model.setType(Integer.valueOf(requestDto.getWithdrawType()));
            model.setBorrowNo(requestDto.getBorrowNo());
            Result result = null;
            result = fuiouFunctionService.withdrawWithParamEncode(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }

    @Override
    public ResultDto<UserInfoDto> getUserInfo(GetInfoRequestDto requestDto) {

        log.info("调用获取用户信息接口：参数【{}】", requestDto.toString());
        SysRequestLog reqLog = getReqLog("channel_getInfo", requestDto.toString());
        Date startTime = new Date();

        try {
            ResultDto validateParam = validateParam(requestDto);
            if (validateParam != null) {
                assembleReqLog(reqLog, false, startTime, "参数为空");
                return validateParam;
            }

            CreditConstant validateRes = validateUser(requestDto.getPhone(), requestDto.getIdNum(), requestDto.getChannelCode());
            if (validateRes != CreditConstant.SUCCESS) {
                log.info("调用获取用户信息接口:" + validateRes.getMsg());
                assembleReqLog(reqLog, false, startTime, validateRes.toString());
                return CreditConstant.getResult(validateRes);
            }
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(requestDto.getPhone());

            UserInfoDto dto = new UserInfoDto();
            dto.setCellPhone(mainInfo.getCmCellphone());
            Boolean isAuth = StringUtils.isNotEmpty(mainInfo.getCmIdnum()) && AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS == mainInfo.getCmStatus();
            dto.setRealNameStatus(isAuth ? "1" : "0");
            Boolean openAccountFlag = StringUtils.isNotEmpty(mainInfo.getFuiouLoginId());
            dto.setOpenAccountStatus(openAccountFlag ? "1" : "0");
            Boolean isBindCard = StringUtils.isNotBlank(mainInfo.getBankAccountId());
            dto.setBindCardStatus(isBindCard ? "1" : "0");
            UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
            dto.setAuthStatus(userGrant != null ? "1" : "0");
            if (userGrant != null) {
                dto.setAutoLendAmt(userGrant.getAutoLendAmt());
            }
            if (isAuth) {
                dto.setCustomerName(mainInfo.getCmRealName());
            }
            //已绑卡再进行卡信息查询
            if (isBindCard) {
                CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Convert.toLong(mainInfo.getBankAccountId()));
                if (bankAccount == null) {
                    throw new BusinessException("调用获取用户信息接口:查询银行卡信息失败");
                }
                dto.setBankCard(bankAccount.getCbAccount().substring(bankAccount.getCbAccount().length() - 4, bankAccount.getCbAccount().length()));
                dto.setBankName(bankAccount.getCbBankName());
            }
            assembleReqLog(reqLog, true, startTime, dto.toString());
            ResultDto<UserInfoDto> res = CreditConstant.getResult(CreditConstant.SUCCESS);
            res.setData(dto);
            return res;
        } catch (BusinessException e) {
            log.error(e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }

    @Override
    public ResultDto<UserInfoDto> getUserInfoWithIdCard(String idNo, String channelCode) {
        log.info("调用用身份证号获取用户信息接口：参数【{}】，【{}】", idNo, channelCode);
        if(idNo == null || "".equals(idNo) || channelCode == null || "".equals(channelCode)){
            return CreditConstant.getResult(PARAM_EMPTY);
        }
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByIdNum(idNo);
        if(mainInfo == null){
            return CreditConstant.getResult(USER_NOTEXIST);
        }
        if(!channelCode.equals(mainInfo.getChannelCode())){
            return CreditConstant.getResult(ILLEGAL_USERS);
        }

        try {
            UserInfoDto dto = new UserInfoDto();
            dto.setCellPhone(mainInfo.getCmCellphone());
            Boolean isAuth = StringUtils.isNotEmpty(mainInfo.getCmIdnum()) && AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS == mainInfo.getCmStatus();
            dto.setRealNameStatus(isAuth ? "1" : "0");
            Boolean openAccountFlag = StringUtils.isNotEmpty(mainInfo.getFuiouLoginId());
            dto.setOpenAccountStatus(openAccountFlag ? "1" : "0");
            Boolean isBindCard = StringUtils.isNotBlank(mainInfo.getBankAccountId());
            dto.setBindCardStatus(isBindCard ? "1" : "0");
            UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
            dto.setAuthStatus(userGrant != null ? "1" : "0");
            if (userGrant != null) {
                dto.setAutoLendAmt(userGrant.getAutoLendAmt());
            }
            if (isAuth) {
                dto.setCustomerName(mainInfo.getCmRealName());
            }
            //已绑卡再进行卡信息查询
            if (isBindCard) {
                CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Convert.toLong(mainInfo.getBankAccountId()));
                if (bankAccount == null) {
                    throw new BusinessException("调用用身份证号获取用户信息接口:查询银行卡信息失败");
                }
                dto.setBankCard(bankAccount.getCbAccount().substring(bankAccount.getCbAccount().length() - 4, bankAccount.getCbAccount().length()));
                dto.setBankName(bankAccount.getCbBankName());
            }
            ResultDto<UserInfoDto> res = CreditConstant.getResult(SUCCESS);
            res.setData(dto);
            return res;
        }catch (BusinessException e) {
            log.error(e.getMessage());
            return CreditConstant.getResult(BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }

    @Override
    public ResultDto<List<WithdrawDto>> getWithdrawFlows(GetInfoRequestDto requestDto) {
        log.info("调用提现查询接口：参数【{}】", requestDto.toString());
        SysRequestLog reqLog = getReqLog("channel_getInfo", requestDto.toString());
        Date startTime = new Date();
        try {
            ResultDto validateParam = validateParam(requestDto);
            if (validateParam != null) {
                assembleReqLog(reqLog, false, startTime, "参数为空");
                return validateParam;
            }
            CreditConstant validateRes = validateUser(requestDto.getPhone(), requestDto.getIdNum(), requestDto.getChannelCode());
            if (validateRes != CreditConstant.SUCCESS) {
                log.info("调用提现查询接口:" + validateRes.getMsg());
                assembleReqLog(reqLog, false, startTime, validateRes.toString());
                return CreditConstant.getResult(validateRes);
            }
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(requestDto.getPhone());
            List<WithdrawDto> list = busiTradeFlowMapper.selectRecentTradeFlowsForWacai(mainInfo.getId());
            return new ResultDto<>(list);
        } catch (BusinessException e) {
            log.error("调用提现查询接口业务异常:" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return CreditConstant.getResult(ERROR);
        }
    }


    private SysRequestLog assembleReqLog(SysRequestLog requestLog, Boolean isSuccess, Date startTime, String res) {
        requestLog.setRspTime(new Date().getTime() - startTime.getTime());
        if (!isSuccess) {
            requestLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
        }
        requestLog.setRspResult(res.length() > 2000 ? res.substring(0, 2000) : res);
        asyncService.saveReqLog(requestLog);
        return requestLog;
    }

    private ReqMain assembleReqMain(ReqParam model, String clientType) {
        ReqMain reqMain = new ReqMain();
        reqMain.setReqParam(model);
        ReqHeadParam reqHeadParam = createDefaultReqHeadParam();
        reqHeadParam.setPlatform(clientType);
        reqMain.setReqHeadParam(reqHeadParam);
        return reqMain;
    }

    private ResultDto<String> dealWithResult(Result result, SysRequestLog reqLog, Date startTime) {
        Map<String, Object> urlMap = (Map) result.getData();
        if (result.getSuccess()) {
            Object url = urlMap.get("url");
            assembleReqLog(reqLog, true, startTime, url.toString());
            ResultDto res = CreditConstant.getResult(CreditConstant.SUCCESS);
            res.setData(url.toString());
            return res;
        } else {
            log.error("调用接口业务异常：" + result.getMessage());
            assembleReqLog(reqLog, false, startTime, result.getMessage());
            Object code = urlMap.get("code");
            if ("0005".equals(code)) {
                return CreditConstant.getResult(CreditConstant.NO_BALANCE);
            } else if ("0002".equals(code)) {
                return CreditConstant.getResult(CreditConstant.TIMES_LIMIT);
            } else {
                return CreditConstant.getResult(CreditConstant.BUSSINESS_ERROR);
            }
        }
    }

    private CreditConstant validateRegisterUser(String name, String phone, String idNum, String channelCode) {
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(phone);
        if (mainInfo == null) return CreditConstant.SUCCESS;

        if (AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS == mainInfo.getCmStatus() && (!name.equals(mainInfo.getCmRealName()) || !idNum.equals(mainInfo.getCmIdnum())))
            return CreditConstant.INFO_INCONSISTENT;

        if (!channelCode.equals(mainInfo.getChannelCode())) return ILLEGAL_USERS;

        return CreditConstant.BUSSINESS_SUCCESS;
    }

    private CreditConstant validateUser(String phone, String idNum, String channelCode) {
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(phone);
        if (mainInfo == null) return USER_NOTEXIST;

        if (AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS == mainInfo.getCmStatus() && !idNum.equals(mainInfo.getCmIdnum()))
            return USER_NOTEXIST;

        if (!channelCode.equals(mainInfo.getChannelCode())) return ILLEGAL_USERS;

        return CreditConstant.SUCCESS;
    }

    private SysRequestLog getReqLog(String method, String reqParams) {
        SysRequestLog reqLog = new SysRequestLog();
        reqLog.setMethodCode(method);
        reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
        reqLog.setReqParams(reqParams);
        return reqLog;
    }

    private ResultDto validateParam(Object requestDto) {
        try {
            ValidatorUtils.validate(requestDto);
        } catch (BusinessException e) {
            log.error("调用接口业务异常：" + e.getMessage());
            return CreditConstant.getResult(PARAM_EMPTY);
        }
        return null;
    }


    @Override
    public void alarmAmountWacaiTask() {
        String waiCaiAccount = configParamBean.getWacaiProductRemainingPartBuyer();
        com.zdmoney.integral.api.common.dto.ResultDto<AccountDto> accountDtoResultDto
                = accountFacadeService.getAccountBalance(waiCaiAccount);


        if (accountDtoResultDto.isSuccess()) {
            BigDecimal alarmAmountWacai = new BigDecimal(configParamBean.getAlarmAmountWacai());
            BigDecimal wacaiBalance = accountDtoResultDto.getData().getBalance();
            log.info("兜底金额告警定时任务： 挖财兜底账户：{} 剩余金额：{}",waiCaiAccount,wacaiBalance);
            if (alarmAmountWacai.compareTo(wacaiBalance) == 1) {
                MailUtil.sendMail("挖财兜底金额预警", new StringBuffer("当前兜底金额：").append(wacaiBalance.toString())
                        .append(";当前告警金额阈值金额：").append(alarmAmountWacai.toString()).toString(), configParamBean.getAlarmAmountMaillWacai(), true);
            }

        }

    }
}