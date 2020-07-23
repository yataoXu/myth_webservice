package com.zdmoney.vo;

import com.zdmoney.secure.utils.ThreeDesUtil;

import java.math.BigDecimal;

/**
 * Created by user on 2016/12/27.
 * 投资记录
 */
public class InvestRecordVO{

    /**
     * 投资人手机号
     */
    private String cellPhone;

    /**
     * 走资金额
     */
    private BigDecimal investAmount;

    /**
     * 投资日期
     */
    private String investDate;

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = ThreeDesUtil.decryptMode(cellPhone).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestDate() {
        return investDate;
    }

    public void setInvestDate(String investDate) {
        this.investDate = investDate;
    }
}
