package com.zdmoney.models.customer;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Gosling on 2016/12/7.
 */
@Data
public class CustomerSignRule implements Serializable {

    private Integer awardType;
    /**
     * 签到天数
     */
    private Integer signDays;

    /**
     * 最大红包金额
     */
    private Integer maxMoney;

    /**
     * 最小红包金额
     */
    private Integer minMoney;

    /**
     * 最大加息率
     */
    private Float maxRate;

    /**
     * 最小加息率
     */
    private Float minRate;

    /**
     * 投资周期
     */
    private Integer investPeriod;

    /**
     * 有效天数(红包,加息券)
     */
    private Integer period;

    /**
     * 有效天数(预约券)
     */
    private Integer validDays;

    /**
     * 投资金额
     */
    private Integer investAmount;

    /**
     * 加息率
     */
    private BigDecimal rate;

    /**
     * 红包发放来源
     */
    private String source;

    /**
     * 加息天数
     */
    private Integer days;

    /**
     * 积分数值
     */
    private Integer integralNum;
}
