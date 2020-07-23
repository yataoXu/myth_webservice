package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_INFO_CENTER")
@Setter
@Getter
public class SysInfoCenter extends AbstractEntity<Long>{

    @Id
    private Long id;

    private String title;

    private String imgUrl;

    private String infoSource;

    private String summary;

    private String status;

    private Date createDate;

    private String creator;

    private Date publishDate;

    private String publishMan;

    private String auditStatus;

    private String topStatus;

    private String hiddenStatus;

    private String remark;

    private int viewNum;

    private String pcImgUrl;

    private String isRecommended;

    private String banner;

}