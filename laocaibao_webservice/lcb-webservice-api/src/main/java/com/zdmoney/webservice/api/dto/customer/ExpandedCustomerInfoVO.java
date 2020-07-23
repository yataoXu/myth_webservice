package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by user on 2017/9/5.
 */
@Data
public class ExpandedCustomerInfoVO extends CustomerInfoVO{

    private String introducerName;

    private String introducerCode;

    private String plannerName;

    private String cbAccount;

    private String cbBankName;

    private String cbBankCode;

    private String cbBranchName;

    //private String capitalZero;

    private Long bankRecordId;

    private String cbValid;

    private String owner;

    private String ownerType;

    private String ownerCode;

}
