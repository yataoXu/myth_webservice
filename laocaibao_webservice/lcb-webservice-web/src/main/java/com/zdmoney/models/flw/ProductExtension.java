package com.zdmoney.models.flw;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 16:46
 * @Mail : xuyt@zendaimoney.com
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "extension")
public class ProductExtension {
    private String time_limit;
    private BigDecimal annual_yield;
    private BigDecimal coupon;
    private BigDecimal raise_interest;

    public void setAnnual_yield(BigDecimal annual_yield) {
        this.annual_yield = annual_yield.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public void setCoupon(BigDecimal coupon) {
        if (coupon.equals(BigDecimal.ZERO)){
             this.coupon = coupon;
             return;
        }
        this.coupon = coupon.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


}
