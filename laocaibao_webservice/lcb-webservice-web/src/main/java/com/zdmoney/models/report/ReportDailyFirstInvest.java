package com.zdmoney.models.report;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "REPORT_DAILY_FIRST_INVEST")
public class ReportDailyFirstInvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select SEQ_report_daily_first_invest.Nextval from dual")
    private Long id;

    private String reportDate;

    private BigDecimal investAmt;

    private Integer investNum;

    private String channel;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return REPORT_DATE
     */
    public String getReportDate() {
        return reportDate;
    }

    /**
     * @param reportDate
     */
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * @return INVEST_AMT
     */
    public BigDecimal getInvestAmt() {
        return investAmt;
    }

    /**
     * @param investAmt
     */
    public void setInvestAmt(BigDecimal investAmt) {
        this.investAmt = investAmt;
    }

    /**
     * @return INVEST_NUM
     */
    public Integer getInvestNum() {
        return investNum;
    }

    /**
     * @param investNum
     */
    public void setInvestNum(Integer investNum) {
        this.investNum = investNum;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}