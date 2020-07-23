package com.zdmoney.models.financePlan;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gaol on 2017/6/7
 **/
@Data
public class BusiFundDetail implements Serializable {

    private Long id;

    /**
     * 用户编号
     */
    private String cmNumber;

    /**
     * 手机号
     */
    private String cmCellPhone;

    /**
     * 优先级
     */
    private Long priority;

    /**
     * 金额
     */
    private BigDecimal orderAmt;

    /**
     * 期限
     */
    private Long closeDay;

    /**
     * 资金类型 1-新出借 2-复投
     */
    private Integer fundType;

    /**
     * 理财计划ID
     */
    private Long productId;

    /**
     * 主订单编号
     */
    private Long orderId;

    /**
     * 预期年化收益率
     */
    private Double yearRate;

    /**
     * 发起时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date launchDate;

    /**
     * 撮合时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date matchDate;

    /**
     * 状态 1-待撮合 2-已撮合
     */
    private Integer status;

    /**
     * 批次号
     */
    private String diskNo;

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


    /**
     * 撮合中金额
     */
    private BigDecimal matchingAmt;

}
