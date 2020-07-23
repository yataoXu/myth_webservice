package com.zdmoney.common.handler;

import java.io.Serializable;

/**
 * Author: silence.cheng
 * Date: 2018/1/4 09:47
 */
public class SecurityString implements Serializable {
    private String value;

    public SecurityString(){ }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public static SecurityString valueof(String value){
        SecurityString ss = new SecurityString();
        ss.setValue(value);
        return ss;
    }
}