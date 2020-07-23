package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 出借凭证验证码表
 * Created by 46186 on 2018/6/21.
 */
@Table(name = "BORROW_VALIDATE_CODE")
@Data
public class BorrowValidateCode extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select CV_SEQ.nextval from dual")
    private Long id;

    private Long customerId;

    private String cvEmail;

    private String cvCode;

    private Integer cvType;

    private Date cvCreateTime;

    private Date cvExpireTime;

    private Integer tryTime;
}
