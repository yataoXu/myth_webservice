package com.zdmoney.models.order;

import java.math.BigDecimal;
import java.util.Date;

public class BusiSellOrder {
	private Long id;

	private Long orderId;

	private Long customerId;

	private String customerName;

	private String cellPhone;

	private String idNum;

	private String orderNum;

	private BigDecimal orderAmt;

	private BigDecimal sellOrderAmt;

	private Date sellOrderTime;

	private Date payTime;

	private String payOper;

	private String status;

	private Long bankAccountId;
	
	private String serialNo;

	private Integer currTerm;

	private String relAccountNo;//关联账户

	private String productType;//产品类型

	private String productName;//产品名称

	private Integer term;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

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

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName == null ? null : customerName.trim();
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone == null ? null : cellPhone.trim();
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum == null ? null : idNum.trim();
	}

	public BigDecimal getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(BigDecimal orderAmt) {
		this.orderAmt = orderAmt;
	}

	public BigDecimal getSellOrderAmt() {
		return sellOrderAmt;
	}

	public void setSellOrderAmt(BigDecimal sellOrderAmt) {
		this.sellOrderAmt = sellOrderAmt;
	}

	public Date getSellOrderTime() {
		return sellOrderTime;
	}

	public void setSellOrderTime(Date sellOrderTime) {
		this.sellOrderTime = sellOrderTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayOper() {
		return payOper;
	}

	public void setPayOper(String payOper) {
		this.payOper = payOper;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public Long getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public Integer getCurrTerm() {
		return currTerm;
	}

	public void setCurrTerm(Integer currTerm) {
		this.currTerm = currTerm;
	}

	public String getRelAccountNo() {
		return relAccountNo;
	}

	public void setRelAccountNo(String relAccountNo) {
		this.relAccountNo = relAccountNo;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}
}