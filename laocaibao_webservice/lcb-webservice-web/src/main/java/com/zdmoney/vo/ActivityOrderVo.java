package com.zdmoney.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by silence.cheng on 2016/9/19.
 */
@Data
public class ActivityOrderVo {

    private String inRank; //是否在排名内

    private String isBuy; //是否购买

    private int selfRank;//个人排名

    private List orderPayList;//排名列表

    private BigDecimal totalAmt;

    private String  customerId;


}
