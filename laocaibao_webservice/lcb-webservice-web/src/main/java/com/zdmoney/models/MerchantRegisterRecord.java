package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
* MerchantRegisterRecord
* <p/>
* Author: jb sun
* Date: 2016-04-29 15:34:57
* Mail: sjb0223@hotmail.com
*/
@Table(name = "MERCHANT_REGISTER_RECORD")
@Getter
@Setter
public class MerchantRegisterRecord extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_MERCHANT_REGISTER_RECORD.nextval from dual")
    private Long id;

    /**
    * 商户编号
    */
    private Object merchantNo;

    /**
    * 用户编号
    */
    private Object customerNo;

    /**
    * 注册日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate;

    /**
    * 验证码
    */
    private Object validCode;

    private String merchantType;
}