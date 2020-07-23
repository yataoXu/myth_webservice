package com.zdmoney.models.product;


import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 子产品（包含所有主产品信息）
 */
@Table(name = "BUSI_PRODUCT_SUB")
@Data
public class BusiProductSub extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = -4485022614860925778L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_PRODUCT_SUB.nextval from dual")
    private Long id;

    private String productName;

    private BigDecimal yearRate;

    private String productDesc;

    private String productFeature;

    private BigDecimal investLower;

    private BigDecimal investUpper;

    private Date interestStartDate;

    private Date interestEndDate;

    private Date saleStartDate;

    private Date saleEndDate;

    private String repayType;

    private String fundArrivalDesc;

    private String riskMeasures;

    private BigDecimal incrementAmount;

    private BigDecimal productPrincipal;

    private String interestRule;

    private String upLowFlag;

    private String topFlag;

    private String hotSellFlag;

    private String auditFlag;

    private String invitationCodeFlag;

    private Integer totalInvestPerson;

    private BigDecimal totalInvestAmt;

    @Column(name = "new_hand")
    private String isNewHand;

    @Column(name = "vm_total_invest_person")
    private Long vmTotalInvestPersonNumber;

    private BigDecimal vmTotalInvestAmt;

    private BigDecimal addInterest;//活动加息

    @Column(name = "INIT_YEAR_RATE")
    private BigDecimal yearRateInit;//初始利率

    @Column(name = "PRODUCT_UEDITOR")
    private String productUeditor;

    private Long limitType;

    private Long contractId;

    private String contractType;

    private String productMemo; //产品备注

    private String marketing;

    private String pcTop;

    private Integer pcSort;

    private String isRecommend;

    private String fundType;

    private String repaySource;

    private String cooperativeDesc;

    private String scurityMode;


    private Date showStartDate;


    private Date showEndDate;

    private String productChannel;

    private BigDecimal productInterest;

    private String transferStatus;

    private Integer transferDay;

    private BigDecimal minRate;

    private BigDecimal maxRate;

    private Integer transferTimes;

    private String isTransfer;

    private String subjectNo;

    @Transient
    private Integer initInvestPeriod;

    private String productType;

    private Date reservatTime;

    private Long ruleId;

    private String isArea;

    private Integer personLoan;

    @Transient
    private String sellOut;

    private String subjectType;

    /**
     * 封闭期
     */
    private Integer closeDay;

    /**
     * 计划金额
     */
    private BigDecimal planAmt;

    /**
     * 最小匹配金额
     */
    private BigDecimal minMatchAmt;

    /**
     * 计划状态
     * 0:未匹配  1:匹配中 2:已匹配 3:匹配失败 4:募集中 5:满标-撮合中
     * 6:兜底满标-撮合中 7:已撮合-起息 8:未满标-待撮合 9:未满标-撮合中 10:已解散 11:已结束
     */
    private Integer planStatus;

    private String goingOnShelf;//是否预约上架：0 否 1 是

    /**
     * 审核日期
     */
    private Date auditDate;

    /**是否兜底* 0-否 1-是*/
    private String isGuarant;

    /**债权编号*/
    private String debtNo;

    /**债权编号*/
    private Long planId;

    @Transient
    private Long countdown;

    /**
     * 目标客户  0：互联网客户 1：理财师客户
     */
    private String targetConsumer;


    private Integer productRank;//产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星

    private String purchaseCondition; //购买条件 A-保守型 B-稳健型 C-平衡型 D-积极型 E-激进型

    private String itemInfo;//项目简介

    private String borrowUse;//借款用途

    private String repaymentGuarantee;//还款保障措施

    private BigDecimal liabilitiesRate;//借款人负债比

    private BigDecimal discountRate; //折扣比例

    private BigDecimal estimatePrice; //债权价值

    private BigDecimal originYearRate;//原始转让产品利率

    private Integer memberLevel;

    private String creditSource;

    /**
     * 是否通知满标 1-已通知 0-未通知
     */
    @Column(name = "IS_NOTIFY")
    private String isNotify;

}