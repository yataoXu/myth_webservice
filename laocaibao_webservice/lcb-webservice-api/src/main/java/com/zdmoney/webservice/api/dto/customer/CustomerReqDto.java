package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/8/23 14:50
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class CustomerReqDto implements Serializable {

    private String startDate;

    private String endDate;

    private Integer pageNo = 1;

    private Integer pageSize = 20;

}
