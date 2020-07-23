package com.zdmoney.models.wdty;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2018-09-03 10:58:13
 */
@Data
public class WdtyLoanUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的唯一编号(不为空,很重要)
	 */
	private String id;
	/**
	 * 投标人姓名
	 */
	private String username;
	/**
	 * 投标人ID
	 */
	private String userid;
	/**
	 * 投标金额
	 */
	private Double account;
	/**
	 * 投标时间
	 */
	private String add_time;
	/**
	 * 数据类型 1:个贷 2:优选/蓝枫赛殷 3:理财计划非转让 4理财计划转让
	 */
	private Long dataType;

}
