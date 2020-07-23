package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2019-03-13 11:08:53
 */
@Data
public class BusiStaffWhilte implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 编号
	 */
	private Long id;

	/**
	 * 用户编号
	 */
	private String customerId;

	/**
	 * 员工姓名
	 */
	private String staffName;

	/**
	 * 员工工号
	 */
	private String staffCode;

	/**
	 * 员工入职时间
	 */
	private Date staffEntryTime;

	/**
	 * 0在职1离职`
	 */
	private Long staffStatus;

	/**
	 * 所在公司
	 */
	private Long staffComp;

	/**
	 * 离职时间
	 */
	private Date staffDismmissTime;

}
