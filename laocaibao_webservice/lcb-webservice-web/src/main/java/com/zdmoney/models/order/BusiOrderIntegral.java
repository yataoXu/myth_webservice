package com.zdmoney.models.order;

public class BusiOrderIntegral {
    private Long id;

    private String integralProductSerialNo;

    private String integralOrderNo;

    private Long orderNo;

    private String isFirstOrder;

    private String integralAmount;

    private String cashAmount;

    private String couponId;

    private String couponAmount;

    private String couponOrderNo;

    private String couponProductSerialNo;

    private String integralRmbAmount;

    private String payChannelId;

    private String tppAgreement;

    private String tppFlowId;

    private String accountSeriNo;

    private String accountRefundSeriNo;

    private String voucherId;

    private String voucherSerialNo;

    private String voucherRefundSerialNo;

    private String appointmentId;

    private String appointmentSerialNo;

    private String appointmentRefundSerialNo;


    public String getIntegralRmbAmount() {
        return integralRmbAmount;
    }

    public void setIntegralRmbAmount(String integralRmbAmount) {
        this.integralRmbAmount = integralRmbAmount;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponOrderNo() {
        return couponOrderNo;
    }

    public void setCouponOrderNo(String couponOrderNo) {
        this.couponOrderNo = couponOrderNo;
    }

    public String getCouponProductSerialNo() {
        return couponProductSerialNo;
    }

    public void setCouponProductSerialNo(String couponProductSerialNo) {
        this.couponProductSerialNo = couponProductSerialNo;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntegralProductSerialNo() {
        return integralProductSerialNo;
    }

    public void setIntegralProductSerialNo(String integralProductSerialNo) {
        this.integralProductSerialNo = integralProductSerialNo == null ? null : integralProductSerialNo.trim();
    }

    public String getIntegralOrderNo() {
        return integralOrderNo;
    }

    public void setIntegralOrderNo(String integralOrderNo) {
        this.integralOrderNo = integralOrderNo == null ? null : integralOrderNo.trim();
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo == null ? null : orderNo;
    }

    public String getIsFirstOrder() {
        return isFirstOrder;
    }

    public void setIsFirstOrder(String isFirstOrder) {
        this.isFirstOrder = isFirstOrder == null ? null : isFirstOrder.trim();
    }

    public String getIntegralAmount() {
        return integralAmount;
    }

    public void setIntegralAmount(String integralAmount) {
        this.integralAmount = integralAmount == null ? null : integralAmount.trim();
    }

    public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    public String getTppAgreement() {
        return tppAgreement;
    }

    public void setTppAgreement(String tppAgreement) {
        this.tppAgreement = tppAgreement;
    }

    public String getTppFlowId() {
        return tppFlowId;
    }

    public void setTppFlowId(String tppFlowId) {
        this.tppFlowId = tppFlowId;
    }

    public String getAccountSeriNo() {
        return accountSeriNo;
    }

    public void setAccountSeriNo(String accountSeriNo) {
        this.accountSeriNo = accountSeriNo;
    }

    public String getAccountRefundSeriNo() {
        return accountRefundSeriNo;
    }

    public void setAccountRefundSeriNo(String accountRefundSeriNo) {
        this.accountRefundSeriNo = accountRefundSeriNo;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherSerialNo() {
        return voucherSerialNo;
    }

    public void setVoucherSerialNo(String voucherSerialNo) {
        this.voucherSerialNo = voucherSerialNo;
    }

    public String getVoucherRefundSerialNo() {
        return voucherRefundSerialNo;
    }

    public void setVoucherRefundSerialNo(String voucherRefundSerialNo) {
        this.voucherRefundSerialNo = voucherRefundSerialNo;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentSerialNo() {
        return appointmentSerialNo;
    }

    public void setAppointmentSerialNo(String appointmentSerialNo) {
        this.appointmentSerialNo = appointmentSerialNo;
    }

    public String getAppointmentRefundSerialNo() {
        return appointmentRefundSerialNo;
    }

    public void setAppointmentRefundSerialNo(String appointmentRefundSerialNo) {
        this.appointmentRefundSerialNo = appointmentRefundSerialNo;
    }
}