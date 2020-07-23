/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.zdmoney.helper.PropertyConfigurer;

/**
 * Log4jdbcDruidDataSource
 * <p/>
 * Author: Hao Chen
 * Date: 2015/9/14 14:57
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
public class Log4jdbcDruidDataSource extends DruidDataSource {

    public void setUsername(String username){
        String realUsername = PropertyConfigurer.decodeBuffer(username);
        super.setUsername(realUsername);
    }

    public void setPassword(String password){
        String realPassword = PropertyConfigurer.decodeBuffer(password);
        super.setPassword(realPassword);
    }
}