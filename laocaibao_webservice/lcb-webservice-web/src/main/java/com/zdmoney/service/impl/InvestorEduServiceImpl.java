package com.zdmoney.service.impl;

import com.zdmoney.mapper.InvestorEduMapper;
import com.zdmoney.models.InvestorEdu;
import com.zdmoney.service.InvestorEduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Gosling
 * @date 2018-08-20 14:17:17
 */
@Service("investorEduService")
public class InvestorEduServiceImpl implements InvestorEduService {

    @Autowired
    private InvestorEduMapper investorEduMapper;

    @Override
    public List<InvestorEdu> queryInvestorEdu(Map<String, Object> paramsMap){
        return investorEduMapper.queryInvestorEdu(paramsMap);
    }

    @Override
    public int queryInvestorEduCount(Map<String, Object> paramsMap) {
        return investorEduMapper.queryInvestorEduCount(paramsMap);
    }

}
