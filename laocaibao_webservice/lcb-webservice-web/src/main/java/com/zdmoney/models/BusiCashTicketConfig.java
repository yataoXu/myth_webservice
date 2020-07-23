package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2018-12-05 09:57:02
 */
@Data
public class BusiCashTicketConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 金额
	 */
	private Long amount;
	/**
	 * 有效天数
	 */
	private Long period;
	/**
	 * 投资金额下限
	 */
	private Long investMinAmount;
	/**
	 * 投资期限下限
	 */
	private Long investMinPeriod;
	/**
	 * 投资金额上限
	 */
	private Long investMaxAmount;
	/**
	 * 理财期限上限
	 */
	private Long investMaxPeriod;
	/**
	 * 状态 0-停用 1-启用
	 */
	private String ticketType;
	/**
	 * $column.comments
	 */
	private String status;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 修改人
	 */
	private String modifyBy;
	/**
	 * 创建日期
	 */
	private Date createTime;
	/**
	 * 修改日期
	 */
	private Date modifyTime;

}
