package com.zdmoney.web.dto;


import com.zdmoney.integral.api.dto.coin.CoinRecordDto;

public class BusiCoinRecordDTO extends CoinRecordDto {
	private String DateStr;//日期格式化

	public String getDateStr() {
		return DateStr;
	}

	public void setDateStr(String dateStr) {
		DateStr = dateStr;
	}
}
