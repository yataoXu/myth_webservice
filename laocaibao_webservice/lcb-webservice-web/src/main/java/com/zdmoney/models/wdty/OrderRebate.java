package com.zdmoney.models.wdty;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName OrderRebate
 * @Description 订单返利dto
 * @Author huangcy
 * @Date 2018/9/13 18:00
 * @Version 1.0
 **/
@Data
public class OrderRebate {

	/**
	 * 用户注册手机号
	 */
	private String mobile;

	/**
	 * 用户注册用户名
	 */
	private String username;

	/**
	 * 注册时间戳
	 */
	private Date reg_time;

	/**
	 * 订单号
	 */
	private String order_id;

	/**
	 * 订单状态 1已投资 2已起息 3流标 4已还款
	 */
	private Integer status;

	/**
	 * 投资金额
	 */
	private Float amount;

	/**
	 * 标的ID
	 */
	private String bid_id;

	/**
	 * 标的名称
	 */
	private String bid_name;

	/**
	 * 标的利率%数额部分
	 */
	private Float rate;

	/**
	 * 还款方式
	 */
	private Integer pay_way;

	/**
	 * 投资期限数额
	 */
	private Integer period;

	/**
	 * 投资期限单位 1月 2天
	 */
	private Integer period_type = 2;

	/**
	 * 投资时间时间戳（秒）
	 */
	private Date trade_time;

	/**
	 * 起息时间时间戳（秒），状态起息时必填
	 */
	private Date start_time;

	/**
	 * 是否计费标投资 1是 0否
	 */
	private Integer is_bill = 1;
}
