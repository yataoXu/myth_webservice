package com.zdmoney.vo;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2015/12/2.
 * 用户持有资产、历史资产vo对象
 */
public class UserAssetIntstAndTtlAmtVo {

    private BigDecimal totalInvestAmt;

    private BigDecimal totalInterest;

    public BigDecimal getTotalInvestAmt() {
        return totalInvestAmt;
    }

    public void setTotalInvestAmt(BigDecimal totalInvestAmt) {
        this.totalInvestAmt = totalInvestAmt;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
}
