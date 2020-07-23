package com.zdmoney.web.dto;


import java.math.BigDecimal;

public class BusiOrderDTO {

	private String orderId;//订单号
	private String orderNum;//业务订单编号
	private String customerId;//客户ID
	private String productId;//产品ID
	private String customerName;//客户姓名
	private String productName;//产品名称
	private String orderAmt;//订单金额
	private String orderTime;//下单时间
	private String principalinterest;//到期本息和
	private String status;//订单状态
	private String interestStartDate;//起息日
	private String interestEndDate;//结息日
	private String systemDate;//系统当前日期
	private String isAddRate;//是否加息，0:未加息，1:已加息
	private BigDecimal inviteRate;//首次填写邀请码加息
	private BigDecimal actionRate;//活动加息
	private BigDecimal totalAddRate;//总加息
	private String projectDetailUrl="";//项目详情URL
	private String agreementUrl="";
	private Long closeDate;//封闭期

	public Long getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Long closeDate) {
		this.closeDate = closeDate;
	}

	public String getProjectDetailUrl() {
		return projectDetailUrl;
	}

	public void setProjectDetailUrl(String projectDetailUrl) {
		this.projectDetailUrl = projectDetailUrl;
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPrincipalinterest() {
		return principalinterest;
	}

	public void setPrincipalinterest(String principalinterest) {
		this.principalinterest = principalinterest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInterestStartDate() {
		return interestStartDate;
	}

	public void setInterestStartDate(String interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	public String getInterestEndDate() {
		return interestEndDate;
	}

	public void setInterestEndDate(String interestEndDate) {
		this.interestEndDate = interestEndDate;
	}

	public String getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

	public String getIsAddRate() {
		return isAddRate;
	}

	public void setIsAddRate(String isAddRate) {
		this.isAddRate = isAddRate;
	}

	public BigDecimal getActionRate() {
		return actionRate== null ? new BigDecimal(0) : actionRate.multiply(new BigDecimal(100));
	}

	public void setActionRate(BigDecimal actionRate) {
		this.actionRate = actionRate;
	}

	public BigDecimal getTotalAddRate() {
		return totalAddRate == null ?new BigDecimal(0) : totalAddRate.multiply(new BigDecimal(100));
	}

	public void setTotalAddRate(BigDecimal totalAddRate) {
		this.totalAddRate = totalAddRate;
	}

	public BigDecimal getInviteRate() {
		return inviteRate == null ?new BigDecimal(0) : inviteRate.multiply(new BigDecimal(100));
	}

	public void setInviteRate(BigDecimal inviteRate) {
		this.inviteRate = inviteRate;
	}


	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getAgreementUrl() {
		return agreementUrl;
	}

	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}
}
