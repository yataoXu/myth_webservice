package com.zdmoney.vo;

/**
 * Created by jb sun on 2015/12/28.
 */
public class IntercreditorAgreementVo{
    //借款人
    private String borrower;
    //借款人身份证
    private String borrowerIdNum;
    //借款金额
    private String borrowAmount;
    //对付金额
    private String payAmount;
    //用途
    private String borrowByUse;

    public String getBorrower() {
        return borrower;
    }

    public IntercreditorAgreementVo setBorrower(String borrower) {
        this.borrower = borrower;
        return this;
    }

    public String getBorrowerIdNum() {
        return borrowerIdNum;
    }

    public IntercreditorAgreementVo setBorrowerIdNum(String borrowerIdNum) {
        this.borrowerIdNum = borrowerIdNum;
        return this;
    }

    public String getBorrowAmount() {
        return borrowAmount;
    }

    public IntercreditorAgreementVo setBorrowAmount(String borrowAmount) {
        this.borrowAmount = borrowAmount;
        return this;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public IntercreditorAgreementVo setPayAmount(String payAmount) {
        this.payAmount = payAmount;
        return this;
    }

    public String getBorrowByUse() {
        return borrowByUse;
    }

    public IntercreditorAgreementVo setBorrowByUse(String borrowByUse) {
        this.borrowByUse = borrowByUse;
        return this;
    }
}
