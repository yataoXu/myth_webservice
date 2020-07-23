package com.zdmoney.models.trade;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 平台数据维护管理
 * Created by gaol on 2017/4/5
 **/
@Table(name = "OPERATION_DATA_STATISTICS")
public class BusiOperationDataStatistics implements Serializable {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    /**
     * 统计时间
     */
    private String statisticsTime;

    /**
     * 累计成交金额
     */
    private Double cumulativeTurnover;

    /**
     * 累计成交数量
     */
    private Long dealTotal;

    /**
     * 交易余额
     */
    private Double dealRemainingSum;

    /**
     * 年营业额
     */
    private Double turnoverYear;

    /**
     * 当年成交数量
     */
    private Long totalAnnualTurnover;

    /**
     * 客单价
     */
    private Double perCustomerTransaction;

    /**
     * 交易集中度
     */
    private List<BusiTradeConcentration> tradeConcentrations;

    /**
     * 最大单户投资金额
     */
    private Double maxSingleInvest;

    /**
     * 最大单户投资金额比例
     */
    private Double maxSingleInvestPercent;

    /**
     * 前十大投资人合计投资金额
     */
    private Double investTotalPercent;

    /**
     * 前十大投资人合计投资金额
     */
    private Double investTotal;

    /**
     * 投资端注册人数
     */
    private Long totalMembers;

    /**
     * 活跃用户数
     */
    private Long activeUsers;

    /**
     * 投资用户性别分布: 男
     * 百分比
     */
    private Double boy;

    /**
     * 投资用户性别分布: 女
     * 百分比
     */
    private Double girl;

    /**
     * 投资人数年龄分布占比:18-20
     */
    private Double twenty;

    /**
     * 投资人数年龄分布占比:21-30
     */
    private Double thirty;

    /**
     * 投资人数年龄分布占比:31-40
     */
    private Double forty;

    /**
     * 投资人数年龄分布占比:41-50
     */
    private Double fifty;

    /**
     * 投资人数年龄分布占比:51-60
     */
    private Double sixty;

    /**
     * 投资人数年龄分布占比:60以上
     */
    private Double otherAge;

    /**
     * 操作人
     */
    private String operationName;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    public Double getCumulativeTurnover() {
        return cumulativeTurnover;
    }

    public void setCumulativeTurnover(Double cumulativeTurnover) {
        this.cumulativeTurnover = cumulativeTurnover;
    }

    public Long getDealTotal() {
        return dealTotal;
    }

    public void setDealTotal(Long dealTotal) {
        this.dealTotal = dealTotal;
    }

    public Double getDealRemainingSum() {
        return dealRemainingSum;
    }

    public void setDealRemainingSum(Double dealRemainingSum) {
        this.dealRemainingSum = dealRemainingSum;
    }

    public Double getTurnoverYear() {
        return turnoverYear;
    }

    public void setTurnoverYear(Double turnoverYear) {
        this.turnoverYear = turnoverYear;
    }

    public Long getTotalAnnualTurnover() {
        return totalAnnualTurnover;
    }

    public void setTotalAnnualTurnover(Long totalAnnualTurnover) {
        this.totalAnnualTurnover = totalAnnualTurnover;
    }

    public Double getPerCustomerTransaction() {
        return perCustomerTransaction;
    }

    public void setPerCustomerTransaction(Double perCustomerTransaction) {
        this.perCustomerTransaction = perCustomerTransaction;
    }

    public Long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(Long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public Long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Double getBoy() {
        return boy;
    }

    public void setBoy(Double boy) {
        this.boy = boy;
    }

    public Double getGirl() {
        return girl;
    }

    public void setGirl(Double girl) {
        this.girl = girl;
    }

    public Double getTwenty() {
        return twenty;
    }

    public void setTwenty(Double twenty) {
        this.twenty = twenty;
    }

    public Double getThirty() {
        return thirty;
    }

    public void setThirty(Double thirty) {
        this.thirty = thirty;
    }

    public Double getForty() {
        return forty;
    }

    public void setForty(Double forty) {
        this.forty = forty;
    }

    public Double getFifty() {
        return fifty;
    }

    public void setFifty(Double fifty) {
        this.fifty = fifty;
    }

    public Double getSixty() {
        return sixty;
    }

    public void setSixty(Double sixty) {
        this.sixty = sixty;
    }

    public Double getOtherAge() {
        return otherAge;
    }

    public void setOtherAge(Double otherAge) {
        this.otherAge = otherAge;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public List<BusiTradeConcentration> getTradeConcentrations() {
        return tradeConcentrations;
    }

    public void setTradeConcentrations(List<BusiTradeConcentration> tradeConcentrations) {
        this.tradeConcentrations = tradeConcentrations;
    }

    public Double getMaxSingleInvest() {
        return maxSingleInvest;
    }

    public void setMaxSingleInvest(Double maxSingleInvest) {
        this.maxSingleInvest = maxSingleInvest;
    }

    public Double getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(Double investTotal) {
        this.investTotal = investTotal;
    }

    public Double getMaxSingleInvestPercent() {
        return maxSingleInvestPercent;
    }

    public void setMaxSingleInvestPercent(Double maxSingleInvestPercent) {
        this.maxSingleInvestPercent = maxSingleInvestPercent;
    }

    public Double getInvestTotalPercent() {
        return investTotalPercent;
    }

    public void setInvestTotalPercent(Double investTotalPercent) {
        this.investTotalPercent = investTotalPercent;
    }
}
