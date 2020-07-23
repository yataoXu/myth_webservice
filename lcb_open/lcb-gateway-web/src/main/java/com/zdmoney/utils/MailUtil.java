/*
 * Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
 */
package com.zdmoney.utils;

import com.zdmoney.conf.OpenConfig;
import com.zdmoney.message.api.dto.email.MsgEmailDto;
import com.zdmoney.message.api.dto.email.enums.EmailType;
import com.zdmoney.message.api.facade.IMsgEmailFacadeService;
import com.zdmoney.message.api.utils.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.OpenOption;
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
public class MailUtil {

    private static IMsgEmailFacadeService msgEmailFacadeService;

    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

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
            logger.info("发送邮件失败:" + e);
        }
    }

    public static void sendMail(String title, String content, String to, boolean appendIp) {
        msgEmailFacadeService = ApplicationContextRegister.getApplicationContext().getBean(IMsgEmailFacadeService.class);
        OpenConfig openConfig = ApplicationContextRegister.getApplicationContext().getBean(OpenConfig.class);
        String localIp = null;
        if (appendIp) {
            try {
                localIp = SystemUtil.getSystemLocalIp();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        MsgEmailDto emailDto = new MsgEmailDto();
        emailDto.setTitle("Webservice：" + title);
        emailDto.setContent(content);
        emailDto.setEmailType(EmailType.SYSTEM);
        if (to == null) {
            //这里先不加配置文件了,如果要加下次加了apollo之后再加
            String users = openConfig.getEmailUsers();
            if (StringUtils.isBlank(users)) return;
            emailDto.setReceiveUser(Arrays.asList(users.split(";")));
        } else {
            List<String> emailList = new ArrayList<>();
            emailList.add(to);
            emailDto.setReceiveUser(emailList);
        }

        emailDto.setServerIp(localIp);
        msgEmailFacadeService.send(emailDto);
    }

    public static void sendMail(String title, Throwable t) {
        sendMail(title, ExceptionUtils.getMessage(t) + "\n" + ExceptionUtils.getStackTrace(t));
    }

    public static void main(String args[]) {
        sendMail("test", "exception");
    }
}