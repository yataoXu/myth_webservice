package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018/10/9.
 */
@Getter
@Setter
public class CustomerGrantInfoSearchDTO extends  CustomerSearchDTO {
    private String grantStatus;//0:未授权 1：授权充足 2：授权不足

    private String accountType;
}
