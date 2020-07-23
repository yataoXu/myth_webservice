package com.zdmoney.mapper.wdty;

import com.zdmoney.models.wdty.WdtyEarlySettlement;
import com.zdmoney.models.wdty.WdtyLoanInfo;
import com.zdmoney.models.wdty.OrderRebate;
import com.zdmoney.webservice.api.dto.wdty.RebateOrderVo;
import com.zdmoney.webservice.api.dto.wdty.RebateReqDto;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-09-03 10:58:17
 */
public interface WdtyLoanInfoMapper {

	List<WdtyLoanInfo> queryWdtyLoanInfo(Map<String, Object> paramsMap);

	int saveWdtyLoanInfo(WdtyLoanInfo wdtyLoanInfo);

	int updateWdtyLoanInfo(WdtyLoanInfo wdtyLoanInfo);

	int removeWdtyLoanInfoById(long id);

	int countWdzyInfoByDate();

	int countWdzyPrepaymentByDate();

	int insertLastDayWdzjInfoData();

	int insertLastDayWdzjPrepaymentData();

	List<WdtyEarlySettlement> earlySettlementByDate(Map<String, Object> paramsMap);

	List<RebateOrderVo> getOrderRebates(RebateReqDto req);
}
