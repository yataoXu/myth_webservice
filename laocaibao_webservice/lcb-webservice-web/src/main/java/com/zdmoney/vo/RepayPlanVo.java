package com.zdmoney.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * Created by silence.cheng on 2019/1/09.
 */
@Data
public class RepayPlanVo {

    private BigDecimal totalInitPrincipal;//上家用户待收本金

    private BigDecimal totalInitInterest;//上家用户待收利息

}
