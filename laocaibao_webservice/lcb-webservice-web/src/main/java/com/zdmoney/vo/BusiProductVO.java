package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.utils.CoreUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BusiProductVO extends BusiProduct implements Serializable {

    private static final long serialVersionUID = 6977402643848374753L;

    private BigDecimal remaindAmt;
    private BigDecimal nowProportion;
    private int investPeriod;
    private BigDecimal remaindTime;
    private BigDecimal remaindSaleStartTime;
    private String isTogether;
    private String agreementUrl;
    private String productDetailUrl;
    private String imgUrl;
    private String contractTypeDict;
    private String contractDesc;
    private String fundTypeName;
    private String isAppoint;
    private String isNewRule;
    private BigDecimal leftReservatTime;
    private Long internalContractId;

    private int productTipStatus;//产品列表图标状态

    //********转让产品字段*************
    private int surplusInvestPeriod;//剩余投资期限
    private BigDecimal transferPrice = new BigDecimal(0);//转让金额
    @JSONField(format = "yyyy-MM-dd")
    private Date transferDate;//转让日期

    @JSONField(format = "yyyy-MM-dd")
    private Date transferEndDate;//到期日期
    private String initProductId;//原产品id
    private String initProductUrl;//原产品连接
    private Integer transferDay;//转让天数
    private Integer memberLevel;

    //内部员工最大可买金额
    private Integer staffMaxAmt;

    @Override
    public Integer getTransferDay() {
        return transferDay;
    }

    @Override
    public void setTransferDay(Integer transferDay) {
        this.transferDay = transferDay;
    }

    public int getProductTipStatus() {
        return productTipStatus;
    }

    public void setProductTipStatus(int productTipStatus) {
        this.productTipStatus = productTipStatus;
    }

    public int getSurplusInvestPeriod() {
        return surplusInvestPeriod;
    }

    public void setSurplusInvestPeriod(int surplusInvestPeriod) {
        this.surplusInvestPeriod = surplusInvestPeriod;
    }

    public BigDecimal getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(BigDecimal transferPrice) {
        this.transferPrice = transferPrice;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Date getTransferEndDate() {
        return transferEndDate;
    }

    public void setTransferEndDate(Date transferEndDate) {
        this.transferEndDate = transferEndDate;
    }

    public String getInitProductId() {
        return initProductId;
    }

    public void setInitProductId(String initProductId) {
        this.initProductId = initProductId;
    }

    public String getInitProductUrl() {
        return initProductUrl;
    }

    public void setInitProductUrl(String initProductUrl) {
        this.initProductUrl = initProductUrl;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public BigDecimal getRemaindAmt() {
        if (remaindAmt == null) {
            return remaindAmt;
        }
        return new BigDecimal(CoreUtil.BigDecimalAccurate(this.remaindAmt, 2));
    }

    public void setRemaindAmt(BigDecimal remaindAmt) {
        this.remaindAmt = remaindAmt;
    }

    public int getInvestPeriod() {
        return investPeriod;
    }

    public void setInvestPeriod(int investPeriod) {
        this.investPeriod = investPeriod;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getSellOut() {
        return sellOut;
    }

    public void setSellOut(String sellOut) {
        this.sellOut = sellOut;
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

    public BigDecimal getNowProportion() {
        if (nowProportion == null) {
            return null;
        }
        return nowProportion.setScale(0, BigDecimal.ROUND_DOWN);
    }

    public void setNowProportion(BigDecimal nowProportion) {
        this.nowProportion = nowProportion;
    }

    public BigDecimal getYearRateProportion() {
        if (yearRate == null) {
            return null;
        }
        BigDecimal big100 = new BigDecimal(100);
        return yearRate.multiply(big100);
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public BigDecimal getRemaindSaleStartTime() {
        return remaindSaleStartTime;
    }

    public void setRemaindSaleStartTime(BigDecimal remaindSaleStartTime) {
        this.remaindSaleStartTime = remaindSaleStartTime;
    }

    public BigDecimal getAddInterest() {
        return addInterest;
    }

    public void setAddInterest(BigDecimal addInterest) {
        this.addInterest = addInterest;
    }

    public String getIsTogether() {
        return isTogether;
    }

    public void setIsTogether(String isTogether) {
        this.isTogether = isTogether;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }


    public String getProductDetailUrl() {
        return productDetailUrl;
    }

    public void setProductDetailUrl(String productDetailUrl) {
        this.productDetailUrl = productDetailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContractDesc() {
        return contractDesc;
    }

    public void setContractDesc(String contractDesc) {
        this.contractDesc = contractDesc;
    }

    public String getContractTypeDict() {
        return contractTypeDict;
    }

    public void setContractTypeDict(String contractTypeDict) {
        this.contractTypeDict = contractTypeDict;
    }

    public String getProductUeditor() {
        return productUeditor;
    }

    public void setProductUeditor(String productUeditor) {
        this.productUeditor = productUeditor;
    }




    public String getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(String isAppoint) {
        this.isAppoint = isAppoint;
    }


    public String getIsNewRule() {
        return isNewRule;
    }

    public void setIsNewRule(String isNewRule) {
        this.isNewRule = isNewRule;
    }

    public BigDecimal getLeftReservatTime() {
        return leftReservatTime;
    }

    public void setLeftReservatTime(BigDecimal leftReservatTime) {
        this.leftReservatTime = leftReservatTime;
    }

    public Long getInternalContractId() {
        return internalContractId;
    }

    public void setInternalContractId(Long internalContractId) {
        this.internalContractId = internalContractId;
    }

    public Long getCountdown() {
        Date a = this.getReservatTime();
        Date b = this.getSaleStartDate();
        Date c = new Date();
        Date d = this.getSaleEndDate();

        BigDecimal principal = this.getProductPrincipal();// 项目本金
        BigDecimal investAmt = this.getTotalInvestAmt();// 投资总金额

        if (a != null && b != null) {
            // 预约产品，起售时间>=当前时间>=预约购买时间 >=结售时间，投资总金额=项目本金， 不显示倒计时
            if (b.getTime() >= c.getTime() || c.getTime() >= a.getTime() || a.getTime() >= d.getTime() || principal.compareTo(investAmt) == 0) {
                return 0L;
            } else {
                Long surplus = (a.getTime() - c.getTime()) / 1000;
                return surplus <= 0 ? 0 : surplus;
            }
        }
        return 0L;
    }

    public void setCountdown(Long countdown) {
        super.countdown = countdown;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Integer getStaffMaxAmt() {
        return staffMaxAmt;
    }

    public void setStaffMaxAmt(Integer staffMaxAmt) {
        this.staffMaxAmt = staffMaxAmt;
    }
}
