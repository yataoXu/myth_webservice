package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/9/5 15:41
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class RecordDto implements Serializable {
    @NotNull(
            message = "用户编号不能为空"
    )
    private String userNo;
    private String userId;
    private Date recordTime;
}
