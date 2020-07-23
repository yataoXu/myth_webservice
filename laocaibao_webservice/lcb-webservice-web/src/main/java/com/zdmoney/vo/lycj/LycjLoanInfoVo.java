package com.zdmoney.vo.lycj;

import lombok.Data;

/**
 * Created by Administrator on 2018/12/25 0025.
 */
@Data
public class LycjLoanInfoVo {

    /**
     * 标的ID
     */
    private String id;

    /**
     * 标的链接
     */
    private String link;

    /**
     * 标的名称
     */
    private String title;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 标的金额
     */
    private Double amount;
    /**
     * 借款期限
     */
    private String borrow_period;
    /**
     * 利率
     */
    private Double interest;
    /**
     * 资产类型
     */
    private String asset_type;
    /**
     * 借款类型
     */
    private String borrow_type;
    /**
     * 产品类型
     */
    private String product_type;
    /**
     * 还款方式
     */
    private String repay_type;
    /**
     * 完成百分比
     */
    private Double percentage;
    /**
     * 标状态
     */
    private String bid_state;
    /**
     * 投标奖励
     */
    private Double reward;

    /**
     * 担保奖励
     */
    private Double guarantee;

    /**
     * 信用等级
     */
    private String credit;
    /**
     * 发标时间
     */
    private String verify_time;
    /**
     * 成功时间
     */
    private String reverify_time;

    /**
     * 投资次数
     */
    private Integer invest_count;

    /**
     * 借款详情
     */
    private String borrow_detail;

    /**
     * 借款用途
     */
    private String attribute1;
    /**
     * 扩展字段
     */
    private String attribute2;
    /**
     * 扩展字段
     */
    private String attribute3;
}
