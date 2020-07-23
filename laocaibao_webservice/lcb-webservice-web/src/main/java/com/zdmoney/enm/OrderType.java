package com.zdmoney.enm;

/**
 * Created by 00232384 on 2017/7/6.
 */
public enum OrderType {
    COMMON("普通订单"),
    SUBORDER("理财计划子订单"),
    SUBTRANSFER("理财计划转让单");

    private String message;

    public String getMessage() {
        return message;
    }

    OrderType(String message) {
        this.message = message;
    }
}
