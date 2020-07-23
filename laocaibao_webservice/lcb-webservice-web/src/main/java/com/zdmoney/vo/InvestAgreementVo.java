package com.zdmoney.vo;

/**
 * Created by jb sun on 2015/12/28.
 */
public class InvestAgreementVo {
    //投资人姓名
    private String userName;
    //投资人身份证
    private String idNum;
    //产品名称
    private String productName;
    //投资金额
    private String joinAmount;
    //年利率
    private String yearRate;
    //到期本息
    private String totalAmount;
    //订单日期
    private String orderDate;
    //封闭期
    private String interestDay;
    //投资起始日期
    private String interestStartDate;
    //投资结束日期
    private String interestEndDate;
    //订单编号
    private String orderNum;
    //借款人
    private String loanName;
    //借款人省份证
    private String idNo;
    //借款目的
    private String loanPropose;

    public String getOrderNum() {
        return orderNum;
    }

    public InvestAgreementVo setOrderNum(String orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public String getInterestDay() {
        return interestDay;
    }

    public InvestAgreementVo setInterestDay(String interestDay) {
        this.interestDay = interestDay;
        return this;
    }

    public String getInterestStartDate() {
        return interestStartDate;
    }

    public InvestAgreementVo setInterestStartDate(String interestStartDate) {
        this.interestStartDate = interestStartDate;
        return this;
    }

    public String getInterestEndDate() {
        return interestEndDate;
    }

    public InvestAgreementVo setInterestEndDate(String interestEndDate) {
        this.interestEndDate = interestEndDate;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public InvestAgreementVo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getIdNum() {
        return idNum;
    }
    public String getIdNumStr() {
        String idNum=getIdNum();
        if (idNum.length()==18){
            idNum= idNum.substring(0,4)+"**********"+idNum.substring(14,18);
        }
        if (idNum.length()==15) {
            idNum = idNum.substring(0, 4) + "**********" + idNum.substring(11, 15);
        }
        return idNum;
    }

    public InvestAgreementVo setIdNum(String idNum) {
        this.idNum = idNum;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public InvestAgreementVo setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getJoinAmount() {
        return joinAmount;
    }

    public InvestAgreementVo setJoinAmount(String joinAmount) {
        this.joinAmount = joinAmount;
        return this;
    }

    public String getYearRate() {
        return yearRate;
    }

    public InvestAgreementVo setYearRate(String yearRate) {
        this.yearRate = yearRate;
        return this;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public InvestAgreementVo setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public InvestAgreementVo setOrderDate(String orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getLoanPropose() {
        return loanPropose;
    }

    public void setLoanPropose(String loanPropose) {
        this.loanPropose = loanPropose;
    }
}
