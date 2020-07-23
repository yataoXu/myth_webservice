package com.zdmoney.models;

import java.math.BigDecimal;
import java.util.Date;

public class BusiAccountOrder {
    private Long id;

    private Long customerId;

    private BigDecimal orderAmt;

    private String cmRechargeType;

    private String orderNum;

    private String payNum;

    private Date orderTime;

    private String bankCardNo;

    private Date payTime;

    private String payStatus;
    
    private BigDecimal counterFee;

    public BigDecimal getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(BigDecimal counterFee) {
		this.counterFee = counterFee;
	}

	private String openPlatform;

    private String togatherType;

    private String openChannel;

    private String openMechanism;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getCmRechargeType() {
        return cmRechargeType;
    }

    public void setCmRechargeType(String cmRechargeType) {
        this.cmRechargeType = cmRechargeType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum == null ? null : payNum.trim();
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getOpenPlatform() {
        return openPlatform;
    }

    public void setOpenPlatform(String openPlatform) {
        this.openPlatform = openPlatform == null ? null : openPlatform.trim();
    }

    public String getTogatherType() {
        return togatherType;
    }

    public void setTogatherType(String togatherType) {
        this.togatherType = togatherType == null ? null : togatherType.trim();
    }

    public String getOpenChannel() {
        return openChannel;
    }

    public void setOpenChannel(String openChannel) {
        this.openChannel = openChannel == null ? null : openChannel.trim();
    }

    public String getOpenMechanism() {
        return openMechanism;
    }

    public void setOpenMechanism(String openMechanism) {
        this.openMechanism = openMechanism == null ? null : openMechanism.trim();
    }
}