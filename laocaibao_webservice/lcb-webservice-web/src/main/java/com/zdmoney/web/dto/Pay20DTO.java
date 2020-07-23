package com.zdmoney.web.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by 00225181 on 2015/12/1.
 */
public class Pay20DTO extends  PayPasswordDTO{
    private String payResult = "";//支付结果，0:申购成功，1:产品已售罄，2:产品已过期，3:产品已下架，4:产品余额不足，5:订单不是待付款
    private String payResultDesc = "";//支付结果描述
    private String interestDate = "";//计息时间
    private BannerDTO buySuccessBanner;//申购成功banner
    private String orderId;
    private String orderTime;

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getPayResultDesc() {
        return payResultDesc;
    }

    public void setPayResultDesc(String payResultDesc) {
        this.payResultDesc = payResultDesc;
    }

    public String getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(String interestDate) {
        this.interestDate = interestDate;
    }

    public BannerDTO getBuySuccessBanner() {
        return buySuccessBanner;
    }

    public void setBuySuccessBanner(BannerDTO buySuccessBanner) {
        this.buySuccessBanner = buySuccessBanner;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
