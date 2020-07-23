package com.zdmoney.web.dto;

import java.math.BigDecimal;

public class ExchangeRecordDTO {
	private String proName;
	private String proType;
	private BigDecimal buyAmt;
	private String dateStr;
	private String interestDay;
	private String couponAmt;
	private BigDecimal interestRate;

	public String getCouponAmt() {
		return couponAmt;
	}

	public void setCouponAmt(String couponAmt) {
		this.couponAmt = couponAmt;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public BigDecimal getBuyAmt() {
		return buyAmt;
	}

	public void setBuyAmt(BigDecimal buyAmt) {
		this.buyAmt = buyAmt;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getInterestDay() {
		return interestDay;
	}

	public void setInterestDay(String interestDay) {
		this.interestDay = interestDay;
	}
}
