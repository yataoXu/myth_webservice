package com.zdmoney.session.eum;

/**
 * Created by 00232384 on 2017/6/27.
 */
public enum RedisKey {
    USR_LOG("用户登陆"),
    PRD_LST("产品列表"),
    PRD_APP_LST("APP产品首页"),
    ORD("订单");

    private String message;

    public String getMessage() {
        return message;
    }

    RedisKey(String message) {
        this.message = message;
    }
}
