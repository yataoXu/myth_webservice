package com.zdmoney.models.customer;

import java.util.Date;

public class CustomerAuthentication {
    private Long id;

    private String cmCellphone;

    private String cmIdnum;

    private Short auStatus;

    private String platform;

    private Date cmInputDate;

    private String realName;

    private String auMsg;

    private String operMan;

    private Integer channelId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCmCellphone() {
        return cmCellphone;
    }

    public void setCmCellphone(String cmCellphone) {
        this.cmCellphone = cmCellphone == null ? null : cmCellphone.trim();
    }

    public String getCmIdnum() {
        return cmIdnum;
    }

    public void setCmIdnum(String cmIdnum) {
        this.cmIdnum = cmIdnum == null ? null : cmIdnum.trim();
    }

    public Short getAuStatus() {
        return auStatus;
    }

    public void setAuStatus(Short auStatus) {
        this.auStatus = auStatus;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public Date getCmInputDate() {
        return cmInputDate;
    }

    public void setCmInputDate(Date cmInputDate) {
        this.cmInputDate = cmInputDate;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getAuMsg() {
        return auMsg;
    }

    public void setAuMsg(String auMsg) {
        this.auMsg = auMsg == null ? null : auMsg.trim();
    }

    public String getOperMan() {
        return operMan;
    }

    public void setOperMan(String operMan) {
        this.operMan = operMan == null ? null : operMan.trim();
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getChannelId() {
        return channelId;
    }
}