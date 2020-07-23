package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2019-03-15 17:24:07
 */
@Data
public class BusiBrand implements Serializable {

	/**
	 * 宣传类型：0文案；1图片
	 */
	private Long brandType;

	/**
	 * 文案
	 */
	private String content;

	/**
	 * 引用
	 */
	private String quote;

	/**
	 * 图片URL
	 */
	private String imgUrl;

	/**
	 * 显示日期
	 */
	private Date displayDate;

}
