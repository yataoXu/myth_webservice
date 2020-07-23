package com.zdmoney.models.sys;


import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_PARAMETER")
@Setter
@Getter
public class SysParameter extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String prTypename;

    private String prType;

    private String prName;

    private String prValue;

    private String prIsedit;

    private String prState;

    private Long prParentId;
}