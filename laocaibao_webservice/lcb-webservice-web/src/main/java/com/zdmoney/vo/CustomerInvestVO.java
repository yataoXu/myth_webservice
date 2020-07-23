package com.zdmoney.vo;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2016/2/26.
 */
public class CustomerInvestVO {
    private Long investPeriod;
    private BigDecimal orderAmt;

    public Long getInvestPeriod() {
        return investPeriod;
    }

    public void setInvestPeriod(Long investPeriod) {
        this.investPeriod = investPeriod;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }
}
