/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.utils;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.message.api.dto.email.MsgEmailDto;
import com.zdmoney.message.api.dto.email.enums.EmailType;
import com.zdmoney.message.api.facade.IMsgEmailFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MailUtil
 * <p/>
 * Author: Panjy
 * Date: 2015/7/1 9:28
 * Mail: panjy@zendaimoney.com
 * $Id$
 */
@Slf4j
public class MailUtil {

    private static IMsgEmailFacadeService msgEmailFacadeService;

    private static ConfigParamBean configParamBean;

    static{
        msgEmailFacadeService = ContextLoader.getCurrentWebApplicationContext().getBean(IMsgEmailFacadeService.class);
        configParamBean = ContextLoader.getCurrentWebApplicationContext().getBean(ConfigParamBean.class);
    }
    /**
     * ip验证
     *
     * @param s
     * @return
     */
    public static Boolean isIpAddress(String s) {
        String regex = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * @param request
     * @return
     */
    public static String getClientAddress(HttpServletRequest request) {
        String address = request.getHeader("X-Forwarded-For");
        if (address != null && isIpAddress(address)) {
            return address;
        }
        return request.getRemoteAddr();
    }

    public static void sendMail(String title, String content) {
        try {
            sendMail(title, content, null, true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void sendMail(String title, String content, String to, boolean appendIp) {
        String localIp = null;
        if (appendIp) {
            try {
                localIp = SystemUtil.getSystemLocalIp();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        MsgEmailDto emailDto = new MsgEmailDto();
        emailDto.setTitle("Webservice："+title);
        emailDto.setContent(content);
        if(to == null ){
            String users = configParamBean.getMail_smtp_to();
            if (StringUtils.isBlank(users)) return;
            emailDto.setReceiveUser(Arrays.asList(users.split(";")));
        }else{
            emailDto.setReceiveUser(Arrays.asList(to.split(";")));
        }

        emailDto.setServerIp(localIp);
        emailDto.setEmailType(EmailType.USER);
        msgEmailFacadeService.send(emailDto);
    }


    public static void sendMail(String title, Throwable t) {
        sendMail(title, ExceptionUtils.getMessage(t) + "\n" + ExceptionUtils.getStackTrace(t));
    }



    public static void sendMailValidateCode(String title, String content, String to) {
        MsgEmailDto emailDto = new MsgEmailDto();
        emailDto.setTitle(title);
        emailDto.setContent(content);
        emailDto.setEmailType(EmailType.USER);
        List<String> emailList = new ArrayList<>();
        emailList.add(to);
        emailDto.setReceiveUser(emailList);

        emailDto.setServerIp("出借凭证");
        msgEmailFacadeService.send(emailDto);
    }

    public static void main(String args[]) {
        sendMail("出借凭证验证码", "验证码（有效期十分钟）：" , "461861123@qq.com", true);
    }

}