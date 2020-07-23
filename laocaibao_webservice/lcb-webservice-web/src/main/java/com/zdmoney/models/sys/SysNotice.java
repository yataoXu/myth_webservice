package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_NOTICE")
@Setter
@Getter
public class SysNotice extends AbstractEntity<Long> {

    @Id
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private String creator;

    private Short status;

    private String title;

    private String content;

    private Short auditStatus;

    private String auditMan;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date auditDate;

    private String remark;

	/**
	 * 公告标签 默认-0:其他 1:紧急公告 2:会员活动 3:会员福利
	 */
	private String noticeLabel;

	private String summary;

}