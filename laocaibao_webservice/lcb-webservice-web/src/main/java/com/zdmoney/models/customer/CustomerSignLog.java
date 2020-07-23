package com.zdmoney.models.customer;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Gosling on 2016/12/7.
 */
@Data
public class CustomerSignLog implements Serializable {

    private Long id;

    private Long customerId;

    private long days;

    private Integer coin;

    private BigDecimal rateCoupon;

    private Integer redPacketMoney;

    private Integer appointCoupon;

    private Integer integral;

    private List<CustomerSignInfo> signDateList;
}
