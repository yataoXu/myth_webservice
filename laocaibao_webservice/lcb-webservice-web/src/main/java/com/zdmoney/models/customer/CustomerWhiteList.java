package com.zdmoney.models.customer;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "CUSTOMER_WHITE_LIST")
public class CustomerWhiteList {
    @Id
    @SequenceGenerator(name="",sequenceName="Oracle")
    @Column(name = "CUSTOMER_MOBILE")
    private String customerMobile;

    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;

    /**
     * @return CUSTOMER_MOBILE
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
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
}