package com.zdmoney.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wu.hg on 2016/6/22.
 */
@Data
public class UserRepaymentVo {

    private BigDecimal unreceivedInterest;

    private BigDecimal receivedInterest;

    private BigDecimal unreceivedPrincipal;

    private BigDecimal receivedPrincipal;

}
