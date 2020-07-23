package com.zdmoney.webservice.api.dto.Asset;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BusiTradeFlowDTO implements Serializable {
    private String trdDate;
    private BigDecimal trdAmt;
    private String flowNum;
}
