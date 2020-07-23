package com.zdmoney.webservice.api.dto.busi;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 46186 on 2018/6/19.
 */
@Data
public class BusiBorrowCertificateDto implements Serializable {
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
