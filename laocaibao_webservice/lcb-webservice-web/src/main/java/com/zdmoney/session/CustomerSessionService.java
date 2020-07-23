/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.session;

import com.alibaba.fastjson.JSON;
import com.zdmoney.component.redis.KeyGenerator;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.utils.Des2;
import com.zdmoney.utils.JackJsonUtil;
import com.zdmoney.web.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import websvc.exception.BusinessException;

import java.io.IOException;

/**
 * CustomerSessionService
 * <p/>
 * Author: Hao Chen
 * Date: 2015/6/24 11:40
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
@Slf4j
public class CustomerSessionService {

    /* session token key */
    private static final String SESSION_TOKEN_KEY = "QWEDCXZAQWEDCXZA";
    @Autowired
    private SessionManager sessionManager;

//    /**
//     * 移除登录状态
//     *
//     * @param loginType
//     * @param phone     手机号
//     * @param deviceId  设备号
//     * @param isEdge    是否增加被T的提示信息
//     */
//    public void removeCustomer(String loginType, String phone, String deviceId, boolean isEdge) {
//        log.info("removeCustomer begin");
//        String key = getKey(loginType, phone, "*");
//        Set<String> keys = sessionManager.getKeys(key);
//        for (String s : keys) {
//            String customerJson = sessionManager.get(s);
//            if (isEdge) {
//                String[] ks = StringUtils.splitByWholeSeparator(s, ":");
//                try {
//                    String sessionDeviceId = null;
//                    if (customerJson != null) {
//                        CustomerDTO customerDTO = JackJsonUtil.strToObj(customerJson, CustomerDTO.class);
//                        sessionDeviceId = customerDTO == null ? null : customerDTO.getDeviceId();
//                    }
//                    // 相同设备号不生成被T信息
//                    if (!StringUtils.equals(deviceId, sessionDeviceId))
//                        sessionManager.addEdgeInfo(ks[2]);
//                } catch (IOException e) {
//                    log.error("exception", e);
//                }
//            }
//            sessionManager.remove(s);
//        }
//        log.info("removeCustomer end");
//    }

    /**
     * 移除登录状态
     *
     * @param loginType
     * @param customerId 用户编号
     * @param deviceId   设备号
     * @param isEdge     是否增加被T的提示信息
     */
    public void removeCustomer(String loginType, Long customerId, String deviceId, boolean isEdge) {
        log.info("removeCustomer begin");
        String key = getKey(customerId+"");
        String value = sessionManager.getHash(key, loginType);
        if (StringUtils.isNotBlank(value)) {
            if (isEdge) {
                String[] values = StringUtils.splitByWholeSeparator(value, "|");
                String customerJson = values[2];
                String token = values[0];
                try {
                    String sessionDeviceId = null;
                    if (customerJson != null) {
                        CustomerDTO customerDTO = JackJsonUtil.strToObj(customerJson, CustomerDTO.class);
                        sessionDeviceId = customerDTO == null ? null : customerDTO.getDeviceId();
                    }
                    // 相同设备号不生成被T信息
                    if (!StringUtils.equals(deviceId, sessionDeviceId))
                        sessionManager.addEdgeInfo(token);
                } catch (IOException e) {
                    log.error("exception", e);
                }
            }
            sessionManager.removeHash(key, loginType);
        }
        log.info("removeCustomer end");
    }


