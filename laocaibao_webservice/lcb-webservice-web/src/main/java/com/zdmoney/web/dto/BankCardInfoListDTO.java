package com.zdmoney.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 00225181 on 2015/12/3.
 */
@Data
public class BankCardInfoListDTO implements Serializable{

    private List<BankCardInfoDTO> BankCardInfoList;

    private String lockDesc;

    private Boolean lockPay;

    private String accountBalance;

    private String extractAmount;
}
