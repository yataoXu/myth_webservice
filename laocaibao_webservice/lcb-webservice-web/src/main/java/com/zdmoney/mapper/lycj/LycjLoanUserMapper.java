package com.zdmoney.mapper.lycj;

import com.zdmoney.models.lycj.LycjLoanUser;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-12-25 17:28:57
 */
public interface LycjLoanUserMapper {

    List<LycjLoanUser> queryLycjLoanUser(Map<String, Object> paramsMap);

    int saveLycjLoanUser(LycjLoanUser lycjLoanUser);

    int updateLycjLoanUser(LycjLoanUser lycjLoanUser);

    int removeLycjLoanUserById(long id);
}
