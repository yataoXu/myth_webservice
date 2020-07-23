package com.zdmoney.webservice.api.dto.busi;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by user on 2018/9/28.
 */
@Getter
@Setter
public class CheckIfPurchaseAllowedDto implements Serializable {

    private Long customerId;

    private Long productId;

    private BigDecimal orderAmt;
}
