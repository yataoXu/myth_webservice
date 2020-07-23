package com.zdmoney.models.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 捞财宝会员等级表
 * @date 2018-10-20 09:25:06
 */
@Data
public class CustomerMemberLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 用户ID
	 */
	private Long customerId;
	/**
	 * 初始在投金额
	 */
	private BigDecimal initAmt;
	/**
	 * 活动结束时在投金额
	 */
	private BigDecimal endAmt;
	/**
	 * 初始会员等级
	 */
	private String initLevel;
	/**
	 * 活动结束时会员等级
	 */
	private String endLevel;

}
