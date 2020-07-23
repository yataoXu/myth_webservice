package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

@Data
public class DataOrderReqDTO implements Serializable {

    @NotEmpty(message = "开始时间不能为空")
    private String startDate;

    @NotEmpty(message = "结束时间不能为空")
    private String endDate;

    private int pageNo = 1;

    private int pageSize = 20;
}
