package com.zdmoney.vo;

import java.math.BigDecimal;

public class OrderInterest {
	//订单金额
	private BigDecimal orderAmt;
	//昨日收益
	private BigDecimal yesterdayInterest;
	//总收益
	private BigDecimal totalInterest;
	
	public BigDecimal getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(BigDecimal orderAmt) {
		this.orderAmt = orderAmt;
	}
	public BigDecimal getYesterdayInterest() {
		return yesterdayInterest;
	}
	public void setYesterdayInterest(BigDecimal yesterdayInterest) {
		this.yesterdayInterest = yesterdayInterest;
	}
	public BigDecimal getTotalInterest() {
		return totalInterest;
	}
	public void setTotalInterest(BigDecimal totalInterest) {
		this.totalInterest = totalInterest;
	}
}
