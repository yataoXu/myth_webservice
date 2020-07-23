package com.zdmoney.vo;

public class AccountVO {
	private String amount;
	private String strType;
	private String strCustomerId;
	private String strCompanyId;
	private String orderNum;
	private String callUrl;
	private String incomeAmt;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStrType() {
		return strType;
	}
	public void setStrType(String strType) {
		this.strType = strType;
	}
	public String getStrCustomerId() {
		return strCustomerId;
	}
	public void setStrCustomerId(String strCustomerId) {
		this.strCustomerId = strCustomerId;
	}
	public String getStrCompanyId() {
		return strCompanyId;
	}
	public void setStrCompanyId(String strCompanyId) {
		this.strCompanyId = strCompanyId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getCallUrl() {
		return callUrl;
	}
	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}
	public String getIncomeAmt() {
		return incomeAmt;
	}
	public void setIncomeAmt(String incomeAmt) {
		this.incomeAmt = incomeAmt;
	}
}
