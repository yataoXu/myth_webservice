/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.vo;  

import java.math.BigDecimal;

public class BusiRebateStatisticsVo {
	
	private BigDecimal curMonRebate = BigDecimal.ZERO;
	
	private BigDecimal lastMonRebate = BigDecimal.ZERO;
	
	private int payStatus;


	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public BigDecimal getCurMonRebate() {
		return curMonRebate;
	}

	public void setCurMonRebate(BigDecimal curMonRebate) {
		this.curMonRebate = curMonRebate;
	}

	public BigDecimal getLastMonRebate() {
		return lastMonRebate;
	}

	public void setLastMonRebate(BigDecimal lastMonRebate) {
		this.lastMonRebate = lastMonRebate;
	}
	
	
	

}
 