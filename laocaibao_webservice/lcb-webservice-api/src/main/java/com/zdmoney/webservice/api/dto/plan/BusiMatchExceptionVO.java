package com.zdmoney.webservice.api.dto.plan;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yangp on 2017/10/17
 **/
@Data
public class BusiMatchExceptionVO implements Serializable {

    private Long id;
    /**
     * 主订单号
     */
    private String orderNum;

    /**
     * 理财计划ID
     */
    private Long productId;

    /**
     * 主订单编号
     */
    private Long orderId;


    /**
     * 状态 0-待处理 1-已处理
     */
    private String status;

    /**
     * 资金编号
     */
    private String fundId;

    /**
     * 撮合债权编号
     */
    private String financeId;

    /**
     * 创建日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 修改日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;


}
