package com.zdmoney.models.customer;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "CUSTOMER_CENTER")
@Setter
@Getter
public class CustomerCenter extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String title;

    private String imgUrl;

    private String h5Url;

    private String canShare;

    private String mustLogin;

    private Date createDate;

    private Date startDate;

    private Date endDate;

    private String creator;

    private String auditStatus;

    private Date auditDate;

    private String auditor;

    private String status;

    private String isRead;

    private String subtitle;
    private String bubbleMark;
    private String pageType;
    private String recordType;
}