package com.zdmoney.web.dto;


import com.zdmoney.models.order.BusiOrderExchange;

public class BusiOrderExchangeDTO extends BusiOrderExchange {
	private String DateStr;//日期格式化

	public String getDateStr() {
		return DateStr;
	}

	public void setDateStr(String dateStr) {
		DateStr = dateStr;
	}
}
