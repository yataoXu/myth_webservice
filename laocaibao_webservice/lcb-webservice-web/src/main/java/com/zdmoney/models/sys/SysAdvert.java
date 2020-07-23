package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_ADVERT")
@Getter
@Setter
public class SysAdvert extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String title;

    private String detail;

    private Date createDate;

    private Date startDate;

    private Date endDate;

    private String creator;

    private String auditStatus;

    private Date auditDate;

    private String auditor;

    private String status;

    private String imgUrl;

}