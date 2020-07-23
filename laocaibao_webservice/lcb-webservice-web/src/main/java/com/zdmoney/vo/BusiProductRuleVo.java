package com.zdmoney.vo;

import java.util.Date;

/**
 * Created by 00245337 on 2016/9/23.
 */
public class BusiProductRuleVo {
    /** 限购类型 */
    private Long limitType;
    /** 限购平台 */
    private String platform;
    /** 会员类型 */
    private String memberType;
    /** 渠道 */
    private String channel;
    /** 福利使用 */
    private String welfare;
    /** 预约产品可预约购买时间至*/
    private Date reservatTime;
    /**个贷标识*/
    private String personLoan;
    /**产品类型*/
    private String isTransfer;
    /**
     * 起息日
     */
    private Date interestStartDate;

    /**
     * 结息日
     */
    private Date interestEndDate;

    private  String subjectType;

    private Integer closeDay;

    public Integer getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(Integer closeDay) {
        this.closeDay = closeDay;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public Long getLimitType() {
        return limitType;
    }

    public void setLimitType(Long limitType) {
        this.limitType = limitType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public Date getReservatTime() {
        return reservatTime;
    }

    public void setReservatTime(Date reservatTime) {
        this.reservatTime = reservatTime;
    }

    public String getPersonLoan() {
        return personLoan;
    }

    public void setPersonLoan(String personLoan) {
        this.personLoan = personLoan;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public Date getInterestStartDate() {
        return interestStartDate;
    }

    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    public Date getInterestEndDate() {
        return interestEndDate;
    }

    public void setInterestEndDate(Date interestEndDate) {
        this.interestEndDate = interestEndDate;
    }
}
