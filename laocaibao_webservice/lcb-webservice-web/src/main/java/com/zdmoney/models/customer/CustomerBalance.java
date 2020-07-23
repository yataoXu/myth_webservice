package com.zdmoney.models.customer;

import java.math.BigDecimal;

public class CustomerBalance {
	
    private Long id;//主键id

    private Long customerId;//客户编号

    private String accountId;//帐户编号

    private BigDecimal balancePub;//余额明文
    
    private BigDecimal lockAmt;//冻结金额

    private String balancePri;//余额密文
    
    private String lockPri;//冻结密文

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getBalancePub() {
		return balancePub;
	}

	public void setBalancePub(BigDecimal balancePub) {
		this.balancePub = balancePub;
	}

	public BigDecimal getLockAmt() {
		return lockAmt;
	}

	public void setLockAmt(BigDecimal lockAmt) {
		this.lockAmt = lockAmt;
	}

	public String getBalancePri() {
		return balancePri;
	}

	public void setBalancePri(String balancePri) {
		this.balancePri = balancePri;
	}

	public String getLockPri() {
		return lockPri;
	}

	public void setLockPri(String lockPri) {
		this.lockPri = lockPri;
	}


}