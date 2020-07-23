package com.zdmoney.webservice.api.dto.app;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 00225181 on 2016/7/7.
 */
@Getter
@Setter
public class HistorySaleDTO implements Serializable {
    private BigDecimal totalOrderAmt;
    private BigDecimal totalInterest;
    private BigDecimal yestodaySale;
    // 稳健运营天数
    private Integer days;
}
