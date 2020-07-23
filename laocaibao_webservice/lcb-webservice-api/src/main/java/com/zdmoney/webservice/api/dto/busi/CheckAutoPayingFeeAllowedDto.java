package com.zdmoney.webservice.api.dto.busi;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by user on 2018/9/27.
 */
@Setter
@Getter
public class CheckAutoPayingFeeAllowedDto implements Serializable {

    private Long customerId;

    private String fuiouLoginId;

    private BigDecimal feeAmount;

}
