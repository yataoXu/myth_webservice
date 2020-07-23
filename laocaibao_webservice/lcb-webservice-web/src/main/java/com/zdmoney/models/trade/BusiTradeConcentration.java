package com.zdmoney.models.trade;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易集中度
 * Created by gaol on 2017/4/7
 **/
public class BusiTradeConcentration implements Serializable {

    /**
     * 用户编号
     */
    private String cmNumber;

    /**
     * 投资总额(万元)
     */
    private BigDecimal investAmount;

    /**
     * 占平台投资总额百分比
     */
    private Double investPercent;

    /**
     * 与BusiOperationDataStatistics关联ID
     */
    private Long operationDataId;

    public String getCmNumber() {
        return cmNumber;
    }

    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Double getInvestPercent() {
        return investPercent;
    }

    public void setInvestPercent(Double investPercent) {
        this.investPercent = investPercent;
    }

    public Long getOperationDataId() {
        return operationDataId;
    }

    public void setOperationDataId(Long operationDataId) {
        this.operationDataId = operationDataId;
    }
}
