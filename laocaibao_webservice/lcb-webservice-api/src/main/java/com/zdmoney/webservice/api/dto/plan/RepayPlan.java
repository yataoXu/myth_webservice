package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RepayPlan implements Serializable {

    private BigDecimal repayAmount;

    private Long repayTime;

    private Integer period;
}
