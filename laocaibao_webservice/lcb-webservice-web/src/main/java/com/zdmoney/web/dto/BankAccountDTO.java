package com.zdmoney.web.dto;

import java.math.BigDecimal;

public class BankAccountDTO {
	
	private BigDecimal accountAmt;    //账户总资产
	private BigDecimal balanceAmt;    //可用余额
	private BigDecimal lockAmt;       //冻结金额
	private BigDecimal investmentAmt; //投资金额
	private BigDecimal incomeAmt;    //收益金额
	private BigDecimal cashAccount;    //现金账户
	private BigDecimal investmentAccount;    //投资账户
	
	
	
	public BigDecimal getAccountAmt() {
		return accountAmt;
	}
	public void setAccountAmt(BigDecimal accountAmt) {
		this.accountAmt = accountAmt;
	}
	public BigDecimal getBalanceAmt() {
		return balanceAmt;
	}
	public void setBalanceAmt(BigDecimal balanceAmt) {
		this.balanceAmt = balanceAmt;
	}
	public BigDecimal getLockAmt() {
		return lockAmt;
	}
	public void setLockAmt(BigDecimal lockAmt) {
		this.lockAmt = lockAmt;
	}
	public BigDecimal getInvestmentAmt() {
		return investmentAmt;
	}
	public void setInvestmentAmt(BigDecimal investmentAmt) {
		this.investmentAmt = investmentAmt;
	}
	public BigDecimal getIncomeAmt() {
		return incomeAmt;
	}
	public void setIncomeAmt(BigDecimal incomeAmt) {
		this.incomeAmt = incomeAmt;
	}
	public BigDecimal getCashAccount() {
		return cashAccount;
	}
	public void setCashAccount(BigDecimal cashAccount) {
		this.cashAccount = cashAccount;
	}
	public BigDecimal getInvestmentAccount() {
		return investmentAccount;
	}
	public void setInvestmentAccount(BigDecimal investmentAccount) {
		this.investmentAccount = investmentAccount;
	}
	
	
	

	

}
