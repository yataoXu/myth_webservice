package com.zdmoney.facade;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Maps;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysRequestLog;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.models.zdpay.UserGrantBO;
import com.zdmoney.service.AsyncService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.utils.ValidatorUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.channel.*;
import com.zdmoney.webservice.api.facade.IZdqqFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import websvc.models.*;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.req.ReqParam;
import websvc.servant.FuiouFunctionService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by qinz on 2019/2/13.
 */
@Component
@Slf4j
public class ZdqqFacadeService implements IZdqqFacadeService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private FuiouFunctionService fuiouFunctionService;

    @Autowired
    private CustomerGrantInfoMapper customerGrantInfoMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    private static final String PLATFORM = "credit";
    private static final String SYSTEM = "credit";
    private static final String MECHANISM = "证大";
    private static final String IP = "0.0.0.0";
    private static final String VERSION = "10.0.0";
    private static final String CHANNEL = "credit";
    private static final String BUSINESSEXCEPTION = "0001";
    private static final String CUSTOMERNOTEXIST = "0002";
    private static final String CUSTOMERNOTAUTH = "0003";

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
    public ResultDto<String> register(RegisterRequestDto requestDto) {
        ResultDto<String> responseDto = null;
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_register");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());

            ReqHeadParam reqHeadParam = createDefaultReqHeadParam();
            String cmPassword = "888888";
            String cmCellphone = requestDto.getPhone();
            String validateCode = requestDto.getVerifyCode();
            String channelCode = requestDto.getChannelCode();
            log.info("调用注册接口：渠道码【{}】，手机号【{}】，验证码【{}】。", channelCode, cmCellphone, validateCode);
            Long customerId = customerMainInfoService.doChannelRegister(cmCellphone, cmPassword, validateCode, channelCode, IP, reqHeadParam);
            responseDto = new ResultDto(String.valueOf(customerId));
            reqLog.setRspResult(String.valueOf(customerId));
        } catch (BusinessException e) {
            responseDto = dealWithBusinessExceprion(e);
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
            reqLog.setRspResult(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            responseDto = ResultDto.FAIL("系统内部错误");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
            reqLog.setRspResult(String.valueOf("系统内部错误"));
        }
        reqLog.setRspTime(new Date().getTime() - startTime.getTime());
        asyncService.saveReqLog(reqLog);
        return responseDto;
    }

    @Override
    public ResultDto<String> getVerificationCode(GetCodeRequestDto requestDto) {
        ResultDto<String> responseDto = null;
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        String phone = requestDto.getPhone();
        String channelCode = requestDto.getChannelCode();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_getVCode");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams("phone:" + phone + ",channelCode:" + channelCode);

            log.info("调用获取验证码接口：渠道码【{}】，手机号【{}】。", channelCode, phone);
            String code = customerValidateCodeService.generateOrSearchVerificationCode(
                    AppConstants.ValidateCode.CREDIT, phone, channelCode);
            responseDto = new ResultDto(code);
            reqLog.setRspResult(code);
        } catch (BusinessException e) {
            responseDto = dealWithBusinessExceprion(e);
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
            reqLog.setRspResult(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            responseDto = ResultDto.FAIL(e.getMessage());
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
            reqLog.setRspResult(String.valueOf("系统内部错误"));
        }
        reqLog.setRspTime(new Date().getTime() - startTime.getTime());
        asyncService.saveReqLog(reqLog);
        return responseDto;
    }

    @Override
    public ResultDto realNameAuth(RealNameAuthRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        String channelCode = requestDto.getChannelCode();
        Long customerId = requestDto.getCustomerId();
        String idNum = requestDto.getIdNum();
        String realName = requestDto.getRealName();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_realName");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams("customerId:" + customerId + ",channelCode:" + channelCode + ",idNum" + idNum + ",realName" + realName);

            log.info("调用实名认证接口：渠道码【{}】，用户id【{}】，身份证【{}】，姓名【{}】。", channelCode, customerId, idNum, realName);
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCustomerId(customerId);

            if (3 == customerMainInfo.getCmStatus()) {
                if (!idNum.equalsIgnoreCase(customerMainInfo.getCmIdnum())) {
                    assembleReqLog(reqLog, false, startTime, "身份信息不一致");
                    return new ResultDto(BUSINESSEXCEPTION, "身份信息不一致");
                }
                if (!realName.equals(customerMainInfo.getCmRealName())) {
                    assembleReqLog(reqLog, false, startTime, "姓名不一致");
                    return new ResultDto(BUSINESSEXCEPTION, "身份信息不一致");
                }
                assembleReqLog(reqLog, true, startTime, "实名认证成功");
                return new ResultDto("实名认证成功", true);
            }
            customerMainInfoService.realNameAuth(customerId, realName, idNum, createDefaultReqHeadParam());

        } catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        }  catch (Exception e) {
            log.error("ZdqqFacadeService实名认证失败:" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, e.getMessage().length() > 2000 ? e.getMessage().substring(0, 2000) : e.getMessage());
            return new ResultDto("实名认证失败:" + e.getMessage(), false);
        }
        assembleReqLog(reqLog, true, startTime, "实名认证成功");
        return new ResultDto("实名认证成功", true);
    }

    @Override
    public ResultDto<String> openAccount(InteractionRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_openAccount");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());
            log.info("调用开户接口：渠道码【{}】", requestDto.getChannelCode());
            //校验用户是否存在
            CustomerMainInfo mainInfo = customerMainInfoService.queryByPhoneAndIdNum(requestDto.getPhone(), requestDto.getIdNum());
            Model_430001 model = new Model_430001();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            Result result = null;
            result = fuiouFunctionService.openAccount(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, "操作失败");
            return ResultDto.FAIL(e.getMessage());
        }
    }

    @Override
    public ResultDto<String> authorize(InteractionRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_authorize");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());
            log.info("调用授权接口：渠道码【{}】", requestDto.getChannelCode());
            //校验用户是否存在
            CustomerMainInfo mainInfo = customerMainInfoService.queryByPhoneAndIdNum(requestDto.getPhone(), requestDto.getIdNum());
            Model_430010 model = new Model_430010();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            Result result = null;
            result = fuiouFunctionService.userGrant(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, "操作失败");
            return ResultDto.FAIL(e.getMessage());
        }
    }

    @Override
    public ResultDto<String> bindBankCard(InteractionRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_bindCard");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());
            log.info("调用绑定银行卡接口：渠道码【{}】", requestDto.getChannelCode());
            //校验用户是否存在
            CustomerMainInfo mainInfo = customerMainInfoService.queryByPhoneAndIdNum(requestDto.getPhone(), requestDto.getIdNum());
            Model_430002 model = new Model_430002();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            Result result = null;
            result = fuiouFunctionService.bankCardBind(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, "操作失败");
            return ResultDto.FAIL(e.getMessage());
        }
    }

    @Override
    public ResultDto<String> unBindBankCard(InteractionRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_unBindCard");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());
            log.info("调用解绑银行卡接口：渠道码【{}】", requestDto.getChannelCode());
            //校验用户是否存在
            CustomerMainInfo mainInfo = customerMainInfoService.queryByPhoneAndIdNum(requestDto.getPhone(), requestDto.getIdNum());
            Model_430003 model = new Model_430003();
            model.setCustomerId(mainInfo.getId());
            model.setPageUrl(requestDto.getPageUrl());
            Result result = null;
            result = fuiouFunctionService.bankCardUnBind(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        }catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        } catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, "操作失败");
            return ResultDto.FAIL(e.getMessage());
        }
    }

    @Override
    public ResultDto<String> withdraw(WithdrawRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_withdraw");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());
            log.info("调用提现接口：渠道码【{}】", requestDto.getChannelCode());
            //校验idNum
            if (!isSameIdNum(requestDto.getCustomerId(), requestDto.getIdNum())) {
                assembleReqLog(reqLog, false, startTime, "身份证不一致");
                return ResultDto.FAIL("身份证不一致");
            }
            Model_430009 model = new Model_430009();
            model.setCustomerId(requestDto.getCustomerId());
            model.setPageUrl(requestDto.getPageUrl());
            model.setPayAmt(requestDto.getAmount());
            model.setType(Integer.valueOf(requestDto.getWithdrawType()));
            Result result = null;
            result = fuiouFunctionService.withdraw(assembleReqMain(model, requestDto.getClientType()));
            return dealWithResult(result, reqLog, startTime);
        } catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        }catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, "操作失败");
            return ResultDto.FAIL(e.getMessage());
        }
    }

    @Override
    public ResultDto<UserInfoDto> getUserInfo(GetInfoRequestDto requestDto) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            ValidatorUtils.validate(requestDto);
            reqLog.setMethodCode("channel_getInfo");
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setReqParams(requestDto.toString());
            log.info("调用获取用户信息接口：渠道码【{}】", requestDto.getChannelCode());
            //校验用户是否存在
            CustomerMainInfo mainInfo = customerMainInfoService.queryByPhoneAndIdNum(requestDto.getPhone(), requestDto.getIdNum());

            UserInfoDto dto = new UserInfoDto();
            Boolean isAuth = StringUtils.isNotEmpty(mainInfo.getCmIdnum()) && 3 == mainInfo.getCmStatus();
            dto.setRealNameStatus(isAuth ? "1" : "0");
            Boolean openAccountFlag = StringUtils.isNotEmpty(mainInfo.getFuiouLoginId());
            dto.setOpenAccountStatus(openAccountFlag ? "1" : "0");
            Boolean isBindCard = StringUtils.isNotBlank(mainInfo.getBankAccountId());
            dto.setBindCardStatus(isBindCard ? "1" : "0");
            UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(mainInfo.getId());
            dto.setAuthStatus(userGrant != null ? "1" : "0");
            if(userGrant != null){
                dto.setAutoLendAmt(userGrant.getAutoLendAmt());
            }
            if (isAuth) {
                dto.setCustomerName(mainInfo.getCmRealName());
            }
            //已绑卡再进行卡信息查询
            if (isBindCard) {
                CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Convert.toLong(mainInfo.getBankAccountId()));
                if(bankAccount == null){
                    throw new BusinessException("查询银行卡信息失败");
                }
                dto.setBankCard(bankAccount.getCbAccount().substring(bankAccount.getCbAccount().length() - 4, bankAccount.getCbAccount().length()));
                dto.setBankName(bankAccount.getCbBankName());
            }

            assembleReqLog(reqLog, true, startTime, dto.toString());
            return new ResultDto(dto);
        }catch (BusinessException e) {
            assembleReqLog(reqLog, false, startTime, e.getMessage());
            return dealWithBusinessExceprion(e);
        }catch (Exception e) {
            log.info("操作失败：" + e.getMessage());
            assembleReqLog(reqLog, false, startTime, "操作失败");
            return ResultDto.FAIL(e.getMessage());
        }
    }

    @Override
    public ResultDto<List<WithdrawFlowsDto>> getWithdrawFlows(GetInfoRequestDto requestDto) {
        try {
            log.info("调用提现申请接口：渠道码{},手机号{},身份证号{}",requestDto.getChannelCode(), requestDto.getPhone(), requestDto.getIdNum());
            ValidatorUtils.validate(requestDto);
            //校验用户是否存在
            CustomerMainInfo mainInfo = customerMainInfoService.queryByPhoneAndIdNum(requestDto.getPhone(), requestDto.getIdNum());
            List<WithdrawFlowsDto> list = busiTradeFlowMapper.selectRecentTradeFlows(mainInfo.getId());
            return new ResultDto<>(list);
        }catch (BusinessException e) {
            return dealWithBusinessExceprion(e);
        }catch (Exception e){
            log.info("操作失败：" + e.getMessage());
            return ResultDto.FAIL(e.getMessage());
        }
    }

    private ResultDto dealWithBusinessExceprion(BusinessException e){
        ResultDto responseDto = new ResultDto<>();
        String message = "";
        if (e.getMessage().matches("[\\w\\.]+")) {
            message += getMessage(e.getMessage(), null);
        }else {
            message += e.getMessage();
        }

        if("customer.not.exist".equals(e.getMessage())){
            responseDto.setCode(CUSTOMERNOTEXIST);
        }else if("customer.not.auth".equals(e.getMessage())){
            responseDto.setCode(CUSTOMERNOTAUTH);
        }else{
            responseDto.setCode(BUSINESSEXCEPTION);
        }
        responseDto.setMsg(message);
        log.error("调用功能时业务异常: {}", message);
        return responseDto;
    }

    private Boolean isSameIdNum(Long customerId, String inputIdNum) {
        CustomerMainInfo mainInfo = customerMainInfoService.findAuthCustomerById(customerId);
        if (!inputIdNum.equals(mainInfo.getCmIdnum())) {
            return false;
        } else {
            return true;
        }
    }

    private ResultDto<String> dealWithResult(Result result, SysRequestLog reqLog, Date startTime) {
        if (result.getSuccess()) {
            Map<String, Object> urlMap = (Map) result.getData();
            Object url = urlMap.get("url");
            assembleReqLog(reqLog, true, startTime, url.toString());
            return new ResultDto(url.toString());
        } else {
            assembleReqLog(reqLog, false, startTime, result.getMessage().length() > 2000 ? result.getMessage().substring(0, 2000) : result.getMessage());
            ResultDto resultDto = new ResultDto();
            resultDto.setCode(BUSINESSEXCEPTION);
            resultDto.setMsg(result.getMessage());
            return resultDto;
        }
    }

    private ReqMain assembleReqMain(ReqParam model, String clientType) {
        ReqMain reqMain = new ReqMain();
        reqMain.setReqParam(model);
        ReqHeadParam reqHeadParam = createDefaultReqHeadParam();
        reqHeadParam.setPlatform(clientType);
        reqMain.setReqHeadParam(reqHeadParam);
        return reqMain;
    }

    private SysRequestLog assembleReqLog(SysRequestLog requestLog, Boolean isSuccess, Date startTime, String res) {
        requestLog.setRspTime(new Date().getTime() - startTime.getTime());
        if (!isSuccess) {
            requestLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
        }
        requestLog.setRspResult(res);
        asyncService.saveReqLog(requestLog);
        return requestLog;
    }

    private String getMessage(String code, Object[] args) {
        if (org.apache.commons.lang.StringUtils.isEmpty(code)) {
            return "未知";
        }
        return messageSource.getMessage(code, args, null);
    }
}
