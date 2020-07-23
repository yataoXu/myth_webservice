package com.zdmoney.exception;

/**
 * 业务公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * 
 */
public class HessianRpcException extends RuntimeException {

	private static final long serialVersionUID = 3583566093089790852L;

	/* 模块 */
	private String model;

	public HessianRpcException() {
		super();
	}

	public HessianRpcException(String message) {
		super(message);
	}

	public HessianRpcException(String model, String message) {
		super(message);
		this.model = model;
	}

	public HessianRpcException(Throwable cause) {
		super(cause);
	}

	public HessianRpcException(String model, String message, Throwable e) {
		super(message, e);
		this.model = model;
	}
	
	public String getModel(){
		return model;
	}

}
