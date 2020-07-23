package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/8/27 11:03
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class RiskEvaluateNotifyDto extends RecordDto {
    private String riskResult;
    private Date riskTime;
}
