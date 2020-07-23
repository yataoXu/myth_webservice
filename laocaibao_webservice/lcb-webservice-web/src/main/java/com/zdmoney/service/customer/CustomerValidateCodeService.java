/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.customer;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.customer.CustomerValidateCodeMapper;
import com.zdmoney.mapper.customer.SysCheckRegisterMapper;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.sm.SendVerifyCodeMsgReqDto;
import com.zdmoney.message.api.dto.sm.SendVerifyCodeMsgRspDto;
import com.zdmoney.message.api.dto.sm.SendVoiceVerifyMsgReqDto;
import com.zdmoney.message.api.dto.sm.SmSource;
import com.zdmoney.message.api.facade.ISmFacadeService;
import com.zdmoney.models.customer.CustomerValidateCode;
import com.zdmoney.models.customer.SysCheckRegister;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import com.zendaimoney.laocaibao.wx.api.facade.IWechatFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

/**
 * CustomerValidateCodeService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-29 10:51
 * Mail: haoc@zendaimoney.com
 */
@Service
@Slf4j
public class CustomerValidateCodeService extends BaseBusinessService<CustomerValidateCode, Long> {

    private CustomerValidateCodeMapper getCustomerValidateCodeMapper() {
        return (CustomerValidateCodeMapper) baseMapper;
    }

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private IWechatFacadeService wechatFacadeService;

    @Autowired
    private ISmFacadeService iSmFacadeService;

    @Autowired
    private SysCheckRegisterMapper sysCheckRegisterMapper;


    public CustomerValidateCode getByCodeByMobileAndType(String mobile, Integer type) {
        CustomerValidateCode validateCode = new CustomerValidateCode();
        validateCode.setCvMobile(mobile);
        validateCode.setCvType(type);
        return getCustomerValidateCodeMapper().selectOne(validateCode);
    }


    public void checkValidateCode(Integer cvType, String cvMobile, String validateCode) {
        CustomerValidateCode customerValidateCode = getByCodeByMobileAndType(cvMobile, cvType);
        if (customerValidateCode == null) {
            throw new BusinessException("validateCode.not.get");
        }
        if (StringUtils.isEmpty(customerValidateCode.getCvCode())) {
            throw new BusinessException("validateCode.not.get");
        }
        if (customerValidateCode.getTryTime() >= configParamBean.getTppValicodeTryTime() && cvType != AppConstants.ValidateCode.CREDIT) {
            throw new BusinessException("validateCode.input.maxLimit", new Object[] {configParamBean.getTppValicodeTryTime()});
        }
        customerValidateCode.setTryTime(customerValidateCode.getTryTime() + 1);
        customerValidateCode = update(customerValidateCode);
        if (customerValidateCode == null) {
            throw new BusinessException("validateCode.update.failed");
        }
        if (!LaocaiUtil.checkValidaterCode(customerValidateCode)) {
            throw new BusinessException("validateCode.invalid");
        }
        if (!customerValidateCode.getCvCode().equals(validateCode)) {
            throw new BusinessException("validateCode.error");
        }
    }

    /**
     * 校验是否诈骗客户
     * 1、针对于注册、出借人
     * 2. 是否存在于白名单中，如果存在直接通过，否则校验是否号码段是否非法号码段（前七位判断），如果是在非法不能通过
     *
     * @param cellphone
     * @return
     */
    public boolean checkIsIllegal(String cellphone){
        //获取白名单
        String registerWhiteList = configParamBean.getRegisterWhiteList();
        log.info("校验是否诈骗客户-checkIsIllegal()-获取白名单"+ JSON.toJSONString(registerWhiteList));
        String[] splitWhiteList = registerWhiteList.split(",");
        boolean whiteFlag = false;
        if(!StringUtil.isEmpty(splitWhiteList)){
            for (String s : splitWhiteList) {
                if(cellphone.equals(s)){
                    whiteFlag =true;
                }
            }

        }
        //获取非法号码段
        SysCheckRegister sysCheckRegister = new SysCheckRegister();
        sysCheckRegister.setIllegalPhone(cellphone.substring(0,7));
        Integer count = sysCheckRegisterMapper.countForRegisterPhone(sysCheckRegister);
        log.info("校验是否诈骗客户-checkIsIllegal()-获取号码段以及accountType"+ JSON.toJSONString(count));
        boolean illegalPhoneFlag = false;
        if (count > 0) {
            illegalPhoneFlag = true;
        }
            //校验白名单
            if(!whiteFlag && illegalPhoneFlag){
                log.info("校验是否诈骗客户-checkIsIllegal()-非法");
                return false;
            }
       return true;
    }

