package com.zdmoney.web.dto.agreement;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.common.handler.SecurityString;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2016/4/6.
 */

public class SubjectInvestAgrementDTO {
    private String orderNum;//订单编号
    private String productName;//产品名称
    private String borrowerName;//借款人名称
    private SecurityString idNo = SecurityString.valueof(null);//借款人身份证
    private String borrowPurpose;//借款目的
    private BigDecimal orderAmt;//订单金额
    private BigDecimal yearRate;//利率
    private BigDecimal principalInterest;//本息
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;//订单时间
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;//起息时间
    @JSONField(format = "yyyy-MM-dd")
    private Date lastExpireDate;//到期日

    private String transName;//转让人姓名
    private SecurityString transIdNo = SecurityString.valueof(null);//转让人身份证号
    private String investName;//投资人姓名
    private SecurityString investIdNo = SecurityString.valueof(null) ;//投资人身份证号
    private int payDays;//还款期限
    private BigDecimal interest;//利息


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getBorrowPurpose() {
        return borrowPurpose;
    }

    public void setBorrowPurpose(String borrowPurpose) {
        this.borrowPurpose = borrowPurpose;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
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

    public Date getLastExpireDate() {
        return lastExpireDate;
    }

    public void setLastExpireDate(Date lastExpireDate) {
        this.lastExpireDate = lastExpireDate;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTransIdNo() {
        return transIdNo.getValue();
    }

    public void setTransIdNo(String transIdNo) {
        this.transIdNo.setValue(transIdNo) ;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public String getInvestIdNo() {
        return investIdNo.getValue();
    }

    public void setInvestIdNo(String investIdNo) {
        this.investIdNo.setValue(investIdNo);
    }

    public int getPayDays() {
        return payDays;
    }

    public void setPayDays(int payDays) {
        this.payDays = payDays;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
}
