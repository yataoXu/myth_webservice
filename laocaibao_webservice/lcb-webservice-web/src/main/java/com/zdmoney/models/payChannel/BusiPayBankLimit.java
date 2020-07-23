package com.zdmoney.models.payChannel;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "BUSI_BANK_LIMIT")
public class BusiPayBankLimit {
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "CARD_TYPE")
    private String cardType;

    @Column(name = "SINGLE_AMT")
    private BigDecimal singleAmt;

    @Column(name = "DAY_AMT")
    private BigDecimal dayAmt;

    @Column(name = "MONTH_AMT")
    private BigDecimal monthAmt;

    @Column(name = "BANK_STATUS")
    private String bankStatus;

    @Column(name = "PAY_CHANNEL")
    private String payChannel;

    @Column(name = "CODE")
    private String code;

    @Column(name = "OPER_DATE")
    private Date operDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "LL_BANK_CODE")
    private String llBankCode;

    @Column(name="HR_BANK_NAME")
    private String hrBankName;

    @Column(name="LL_RECHARGE")
    private String llRecharge;

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
     * @return BANK_CODE
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * @return BANK_NAME
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return CARD_TYPE
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * @param cardType
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * @return SINGLE_AMT
     */
    public BigDecimal getSingleAmt() {
        return singleAmt;
    }

    /**
     * @param singleAmt
     */
    public void setSingleAmt(BigDecimal singleAmt) {
        this.singleAmt = singleAmt;
    }

    /**
     * @return DAY_AMT
     */
    public BigDecimal getDayAmt() {
        return dayAmt;
    }

    /**
     * @param dayAmt
     */
    public void setDayAmt(BigDecimal dayAmt) {
        this.dayAmt = dayAmt;
    }

    /**
     * @return MONTH_AMT
     */
    public BigDecimal getMonthAmt() {
        return monthAmt;
    }

    /**
     * @param monthAmt
     */
    public void setMonthAmt(BigDecimal monthAmt) {
        this.monthAmt = monthAmt;
    }

    /**
     * @return BANK_STATUS
     */
    public String getBankStatus() {
        return bankStatus;
    }

    /**
     * @param bankStatus
     */
    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    /**
     * @return PAY_CHANNEL
     */
    public String getPayChannel() {
        return payChannel;
    }

    /**
     * @param payChannel
     */
    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    /**
     * @return CODE
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return OPER_DATE
     */
    public Date getOperDate() {
        return operDate;
    }

    /**
     * @param operDate
     */
    public void setOperDate(Date operDate) {
        this.operDate = operDate;
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

    public String getLlBankCode() {
        return llBankCode;
    }

    public void setLlBankCode(String llBankCode) {
        this.llBankCode = llBankCode;
    }

    public String getHrBankName() {
        return hrBankName;
    }

    public void setHrBankName(String hrBankName) {
        this.hrBankName = hrBankName;
    }

    public String getLlRecharge() {
        return llRecharge;
    }

    public void setLlRecharge(String llRecharge) {
        this.llRecharge = llRecharge;
    }
}