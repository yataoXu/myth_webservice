package com.zdmoney.models.customer;

import java.util.Date;
import javax.persistence.*;

@Table(name = "CUSTOMER_WELFARE")
public class CustomerWelfare {
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    @Column(name = "CM_NUMBER")
    private String cmNumber;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "WELFARE_NAME")
    private String welfareName;

    @Column(name = "WELFARE_ID")
    private Long welfareId;

    @Column(name = "WELFARE_SERIO_NO")
    private String welfareSerioNo;

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
     * @return CM_NUMBER
     */
    public String getCmNumber() {
        return cmNumber;
    }

    /**
     * @param cmNumber
     */
    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    /**
     * @return CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return WELFARE_NAME
     */
    public String getWelfareName() {
        return welfareName;
    }

    /**
     * @param welfareName
     */
    public void setWelfareName(String welfareName) {
        this.welfareName = welfareName;
    }

    /**
     * @return WELFARE_ID
     */
    public Long getWelfareId() {
        return welfareId;
    }

    /**
     * @param welfareId
     */
    public void setWelfareId(Long welfareId) {
        this.welfareId = welfareId;
    }

    /**
     * @return WELFARE_SERIO_NO
     */
    public String getWelfareSerioNo() {
        return welfareSerioNo;
    }

    /**
     * @param welfareSerioNo
     */
    public void setWelfareSerioNo(String welfareSerioNo) {
        this.welfareSerioNo = welfareSerioNo;
    }
}