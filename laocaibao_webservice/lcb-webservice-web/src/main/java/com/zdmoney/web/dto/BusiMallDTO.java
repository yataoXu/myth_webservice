package com.zdmoney.web.dto;


import com.zdmoney.models.BusiMall;

public class BusiMallDTO extends BusiMall {
	private String createDateStr;//日期格式化

	private Long surplusNum; //剩余个数

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Long getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Long surplusNum) {
		this.surplusNum = surplusNum;
	}
}
