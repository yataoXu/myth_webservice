package com.zdmoney.models.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @date 2019-01-02 23:47:00
 */
@Data
public class CustRatingChangingRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String DIRECTION_UP = "up";

	public static final String DIRECTION_DOWN = "down";

	public static class Status{
		public static final String NOT_SENT = "0";

		public static final String SENDING= "2";

		public static final String ALREADY_SENT = "1";

		public static final String EXCEPTION_HAPPENED = "4";
	}

	private Long id;
	/**
	 * 用户id
	 */
	private Long customerId;
	/**
	 * 用户编号
	 */
	private String customerNumber;
	/**
	 * 用户当前等级编码
	 */
	private String currentRatingCode;
	/**
	 * 用户下一级后等级编码
	 */
	private String nextRatingCode;

	private Integer currentRatingNum;//用户当前等级数值

	private Integer nextRatingNum;//用户下一等级数值

	private String changingDirection;/**变动方向*/
	/**
	 * 用户升级时持有的资产
	 */
	private BigDecimal currentAssets;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
