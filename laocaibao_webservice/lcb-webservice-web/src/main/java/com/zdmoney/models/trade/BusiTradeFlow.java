package com.zdmoney.models.trade;

import com.zdmoney.constant.AppConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BusiTradeFlow implements Serializable {

    private static final long serialVersionUID = -382483341326047587L;

    private Long id;

    private BigDecimal trdAmt;

    private String trdType;

    private Date trdDate;

    private String flowNum;

    private Long customerId;

    private String status;

    private String paySeriNo;

    private String accountSeriNo;

    private String tppSeriNo;

    private String bankName;

    private String bankCardNum;

    private Long bankCardId;

    private String applicantType;

    private Long flowDetailId;

    /**
     * 提现类型  1:普通提现  2:快速提现
     */
    private Integer type;

    /**
     * 快速提现服务费
     */
    private BigDecimal serviceCharge = new BigDecimal(0);

    /**
     * job反查是否成功
     */
    private Integer jobFlag;

    public BusiTradeFlow() {

    }

    public static BusiTradeFlow buildRechargeTradeFlow(String flowNum, BigDecimal trdAmt) {
        return new BusiTradeFlow(flowNum, AppConstants.TradeStatusContants.RECHARGEING, trdAmt);
    }

    public BusiTradeFlow(String flowNum, String trdType, BigDecimal trdAmt) {
        this.flowNum = flowNum;
        this.trdType = trdType;
        this.trdAmt  = trdAmt;
        this.trdDate = new Date();
    }

    public Long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTrdAmt() {
        return trdAmt;
    }

    public void setTrdAmt(BigDecimal trdAmt) {
        this.trdAmt = trdAmt;
    }

    public String getTrdType() {
        return trdType;
    }

    public void setTrdType(String trdType) {
        this.trdType = trdType == null ? null : trdType.trim();
    }

    public Date getTrdDate() {
        return trdDate;
    }

    public void setTrdDate(Date trdDate) {
        this.trdDate = trdDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(String flowNum) {
        this.flowNum = flowNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaySeriNo() {
        return paySeriNo;
    }

    public void setPaySeriNo(String paySeriNo) {
        this.paySeriNo = paySeriNo;
    }

    public String getAccountSeriNo() {
        return accountSeriNo;
    }

    public void setAccountSeriNo(String accountSeriNo) {
        this.accountSeriNo = accountSeriNo;
    }

    public String getTppSeriNo() {
        return tppSeriNo;
    }

    public void setTppSeriNo(String tppSeriNo) {
        this.tppSeriNo = tppSeriNo;
    }

    public String getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(String applicantType) {
        this.applicantType = applicantType;
    }

    public Long getFlowDetailId() {
        return flowDetailId;
    }

    public void setFlowDetailId(Long flowDetailId) {
        this.flowDetailId = flowDetailId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getJobFlag() {
        return jobFlag;
    }

    public void setJobFlag(Integer jobFlag) {
        this.jobFlag = jobFlag;
    }
}