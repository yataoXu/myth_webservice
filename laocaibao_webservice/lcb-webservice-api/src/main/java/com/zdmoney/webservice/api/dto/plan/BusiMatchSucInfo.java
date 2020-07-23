package com.zdmoney.webservice.api.dto.plan;

import java.math.BigDecimal;
import java.util.Date;

public class BusiMatchSucInfo {
    private Long id;

    private String capitalCode;

    private BigDecimal capitalAmount;

    private String capitalType;

    private String ledgerId;

    private String priority;

    private String financeId;

    private String status;

    private String totalTerm;

    private Date createTime;

    private BigDecimal earningsRate;

    private String productCode;

    private String subjectNo;

    private String subjectAmt;

    private String loanCustomerNo;

    private String loanCustomerName;

    private String debtType;

    private String initOrderNum;

    private String matchOrderCode;

    private String manFinanceId;

    private Date borrowerDate;

    private BigDecimal debtWorth;

    private BigDecimal interest;

    private Long masterId;

    private String operStatus;

    private int financeNum;

    private int transferNum;

    private String batchNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCapitalCode() {
        return capitalCode;
    }

    public void setCapitalCode(String capitalCode) {
        this.capitalCode = capitalCode == null ? null : capitalCode.trim();
    }

    public BigDecimal getCapitalAmount() {
        return capitalAmount;
    }

    public void setCapitalAmount(BigDecimal capitalAmount) {
        this.capitalAmount = capitalAmount;
    }

    public String getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(String capitalType) {
        this.capitalType = capitalType == null ? null : capitalType.trim();
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId == null ? null : ledgerId.trim();
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority == null ? null : priority.trim();
    }

    public String getFinanceId() {
        return financeId;
    }

    public void setFinanceId(String financeId) {
        this.financeId = financeId == null ? null : financeId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getTotalTerm() {
        return totalTerm;
    }

    public void setTotalTerm(String totalTerm) {
        this.totalTerm = totalTerm == null ? null : totalTerm.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getEarningsRate() {
        return earningsRate;
    }

    public void setEarningsRate(BigDecimal earningsRate) {
        this.earningsRate = earningsRate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getSubjectNo() {
        return subjectNo;
    }

    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo == null ? null : subjectNo.trim();
    }

    public String getSubjectAmt() {
        return subjectAmt;
    }

    public void setSubjectAmt(String subjectAmt) {
        this.subjectAmt = subjectAmt == null ? null : subjectAmt.trim();
    }

    public String getLoanCustomerNo() {
        return loanCustomerNo;
    }

    public void setLoanCustomerNo(String loanCustomerNo) {
        this.loanCustomerNo = loanCustomerNo == null ? null : loanCustomerNo.trim();
    }

    public String getLoanCustomerName() {
        return loanCustomerName;
    }

    public void setLoanCustomerName(String loanCustomerName) {
        this.loanCustomerName = loanCustomerName == null ? null : loanCustomerName.trim();
    }

    public String getDebtType() {
        return debtType;
    }

    public void setDebtType(String debtType) {
        this.debtType = debtType == null ? null : debtType.trim();
    }

    public String getInitOrderNum() {
        return initOrderNum;
    }

    public void setInitOrderNum(String initOrderNum) {
        this.initOrderNum = initOrderNum == null ? null : initOrderNum.trim();
    }

    public String getMatchOrderCode() {
        return matchOrderCode;
    }

    public void setMatchOrderCode(String matchOrderCode) {
        this.matchOrderCode = matchOrderCode == null ? null : matchOrderCode.trim();
    }

    public String getManFinanceId() {
        return manFinanceId;
    }

    public void setManFinanceId(String manFinanceId) {
        this.manFinanceId = manFinanceId == null ? null : manFinanceId.trim();
    }

    public Date getBorrowerDate() {
        return borrowerDate;
    }

    public void setBorrowerDate(Date borrowerDate) {
        this.borrowerDate = borrowerDate;
    }

    public BigDecimal getDebtWorth() {
        return debtWorth;
    }

    public void setDebtWorth(BigDecimal debtWorth) {
        this.debtWorth = debtWorth;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public int getFinanceNum() {
        return financeNum;
    }

    public void setFinanceNum(int financeNum) {
        this.financeNum = financeNum;
    }

    public int getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(int transferNum) {
        this.transferNum = transferNum;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}