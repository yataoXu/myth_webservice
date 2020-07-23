package com.zdmoney.models.bank;

import java.util.Date;

public class BusiBankLimit {
    private Long id;

    private String bankCode = "";

    private String bankName = "";

    private String cardType;

    private Long singleAmt =0l;

    private Long dayAmt = 0l;

    private Long monthAmt = 0l;

    private String bankStatus;

    private Date operDate;

    private String remark = "";

    private String payChannel;

    private String code;

    private String llBankCode;

    private String hrBankName;

    private String llRecharge;//是否支持连连充值 0：是 1：否


    public String getLlRecharge() {
        return llRecharge;
    }

    public void setLlRecharge(String llRecharge) {
        this.llRecharge = llRecharge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public Long getSingleAmt() {
        return singleAmt;
    }

    public void setSingleAmt(Long singleAmt) {
        this.singleAmt = singleAmt;
    }

    public Long getDayAmt() {
        return dayAmt;
    }

    public void setDayAmt(Long dayAmt) {
        this.dayAmt = dayAmt;
    }

    public Long getMonthAmt() {
        return monthAmt;
    }

    public void setMonthAmt(Long monthAmt) {
        this.monthAmt = monthAmt;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus == null ? null : bankStatus.trim();
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLlBankCode() {
        return llBankCode;
    }

    public void setLlBankCode(String llBankCode) {
        this.llBankCode = llBankCode;
    }

    public String getHrBankName() {
        return hrBankName;
    }

    public void setHrBankName(String hrBankName) {
        this.hrBankName = hrBankName;
    }
}