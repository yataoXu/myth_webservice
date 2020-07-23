package com.zdmoney.webservice.api.dto.plan;

import com.zdmoney.secure.utils.ThreeDesUtil;

import java.io.Serializable;

/**
 * Created by 00232384 on 2017/6/13.
 */
public class SpecialFinancialPlannerVO implements Serializable{

    /**
     * 用户ID
     */
    private Long customerId;

    /**
     * 用户编号
     */
    private String cmNumber;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String idNum;

    /**
     * 手机号
     */
    private String cellPhone;

    /**
     * 银行卡号
     */
    private String bankAccount;

    /**
     * 银行编号
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行联行号
     */
    private String subbankCode;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCmNumber() {
        return cmNumber;
    }

    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = ThreeDesUtil.decryptMode(idNum);
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = ThreeDesUtil.decryptMode(cellPhone);
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = ThreeDesUtil.decryptMode(bankAccount);
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSubbankCode() {
        return subbankCode;
    }

    public void setSubbankCode(String subbankCode) {
        this.subbankCode = subbankCode;
    }
}
