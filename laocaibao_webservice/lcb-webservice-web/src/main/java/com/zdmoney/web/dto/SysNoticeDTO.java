package com.zdmoney.web.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class SysNoticeDTO implements Serializable {

	private Long id;

	/**
	 * h5跳转链接
	 */
    private String url = "";
	/**
	 * 标题
	 */
	private String title = "";
	/**
	 *公告标签 默认-0:其他 1:紧急公告 2:会员活动 3:会员福利
	 */
	private String contentType = "";
	/**
	 * 系统公告UI更新
	 */
	private String shareRedPackUrl = "";
}
