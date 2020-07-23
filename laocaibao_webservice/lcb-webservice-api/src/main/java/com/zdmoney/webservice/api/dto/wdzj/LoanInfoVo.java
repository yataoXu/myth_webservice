package com.zdmoney.webservice.api.dto.wdzj;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoanInfoVo implements Serializable {

    private static final long serialVersionUID = 3205463233949057914L;
    /**
     * 项目主键(唯一)
     */
    private String projectId;

    /**
     * 借款标题
     */
    private String title;

    /**
     * 借款金额(若标未满截标，以投标总额为准)
     */
    private Double amount;

    /**
     * 进度
     */
    private String schedule;

    /**
     * 利率
     */
    private String interestRate;

    /**
     * 借款期限
     */
    private Integer deadline;

    /**
     * 期限单位
     */
    private String deadlineUnit;

    /**
     * 奖励
     */
    private Double reward;

    /**
     * 投标类型
     */
    private String type;

    /**
     * 还款方式
     */
    private Integer repaymentType;

    /**
     * 投资人数据（具体字段看下面的投标列表信息）
     */
    private List<LoanUserVo> subscribes;

    /**
     * 借款人所在省份
     */
    private String province = "";

    /**
     * 借款人所在城市
     */
    private String city = "";

    /**
     * 借款人ID
     */
    private String userName;

    /**
     * 发标人头像的URL
     */
    private String userAvatarUrl = "";

    /**
     * 借款用途
     */
    private String amountUsedDesc = "";

    /**
     * 营收
     */
    private Double revenue = 0.0d;

    /**
     * 标的详细页面地址链接
     */
    private String loanUrl;

    /**
     * 标的成功时间
     */
    private String successTime;

    /**
     * 发标时间
     */
    private String publishTime = "";

    /**
     * 标所属平台频道板块
     */
    private String plateType = "";

    /**
     * 保障担保机构名称
     */
    private String guarantorsType = "";

    /**
     * 是否机构借款
     */
    private Integer isAgency = 0;

    private Integer dataType = 0;

    private Integer transferType;

    /**
     * 原始标ID
     */
    private String planId;
}
