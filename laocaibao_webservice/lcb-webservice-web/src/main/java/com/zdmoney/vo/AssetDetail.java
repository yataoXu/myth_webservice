package com.zdmoney.vo;

public class AssetDetail {
	//订单记录号
	private String id;
	//产品名称
	private String productName;
	//本金
	private String corpus;
	//年化收益
	private String yearRate;
	//昨天收益
	private String yesterProfit;
	//当前累计收益
	private String currProfit;
	//产品开始日期
	private String startDate;
	//产品结束日期
	private String endDate;
	//产品总收益
	private String totalProfit;
	//订单状态
	private String status;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCorpus() {
		return corpus;
	}
	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}
	public String getYearRate() {
		return yearRate;
	}
	public void setYearRate(String yearRate) {
		this.yearRate = yearRate;
	}
	public String getYesterProfit() {
		return yesterProfit;
	}
	public void setYesterProfit(String yesterProfit) {
		this.yesterProfit = yesterProfit;
	}
	public String getCurrProfit() {
		return currProfit;
	}
	public void setCurrProfit(String currProfit) {
		this.currProfit = currProfit;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(String totalProfit) {
		this.totalProfit = totalProfit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
