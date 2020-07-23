package com.zdmoney.models.sys;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2018-10-24 08:44:19
 */
@Data
public class SysRequestLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 请求流水号
	 */
	private String transNo;
	/**
	 * 接口号
	 */
	private String methodCode;
	/**
	 * 请求参数
	 */
	private String reqParams;
	/**
	 * 响应结果
	 */
	private String rspResult;
	/**
	 * 响应时间, 单位毫秒
	 */
	private Long rspTime;
	/**
	 * 处理状态  1:新建  2: 处理中  3:处理成功  4:处理失败
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	/**
	 * 备注
	 */
	private String remark;

}
