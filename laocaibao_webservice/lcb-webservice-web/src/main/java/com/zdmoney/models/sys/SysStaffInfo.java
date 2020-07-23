package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityFieldTypeHandler;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;

@Table(name = "SYS_STAFF_INFO")
@Setter
@Getter
public class SysStaffInfo extends AbstractEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_CUST_INVITE_LINE.nextval from dual")
    private Long id;

    private String staffName;

    @Column(name = "STAFF_IDNUM")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String staffIdnum;

    private String staffAccountBankName;

    private String staffAccountBankBranch;

    @Column(name = "STAFF_ACCOUNT")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String staffAccount;

    private String staffAccountName;

    private String staffCode;

    private Date staffEntryTime;

    private Short staffStatus;

    private Short staffDismmiss;

}