package com.zdmoney.models.sys;


import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SYS_SWITCH")
@Getter
@Setter
public class SysSwitch extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String switchTypename;

    private String switchType;

    private String switchName;

    private String switchValue;

    private String switchIsdelete;
}