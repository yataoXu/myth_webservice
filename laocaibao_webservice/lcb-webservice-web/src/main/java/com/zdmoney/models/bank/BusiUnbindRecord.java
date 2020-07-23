package com.zdmoney.models.bank;

import java.util.Date;
import javax.persistence.*;

@Table(name = "BUSI_UNBIND_RECORD")
public class BusiUnbindRecord {
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    @Column(name = "UB_BANK_CODE")
    private String ubBankCode;

    @Column(name = "UB_TELEPHONE")
    private String ubTelephone;

    @Column(name = "UB_REAL_NAME")
    private String ubRealName;

    @Column(name = "UB_IDNUM")
    private String ubIdnum;

    @Column(name = "OPER_TIME")
    private Date operTime;

    @Column(name = "OPER_MAN")
    private String operMan;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "UN_TYPE")
    private String unType;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return UB_BANK_CODE
     */
    public String getUbBankCode() {
        return ubBankCode;
    }

    /**
     * @param ubBankCode
     */
    public void setUbBankCode(String ubBankCode) {
        this.ubBankCode = ubBankCode;
    }

    /**
     * @return UB_TELEPHONE
     */
    public String getUbTelephone() {
        return ubTelephone;
    }

    /**
     * @param ubTelephone
     */
    public void setUbTelephone(String ubTelephone) {
        this.ubTelephone = ubTelephone;
    }

    /**
     * @return UB_REAL_NAME
     */
    public String getUbRealName() {
        return ubRealName;
    }

    /**
     * @param ubRealName
     */
    public void setUbRealName(String ubRealName) {
        this.ubRealName = ubRealName;
    }

    /**
     * @return UB_IDNUM
     */
    public String getUbIdnum() {
        return ubIdnum;
    }

    /**
     * @param ubIdnum
     */
    public void setUbIdnum(String ubIdnum) {
        this.ubIdnum = ubIdnum;
    }

    /**
     * @return OPER_TIME
     */
    public Date getOperTime() {
        return operTime;
    }

    /**
     * @param operTime
     */
    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    /**
     * @return OPER_MAN
     */
    public String getOperMan() {
        return operMan;
    }

    /**
     * @param operMan
     */
    public void setOperMan(String operMan) {
        this.operMan = operMan;
    }

    /**
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return UN_TYPE
     */
    public String getUnType() {
        return unType;
    }

    /**
     * @param unType
     */
    public void setUnType(String unType) {
        this.unType = unType;
    }
}