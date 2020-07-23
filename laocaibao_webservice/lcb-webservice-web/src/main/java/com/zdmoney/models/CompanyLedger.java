package com.zdmoney.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对应于公司总账表
 * @author yangp
 *
 */
public class CompanyLedger {
	
    private Long id;//主键id

    private Long companyId;//公司编号
    
    private Date tradeDate;//当日创建或最近更新时间

    private BigDecimal beginAmt;//期初
    
    private BigDecimal purchaseAmt;//购买
    
    private BigDecimal incomeAmt;//回款
    
    private BigDecimal profitWdAmt;//产品收益提现
    
    private BigDecimal profitAmt;//产品收益
    
    private BigDecimal personAmt;//个人收益
    
    private BigDecimal feeAmt;//手续费收入
    
    private BigDecimal rebateOutAmt;//返利支出
    
    private BigDecimal rebateRhAmt;//返利充值
    
    private BigDecimal endAmt;//期末

    private String remark;//备注
    
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

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public BigDecimal getPurchaseAmt() {
		return purchaseAmt;
	}

	public void setPurchaseAmt(BigDecimal purchaseAmt) {
		this.purchaseAmt = purchaseAmt;
	}

	public BigDecimal getIncomeAmt() {
		return incomeAmt;
	}

	public void setIncomeAmt(BigDecimal incomeAmt) {
		this.incomeAmt = incomeAmt;
	}

	public BigDecimal getProfitWdAmt() {
		return profitWdAmt;
	}

	public void setProfitWdAmt(BigDecimal profitWdAmt) {
		this.profitWdAmt = profitWdAmt;
	}

	public BigDecimal getProfitAmt() {
		return profitAmt;
	}

	public void setProfitAmt(BigDecimal profitAmt) {
		this.profitAmt = profitAmt;
	}

	public BigDecimal getPersonAmt() {
		return personAmt;
	}

	public void setPersonAmt(BigDecimal personAmt) {
		this.personAmt = personAmt;
	}

	public BigDecimal getRebateOutAmt() {
		return rebateOutAmt;
	}

	public void setRebateOutAmt(BigDecimal rebateOutAmt) {
		this.rebateOutAmt = rebateOutAmt;
	}

	public BigDecimal getRebateRhAmt() {
		return rebateRhAmt;
	}

	public void setRebateRhAmt(BigDecimal rebateRhAmt) {
		this.rebateRhAmt = rebateRhAmt;
	}

	public BigDecimal getBeginAmt() {
		return beginAmt;
	}

	public void setBeginAmt(BigDecimal beginAmt) {
		this.beginAmt = beginAmt;
	}

	public BigDecimal getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(BigDecimal feeAmt) {
		this.feeAmt = feeAmt;
	}

	public BigDecimal getEndAmt() {
		return endAmt;
	}

	public void setEndAmt(BigDecimal endAmt) {
		this.endAmt = endAmt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}