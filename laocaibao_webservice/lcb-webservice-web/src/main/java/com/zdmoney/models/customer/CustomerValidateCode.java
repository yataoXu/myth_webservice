package com.zdmoney.models.customer;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityFieldTypeHandler;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;

@Table(name = "CUSTOMER_VALIDATE_CODE")
@Getter
@Setter
public class CustomerValidateCode extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select CV_SEQ.nextval from dual")
    private Long id;

    private Long customerId;

    @Column(name = "CV_MOBILE")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String cvMobile;

    private String cvCode;

    private Integer cvType;

    private Date cvCreateTime;

    private Integer tryTime;

    private String channelCode;

}