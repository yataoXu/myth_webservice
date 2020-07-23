package com.zdmoney.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对应于总账表
 * @author yangp
 *
 */
public class GeneralLedger {
	
    private Long id;//主键id

    private Long customerId;//客户编号
    
    private Date createDate;//当日创建或最近更新时间

    private BigDecimal beginAmt;//期初
    
    private BigDecimal rechargeAmt;//充值
    
    private BigDecimal repayAmt;//还款
    
    private BigDecimal rebateAmt;//返利
    
    private BigDecimal withdrawAmt;//提现
    
    private BigDecimal consumeAmt;//消费
    
    private BigDecimal feeAmt;//手续费
    
    private BigDecimal lockAmt;//冻结金额
    
    private BigDecimal unLockAmt;//解冻金额
    
    private BigDecimal refundAmt;//退款金额
    
    private BigDecimal otherInAmt;//其它入
    
    private BigDecimal otherOutAmt;//其它出
    
    private BigDecimal endAmt;//期末

    private String remark;//备注
    
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getBeginAmt() {
		return beginAmt;
	}

	public void setBeginAmt(BigDecimal beginAmt) {
		this.beginAmt = beginAmt;
	}

	public BigDecimal getRechargeAmt() {
		return rechargeAmt;
	}

	public void setRechargeAmt(BigDecimal rechargeAmt) {
		this.rechargeAmt = rechargeAmt;
	}

	public BigDecimal getRepayAmt() {
		return repayAmt;
	}

	public void setRepayAmt(BigDecimal repayAmt) {
		this.repayAmt = repayAmt;
	}

	public BigDecimal getRebateAmt() {
		return rebateAmt;
	}

	public void setRebateAmt(BigDecimal rebateAmt) {
		this.rebateAmt = rebateAmt;
	}

	public BigDecimal getWithdrawAmt() {
		return withdrawAmt;
	}

	public void setWithdrawAmt(BigDecimal withdrawAmt) {
		this.withdrawAmt = withdrawAmt;
	}

	public BigDecimal getConsumeAmt() {
		return consumeAmt;
	}

	public void setConsumeAmt(BigDecimal consumeAmt) {
		this.consumeAmt = consumeAmt;
	}

	public BigDecimal getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(BigDecimal feeAmt) {
		this.feeAmt = feeAmt;
	}

	public BigDecimal getLockAmt() {
		return lockAmt;
	}

	public void setLockAmt(BigDecimal lockAmt) {
		this.lockAmt = lockAmt;
	}

	public BigDecimal getUnLockAmt() {
		return unLockAmt;
	}

	public void setUnLockAmt(BigDecimal unLockAmt) {
		this.unLockAmt = unLockAmt;
	}

	public BigDecimal getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(BigDecimal refundAmt) {
		this.refundAmt = refundAmt;
	}

	public BigDecimal getOtherInAmt() {
		return otherInAmt;
	}

	public void setOtherInAmt(BigDecimal otherInAmt) {
		this.otherInAmt = otherInAmt;
	}

	public BigDecimal getOtherOutAmt() {
		return otherOutAmt;
	}

	public void setOtherOutAmt(BigDecimal otherOutAmt) {
		this.otherOutAmt = otherOutAmt;
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