package com.zdmoney.webservice.api.dto.channel;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 46186 on 2019/2/21.
 */
@Data
public class WithdrawFlowsDto implements Serializable {

    private String tradeDate;

    private BigDecimal withdrawAmount;

    private String flowNum;

    private String status;
}
