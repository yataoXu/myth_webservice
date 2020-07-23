package com.zdmoney.models.bank;

import java.util.Date;

public class CustomerBankAccount {
    private Long id;

    private Long customerId;

    private String cbAccount;

    private String cbAccountName;

    private Short cbAccountType;

    private String cbBankCode;

    private String cbBankName;

    private String cbBranchName;

    private Long cbInputId;

    private Date cbInputDate;

    private Long cbModifyId;

    private Date cbModifyDate;

    private Short cbValid;

    private String cbMemo;
    
    private String cbSubBankCode;

    private String cbBindPhone;

    public String getCbSubBankCode() {
		return cbSubBankCode;
	}

	public void setCbSubBankCode(String cbSubBankCode) {
		this.cbSubBankCode = cbSubBankCode;
	}

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

    public String getCbAccount() {
        return cbAccount;
    }

    public void setCbAccount(String cbAccount) {
        this.cbAccount = cbAccount == null ? null : cbAccount.trim();
    }

    public String getCbAccountName() {
        return cbAccountName;
    }

    public void setCbAccountName(String cbAccountName) {
        this.cbAccountName = cbAccountName == null ? null : cbAccountName.trim();
    }

    public Short getCbAccountType() {
        return cbAccountType;
    }

    public void setCbAccountType(Short cbAccountType) {
        this.cbAccountType = cbAccountType;
    }

    public String getCbBankCode() {
        return cbBankCode;
    }

    public void setCbBankCode(String cbBankCode) {
        this.cbBankCode = cbBankCode == null ? null : cbBankCode.trim();
    }

    public String getCbBankName() {
        return cbBankName;
    }

    public void setCbBankName(String cbBankName) {
        this.cbBankName = cbBankName == null ? null : cbBankName.trim();
    }

    public String getCbBranchName() {
        return cbBranchName;
    }

    public void setCbBranchName(String cbBranchName) {
        this.cbBranchName = cbBranchName == null ? null : cbBranchName.trim();
    }

    public Long getCbInputId() {
        return cbInputId;
    }

    public void setCbInputId(Long cbInputId) {
        this.cbInputId = cbInputId;
    }

    public Date getCbInputDate() {
        return cbInputDate;
    }

    public void setCbInputDate(Date cbInputDate) {
        this.cbInputDate = cbInputDate;
    }

    public Long getCbModifyId() {
        return cbModifyId;
    }

    public void setCbModifyId(Long cbModifyId) {
        this.cbModifyId = cbModifyId;
    }

    public Date getCbModifyDate() {
        return cbModifyDate;
    }

    public void setCbModifyDate(Date cbModifyDate) {
        this.cbModifyDate = cbModifyDate;
    }

    public Short getCbValid() {
        return cbValid;
    }

    public void setCbValid(Short cbValid) {
        this.cbValid = cbValid;
    }

    public String getCbMemo() {
        return cbMemo;
    }

    public void setCbMemo(String cbMemo) {
        this.cbMemo = cbMemo == null ? null : cbMemo.trim();
    }

    public String getCbBindPhone() {
        return cbBindPhone;
    }

    public void setCbBindPhone(String cbBindPhone) {
        this.cbBindPhone = cbBindPhone;
    }
}