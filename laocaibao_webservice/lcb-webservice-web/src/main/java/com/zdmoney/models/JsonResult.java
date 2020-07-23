package com.zdmoney.models;

public class JsonResult {
	private boolean success;
	private String msg;
	private Object result;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public JsonResult()
	{
	}

	public JsonResult(boolean flag, String json)
	{
		this.success = flag;
		this.msg = json;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}
}
