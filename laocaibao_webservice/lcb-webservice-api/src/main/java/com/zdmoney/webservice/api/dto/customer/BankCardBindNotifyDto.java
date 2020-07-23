package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description : 绑卡
 * @ Date : Crreate in 2018/8/24 11:39
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class BankCardBindNotifyDto implements Serializable {
    private String userId;
    private String cardNo;
    private Date bindCardTime;
    private String bindCardStatus;
}
