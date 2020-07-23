package com.zdmoney.models.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerAuthorizeInfo implements Serializable {

    private Long id;

    private Long customerId;

    private Integer authorizeStatus;

    private Date authorizeDate;
}