    public String checkValiCode(Integer cvType, String cvMobile, String validateCode) {
        CustomerValidateCode customerValidateCode = getByCodeByMobileAndType(cvMobile, cvType);
        if (customerValidateCode == null || StringUtils.isEmpty(customerValidateCode.getCvCode())) {
            return "用户没有获取验证码";
        }
        if (!customerValidateCode.getCvCode().equals(validateCode)) {
            return "验证码错误";
        }
        if (customerValidateCode.getTryTime() >= configParamBean.getTppValicodeTryTime()) {
            return "验证码尝试次数已超过"+configParamBean.getTppValicodeTryTime().toString()+"次，不能再次尝试";
        }
        customerValidateCode.setTryTime(customerValidateCode.getTryTime() + 1);
        customerValidateCode = update(customerValidateCode);
        if (customerValidateCode == null) {
            return "验证码更新失败";
        }
        if (!LaocaiUtil.checkValidaterCode(customerValidateCode)) {
            return "验证码已经失效";
        }
        return null;
    }

    public String generateOrSearchVerificationCode(int cvType, String cvMobile,String channelCode){
        String code = RandomStringUtils.randomNumeric(4);
        log.info(cvMobile + "的验证码是: {}", code);
        CustomerValidateCode customerValidateCode = getByCodeByMobileAndType(cvMobile, cvType);
        if (customerValidateCode != null) {
            customerValidateCode.setCvCode(code);
            customerValidateCode.setCvCreateTime(new Date());
            customerValidateCode.setTryTime(0);
            customerValidateCode.setChannelCode(channelCode);
            customerValidateCode = update(customerValidateCode);
            if (customerValidateCode == null) {
                log.error("更新短信发送次数失败！cvMobile=" + cvMobile);
                throw new BusinessException("validateCode.update.failed");
            }
        } else {
            CustomerValidateCode entity = new CustomerValidateCode();
            entity.setCvCode(code);
            entity.setCvCreateTime(new Date());
            entity.setCvMobile(cvMobile);
            entity.setCvType(cvType);
            entity.setTryTime(0);
            entity.setChannelCode(channelCode);
            entity = save(entity);
            if (entity == null) {
                log.error("插入发送短信信息失败！cvMobile=" + cvMobile);
                throw new BusinessException("validateCode.send.failed");
            }
        }
        // 发送验证码
        sendCode(cvMobile, code, channelCode);
        return code;
    }