//    /**
//     * @param loginType     登录类型
//     * @param token         令牌
//     * @param updateTimeout 是否重置超时时间
//     * @return 用户对象
//     */
//    public CustomerDTO getCustomer(String loginType, String token, boolean updateTimeout) {
//        log.info("entry getCustomer begin,loginType【{}】,token【{}】，updateTimeout【{}】",loginType,token,updateTimeout);
//        final String key = getKey(loginType, "*", token);
//        log.info("redis getLike begin,key:【{}】",key);
//        String json = sessionManager.getLike(key);
//        log.info("redis getLike end,json :【{}】",json);
//        if (json == null)
//            return null;
//        if (updateTimeout) {
//            executor.submit(new Runnable() {
//                public void run() {
//                    try {
//                        sessionManager.updateTimeout(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
//                    } catch (Exception e) {
//                        log.error("异步更新redis失败", e);
//                    }
//                }
//            });
//        }
//        log.info("entry getCustomer end,token【{}】",token);
//        return JSON.parseObject(json, CustomerDTO.class);
//    }

    /**
     * @param loginType 登录类型
     * @param cellPhone 手机号
     * @param identify  标识
     * @param customer  用户信息
     * @return sessionToken
     */
    public String loginCustomer(String loginType, String cellPhone, String identify, CustomerDTO customer, Long customerId) {
//        String sessionToken = createSessionToken(identify);
        String sessionToken = createSessionToken(identify, customerId);
        setCustomer(loginType,  sessionToken, customer, customerId,sessionToken);
        return sessionToken;
    }


    /**
     * @param loginType     登录类型
     * @param token         令牌
     * @param updateTimeout 是否重置超时时间
     * @return 用户对象
     */
    public CustomerDTO getCustomer(String loginType, String token, boolean updateTimeout) {
        CustomerDTO customerDto = null;
        log.info("entry getCustomer begin,loginType【{}】,token【{}】，updateTimeout【{}】", loginType, token, updateTimeout);
        try {
            String[] tokens = StringUtils.splitByWholeSeparator(Des2.decodeSafe(SESSION_TOKEN_KEY, token), ":");
            String key = getKey(tokens[0]);
            log.info("The key is :【{}】", key);
            String value = sessionManager.getHash(key, loginType);
            if (StringUtils.isNotBlank(value)) {
                String[] values = StringUtils.splitByWholeSeparator(value, "|");
                if (values != null && values.length >= 3) {
                    long oldExpireTime = Long.valueOf(values[1]);
                    //在有效期内，重置时间
                    if (System.currentTimeMillis() <= oldExpireTime) {
                        customerDto = JSON.parseObject(values[2], CustomerDTO.class);
                        if (updateTimeout) {
                            String json = getSessionValue(token, values[2]);
                            sessionManager.putHash(key, loginType, json);
                        }
                    }
                }
            }
        }
        catch (Exception e){
            log.error("getCustomer exception",e);
        }
        log.info("entry getCustomer end,token【{}】", token);
        return customerDto;
    }

    public void setCustomer(String loginType, String token, CustomerDTO customerDTO, Long customerId,String sessionToken) {
        log.info("setCustomer begin");
        sessionManager.removeEdgeInfo(token);
        if (BusiConstants.LOGIN_TYPE_APP.equals(loginType)) {
            removeCustomer(loginType, customerId, customerDTO.getDeviceId(), true);
            sessionManager.removeEdgeInfo(token);
        }
//        cleanToken(loginType, sessionToken);
        String json = JSON.toJSONString(customerDTO);
        String key = getKey(customerId+"");
//      sessionManager.put(key, json, SESSION_TIMEOUT, TimeUnit.MINUTES);
        String value = getSessionValue(token, json);
        sessionManager.putHash(key , loginType, value);
        log.info("setCustomer end");
    }

//    private String getKey(String loginType, String phone, String token) {
//        return StringUtils.join(new String[]{SESSION_KEY_PREFIX + "_" + loginType, phone, token}, ":");
//    }

    private String getKey(String customerId) {
        return StringUtils.join(new String[]{KeyGenerator.USER_SESSION.getKey(), customerId}, ":");
    }

    private String getSessionValue(String token, String json) {
        long times = System.currentTimeMillis() + KeyGenerator.USER_SESSION.getTime();
        String value = StringUtils.join(new String[]{token, times + "", json}, "|");
        return value;
    }

//    public String createSessionToken(String deviceId) {
//        return MD5.MD5Encode(deviceId + SESSION_TOKEN_KEY);
//
//    }

    public String createSessionToken(String deviceId, Long id) {
        String sessionToken = null;
        try {
            sessionToken = Des2.encodeSafe(SESSION_TOKEN_KEY, StringUtils.join(new String[]{String.valueOf(id), deviceId}, ":"));
        } catch (Exception e) {
            log.error("create sessionToken Error", e);
            throw new BusinessException("生成sessionToken失败");
        }
        return sessionToken;
    }

    public String getEdgeInfo(String key) {
        log.info("getEdgeInfo begin");
        String edgeInfo = sessionManager.getEdgeInfo(key);
        if (edgeInfo != null) {
            sessionManager.removeEdgeInfo(key);
        }
        log.info("getEdgeInfo end");
        return edgeInfo;
    }


//    public boolean cleanToken(String loginType, String token) {
//        log.info("cleanToken begin");
//        String key = getKey(loginType, "*", token);
//        Set<String> keys = sessionManager.getKeys(key);
//        for (String s : keys) {
//            sessionManager.remove(s);
//        }
//        sessionManager.removeEdgeInfo(token);
//        log.info("cleanToken end");
//        return true;
//    }

    public boolean cleanToken(String loginType, String token) {
        log.info("cleanToken begin");
        String sessionToken = Des2.decodeSafe(SESSION_TOKEN_KEY, token);
        if (StringUtils.isNotBlank(sessionToken)) {
            String[] tokens = StringUtils.splitByWholeSeparator(sessionToken, ":");
            if (tokens != null && tokens.length > 1) {
                String key = getKey(tokens[0]);
                sessionManager.removeHash(key, loginType);
            }
        }
        sessionManager.removeEdgeInfo(token);
        log.info("cleanToken end");
        return true;
    }

    /**
     * 注销登录
     * @param loginTyp
     * @param customerId
     * @return
     */
    public boolean cleanToken(String loginTyp,String token,Long customerId) {
        log.info("cleanToken begin");
        String key = getKey(customerId+"");
        sessionManager.removeHash(key, loginTyp);
        sessionManager.removeEdgeInfo(token);
        log.info("cleanToken end");
        return true;
    }

//    public void cleanSessionByLike(String orgMobile) {
//        Set<String> keys = sessionManager.getKeys("*" + orgMobile + "*");
//        for (String key : keys) {
//            sessionManager.remove(key);
//        }
//    }

    public void cleanSessionById(Long id) {
        sessionManager.remove(getKey(id + ""));
    }
}