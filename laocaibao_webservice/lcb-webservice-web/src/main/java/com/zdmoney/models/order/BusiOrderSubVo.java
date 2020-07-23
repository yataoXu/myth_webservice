package com.zdmoney.models.order;

import com.zdmoney.models.financePlan.BusiOrderSub;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by user on 2017/6/22.
 */
@Data
public class BusiOrderSubVo extends BusiOrderSub {

    private String parentOrderNum;

    private Integer leftDays;


    private Integer currTerm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date repayDay;

}
