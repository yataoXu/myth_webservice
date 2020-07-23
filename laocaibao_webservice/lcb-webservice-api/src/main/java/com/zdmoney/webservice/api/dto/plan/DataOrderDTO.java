package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DataOrderDTO implements Serializable {

    private Long userId;

    private String userNo;

    private String orderNo;

    private BigDecimal orderAmount;

    private Date orderTime;

    private Integer productType;

    private String tenderDevice;

}
