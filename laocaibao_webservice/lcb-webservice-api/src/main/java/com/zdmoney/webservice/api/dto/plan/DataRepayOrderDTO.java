package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DataRepayOrderDTO implements Serializable {

    private Long userId;

    private String userNo;

    private String orderNo;

    private Long completeTime;

    private BigDecimal orderAmt;

    private Integer currTerm;
}
