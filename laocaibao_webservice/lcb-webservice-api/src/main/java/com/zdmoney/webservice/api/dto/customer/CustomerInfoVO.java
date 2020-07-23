package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2017/9/5.
 */
public class CustomerInfoVO implements Serializable {
    private Long id;

    private String cmNumber;

    private String cmCellphone;

    private String cmRealName;

    private String cmIdnum;

    private String cmAuthenCount;

    private Date cmBindcardDate;

    private String cmEmployee;

    private Long customerBankAccountId;

    private String openId;

    private String memberType;

    private String accountType;

    private String userLevel;

    private String userLabel;

    private String fuiouLoginId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCmNumber() {
        return cmNumber;
    }

    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    public String getCmCellphone() {
        return cmCellphone;
    }

    public void setCmCellphone(String cmCellphone) {
        this.cmCellphone = cmCellphone;
    }

    public String getCmRealName() {
        return cmRealName;
    }

    public void setCmRealName(String cmRealName) {
        this.cmRealName = cmRealName;
    }

    public String getCmIdnum() {
        return cmIdnum;
    }

    public void setCmIdnum(String cmIdnum) {
        this.cmIdnum = cmIdnum;
    }

    public String getCmAuthenCount() {
        return cmAuthenCount;
    }

    public void setCmAuthenCount(String cmAuthenCount) {
        this.cmAuthenCount = cmAuthenCount;
    }

    public Date getCmBindcardDate() {
        return cmBindcardDate;
    }

    public void setCmBindcardDate(Date cmBindcardDate) {
        this.cmBindcardDate = cmBindcardDate;
    }

    public String getCmEmployee() {
        return cmEmployee;
    }

    public void setCmEmployee(String cmEmployee) {
        this.cmEmployee = cmEmployee;
    }

    public Long getCustomerBankAccountId() {
        return customerBankAccountId;
    }

    public void setCustomerBankAccountId(Long customerBankAccountId) {
        this.customerBankAccountId = customerBankAccountId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMemberType() {
        if ("0".equals(this.userLabel) && "4".equals(this.userLevel)) {
            return "是";
        } else {
            return "后";
        }
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getFuiouLoginId() {
        return fuiouLoginId;
    }

    public void setFuiouLoginId(String fuiouLoginId) {
        this.fuiouLoginId = fuiouLoginId;
    }
}
