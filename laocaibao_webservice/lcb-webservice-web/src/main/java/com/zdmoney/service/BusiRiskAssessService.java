package com.zdmoney.service;

import com.zdmoney.models.BusiRiskAssess;
import com.zdmoney.models.BusiRiskTest;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.RiskEvaluateNotifyDto;

import java.util.List;
import java.util.Map;

/**
 * Created by gaol on 2017/5/16
 **/
public interface BusiRiskAssessService {

    List<BusiRiskAssess> listBusiRiskAssess();

    void saveRiskAnswer(BusiRiskTest riskTest);

    long queryRiskTest(Long customerId);

    void updateRiskAnswer(BusiRiskTest riskTest);

    BusiRiskAssess queryCustomerRiskTestType(Long customerId);

    PageResultDto<RiskEvaluateNotifyDto> queryRiskEvaluateNotify(Map<String, Object> map);

}
