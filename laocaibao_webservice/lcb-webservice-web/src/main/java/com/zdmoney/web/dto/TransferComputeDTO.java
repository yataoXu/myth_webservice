package com.zdmoney.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferComputeDTO {

    private String tips="";

    private BigDecimal transferRate = BigDecimal.ZERO;

    private BigDecimal transferFee = BigDecimal.ZERO;

    private BigDecimal expectAmt = BigDecimal.ZERO;

}
