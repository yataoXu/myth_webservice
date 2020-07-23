package com.zdmoney.web.dto;

/**
 * Created by 00225181 on 2015/12/23.
 */
public class BusiPayBankDTO {
    private String bankCode;

    private String bankName;

    private String limitMsg;

    private String remark="";

    private String llRecharge;//是否支持连连充值 0：是 1：否

    public String getLlRecharge() {
        return llRecharge;
    }

    public void setLlRecharge(String llRecharge) {
        this.llRecharge = llRecharge;
    }

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

    public String getLimitMsg() {
        return limitMsg;
    }

    public void setLimitMsg(String limitMsg) {
        this.limitMsg = limitMsg;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
