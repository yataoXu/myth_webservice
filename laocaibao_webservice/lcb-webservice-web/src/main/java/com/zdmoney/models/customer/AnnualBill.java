package com.zdmoney.models.customer;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by gaol on 2017/1/9
 **/
public class AnnualBill implements Serializable {

    /**
     * 注册日期
     */
    private String registerDate;

    /**
     * 首次投资日期
     */
    private String firstInvestDate;

    /**
     * 总投资次数
     */
    private Long investTotal;

    /**
     * 总投资金额
     */
    private BigDecimal investAmount;

    /**
     * 收益总额
     */
    private BigDecimal investIncome;

    /**
     * 总红包金额
     */
    private BigDecimal  redBagAmount;

    /**
     * 使用红包金额
     */
    private Long useRedBagAmount;

    /**
     * 获得的加息券数量
     */
    private Long voucherTotal;

    /**
     * 使用的加息券数量
     */
    private Long useVoucherCount;

    /**
     * 最大投资总额
     */
    private BigDecimal maxInvestAmount;

    /**
     * 最大投资金额日期
     */
    private String maxInvestDate;

    /**
     * 投资使用福利次数
     */
    private Long welfareCount;

    /**
     * 使用福利 节省金额
     */
    private BigDecimal economizeAmount;

    /**
     * 邀请好友数量
     */
    private Long friendCount;

    /**
     * 积分总数
     */
    private Long integralTotal;

    /**
     * 用户称号
     */
    private String customerTitle;

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getFirstInvestDate() {
        return firstInvestDate;
    }

    public void setFirstInvestDate(String firstInvestDate) {
        this.firstInvestDate = firstInvestDate;
    }

    public Long getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(Long investTotal) {
        this.investTotal = investTotal;
    }

    public BigDecimal getInvestAmount() {
        return investAmount != null ? investAmount.setScale(0, BigDecimal.ROUND_HALF_UP):investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getInvestIncome() {
        return investIncome;
    }

    public void setInvestIncome(BigDecimal investIncome) {
        this.investIncome = investIncome;
    }

    public BigDecimal getRedBagAmount() {
        return redBagAmount;
    }

    public void setRedBagAmount(BigDecimal redBagAmount) {
        this.redBagAmount = redBagAmount;
    }

    public Long getUseRedBagAmount() {
        return useRedBagAmount;
    }

    public void setUseRedBagAmount(Long useRedBagAmount) {
        this.useRedBagAmount = useRedBagAmount;
    }

    public Long getVoucherTotal() {
        return voucherTotal;
    }

    public void setVoucherTotal(Long voucherTotal) {
        this.voucherTotal = voucherTotal;
    }

    public Long getUseVoucherCount() {
        return useVoucherCount;
    }

    public void setUseVoucherCount(Long useVoucherCount) {
        this.useVoucherCount = useVoucherCount;
    }

    public BigDecimal getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(BigDecimal maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public String getMaxInvestDate() {
        return maxInvestDate;
    }

    public void setMaxInvestDate(String maxInvestDate) {
        this.maxInvestDate = maxInvestDate;
    }

    public Long getWelfareCount() {
        return welfareCount;
    }

    public void setWelfareCount(Long welfareCount) {
        this.welfareCount = welfareCount;
    }

    public BigDecimal getEconomizeAmount() {
        return economizeAmount;
    }

    public void setEconomizeAmount(BigDecimal economizeAmount) {
        this.economizeAmount = economizeAmount;
    }

    public Long getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(Long friendCount) {
        this.friendCount = friendCount;
    }

    public Long getIntegralTotal() {
        return integralTotal;
    }

    public void setIntegralTotal(Long integralTotal) {
        this.integralTotal = integralTotal;
    }

    public String getCustomerTitle() {
        String title = "";
        Long money = this.getInvestAmount() != null ? Math.round(this.getInvestAmount().doubleValue()) : 0;
        if(money != null && money > 0){
            if(9999 >= money) title = "小有积蓄";
            else if(99999 >= money && money >= 10000) title = "小康之家";
            else if(499999 >= money && money >= 100000) title = "理财达人";
            else if(money >= 500000) title = "财大气粗";
        }
        return title;
    }

    public void setCustomerTitle(String customerTitle) {
        this.customerTitle = customerTitle;
    }
}
