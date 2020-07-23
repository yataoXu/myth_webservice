package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.common.handler.SecurityString;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2016/4/9.
 */

public class ContractInvestAgreementVo {
    private String orderNum;
    private String customerName;
    private SecurityString investIdNum = SecurityString.valueof(null);
    private String productName;
    private String borrowName;
    private SecurityString borrowerIdNo = SecurityString.valueof(null);
    private String borrowPurpose;
    private BigDecimal yearRate;
    private BigDecimal orderAmt;
    private BigDecimal principalInterest;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInvestIdNum() {
        return investIdNum.getValue();
    }

    public void setInvestIdNum(String investIdNum) {
        this.investIdNum.setValue(investIdNum);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getBorrowerIdNo() {
        return borrowerIdNo.getValue();
    }

    public void setBorrowerIdNo(String borrowerIdNo) {
        this.borrowerIdNo.setValue(borrowerIdNo) ;
    }

    public String getBorrowPurpose() {
        return borrowPurpose;
    }

    public void setBorrowPurpose(String borrowPurpose) {
        this.borrowPurpose = borrowPurpose;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public BigDecimal getPrincipalInterest() {
        return principalInterest;
    }

    public void setPrincipalInterest(BigDecimal principalInterest) {
        this.principalInterest = principalInterest;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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
}
