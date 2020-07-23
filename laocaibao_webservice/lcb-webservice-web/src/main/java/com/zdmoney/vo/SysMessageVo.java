package com.zdmoney.vo;

import lombok.Data;

import java.util.Date;

/**
 * 消息公告详情vo
 * Created by 00245337 on 2016/8/23.
 */
@Data
public class SysMessageVo{

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 发布时间
	 */
	private Date pubDate;
    private String content;
    private String pubMan;
}
