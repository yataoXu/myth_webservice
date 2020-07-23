package com.zdmoney.mapper;

import com.zdmoney.models.BusiRiskAssess;
import com.zdmoney.models.BusiRiskTest;
import com.zdmoney.webservice.api.dto.customer.RiskEvaluateNotifyDto;

import java.util.List;
import java.util.Map;

/**
 * Created by gaol on 2017/5/16
 **/
public interface BusiRiskAssessMapper {

    List<BusiRiskAssess> listBusiRiskAssess();

    void saveRiskAnswer(BusiRiskTest riskTest);

    long queryRiskTest(Long customerId);

    void updateRiskAnswer(BusiRiskTest riskTest);

    BusiRiskAssess queryCustomerRiskTestType(Long customerId);

    List<RiskEvaluateNotifyDto> queryRiskEvaluateNotify(Map<String,Object> map);

}
