package com.zdmoney.webservice.api.dto.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by user on 2017/11/28.
 */
public class TransferEstimatedPricesVo implements Serializable{
    private Date date;

    private BigDecimal estimatePrice;

    public TransferEstimatedPricesVo(){}

    public TransferEstimatedPricesVo(Date date, BigDecimal estimatePrice) {
        this.date = date;
        this.estimatePrice = estimatePrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(BigDecimal estimatePrice) {
        this.estimatePrice = estimatePrice;
    }
}
