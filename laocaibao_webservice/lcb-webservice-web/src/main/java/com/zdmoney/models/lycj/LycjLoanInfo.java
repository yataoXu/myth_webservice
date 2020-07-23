package com.zdmoney.models.lycj;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2018-12-25 17:28:39
 */
@Data
public class LycjLoanInfo implements Serializable {

	private static final Long serialVersionUID = 1L;

	/**
	 * 标的ID
	 */
	private String id;
	/**
	 * 标的名称
	 */
	private String title;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户id
	 */
	private String userid;
	/**
	 * 标的金额
	 */
	private Double amount;
	/**
	 * 借款期限
	 */
	private String borrowPeriod;
	/**
	 * 利率
	 */
	private Double interest;
	/**
	 * 资产类型
	 */
	private String assetType;
	/**
	 * 还款方式
	 */
	private String repayType;
	/**
	 * 完成百分比
	 */
	private Double percentage;
	/**
	 * 标状态
	 */
	private String bidState;
	/**
	 * 发标时间
	 */
	private String verifyTime;
	/**
	 * 成功时间
	 */
	private String reverifyTime;
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
