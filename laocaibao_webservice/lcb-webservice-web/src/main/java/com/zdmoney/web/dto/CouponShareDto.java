package com.zdmoney.web.dto;

/**
 * Created by 00225181 on 2015/11/16.
 */
public class CouponShareDto {
//    private String accountNo="";
//    private String openedAmount="";
//    private String unOpenedAmount="";
    private String no="";
    private Integer amount=0;
    private Integer period=0;
    private Integer investAmount=0;
    private Integer investPeriod=0;
//    private Date startTime;
//    private Date endTime;
//    private String dateString="";
    private String conditionString="";
//    private String inviteStatus="";
//    private String groupNo="";
    private String inviteUrl="";

//    public String getAccountNo() {
//        return accountNo;
//    }
//
//    public void setAccountNo(String accountNo) {
//        this.accountNo = accountNo;
//    }
//
//    public String getOpenedAmount() {
//        return openedAmount;
//    }
//
//    public void setOpenedAmount(String openedAmount) {
//        this.openedAmount = openedAmount;
//    }
//
//    public String getUnOpenedAmount() {
//        return unOpenedAmount;
//    }
//
//    public void setUnOpenedAmount(String unOpenedAmount) {
//        this.unOpenedAmount = unOpenedAmount;
//    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Integer investAmount) {
        this.investAmount = investAmount;
    }

    public Integer getInvestPeriod() {
        return investPeriod;
    }

    public void setInvestPeriod(Integer investPeriod) {
        this.investPeriod = investPeriod;
    }

//    @JsonSerialize(using=JsonDateYMDHMSSerializer.class)
//    public Date getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Date startTime) {
//        this.startTime = startTime;
//    }
//
//    @JsonSerialize(using=JsonDateYMDHMSSerializer.class)
//    public Date getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Date endTime) {
//        this.endTime = endTime;
//    }
//
//    public String getDateString() {
//        return dateString;
//    }
//
//    public void setDateString(String dateString) {
//        this.dateString = dateString;
//    }

    public String getConditionString() {
        return conditionString;
    }

    public void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }

//    public String getInviteStatus() {
//        return inviteStatus;
//    }
//
//    public void setInviteStatus(String inviteStatus) {
//        this.inviteStatus = inviteStatus;
//    }
//
//    public String getGroupNo() {
//        return groupNo;
//    }
//
//    public void setGroupNo(String groupNo) {
//        this.groupNo = groupNo;
//    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }
}
