package com.zdmoney.vo;

import java.math.BigDecimal;

/**
 * Created by jb sun on 2016/2/23.
 */
public class BusiAddupCustomerOrderVo {
    private String customerNo;
    private String yearMonth;
    private BigDecimal orderAmt;
    private BigDecimal orderNum;

    public String getCustomerNo() {
        return customerNo;
    }

    public BusiAddupCustomerOrderVo setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
        return this;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public BusiAddupCustomerOrderVo setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
        return this;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public BusiAddupCustomerOrderVo setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
        return this;
    }

    public BigDecimal getOrderNum() {
        return orderNum;
    }

    public BusiAddupCustomerOrderVo setOrderNum(BigDecimal orderNum) {
        this.orderNum = orderNum;
        return this;
    }
}
