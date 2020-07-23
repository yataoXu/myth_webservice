package com.zdmoney.models.wdzj;

import lombok.Data;

@Data
public class LoanUser {

    /**
     * 投标人ID
     */
    private String subscribeUserName;

    /**
     * 投标金额
     */
    private Double amount;

    /**
     * 有效金额
     */
    private Double validAmount;

    /**
     * 投标时间
     */
    private String addDate;

    /**
     * 投标状态
     */
    private Integer status;

    /**
     * 标识手动或自动投标
     */
    private Integer type;

    /**
     * 投标来源
     */
    private Integer sourceType;
}
