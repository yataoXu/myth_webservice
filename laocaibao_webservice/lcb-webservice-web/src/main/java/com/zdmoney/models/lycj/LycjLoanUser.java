package com.zdmoney.models.lycj;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2018-12-25 17:28:57
 */
@Data
public class LycjLoanUser implements Serializable {

	private static final Long serialVersionUID = 1L;

	/**
	 * 标的ID
	 */
	private String investId;
	/**
	 * 标的ID
	 */
	private String id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户ID
	 */
	private String userid;
	/**
	 * 投标金额
	 */
	private Double money;
	/**
	 * 有效金额
	 */
	private Double account;
	/**
	 * 投标状态
	 */
	private String status;
	/**
	 * 投标时间
	 */
	private String addTime;
	/**
	 * 产品类型 1:散标 2:智投宝 3:转让
	 */
	private Integer productType;
	/**
	 * 数据来源 0:默认 1:个贷 2:优选/蓝枫 3:理财计划非转让 4:理财计划转让 5:优选拆分的数据 7:优转智拆分数据
	 */
	private Integer dataType;
	/**
	 * 原产品ID
	 */
	private String planId;

}
