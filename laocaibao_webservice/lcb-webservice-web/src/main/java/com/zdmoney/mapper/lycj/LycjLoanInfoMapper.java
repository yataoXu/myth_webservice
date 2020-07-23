package com.zdmoney.mapper.lycj;


import com.zdmoney.models.lycj.LycjLoanInfo;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-12-25 17:28:39
 */
public interface LycjLoanInfoMapper {

    List<LycjLoanInfo> queryLycjLoanInfo(Map<String, Object> paramsMap);

    int saveLycjLoanInfo(LycjLoanInfo lycjLoanInfo);

    int updateLycjLoanInfo(LycjLoanInfo lycjLoanInfo);

    int removeLycjLoanInfoById(long id);
}
