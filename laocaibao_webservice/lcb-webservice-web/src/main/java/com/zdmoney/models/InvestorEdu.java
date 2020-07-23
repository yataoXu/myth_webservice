package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author Gosling
 * @date 2018-08-20 14:17:17
 */
@Data
public class InvestorEdu implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	private Long id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 图片URL
	 */
	private String imgUrl;

	/**
	 * 来源
	 */
	private String infoSource;

	/**
	 * 摘要
	 */
	private String summary;

	/**
	 * 状态 0-未发布 1-已发布
	 */
	private String status;

	/**
	 * 创建日期
	 */
	private Date createDate;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 发布日期
	 */
	private Date publishDate;

	/**
	 * 发布人
	 */
	private String publishMan;

	/**
	 * 审核状态 0-未审核 1-已审核
	 */
	private String auditStatus;

	/**
	 * 置顶状态 0-未置顶 1-已置顶
	 */
	private String topStatus;

	/**
	 * 正文
	 */
	private String remark;

	/**
	 * 置顶时间
	 */
	private Date topDate;

	/**
	 * PC图片URL
	 */
	private String pcImgUrl;

}
