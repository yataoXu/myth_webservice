package com.zdmoney.web.dto;

/**
 * Created by 00225181 on 2015/11/23.
 */
public class InviteFriendDto {
    private String sourceType;
    private String sourceName;
    private String mobile;
    private String actionTimeVal;

    public String getActionTimeVal() {
        return actionTimeVal;
    }

    public void setActionTimeVal(String actionTimeVal) {
        this.actionTimeVal = actionTimeVal;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
