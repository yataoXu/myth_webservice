package com.zdmoney.mapper.wdty;

import com.zdmoney.models.wdty.WdtyLoanUser;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-09-03 10:58:13
 */
public interface WdtyLoanUserMapper {

	List<WdtyLoanUser> queryWdtyLoanUser(Map<String, Object> paramsMap);

	List<WdtyLoanUser> queryWdtyLoanUserByInfo(Map<String, Object> paramsMap);

	int saveWdtyLoanUser(WdtyLoanUser wdtyLoanUser);

	int updateWdtyLoanUser(WdtyLoanUser wdtyLoanUser);

	int removeWdtyLoanUserById(long id);

	int countWdzyUserByDate();

	int insertLastDayWdzjUserData();
}
