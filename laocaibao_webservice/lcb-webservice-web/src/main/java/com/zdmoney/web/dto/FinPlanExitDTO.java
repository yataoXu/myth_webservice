package com.zdmoney.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@Getter
@Setter
public class FinPlanExitDTO {


    private String exitFeePercent;//退出手续费比例

    private BigDecimal exitFeeRateAmt = new BigDecimal(0);//扣除手续费

    private BigDecimal totalAmt = new BigDecimal(0);//扣除手续费后金额

}
