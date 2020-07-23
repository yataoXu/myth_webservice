package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 00225181 on 2015/12/4.
 */
@Setter
@Getter
public class AccountBalanceDTO {
    /*账户余额*/
    private BigDecimal accountBalance;

    /*体现中金额*/
    private BigDecimal withdrawBalance;

    /*当前页数*/
    private int pageNo;

    /*总页数*/
    private int totalPage;

    /*总条数*/
    private int totalRecord;

    /*账户流水*/
    private List<AccountRecordDTO> accountRecordList = Lists.newArrayList();

}
