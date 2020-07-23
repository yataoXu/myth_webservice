package com.zdmoney.web.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

public class CustomerAuthorizeDTO implements Serializable {

    @JSONField(format = "yyyy-MM-dd")
    private Date settleDate;

    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;

    // 0: 可授权  1: 不可授权
    private int status;

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        int flag = new Date().compareTo(settleDate);
        // 交割日当天或之后不可授权
        return flag >= 0 ? 1 : 0;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
