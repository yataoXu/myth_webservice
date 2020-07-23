package com.zdmoney.web.dto;

public class IntegralAccountBalanceDTO {
	/*
	 * 积分账户余额
	 */
	private Integer integral;
	/*
	 * 抵扣金额
	 */
	private Integer convertAmount;
	/*
	 * 抵扣积分
	 */
	private Double convertIntegral;
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public Integer getConvertAmount() {
		return convertAmount;
	}
	public void setConvertAmount(Integer convertAmount) {
		this.convertAmount = convertAmount;
	}
	public Double getConvertIntegral() {
		return convertIntegral;
	}
	public void setConvertIntegral(Double convertIntegral) {
		this.convertIntegral = convertIntegral;
	}
	
	
}
