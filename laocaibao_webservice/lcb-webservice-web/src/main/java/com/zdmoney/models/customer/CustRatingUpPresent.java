package com.zdmoney.models.customer;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2019-01-02 23:46:51
 */
@Data
public class CustRatingUpPresent implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String NOT_SENT = "0";

	public static final String SENDING= "2";

	public static final String ALREADY_SENT = "1";

	public static final String EXCEPTION_HAPPENED = "4";

	/**
	 * 用户id
	 */
	private Long customerId;
	/**
	 * 用户编号
	 */
	private String customerNumber;
	/**
	 * 用户升级后等级编码
	 */
	private String nextRatingCode;

	private Integer nextRatingNum;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 升级赠送积分数量
	 */
	private Integer creditNum;

	private int status;

}
