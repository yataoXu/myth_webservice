package com.zdmoney.models.trade;

import java.util.Date;

public class BusiTradeFlowDetail {
    private Long id;

    private Object flowNum;

    private Object bankCode;

    private Object bankName;

    private Object subBankCode;

    private Object subBankName;

    private Object bankCardNum;

    private Object paySerialno;

    private Object memo;

    private Object payChannel;

    private String requestSerialNo;

    private String creator;

    private Date createTime;

    private String tppAgreement;

    private Date dealTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(Object flowNum) {
        this.flowNum = flowNum;
    }

    public Object getBankCode() {
        return bankCode;
    }

    public void setBankCode(Object bankCode) {
        this.bankCode = bankCode;
    }

    public Object getBankName() {
        return bankName;
    }

    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }

    public Object getSubBankCode() {
        return subBankCode;
    }

    public void setSubBankCode(Object subBankCode) {
        this.subBankCode = subBankCode;
    }

    public Object getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(Object subBankName) {
        this.subBankName = subBankName;
    }

    public Object getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(Object bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public Object getPaySerialno() {
        return paySerialno;
    }

    public void setPaySerialno(Object paySerialno) {
        this.paySerialno = paySerialno;
    }

    public Object getMemo() {
        return memo;
    }

    public void setMemo(Object memo) {
        this.memo = memo;
    }

    public Object getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Object payChannel) {
        this.payChannel = payChannel;
    }

    public String getRequestSerialNo() {
        return requestSerialNo;
    }

    public void setRequestSerialNo(String requestSerialNo) {
        this.requestSerialNo = requestSerialNo == null ? null : requestSerialNo.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTppAgreement() {
        return tppAgreement;
    }

    public void setTppAgreement(String tppAgreement) {
        this.tppAgreement = tppAgreement;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }
}