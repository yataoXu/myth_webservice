package com.zdmoney.web.dto.mall;

import lombok.Data;

/**
 * Created by gaol on 2017/5/27
 **/
@Data
public class RiskTestDTO extends TaskCommonDTO{

    private int isRiskTest;

    private String riskTestUrl;
}
