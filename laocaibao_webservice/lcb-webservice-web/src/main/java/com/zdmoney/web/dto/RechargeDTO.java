package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.models.payChannel.BusiPayChannel;

import java.util.List;

/**
 * Created by 00225181 on 2016/1/19.
 */
public class RechargeDTO {
    List<BusiPayChannel> channels = Lists.newArrayList();
    private String flowId="";
    private String amount = "";
    private String tppAgreementUrl = "";
    private String cellphone="";

    public List<BusiPayChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<BusiPayChannel> channels) {
        this.channels = channels;
    }

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

    public String getTppAgreementUrl() {
        return tppAgreementUrl;
    }

    public void setTppAgreementUrl(String tppAgreementUrl) {
        this.tppAgreementUrl = tppAgreementUrl;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
