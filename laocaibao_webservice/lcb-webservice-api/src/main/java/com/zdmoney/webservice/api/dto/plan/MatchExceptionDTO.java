package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by user on 2017/9/5.
 */
@Data
public class MatchExceptionDTO implements Serializable{
    private String status;

    private String orderNum;

    private String productId;

    private int pageSize = 20;

    private int pageNo = 1;

}
