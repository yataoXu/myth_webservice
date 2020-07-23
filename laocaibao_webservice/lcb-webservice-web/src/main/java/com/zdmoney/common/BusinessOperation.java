package com.zdmoney.common;

/**
 * Created by user on 2018/10/9.
 */
public enum BusinessOperation {
    CREDIT_TRANSFER("credit_transfer", "债权转让交割"),
    UNPAID_DEBT_TRANSFER("transferUnpaidDebt", "逾期垫付转让"),
    REFUND_TRANSFEREE("refund_transferee", "交割失败退款给受让人"),
    SEND_RATING_UP_CREDIT("send_rating_up_credit", "用户升级送积分"),
    MATCHING_RESULT_CALLBACK("matching_result_callback", "撮合结果回调");
    private String operType;

    private String desc;

    BusinessOperation(String operType, String desc) {
        this.operType = operType;
        this.desc = desc;
    }

    public String getOperType() {
        return operType;
    }

    public String getDesc() {
        return desc;
    }
}
