package com.zdmoney.models;

import com.zdmoney.common.handler.SecurityFieldTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "AT_CH")
public class AtCh {
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Integer id;

    @Column(name = "CJBH")
    private String cjbh;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "LENDER_NAME")
    private String lenderName;

    @Column(name = "LENDER_IDNO")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String lenderIdno;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "ORDER_ID")
    private BigDecimal orderId;

    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;

    @Column(name = "CM_REAL_NAME")
    private String cmRealName;

    @Column(name = "INVEST_AMOUNT")
    private BigDecimal investAmount;

    @Column(name = "YT")
    private String yt;

    @Column(name = "CH_AMOUNT")
    private BigDecimal chAmount;


    /**
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return CJBH
     */
    public String getCjbh() {
        return cjbh;
    }

    /**
     * @param cjbh
     */
    public void setCjbh(String cjbh) {
        this.cjbh = cjbh;
    }

    /**
     * @return PRODUCT_NAME
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return LENDER_NAME
     */
    public String getLenderName() {
        return lenderName;
    }

    /**
     * @param lenderName
     */
    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    /**
     * @return LENDER_IDNO
     */
    public String getLenderIdno() {
        return lenderIdno;
    }

    /**
     * @param lenderIdno
     */
    public void setLenderIdno(String lenderIdno) {
        this.lenderIdno = lenderIdno;
    }

    /**
     * @return AMOUNT
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return ORDER_ID
     */
    public BigDecimal getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(BigDecimal orderId) {
        this.orderId = orderId;
    }

    /**
     * @return CUSTOMER_ID
     */
    public BigDecimal getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    /**
     * @return CM_REAL_NAME
     */
    public String getCmRealName() {
        return cmRealName;
    }

    /**
     * @param cmRealName
     */
    public void setCmRealName(String cmRealName) {
        this.cmRealName = cmRealName;
    }

    /**
     * @return INVEST_AMOUNT
     */
    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    /**
     * @param investAmount
     */
    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    /**
     * @return YT
     */
    public String getYt() {
        return yt;
    }

    /**
     * @param yt
     */
    public void setYt(String yt) {
        this.yt = yt;
    }

    public BigDecimal getChAmount() {
        return chAmount;
    }

    public void setChAmount(BigDecimal chAmount) {
        this.chAmount = chAmount;
    }
}