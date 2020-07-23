package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现流水
 * Created by 46186 on 2019/3/7.
 */
@Data
public class WithdrawDto implements Serializable {

    private String tradeDate;

    private BigDecimal withdrawAmount;

    private String flowNum;

    private String status;

}
