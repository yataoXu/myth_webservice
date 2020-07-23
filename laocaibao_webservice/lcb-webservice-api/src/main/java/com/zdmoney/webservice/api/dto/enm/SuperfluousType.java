package com.zdmoney.webservice.api.dto.enm;

/**
 * Created by 00232384 on 2017/7/21.
 */
public enum SuperfluousType {

    NEW("新出借"),

    REPEAT("复投");

    private String msg;

    SuperfluousType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
