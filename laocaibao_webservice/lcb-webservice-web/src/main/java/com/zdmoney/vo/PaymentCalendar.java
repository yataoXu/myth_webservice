package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.PriorityQueue;

/**
 * Created by 00225181 on 2016/4/6.
 */
@Getter
@Setter
public class PaymentCalendar {
    private BigDecimal totalPrincipalInterest;
    private Integer orderNum;
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;
    private String receiveMonth;
    private String receiveStatus;
}
