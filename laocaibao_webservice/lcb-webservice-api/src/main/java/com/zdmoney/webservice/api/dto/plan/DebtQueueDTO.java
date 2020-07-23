package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by gaol on 2017/6/30
 **/
@Data
public class DebtQueueDTO implements Serializable{

    private String orderNo;

    /**
     * 装让类型
     * 1：到期转让
     * 2：提前退出转让
     */
    private String debtType;
}
