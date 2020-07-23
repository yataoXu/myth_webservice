package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ******声明*************
 * <p/>
 * 版权所有：zendaimoney
 * <p/>
 * 项目名称：laocaibao_webservice
 * 类    名称：SysAnnouncement
 * 功能描述：
 * <p/>
 * 创建人员：CJ
 * 创建时间：2015年6月26日
 *
 * @version *******修改记录************
 *          修改人员：
 *          修改时间：
 *          修改描述：
 */
@Table(name = "SYS_ANNOUNCEMENT")
@Getter
@Setter
public class SysAnnouncement extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select S_ANN_SEQ.nextval from dual")
    private Long id;

    private Integer announcementType;

    //前台页面日期控件，时间格式转换
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String openLogin;

    private String pubMan;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubDate;

    private String creator;

    private Integer status;

    private String title;

    private String content;

    private Short pushMessageStatus;

    private Short auditStatus;

    private String auditMan;//审核人


}