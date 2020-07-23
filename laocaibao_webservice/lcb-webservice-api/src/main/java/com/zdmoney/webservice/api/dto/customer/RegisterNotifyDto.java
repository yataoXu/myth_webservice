package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/8/23 14:23
 * @Mail : xuyt@zendaimoney.com
 */

@Data
public class RegisterNotifyDto extends RecordDto {
    private String telPhone;
    private String userType;
    private String ipAddress;
    private String registerStatus;
    private String userTag;
}
