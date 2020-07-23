package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityFieldTypeHandler;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;

@Table(name = "CUST_INVITE_LINE")
@Setter
@Getter
public class CustInviteLine extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_CUST_INVITE_LINE.nextval from dual")
    private Long id;

    @Column(name = "CELLPHONE")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String cellphone;

    @Column(name = "INVITED_CELLPHONE")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String invitedCellphone;

    private String status;

    private Date actionTime;


    @Column(name = "invitee_id")
    private Long inviteeId; //被邀请人用户id

    @Column(name = "inviter_id")
    private Long inviterId; //介绍人用户id
}