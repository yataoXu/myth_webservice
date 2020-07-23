package com.zdmoney.vo.lycj;


import lombok.Data;

@Data
public class LycjPrepaymentVo {
    /**
     * 标的ID
     */
    private String repay_id;
    /**
     * 标的ID
     */
    private String id;
    /**
     * 提前还款日期
     */
    private String advanced_time;
    /**
     * 实际借款天数
     */
    private String actual_period;
    /**
     * 提前还款总额
     */
    private Long advanced_amount;
}
