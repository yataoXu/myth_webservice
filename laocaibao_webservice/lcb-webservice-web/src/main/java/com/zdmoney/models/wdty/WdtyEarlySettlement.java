package com.zdmoney.models.wdty;

import lombok.Data;

import java.io.Serializable;

/**
 * @date 2018-09-03 10:58:17
 */
@Data
public class WdtyEarlySettlement implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的唯一编号(不为空,很重要)
	 */
	private String id;

	/**
	 * 平台中文名称
	 */
	private String platform_name="捞财宝";

	/**
	 * 借款标的URL链接
	 */
	private String url;

	/**
	 * 标的标题信息
	 */
	private String title;
	/**
	 * 借款人姓名
	 */
	private String username;
	/**
	 * 借款人ID
	 */
	private String userid;
	/**
	 * 0,正在投标中的借款标;1,已完成(包括还款中和已完成的借款标)
	 */
	private Integer status;
	/**
	 * 借款类型 0.1：信用标-个人信贷 4 债权转让标
	 */
	private Float c_type;
	/**
	 * 借款金额 以元为单位,精度2位(1000.00)
	 */
	private Double amount;
	/**
	 * 借款年利率 精度4位,如:0.0910
	 */
	private Double rate;
	/**
	 * 借款期限
	 */
	private Integer period;

	/**
	 * 0 代表天,1 代表月.
	 */
	private Integer p_type=0;
	/**
	 * 还款方式1 按月等额本息还款 4,按月计息,一次性还本付息
	 */
	private Integer pay_way;
	/**
	 * 完成百分比
	 */
	private Double process;
	/**
	 * 提前还款时间
	 */
	private String prepayment_time;

	/**
	 * 计息截止日期
	 */
	private String accrual_end_time;
	/**
	 * 满标时间
	 */
	private String end_time;
	/**
	 * 提前还款类型
	 */
	private Integer prepayment_type = 0;
	/**
	 * 提前还款金额
	 */
	private Double prepayment_amount;
	/**
	 * 提前还款利息
	 */
	private Double prepayment_accrual;

	/**
	 * 转让状态 0:非转让 1:转让
	 */
	private String transferStatus;
	/**
	 * 数据类型 1:个贷 2:优选/蓝枫赛殷 3理财计划 4理财计划转让
	 */
	private Integer dataType;
	/**
	 * 原始ID
	 */
	private String planId;
}
