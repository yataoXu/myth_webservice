package com.zdmoney.models.zdpay;

import lombok.Data;

@Data
public class UserRechargeBO extends BaseBO {

    /**
     * 商户流水号
     */
    private String channelOrderNo;

    /**
     * 客户端类型：0：PC，1：APP
     */
    protected String clientType;

    /**
     * 金额：分
     */
    private String payAmt;

    /**
     * 收银台流水号
     */
    private String mchntTxnSsn;

    /**
     * 请求流水
     */
    private String transNo;

    /**
     * 交易日期
     */
    private String settleDate;
}
