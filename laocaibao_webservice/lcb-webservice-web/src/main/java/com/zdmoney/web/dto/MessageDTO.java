package com.zdmoney.web.dto;

import java.util.List;

public class MessageDTO {

	private String status;
	private String respDesc;
	private List<MessageDetailDTO> msgList;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public List<MessageDetailDTO> getMsgList() {
		return msgList;
	}
	public void setMsgList(List<MessageDetailDTO> msgList) {
		this.msgList = msgList;
	}

	
}
