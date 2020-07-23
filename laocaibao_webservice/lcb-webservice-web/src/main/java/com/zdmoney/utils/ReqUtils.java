/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.utils;

import com.zdmoney.constant.AppConstants;
import org.apache.commons.lang3.StringUtils;
import websvc.req.ReqHeadParam;

/**
 * ReqUtils
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-28 14:06
 * Mail: haoc@zendaimoney.com
 */
public class ReqUtils {

    public static String getUserAgent(ReqHeadParam headParam) {
        if(StringUtils.isNotEmpty(headParam.getUserAgent())) {
            String userAgent = headParam.getUserAgent().split(";")[0];//设备型号
            return userAgent.substring(userAgent.indexOf("(") + 1, userAgent.length());
        }
        return null;
    }

    public static String getDeviceType(ReqHeadParam headParam) {
        String userAgent = getUserAgent(headParam);
        return getDeviceType(userAgent);
    }

    public static String getDeviceType(String userAgent) {
        if (StringUtils.equalsIgnoreCase("iOS", userAgent)) {
            return AppConstants.HttpHead.IOS;
        } else {
            return AppConstants.HttpHead.ANDROID;
        }
    }

}