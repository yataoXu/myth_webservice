package com.zdmoney.models;

import java.math.BigDecimal;

public class CompanyBalance {
	
    private Long id;//主键id

    private Long companyId;//公司编号

    private String companyName;//公司名称
    
    private String accountId;//帐户编号

    private BigDecimal balancePub;//余额明文

    private String balancePri;//余额密文

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getBalancePri() {
		return balancePri;
	}

	public void setBalancePri(String balancePri) {
		this.balancePri = balancePri;
	}


}