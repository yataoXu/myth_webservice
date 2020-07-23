package com.zdmoney.web.dto.mall;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2016/2/26.
 */
@Getter
@Setter
public class MonthTaskDTO extends TaskCommonDTO {
    private BigDecimal investAmt=new BigDecimal(0);//已投金额
    private BigDecimal taskInvestAmt=new BigDecimal(0);//任务金额
}
