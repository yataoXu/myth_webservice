/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.models;  

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BusiRebate {
	//主键
	private Long id;
	
	//订单编号
	private Long orderId;
	
	//受利人ID
	private Long customerId;
	
	//受利人邀请码
    private String inviteCode;
    
    //受利人手机号
    private String cellPhone;
    
    //受利人证件号码
    private String idNum;
    
    //返利金额
    private BigDecimal rebateAmt;
    
    //受利人姓名
    private String customerName;
    
    //创建时间
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date createDate;
    
    //起息时间
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date purchaseDate;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerID(Long customerId) {
		this.customerId = customerId;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public BigDecimal getRebateAmt() {
		return rebateAmt;
	}

	public void setRebateAmt(BigDecimal rebateAmt) {
		this.rebateAmt = rebateAmt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
}
 