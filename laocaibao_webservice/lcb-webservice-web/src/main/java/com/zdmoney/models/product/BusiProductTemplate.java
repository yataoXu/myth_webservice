package com.zdmoney.models.product;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "BUSI_PRODUCT_TEMPLATE")
public class BusiProductTemplate extends AbstractEntity<Long> implements Serializable {
    /**
     * 序列
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_PRODUCT_TEMPLATE.nextval from dual")
    private Long id;

    /**
     * 起息规则
     */
    @Column(name = "INTEREST_RULE")
    private String interestRule;

    /**
     * 投资下限
     */
    @Column(name = "INVEST_LOWER")
    private BigDecimal investLower;

    /**
     * 递增金额
     */
    @Column(name = "INCREMENT_AMOUNT")
    private BigDecimal incrementAmount;

    /**
     * LOAN:债权 SUBJECT:标的
     */
    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    /**
     * 产品系列
     */
    @Column(name = "PRODUCT_TYPE")
    private Long productType;

    /**
     * 产品系列名称
     */
    @Column(name = "PRODUCT_TYPE_NAME")
    private String productTypeName;

    /**
     * 是否需要邀请码
   1:是
    0:否
     */
    @Column(name = "INVITATION_CODE_FLAG")
    private String invitationCodeFlag;

    /**
     * 合作产品： 1-是 0-否
     */
    @Column(name = "IS_TOGETHER")
    private String isTogether;

    /**
     * 产品简介
     */
    @Column(name = "PRODUCT_DESC")
    private String productDesc;

    /**
     * 项目特点
     */
    @Column(name = "PRODUCT_FEATURE")
    private String productFeature;

    /**
     * 合作机构简介
     */
    @Column(name = "COOPERATIVE_DESC")
    private String cooperativeDesc;

    /**
     * 资金到账说明
     */
    @Column(name = "FUND_ARRIVAL_DESC")
    private String fundArrivalDesc;

    /**
     * 风控措施
     */
    @Column(name = "RISK_MEASURES")
    private String riskMeasures;

    /**
     * 还款来源
     */
    @Column(name = "REPAY_SOURCE")
    private String repaySource;

    /**
     * 保障方式’
     */
    @Column(name = "SCURITY_MODE")
    private String scurityMode;

    /**
     * 营销字段
     */
    @Column(name = "MARKETING")
    private String marketing;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 修改时间
     */
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;

    /**
     * 操作人ID
     */
    @Column(name = "USER_ID")
    private Long userId;

    /**
     * 操作人名称
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * 合作机构代码
     */
    @Column(name = "ORG_INVITE_CODE")
    private String orgInviteCode;

    /**
     * 了解智投计划
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 募集期限
     */
    @Column(name = "RAISE_DAY")
    private Integer raiseDay;

    /**
     * 会员等级ID
     */
    @Column(name = "MEMBER_LEVEL")
    private Short memberLevel;

    /**
     * 资产类型
     */
    @Column(name = "FUND_TYPE")
    private String fundType;

    /**
     * 封闭期
     */
    @Column(name = "CLOSE_DAY")
    private Integer closeDay;

    /**
     * 原始协议约定利率
     */
    @Column(name = "INIT_YEAR_RATE")
    private BigDecimal initYearRate;

    /**
     * 还款方式
     */
    @Column(name = "REPAY_TYPE")
    private String repayType;

    /**
     * 加息收益率
     */
    @Column(name = "ADD_INTEREST")
    private BigDecimal addInterest;

    /**
     * 出借上限
     */
    @Column(name = "INVEST_UPPER")
    private BigDecimal investUpper;

    /**
     * 用户类型
     */
    @Column(name = "USER_TYPE")
    private BigDecimal userType;

    /**
     * 产品规则类型
     */
    @Column(name = "RULE_ID")
    private Long ruleId;

    /**
     * 产品类型
     */
    @Column(name = "LIMIT_TYPE")
    private BigDecimal limitType;

    /**
     * 富文本编辑内容
     */
    @Column(name = "PRODUCT_UEDITOR")
    private String productUeditor;

    /**
     * 还款保障措施
     */
    @Column(name = "REPAYMENT_GUARANTEE")
    private String repaymentGuarantee;

}