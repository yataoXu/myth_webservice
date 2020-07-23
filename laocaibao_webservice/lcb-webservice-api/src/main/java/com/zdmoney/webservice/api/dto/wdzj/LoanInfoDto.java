package com.zdmoney.webservice.api.dto.wdzj;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoanInfoDto implements Serializable {

    private static final long serialVersionUID = -2303839272191470652L;
    private String date;

    private String page;

    private String pageSize;
}
