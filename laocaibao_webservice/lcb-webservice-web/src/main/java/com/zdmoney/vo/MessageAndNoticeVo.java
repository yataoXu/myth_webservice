package com.zdmoney.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName MessageAndNoticeVo
 * @Description 消息公告列表vo
 * @Author huangcy
 * @Date 2018/8/27 9:35
 * @Version 1.0
 **/
@Data
public class MessageAndNoticeVo {

	private Integer id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 消息/公告状态 1：未读；2：已读
	 */
	private Integer status;
	/**
	 * 发布时间
	 */
	private String pubDate;
	/**
	 * 公告/消息标签 默认- 0:平台公告 1:会员福利 2:会员活动 3:其他
	 */
	private String contentType;

	/**
	 * h5 url
	 */
	private String url;

	/**
	 * 类型 (前端需要用来判断)
	 */
	private String type;
}
