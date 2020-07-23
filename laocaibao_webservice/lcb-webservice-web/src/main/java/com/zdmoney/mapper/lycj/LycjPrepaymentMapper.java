package com.zdmoney.mapper.lycj;


import com.zdmoney.models.lycj.LycjPrepayment;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-12-25 17:46:00
 */
public interface LycjPrepaymentMapper {

    List<LycjPrepayment> queryLycjPrepayment(Map<String, Object> paramsMap);

    int saveLycjPrepayment(LycjPrepayment lycjPrepayment);

    int updateLycjPrepayment(LycjPrepayment lycjPrepayment);

    int removeLycjPrepaymentById(long id);


}
