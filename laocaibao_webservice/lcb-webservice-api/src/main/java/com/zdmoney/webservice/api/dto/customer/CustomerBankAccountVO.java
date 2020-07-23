package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2017/9/7.
 */
@Data
public class CustomerBankAccountVO implements Serializable{
    private Long id;

    private Long customerId;

    private String cbAccount;

    private String cbAccountName;

    private Short cbAccountType;

    private String cbBankCode;

    private String cbBankName;

    private String cbBranchName;

    private Long cbInputId;

    private Date cbInputDate;

    private Long cbModifyId;

    private Date cbModifyDate;

    private Short cbValid;

    private String cbMemo;

    private String cbSubBankCode;

    private String cbBindPhone;
}
