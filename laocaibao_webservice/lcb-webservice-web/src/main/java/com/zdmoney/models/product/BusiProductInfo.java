package com.zdmoney.models.product;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00250968 on 2017/9/4
 **/
@Data
public class BusiProductInfo implements Serializable {

    /**
     * 产品ID
     */
    private Long id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 年化收益率
     */
    protected BigDecimal yearRateInit;

    /**
     * 投资期限
     */
    private Long investPeriod;

    /**
     * 还款方式
     * 0: 等额本息
     * 1: 一次性还本付息
     * 2: 先息后本
     */
    private String repayType;

    /**
     * 剩余可投金额
     */
    private String remaindAmt;

    /**
     * 产品标签
     *
     * 0: 普通产品
     * 1: 限购产品
     * 2: 新手标
     * 3: 微信专享
     * 4: 渠道产品
     * 5: 预约产品
     * 6: 转让产品
     * 7: 专区产品
     */
    private int productTag;

    /**
     * 加息利率
     */
    private BigDecimal addInterest;

    /**
     * 是否推荐
     * 1: 是
     * 0: 否
     */
    private String isRecommend;

    /**
     * 产品类型
     *
     * 0: 所有
     * 1,2: 定期
     * 3: 个贷
     * 4: 理财计划
     */
    private int subjectType;

    /**
     * 售罄标识
     *
     * 0: 未售罄
     * 1: 已售罄
     * 2: 未起售
     * 3: 已结售
     */
    private int sellOut;

    /**
     * 推荐专区
     */
    private int isArea;

    /**
     * 预约显示时间
     */
    private Date showStartDate;

    /**
     * 预约结束时间
     */
    private Date showEndDate;

    /**
     * 营销字段
     */
    private String marketing;

    /**
     * 产品详情URL
     */
    private String productDetailUrl;

    /**
     * 转让状态
     *
     * 0: 不可转让(默认)
     * 1: 可转让
     */
    private Long transferStatus;

    /**
     * 起售日
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleStartDate;

    /**
     * 结售日
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date saleEndDate;

    /**
     * 投资上限
     */
    private BigDecimal investUpper;

    /**
     * 投资上限
     */
    private BigDecimal investLower;

    /**
     * 是否是转让产品
     * 0: 固收产品
     * 1: 转让产品
     */
    private int isTransfer;

    /**
     * 项目本金
     */
    private BigDecimal productPrincipal;

    /**
     * 项目利息
     */
    private BigDecimal productInterest;

    /**
     * 起息日
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;

    /**
     * 结息日
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;

    /**
     * 封闭期(理财计划)
     */
    @JSONField(serialize = false)
    private Long closeDay;

    /**
     * 预约截至时间
     */
    @JSONField(serialize = false)
    private Date reservatTime;

    /**
     * 剩余秒数
     */
    @JSONField(serialize = false)
    private Long countdown;

    /**
     * 投资总金额
     */
    @JSONField(serialize = false)
    private BigDecimal totalInvestAmt;

    /**
     * 新手标
     */
    private String newHand;

    /**
     * 会员等级
     */
    private Integer memberLevel;

    /** 转让原始利率 */
    private String originYearRate;

    /** 转让后年利率 */
    private String yearRate;
}
