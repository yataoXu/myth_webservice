package com.zdmoney.models.sys;

import java.util.Date;

public class SysHttpMessage {
    private Long id;

    private String sendmessage;

    private String handlermessage;

    private String temp1;

    private String temp2;

    private Date createDate;

    private String orderNum;

    private String strType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSendmessage() {
        return sendmessage;
    }

    public void setSendmessage(String sendmessage) {
        this.sendmessage = sendmessage == null ? null : sendmessage.trim();
    }

    public String getHandlermessage() {
        return handlermessage;
    }

    public void setHandlermessage(String handlermessage) {
        this.handlermessage = handlermessage == null ? null : handlermessage.trim();
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1 == null ? null : temp1.trim();
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2 == null ? null : temp2.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum == null ? null : orderNum.trim();
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType == null ? null : strType.trim();
    }
}