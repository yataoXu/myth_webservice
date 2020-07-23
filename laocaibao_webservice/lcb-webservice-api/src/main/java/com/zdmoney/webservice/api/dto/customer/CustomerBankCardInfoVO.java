package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

/**
 * Created by user on 2017/9/5.
 */
@Data
public class CustomerBankCardInfoVO extends CustomerInfoVO {
    private String cbAccount;

    private String cbBankName;

    private String cbBranchName;

    private String capitalZero;
}
