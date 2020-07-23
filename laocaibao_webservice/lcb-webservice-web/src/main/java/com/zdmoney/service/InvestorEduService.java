package com.zdmoney.service;

import com.zdmoney.models.InvestorEdu;
import java.util.List;
import java.util.Map;

/**
 * @author lCB
 * @email LCB@zendaimoney.com
 * @date 2018-08-20 14:17:17
 */
public interface InvestorEduService {

    List<InvestorEdu> queryInvestorEdu(Map<String, Object> paramsMap);

    int queryInvestorEduCount(Map<String, Object> paramsMap);

}