    public void sendValidateCode(Integer cvType, String cvMobile, String busiName, Integer codeType) {
        if (cvType == 1) {//判断重置密码情况下是否已注册
            checkExistByCellphone(cvMobile);
        }
        if (cvType == 0) {//注册时判断该手机是否已注册
            checkNotRegisterByCellphone(cvMobile);
        }

        if (cvType == 13) {//判断该手机是否加入黑白名单
            checkWhiteListByPhone(cvMobile);
        }



        String code = RandomStringUtils.randomNumeric(4);
        log.info("验证码: {}", code);
        CustomerValidateCode customerValidateCode = getByCodeByMobileAndType(cvMobile, cvType);
        if (customerValidateCode != null) {
            customerValidateCode.setCvCode(code);
            customerValidateCode.setCvCreateTime(new Date());
            customerValidateCode.setTryTime(0);
            customerValidateCode = update(customerValidateCode);
            if (customerValidateCode == null) {
                log.error("更新短信发送次数失败！cvMobile=" + cvMobile);
                throw new BusinessException("validateCode.update.failed");
            }
        } else {
            CustomerValidateCode entity = new CustomerValidateCode();
            entity.setCvCode(code);
            entity.setCvCreateTime(new Date());
            entity.setCvMobile(cvMobile);
            entity.setCvType(cvType);
            entity.setTryTime(0);
            entity = save(entity);
            if (entity == null) {
                log.error("插入发送短信信息失败！cvMobile=" + cvMobile);
                throw new BusinessException("validateCode.send.failed");
            }
        }
        if (codeType == 1) {
            SendVoiceVerifyMsgReqDto voiceVerifyMsgReqDto = new SendVoiceVerifyMsgReqDto();
            voiceVerifyMsgReqDto.setMobile(cvMobile);
            voiceVerifyMsgReqDto.setVerifyCode(code);
            voiceVerifyMsgReqDto.setSmSource(SmSource.LCB);
            MessageResultDto<String> msgResult = iSmFacadeService.sendVoiceVerifyCodeMsg(voiceVerifyMsgReqDto);
            if (!msgResult.isSuccess()) {
                log.error("发送验证码短信失败！失败原因=" + msgResult.getMsg());
                if("2222".equals(msgResult.getCode())) {
                    throw new BusinessException(msgResult.getMsg());
                } else {
                    throw new BusinessException("发送验证码短信失败");
                }
            }
        } else {
            SmSource smSource = null;
            if ("credit".equals(busiName)) {
                smSource = SmSource.TZZX;
                busiName = "";
            } else {
                smSource = SmSource.LCB;
            }
            String msgContent = "";
            if (org.apache.commons.lang.StringUtils.isEmpty(busiName)) {
                msgContent += "";
            } else {
                msgContent += busiName + ",";
            }
            msgContent += "验证码" + code + ",有效期为10分钟，感谢您使用捞财宝，客服热线400-096-6588。";
            SendVerifyCodeMsgReqDto verifyCodeMsgReqDto = new SendVerifyCodeMsgReqDto();
            verifyCodeMsgReqDto.setMobile(cvMobile);
            verifyCodeMsgReqDto.setVerifyCode(code);
            verifyCodeMsgReqDto.setSendMsg(msgContent);
            verifyCodeMsgReqDto.setSmSource(smSource);
            MessageResultDto<SendVerifyCodeMsgRspDto> resultDto = iSmFacadeService.sendVerifyCodeMsg(verifyCodeMsgReqDto);
            if (!resultDto.isSuccess()) {
                log.error("发送验证码短信失败！失败原因=" + resultDto.getMsg());
                if("2222".equals(resultDto.getCode())) {
                    throw new BusinessException(resultDto.getMsg());
                } else {
                    throw new BusinessException("发送验证码短信失败");
                }
            }
        }
    }

    /**
     * 发送验证码
     * @param mobile
     * @param code
     * @param channel
     */
    public void sendCode(String mobile, String code, String channel) {
        SmSource smSource = null;
        if ("credit".equals(channel)) {
            smSource = SmSource.TZZX;
        } else {
            smSource = SmSource.LCB;
        }
        String msgContent = "验证码" + code + ",有效期为10分钟，感谢您使用捞财宝，客服热线400-096-6588。";
            SendVerifyCodeMsgReqDto verifyCodeMsgReqDto = new SendVerifyCodeMsgReqDto();
            verifyCodeMsgReqDto.setMobile(mobile);
            verifyCodeMsgReqDto.setVerifyCode(code);
            verifyCodeMsgReqDto.setSendMsg(msgContent);
            verifyCodeMsgReqDto.setSmSource(smSource);
            MessageResultDto<SendVerifyCodeMsgRspDto> resultDto = iSmFacadeService.sendVerifyCodeMsg(verifyCodeMsgReqDto);
            if (!resultDto.isSuccess()) {
                log.error("发送验证码短信失败！失败原因=" + resultDto.getMsg());
                if("2222".equals(resultDto.getCode())) {
                    throw new BusinessException(resultDto.getMsg());
                } else {
                    throw new BusinessException("发送验证码短信失败");
                }
            }
    }
}