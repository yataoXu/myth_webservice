package com.zdmoney.models.lycj;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2018-12-25 17:46:00
 */
@Data
public class LycjPrepayment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的ID
	 */
	private String repayId;
	/**
	 * 标的ID
	 */
	private String id;
	/**
	 * 提前还款日期
	 */
	private String advancedTime;
	/**
	 * 实际借款天数
	 */
	private String actualPeriod;
	/**
	 * 提前还款总额
	 */
	private Long advancedAmount;
	/**
	 * 标的编号
	 */
	private String subjectNo;

}
