package com.zdmoney.models.order;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "BUSI_ORDER_EXCHANGE")
public class BusiOrderExchange {
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    @Column(name = "ORDER_NUM")
    private String orderNum;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ORDER_AMT")
    private BigDecimal orderAmt;

    @Column(name = "AMT_TYPE")
    private String amtType;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "ORDER_TIME")
    private Date orderTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SUCCESS_TIME")
    private Date successTime;

    @Column(name = "PAY_SER_NUM")
    private String paySerNum;

    @Column(name = "EXCHANGE_SER_NUM")
    private String exchangeSerNum;

    @Column(name = "PRODUCT_NUM")
    private String productNum;

    @Column(name = "RETURN_SER_NUM")
    private String returnSerNum;

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
     * @return ORDER_NUM
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * @param orderNum
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return CUSTOMER_ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return ORDER_AMT
     */
    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    /**
     * @param orderAmt
     */
    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    /**
     * @return AMT_TYPE
     */
    public String getAmtType() {
        return amtType;
    }

    /**
     * @param amtType
     */
    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }

    /**
     * @return PRODUCT_ID
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return PRODUCT_TYPE
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return ORDER_TIME
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * @param orderTime
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return SUCCESS_TIME
     */
    public Date getSuccessTime() {
        return successTime;
    }

    /**
     * @param successTime
     */
    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    /**
     * @return PAY_SER_NUM
     */
    public String getPaySerNum() {
        return paySerNum;
    }

    /**
     * @param paySerNum
     */
    public void setPaySerNum(String paySerNum) {
        this.paySerNum = paySerNum;
    }

    /**
     * @return EXCHANGE_SER_NUM
     */
    public String getExchangeSerNum() {
        return exchangeSerNum;
    }

    /**
     * @param exchangeSerNum
     */
    public void setExchangeSerNum(String exchangeSerNum) {
        this.exchangeSerNum = exchangeSerNum;
    }

    /**
     * @return PRODUCT_NUM
     */
    public String getProductNum() {
        return productNum;
    }

    /**
     * @param productNum
     */
    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getReturnSerNum() {
        return returnSerNum;
    }

    public void setReturnSerNum(String returnSerNum) {
        this.returnSerNum = returnSerNum;
    }
}