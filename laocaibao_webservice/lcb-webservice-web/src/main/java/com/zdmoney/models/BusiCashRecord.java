package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2018-11-30 15:39:13
 */
@Data
public class BusiCashRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 用户编号
	 */
	private String cmNumber;
	/**
	 * 现金券id
	 */
	private String cashCouponId;
	/**
	 * 现金券类型 0：注册 1：投资 2：回款 
	 */
	private String cashCouponType;
	/**
	 * 发放状态：0 未发 1：已发
	 */
	private String status;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
	 */
	private Date modifyDate;
	/**
	 * 过期时间
	 */
	private Date expireDate;

}
