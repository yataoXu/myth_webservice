package com.zdmoney.webservice.api.dto.Asset;

import java.io.Serializable;

/**
 * Created by 00225181 on 2015/12/3.
 */
public class BankCardInfoDTO implements Serializable {

    /*银行名称*/
    private String bankName="";

    /*银行代码*/
    private String bankCode="";

    /*支行名称*/
    private String subBankName="";

    /*支行代码*/
    private String subBankCode="";

    /*银行卡号*/
    private String bankCard="";

    /*限额描述*/
    private String limitDesc="";

    /*备注信息*/
    private String remark="";

    /*提现描述*/
    private String withdrawDesc="";

    /*可用余额*/
    private String accountBalance = "0";

    /*支付渠道编号*/
    private String payCode="";

    private boolean isLockPay = false;//是否锁定支付

    private String lockDesc="";//锁定描述

    private String cellphone = "";//银行预留手机号


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }

    public String getSubBankCode() {
        return subBankCode;
    }

    public void setSubBankCode(String subBankCode) {
        this.subBankCode = subBankCode;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getLimitDesc() {
        return limitDesc;
    }

    public void setLimitDesc(String limitDesc) {
        this.limitDesc = limitDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getWithdrawDesc() {
        return withdrawDesc;
    }

    public void setWithdrawDesc(String withdrawDesc) {
        this.withdrawDesc = withdrawDesc;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public boolean isLockPay() {
        return isLockPay;
    }

    public void setIsLockPay(boolean isLockPay) {
        this.isLockPay = isLockPay;
    }

    public String getLockDesc() {
        return lockDesc;
    }

    public void setLockDesc(String lockDesc) {
        this.lockDesc = lockDesc;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
