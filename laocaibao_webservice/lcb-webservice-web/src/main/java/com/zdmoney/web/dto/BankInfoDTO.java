package com.zdmoney.web.dto;

import java.math.BigDecimal;

public class BankInfoDTO {
	
	private String bankNo;
	
	private String bankName;
	
	private String bankCardNo;
	
	private String bankSubName;
	
	private BigDecimal accountAmt;
	
	private String firstCash;    //是否第一次提现  2 是  1否  
	
	
	private String bankSubCode;
	

	public String getBankSubCode() {
		return bankSubCode;
	}

	public void setBankSubCode(String bankSubCode) {
		this.bankSubCode = bankSubCode;
	}

	public String getBankSubName() {
		return bankSubName;
	}

	public void setBankSubName(String bankSubName) {
		this.bankSubName = bankSubName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public BigDecimal getAccountAmt() {
		return accountAmt;
	}

	public void setAccountAmt(BigDecimal accountAmt) {
		this.accountAmt = accountAmt;
	}

	public String getFirstCash() {
		return firstCash;
	}

	public void setFirstCash(String firstCash) {
		this.firstCash = firstCash;
	}
	
	

}
