package com.zdmoney.enm;

/**
 * Author: silence.cheng
 * Date: 2017/9/29 14:25
 * 审计业务功能号描述
 */
public enum AuditMethodType {

    AUDIT_REG("注册送红包","001"),
    AUDIT_BIND_WX("绑定微信","006"),
    AUDIT_ORDER_PAY("下单支付","005"),
    AUDIT_coin_EXCHANGE("捞财币商城兑换","004");



    private String desc;
    private String method;

    public String getDesc() {
        return desc;
    }


    public String getMethod() {
        return method;
    }


    AuditMethodType(String desc, String method) {
        this.desc = desc;
        this.method = method;
    }

    public static void main(String[] args) {
        System.out.println(AuditMethodType.AUDIT_REG.getMethod());
    }
}
