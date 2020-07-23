package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description : 实名
 * @ Date : Create in 2018/8/24 14:35
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class NameCertificateNotifyDto extends RecordDto {
    private String name;
    private String idCardNo;
}
