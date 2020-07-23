package com.zdmoney.web.dto;

import java.util.List;

public class PayInfoDTO {

	private String isPay="0";//是否已支付，0:未支付，1:已支付
	private String cashAmt="0";//积分兑换，单位元
	private String rate="0";//积分兑换，单位积分
	private String integral="0";//积分账户余额
	private String orderAmt="0";//订单金额
	private String cashPayAmt="0";//现金支付金额
	private String integralPayAmt="0";//积分支付金额
	private String cbAccount="";//卡号
	private String cbAccountName="";//持卡人姓名
	private String cbBankCode="";//银行代码
	private String cbBankName="";//银行名
	private String cbBranchName="";//支行名
	private String cbSubBankCode="";//支行代码
	private String limitMsg = "";//银行限额信息
	private String integralPro="1";//积分可交易比例
	private String couponId="";//红包id
	private String couponAmount="0";//红包金额
	private List<PacketDTO> packets;
	private String payChannelId="";//渠道号
	private String payChannelName="";//渠道名


	public List<PacketDTO> getPackets() {
		return packets;
	}

	public void setPackets(List<PacketDTO> packets) {
		this.packets = packets;
	}

	public String getCbAccount() {
		return cbAccount;
	}

	public void setCbAccount(String cbAccount) {
		this.cbAccount = cbAccount;
	}

	public String getCbAccountName() {
		return cbAccountName;
	}

	public void setCbAccountName(String cbAccountName) {
		this.cbAccountName = cbAccountName;
	}

	public String getCbBankCode() {
		return cbBankCode;
	}

	public void setCbBankCode(String cbBankCode) {
		this.cbBankCode = cbBankCode;
	}

	public String getCbBankName() {
		return cbBankName;
	}

	public void setCbBankName(String cbBankName) {
		this.cbBankName = cbBankName;
	}

	public String getCbBranchName() {
		return cbBranchName;
	}

	public void setCbBranchName(String cbBranchName) {
		this.cbBranchName = cbBranchName;
	}

	public String getCbSubBankCode() {
		return cbSubBankCode;
	}

	public void setCbSubBankCode(String cbSubBankCode) {
		this.cbSubBankCode = cbSubBankCode;
	}


	public String getCashPayAmt() {
		return cashPayAmt;
	}

	public void setCashPayAmt(String cashPayAmt) {
		this.cashPayAmt = cashPayAmt;
	}


	public String getIntegralPayAmt() {
		return integralPayAmt;
	}

	public void setIntegralPayAmt(String integralPayAmt) {
		this.integralPayAmt = integralPayAmt;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}

	public String getIntegralPro() {
		return integralPro;
	}

	public void setIntegralPro(String integralPro) {
		this.integralPro = integralPro;
	}

	public String getCashAmt() {
		return cashAmt;
	}

	public void setCashAmt(String cashAmt) {
		this.cashAmt = cashAmt;
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

	public String getPayChannelId() {
		return payChannelId;
	}

	public void setPayChannelId(String payChannelId) {
		this.payChannelId = payChannelId;
	}

	public String getPayChannelName() {
		return payChannelName;
	}

	public void setPayChannelName(String payChannelName) {
		this.payChannelName = payChannelName;
	}

	public String getLimitMsg() {
		return limitMsg;
	}

	public void setLimitMsg(String limitMsg) {
		this.limitMsg = limitMsg;
	}
}
