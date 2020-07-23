package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataOrderInfoDTO implements Serializable {

    private String orderNo;

    private Long customerId;

    private Long completeDate;

}