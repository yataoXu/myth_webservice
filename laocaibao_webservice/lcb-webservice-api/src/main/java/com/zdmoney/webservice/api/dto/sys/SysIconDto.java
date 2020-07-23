package com.zdmoney.webservice.api.dto.sys;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SysIconDto
 * @Description app icon
 * @Author huangcy
 * @Date 2018/12/11 11:18
 * @Version 1.0
 **/
@Data
public class SysIconDto implements Serializable {
	/**
	 * $column.comments
	 */
	private Long id;

	/**
	 * icon位置 1: 位置1 2:位置2 3:位置3 4:位置4
	 */
	private Integer location;
	/**
	 * 不点击图片地址
	 */
	private String defaultIconUrl;

	/**
	 * 点击图片地址
	 */
	private String checkedIconUrl;
}
