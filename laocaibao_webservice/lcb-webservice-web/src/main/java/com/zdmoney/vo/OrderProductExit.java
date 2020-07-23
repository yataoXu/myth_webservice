package com.zdmoney.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderProductExit {
    private Long id;

    private String orderId;

    private Long customerId;

    private BigDecimal orderAmt;

    private Long productId;
    
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private Date dateToSettle;



}