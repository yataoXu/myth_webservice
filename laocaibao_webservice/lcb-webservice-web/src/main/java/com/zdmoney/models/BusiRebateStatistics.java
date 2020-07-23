package com.zdmoney.models;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 返利统计
 * 
 * @author longjw
 * 
 */
public class BusiRebateStatistics {

	private Long id;

	private Long customerId;

	private String statisMonth;

	private BigDecimal rebateAmt;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastModified;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	private int status;

	private Date payDate;

	private String staffId;
	
	private String staffName;

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
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

	public String getStatisMonth() {
		return statisMonth;
	}

	public void setStatisMonth(String statisMonth) {
		this.statisMonth = statisMonth;
	}

	public BigDecimal getRebateAmt() {
		return rebateAmt;
	}

	public void setRebateAmt(BigDecimal rebateAmt) {
		this.rebateAmt = rebateAmt;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

}
