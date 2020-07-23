package com.zdmoney.web.dto;

import java.math.BigDecimal;

/**
 * Created by jb sun on 2016/2/24.
 */
public class PayVoucherDTO {
    private String voucherNo;       //加息券编号
    private String rateStr;    //加息比列中文说明 eg:0.005
    private BigDecimal rate;    //加息比列
    private String conditionString;     //优惠券使用描述
    private String voucherTitle;        //加息券标题
    private String voucherMemo;         //使用规则
    private String validDate;     //有效日期
    private Integer flag= -1;  //1可使用, 2已过期，3已使用
    private Integer plusInterestDays;  //加息天数

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getConditionString() {
        return conditionString;
    }

    public void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public String getVoucherMemo() {
        return voucherMemo;
    }

    public void setVoucherMemo(String voucherMemo) {
        this.voucherMemo = voucherMemo;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getPlusInterestDays() {
        return plusInterestDays;
    }

    public void setPlusInterestDays(Integer plusInterestDays) {
        this.plusInterestDays = plusInterestDays;
    }
}
