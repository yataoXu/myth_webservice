package com.zdmoney.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by gaol on 2017/5/16
 **/
@Data
public class BusiRiskAssess {

    private Long id;

    private Long pointMin;

    private Long pointMax;

    private String type;

    private String explain;

    private String synopsis;

    // 1:是  0: 否
    private Integer isFirst;

    private Long awardCoin;

    //是否已下单
    private Integer consumed;
    //是否已签章  0-未签约 1-已签约
    private Integer signContract;
    //是否可以签章 0-不可以签约 1-可以签约
    private Integer canSignContract;

    private Long customerId;

    @JSONField(format = "yyyy-MM-dd")
    private Date riskExpireTime;
}
