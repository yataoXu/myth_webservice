package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2018-12-06 09:57:54
 */
@Data
public class BusiCashFlow implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 用户编号
	 */
	private String cmNumber;
	/**
	 * 用户姓名
	 */
	private String cmName;
	/**
	 * 手机号
	 */
	private String cmPhone;
	/**
	 * 现金券编号
	 */
	private String cashNo;
	/**
	 * 来源
	 */
	private String cashSource;
	/**
	 * 金额
	 */
	private Long cashAmt;
	/**
	 * 交易时间
	 */
	private Date trdDate;
	/**
	 * 流水号
	 */
	private String flowNum;
	/**
	 * 账户流水号
	 */
	private String accountSeriNo;
	/**
	 * 发放状态：0 成功 1：失败
	 */
	private String status;

}
