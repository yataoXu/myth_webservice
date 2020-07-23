package com.zdmoney.models;

public class LoginEntry {
	
private String projectNo;
	
	private String userNo;
	
	private String deviceNo;
	
	private String reqTimestamp;
	
	private String sn;
	
	private String sign;

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getReqTimestamp() {
		return reqTimestamp;
	}

	public void setReqTimestamp(String reqTimestamp) {
		this.reqTimestamp = reqTimestamp;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	

}
