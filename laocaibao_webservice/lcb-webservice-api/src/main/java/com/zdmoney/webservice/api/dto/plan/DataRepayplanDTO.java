package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataRepayplanDTO implements Serializable {

    private Long userId;

    private String orderNo;

    private List<RepayPlan> repayPlanList;

}
