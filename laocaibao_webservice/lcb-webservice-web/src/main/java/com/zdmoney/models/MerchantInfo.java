package com.zdmoney.models;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
* MerchantInfo
* <p/>
* Author: jb sun
* Date: 2016-04-29 15:40:10
* Mail: sjb0223@hotmail.com
*/
@Table(name = "MERCHANT_INFO")
@Getter
@Setter
public class MerchantInfo extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_MERCHANT_INFO.nextval from dual")
    private Long id;

    /**
    * 商户码
    */
    private String merchantCode;

    /**
    * 商户名
    */
    private String merchantName;

    /**
    * 状态 0-停用 1-启用
    */
    private String status;

    /**
    * 备注
    */
    private String remark;

    /**
    * 显示状态 0-隐藏 1-显示
    */
    private String showStatus;

    /**
    * 创建时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}