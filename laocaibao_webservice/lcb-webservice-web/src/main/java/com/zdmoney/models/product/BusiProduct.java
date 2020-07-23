package com.zdmoney.models.product;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

@Table(name = "BUSI_PRODUCT")
@Getter
@Setter
public class BusiProduct extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 2337937881709830076L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SQ_BUSI_PRODUCT.nextval from dual")
    private Long id;

    private String productName;

    protected BigDecimal yearRate;

    private String productDesc;

    private String productFeature;

    private BigDecimal investLower;

    private BigDecimal investUpper;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;

    //@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleStartDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

    @Column(name="new_hand")
    private String isNewHand;

    @Column(name="vm_total_invest_person")
    private Long vmTotalInvestPersonNumber;

    private BigDecimal vmTotalInvestAmt;

    protected BigDecimal addInterest;//活动加息

    @Column(name="INIT_YEAR_RATE")
    private BigDecimal yearRateInit;//初始利率

    @Column(name = "PRODUCT_UEDITOR")
    protected String productUeditor;

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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date showStartDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
    protected String sellOut;

    private String subjectType;

    private String goingOnShelf;//是否预约上架：0 否 1 是

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

    private Date auditDate;

    @Transient
    protected Long countdown;

    private String remark;

    @Transient
    private Integer collectPeriod;//募集期限

    private Integer productRank;//产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星

    private String purchaseCondition; //购买条件 A-保守型 B-稳健型 C-平衡型 D-积极型 E-激进型

    private String itemInfo;//项目简介

    private String borrowUse;//借款用途

    private String repaymentGuarantee;//还款保障措施

    private BigDecimal liabilitiesRate;//借款人负债比

    /**
     * 目标客户  0：互联网客户 1：理财师客户
     */
    private String targetConsumer;

    @Transient
    protected String welfare;//可用福利

    private BigDecimal discountRate; //折扣比例

    private BigDecimal estimatePrice; //债权价值

    private BigDecimal originYearRate;//原始转让产品利率

    private Integer memberLevel;
    
    private String creditSource;


    public BigDecimal getResidualAmount(){
        if(productPrincipal.compareTo(totalInvestAmt) == -1){
            return new BigDecimal(0);
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return new BigDecimal(df.format(productPrincipal.subtract(totalInvestAmt)));
    }
}