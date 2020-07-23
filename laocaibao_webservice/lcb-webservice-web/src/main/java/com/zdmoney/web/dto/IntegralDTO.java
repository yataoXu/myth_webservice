package com.zdmoney.web.dto;

import java.util.List;
import java.util.Map;

public class IntegralDTO {

	private String integral="0";
	private String isSign="0";
	private List<Map<String,String>> details;
	private String totalPage="0";
	private String totalSize="0";
	private String pageNo="0";
	private String signPercent="0";
	private String signStr="0";
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getIsSign() {
		return isSign;
	}
	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public String getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public List<Map<String, String>> getDetails() {
		return details;
	}
	public void setDetails(List<Map<String, String>> details) {
		this.details = details;
	}

	public String getSignPercent() {
		return signPercent;
	}

	public void setSignPercent(String signPercent) {
		this.signPercent = signPercent;
	}

	public String getSignStr() {
		return signStr;
	}

	public void setSignStr(String signStr) {
		this.signStr = signStr;
	}
}
