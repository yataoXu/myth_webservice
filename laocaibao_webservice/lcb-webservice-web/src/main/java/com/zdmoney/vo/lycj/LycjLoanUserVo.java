package com.zdmoney.vo.lycj;

import lombok.Data;

/**
 * Created by Administrator on 2018/12/25 0025.
 */
@Data
public class LycjLoanUserVo {
    /**
     * 标的ID
     */
    private String invest_id;
    /**
     * 标的ID
     */
    private String id;

    /**
     * 标的链接
     */
    private String link;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户ID
     */
    private String userid;
    /**
     * 投标方式
     */
    private String type;
    /**
     * 投标金额
     */
    private Double money;
    /**
     * 有效金额
     */
    private Double account;
    /**
     * 投标状态
     */
    private String status;
    /**
     * 投标时间
     */
    private String add_time;

    /**
     * 投标来源
     */
    private String bid_source;
}
