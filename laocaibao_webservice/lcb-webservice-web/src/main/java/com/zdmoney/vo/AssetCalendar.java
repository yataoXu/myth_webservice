package com.zdmoney.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 00250968 on 2017/11/27
 **/
@Data
public class AssetCalendar implements Serializable {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 单个产品本息
     */
    private BigDecimal principalInterest;

    /**
     * 到期日(1~31)
     */
    private Integer day;

}
