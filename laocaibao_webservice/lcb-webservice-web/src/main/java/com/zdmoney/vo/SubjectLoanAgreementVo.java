package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.common.handler.SecurityString;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2016/4/5.
 * git commit test yangj
 */

public class SubjectLoanAgreementVo {
    private String orderNum;//订单号
    private String borrowerName;
    private SecurityString idNo = SecurityString.valueof(null) ;
    private String customerName;
    private String cmNumber;
    private BigDecimal orderAmt;//订单金额
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;
    private BigDecimal principalInterest;
    private String borrowPurpose;
    private BigDecimal totalInvestAmt;
    private BigDecimal yearRate;
    @JSONField(format = "yyyy-MM-dd")
    private Date lastExpire;
    private String payEndTime;
    private String bankCardNo;
    private String bankName;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getIdNo() {
        return idNo.getValue();
    }

    public void setIdNo(String idNo) {
        this.idNo.setValue(idNo);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCmNumber() {
        return cmNumber;
    }

    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Date getInterestStartDate() {
        return interestStartDate;
    }

    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    public Date getInterestEndDate() {
        return interestEndDate;
    }

    public void setInterestEndDate(Date interestEndDate) {
        this.interestEndDate = interestEndDate;
    }

    public BigDecimal getPrincipalInterest() {
        return principalInterest;
    }

    public void setPrincipalInterest(BigDecimal principalInterest) {
        this.principalInterest = principalInterest;
    }

    public String getBorrowPurpose() {
        return borrowPurpose;
    }

    public void setBorrowPurpose(String borrowPurpose) {
        this.borrowPurpose = borrowPurpose;
    }

    public BigDecimal getTotalInvestAmt() {
        return totalInvestAmt;
    }

    public void setTotalInvestAmt(BigDecimal totalInvestAmt) {
        this.totalInvestAmt = totalInvestAmt;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public Date getLastExpire() {
        return lastExpire;
    }

    public void setLastExpire(Date lastExpire) {
        this.lastExpire = lastExpire;
    }

    public String getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
