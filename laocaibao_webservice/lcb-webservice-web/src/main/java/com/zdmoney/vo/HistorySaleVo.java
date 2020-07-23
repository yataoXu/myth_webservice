package com.zdmoney.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2016/7/7.
 */
@Getter
@Setter
public class HistorySaleVo {
    private BigDecimal totalOrderAmt;
    private BigDecimal totalInterest;
}
