package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by gaol on 2017/7/28
 **/
@Data
public class BuyBackOrder implements Serializable {

    private String financeId;

    private String orderNo;

    private Long planId;
}
