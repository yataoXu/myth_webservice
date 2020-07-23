package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by jb sun on 2016/3/17.
 */
@Setter
@Getter
public class LifeServiceOrder {
    private String id;
    private String name;
    private String consumeAmount;
    private String mobilePhone;
    private String orderTime;
    private String status;
    private String statusDesc;
    private String productSerialNo;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date expiredTime;
    private String lifeType;
}
