package com.zdmoney.web.dto;

/**
 * Created by 00225181 on 2015/12/3.
 * 2.0充值绑卡DTO
 */
public class RechargeAndBindDTO {
    /*充值流水号*/
    private String flowId;

    /*充值金额*/
    private String amount;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
