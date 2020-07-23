package com.zdmoney.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 00225181 on 2016/4/6.
 */
@Setter
@Getter
public class PaymentVo {
    private List<PaymentCalendar> thisMonthPayment;
    private BigDecimal totalAmt;
    private int totalCount;
    private String month;
}
