package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zdmoney.models.order.BusiOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description : 续投订单
 * @ Date : Crreate in 2018/8/29 14:32
 * @Mail : xuyt@zendaimoney.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrderContinuedInitVO extends BusiOrderProductVo {

    private String reinvestName;
    private String rateDiffer;
    /*续期类型*/
    private String reinvestType;
    /*续期天数*/
    private Integer closeDayExt;
    /*续期年化收益率*/
    private BigDecimal reinvestYearRate = new BigDecimal(0);
    /*续期年化收益率*/
    private String reinvestYearRateStr;
    /*续投结息时间*/
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDateExt;
    /*续约预期收益*/
    private BigDecimal continuedInterest;
    /*续约到期本金*/
    private BigDecimal reinvestPrincipalInterest;
    /*在投到期收益*/
    private BigDecimal interest;


}
