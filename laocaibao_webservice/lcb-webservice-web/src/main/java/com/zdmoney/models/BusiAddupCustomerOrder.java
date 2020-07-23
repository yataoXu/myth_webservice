package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BusiAddupCustomerOrder extends AbstractEntity<Long>{
    private Long id;

    private String customerNo;

    private String yearMonth;

    private BigDecimal orderAmt;

    private BigDecimal orderNum;

    private int inviteFirstInvest;

}