package com.zdmoney.models.product;/**
 * Created by pc05 on 2017/9/11.
 */

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.utils.CoreUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-09-11 16:44
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class BusiProductInit implements Serializable {

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
    protected BigDecimal yearRate;

    /**
     * 投资期限
     */
    private int investPeriod;

    /**
     * 剩余可投金额
     */
    private BigDecimal remaindAmt;

    private String isTransfer;

    /**
     * 售罄标识
     *
     * 0: 未售罄
     * 1: 已售罄
     * 2: 未起售
     * 3: 已结售
     */
    private int sellOut;

    private Long limitType;

    private BigDecimal productPrincipal;//项目本金

    private BigDecimal totalInvestAmt; //投资总金额

    private BigDecimal remaindTime;// 距离结束时间

    private BigDecimal remaindSaleStartTime; // 距离开售时间

    private Date reservatTime;//预约时间（预约产品)

    private String subjectType;

    private String limitName;

    private BigDecimal addInterest;//加息

    private int productTipStatus;//产品列表图标状态

    private String productDetailUrl;

    private BigDecimal investLower;//投资下线

    private BigDecimal investUpper;//投资上线

    private Integer totalInvestPerson;//总投资人数

    private BigDecimal productInterest;

    @Column(name="INIT_YEAR_RATE")
    private BigDecimal yearRateInit;//初始利率


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;// 起息日

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;// 到期日

    private Date saleStartDate;// 起售日

    private BigDecimal nowProportion;

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
     */
    private int productTag;

    /**
     * 原产品利率
     */
    private String originYearRate;

    public BigDecimal getNowProportion() {
        if (nowProportion == null) {
            return null;
        }
        return nowProportion.setScale(0, BigDecimal.ROUND_DOWN);
    }

    public void setNowProportion(BigDecimal nowProportion) {
        this.nowProportion = nowProportion;
    }

    public BigDecimal getRemaindTime() {
        if (remaindTime == null) {
            return null;
        }
        return new BigDecimal(CoreUtil.BigDecimalAccurate(this.remaindTime, 0));
    }

    public void setRemaindTime(BigDecimal remaindTime) {
        this.remaindTime = remaindTime;
    }

    public String getProductType(){
        if(productTipStatus == 0L || productTipStatus == 6L){//普通产品和渠道产品没有一级标签
            if(StringUtils.equals(subjectType,"1")||StringUtils.equals(subjectType,"2")){
                return "定期";
            }
            if(StringUtils.equals(subjectType,"3")){
                return "散标";
            }
            if(StringUtils.equals(subjectType,"4")){
                return "智投宝";
            }
        }
        if(productTipStatus == 2L){
            return "新手专享";
        }
        //if(productTipStatus == 6L){
        //    return "可转让";
        //}
        return limitName;
    }
}
