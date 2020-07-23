package com.zdmoney.vo;

import com.zdmoney.models.order.BusiOrder;

public class BusiOrderProductVo extends BusiOrder{
	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
