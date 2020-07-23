package com.zdmoney.vo;


/**
 * Created by 00245337 on 2016/9/23.
 */
public class BusiBeforeOrderVo {
    /** 是否有预约券 */
    private Boolean isHasBespeakTicket;
    /** 是否需扣预约券 */
    private Boolean isUseBespeakTicket;
    /** 预约券Id */
    private Long ticketId;
    /** 捞财币余额 */
    private Long coinBalance;
    /** 上期捞财币余额 */
    private Long lastCoinBalance;
    /** 兑换所需捞财币 */
    private Long buyAmt;
    /** 结束预约时间 */
    private String bespeakTimeOver;

    private Integer errorCode;

    private String errorMsg;

    public Boolean getIsHasBespeakTicket() {
        return isHasBespeakTicket;
    }

    public void setIsHasBespeakTicket(Boolean isHasBespeakTicket) {
        this.isHasBespeakTicket = isHasBespeakTicket;
    }

    public Boolean getIsUseBespeakTicket() {
        return isUseBespeakTicket;
    }

    public void setIsUseBespeakTicket(Boolean isUseBespeakTicket) {
        this.isUseBespeakTicket = isUseBespeakTicket;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(Long coinBalance) {
        this.coinBalance = coinBalance;
    }

    public Long getLastCoinBalance() {
        return lastCoinBalance;
    }

    public void setLastCoinBalance(Long lastCoinBalance) {
        this.lastCoinBalance = lastCoinBalance;
    }

    public Long getBuyAmt() {
        return buyAmt;
    }

    public void setBuyAmt(Long buyAmt) {
        this.buyAmt = buyAmt;
    }

    public String getBespeakTimeOver() {
        return bespeakTimeOver;
    }

    public void setBespeakTimeOver(String bespeakTimeOver) {
        this.bespeakTimeOver = bespeakTimeOver;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
