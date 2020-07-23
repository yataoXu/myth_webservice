package com.zdmoney.webservice.api.common.dto;

/**
 * Created by rui on 15/8/26.
 */
public enum WebserviceOperator {

    SYS("系统");

    private String message;

    WebserviceOperator(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
