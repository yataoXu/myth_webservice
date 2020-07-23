package com.zdmoney.models.customer;

import java.util.Date;

public class CustomerDetailInfo {
    private Long id;

    private Long customerId;

    private String cdEmail;

    private String cdNick;

    private Long cdInputId;

    private Date cdInputDate;

    private String cdModifyId;

    private Date cdModifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCdEmail() {
        return cdEmail;
    }

    public void setCdEmail(String cdEmail) {
        this.cdEmail = cdEmail == null ? null : cdEmail.trim();
    }

    public String getCdNick() {
        return cdNick;
    }

    public void setCdNick(String cdNick) {
        this.cdNick = cdNick == null ? null : cdNick.trim();
    }

    public Long getCdInputId() {
        return cdInputId;
    }

    public void setCdInputId(Long cdInputId) {
        this.cdInputId = cdInputId;
    }

    public Date getCdInputDate() {
        return cdInputDate;
    }

    public void setCdInputDate(Date cdInputDate) {
        this.cdInputDate = cdInputDate;
    }

    public String getCdModifyId() {
        return cdModifyId;
    }

    public void setCdModifyId(String cdModifyId) {
        this.cdModifyId = cdModifyId == null ? null : cdModifyId.trim();
    }

    public Date getCdModifyDate() {
        return cdModifyDate;
    }

    public void setCdModifyDate(Date cdModifyDate) {
        this.cdModifyDate = cdModifyDate;
    }
}