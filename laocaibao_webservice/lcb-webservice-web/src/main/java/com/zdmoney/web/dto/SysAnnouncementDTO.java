package com.zdmoney.web.dto;
/**
 * 
* @ClassName: SysAnnouncementDTO 
* @Description: 系统公告传值类 
* @author CJ 
* @date 2015年6月25日 下午3:41:23 
*
 */
public class SysAnnouncementDTO {
	/**
	 * 公告id
	 */
	private String  id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 正文
	 */
	private String content;
	/**
	 * 公告类型
	 */
	private String announcementType;
	/**
	 * 发布人
	 */
	private String pubMan;
	/**
	 * 发布时间
	 */
	private String pubDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAnnouncementType() {
		return announcementType;
	}
	public void setAnnouncementType(String announcementType) {
		this.announcementType = announcementType;
	}
	public String getPubMan() {
		return pubMan;
	}
	public void setPubMan(String pubMan) {
		this.pubMan = pubMan;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	
	
}
