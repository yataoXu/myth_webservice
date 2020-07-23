package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_MESSAGE_LOG")
@Getter
@Setter
public class SysMessageLog extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select S_MEG_SEQ.nextval from dual")
    private Long id;

    private String meContent;

    private String meTelNum;

    private Date meSendTime;

    private Short meResult;

    private String meResponse;

    private String mePlatform;

}