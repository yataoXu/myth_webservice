package com.zdmoney.web.dto;

import java.math.BigDecimal;

/**
 * Created by zhou on 2016/2/24.
 */
public class VoucherResultDto {
    private BigDecimal rate = new BigDecimal("0"); //加息比例
    private Integer days=0; //加息天数
    private Integer period=0; //有效天数
    private BigDecimal investAmount=new BigDecimal("0"); //投资金额
    private Integer investPeriod=0; //商品期限
    private String publishReason = ""; //加息比例
    private String dateString = ""; //加息比例
    private String conditionString = ""; //条件字符串

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Integer getInvestPeriod() {
        return investPeriod;
    }

    public void setInvestPeriod(Integer investPeriod) {
        this.investPeriod = investPeriod;
    }

    public String getPublishReason() {
        return publishReason;
    }

    public void setPublishReason(String publishReason) {
        this.publishReason = publishReason;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getConditionString() {
        return conditionString;
    }

    public void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }
}
