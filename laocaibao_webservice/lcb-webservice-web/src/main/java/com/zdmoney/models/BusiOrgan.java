package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BUSI_ORGAN")
@Getter
@Setter
public class BusiOrgan extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String orgName;

    private String inviteCode;

    private String rebateRate;

    private Date createDate;

    private String auditStatus;

    private String remark;
}