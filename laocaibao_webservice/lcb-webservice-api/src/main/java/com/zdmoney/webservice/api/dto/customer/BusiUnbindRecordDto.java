package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/9/6 12:52
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class BusiUnbindRecordDto extends RecordDto{

    private String ubBankCode;
    private String ubTelephone;
}
