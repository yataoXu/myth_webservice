package com.zdmoney.webservice.api.dto.welfare;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/12/4 18:30
 * @Mail : xuyt@zendaimoney.com
 */

@Data
public class BusiCashTicketConfigDto implements Serializable {
    private Long id;
    //金额
    private BigDecimal amount;
    //有效天数
    private Integer period;
    private BigDecimal investMinAmount;
    private BigDecimal investMaxAmount;
    private Integer investMinPeriod;
    private Integer investMaxPeriod;
    //创建人
    private String createBy;
    //修改人
    private String modifyBy;
    //创建日期
    private Date createTime;
    //修改日期
    private Date modifyTime;
    //规则类型
    private String ticketType;
    //状态 0-停用 1-启用
    private String status;

}
