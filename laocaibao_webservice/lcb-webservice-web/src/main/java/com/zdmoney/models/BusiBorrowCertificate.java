package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 出借凭证表
 * Created by qinzhuang on 2018/6/12.
 */

@Table(name = "BUSI_BORROW_CERTIFICATE")
@Data
public class BusiBorrowCertificate extends AbstractEntity<Long> {
    @Id
    private Long id;
    //用户编号
    private String cmNumber;
    //创建时间
    private Date createDate;
    //过期时间
    private Date expireDate;
    //用户姓名
    private String cmName;
    //手机号
    private String cellphone;
    //金额
    private BigDecimal money;
    //来源
    private String origin;
}
