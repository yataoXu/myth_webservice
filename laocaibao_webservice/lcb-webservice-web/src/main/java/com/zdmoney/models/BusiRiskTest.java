package com.zdmoney.models;

import lombok.Data;

import java.util.Date;

/**
 * Created by gaol on 2017/5/16
 **/
@Data
public class BusiRiskTest {

    private Long id;

    private Long customerId;

    private String answerResult;

    private Long isFirst;

    private String type;

    private Date insertTime;

    private Date updateTime;


}
