package com.zdmoney.facade;

import com.google.common.collect.Lists;
import com.zdmoney.base.JUnitActionBase;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.plan.HRbidTenderCollectDto;
import com.zdmoney.webservice.api.dto.plan.MatchResultDto;
import com.zdmoney.webservice.api.dto.plan.MatchSucResult;
import com.zdmoney.webservice.api.facade.IFinancePlanFacadeService;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancePlanFacadeServiceTest extends JUnitActionBase{

//	@Autowired
	private IFinancePlanFacadeService financePlanFacadeService = JUnitActionBase.context.getBean(IFinancePlanFacadeService.class);



	@Test
	public void insertDebtDetail() {
		MatchResultDto matchResultDto = new MatchResultDto();
		matchResultDto.setMatchAmount(new BigDecimal(5001.45));
		matchResultDto.setProductId(9821L);
		matchResultDto.setBatchNo("2017072417185998213887");
		matchResultDto.setSerialNo("L2017111717185998213887");

		List<MatchSucResult> matchSucResultList = new ArrayList<MatchSucResult>();
		MatchSucResult matchSucResult = new MatchSucResult();
		matchSucResult.setCapitalAmount(new BigDecimal(100));
		matchSucResult.setCapitalCode("1000");
		matchSucResult.setCapitalType("0");
		matchSucResult.setFinanceId("2075");
		matchSucResult.setLedgerId("01170707000002869");
		matchSucResult.setPriority("3");
		matchSucResult.setStatus("03000001");
		matchSucResult.setCreateTime(new Date());
		matchSucResult.setEarningsRate(new BigDecimal(0.105));
		matchSucResult.setTotalTerm("19");
		matchSucResult.setProductCode("1000000620");
		matchSucResult.setSubjectNo("ZDAT_BD_00000682");
		matchSucResult.setSubjectAmt("5000");
		matchSucResult.setLoanCustomerNo("01170707000002869");
		matchSucResult.setLoanCustomerName("董倩如");
		matchSucResult.setDebtType("1");
		matchSucResult.setManFinanceId("2046");
		matchSucResult.setMatchOrderCode("2017072417185902556");
		matchSucResult.setBorrowerDate(new Date());
		matchSucResult.setInitOrderNum("C8888133537201706301010074769");
		matchSucResult.setDebtWorth(new BigDecimal(100));
		matchSucResult.setFinanceNum(1);
		matchSucResult.setTransferNum(1);
		matchSucResultList.add(matchSucResult);

		List<MatchSucResult> abnormalMatchSucResult = Lists.newArrayList();
		MatchSucResult abnorMatchSucResult = new MatchSucResult();
		abnorMatchSucResult.setCapitalAmount(new BigDecimal(100));
		abnorMatchSucResult.setCapitalCode("1000");
		abnorMatchSucResult.setCapitalType("0");
		abnorMatchSucResult.setFinanceId("2075");
		abnorMatchSucResult.setLedgerId("01170707000002869");
		abnorMatchSucResult.setPriority("3");
		abnorMatchSucResult.setStatus("03000001");
		abnorMatchSucResult.setCreateTime(new Date());
		abnorMatchSucResult.setEarningsRate(new BigDecimal(0.105));
		abnorMatchSucResult.setTotalTerm("19");
		abnorMatchSucResult.setProductCode("1000000620");
		abnorMatchSucResult.setSubjectNo("ZDAT_BD_00000682");
		abnorMatchSucResult.setSubjectAmt("5000");
		abnorMatchSucResult.setLoanCustomerNo("01170707000002869");
		abnorMatchSucResult.setLoanCustomerName("董倩如");
		abnorMatchSucResult.setDebtType("1");
		abnorMatchSucResult.setManFinanceId("2046");
		abnorMatchSucResult.setMatchOrderCode("2017072417185902556");
		abnorMatchSucResult.setBorrowerDate(new Date());
		abnorMatchSucResult.setInitOrderNum("C8888133537201706301010074769");
		abnorMatchSucResult.setDebtWorth(new BigDecimal(100));
		abnorMatchSucResult.setFinanceNum(1);
		abnorMatchSucResult.setTransferNum(1);
		abnormalMatchSucResult.add(abnorMatchSucResult);

		matchResultDto.setMatchSucResultList(matchSucResultList);
		matchResultDto.setAbnormalMatchSucResult(abnormalMatchSucResult);
		ResultDto resultDto = financePlanFacadeService.matchResultOrder(matchResultDto);
		System.out.println(resultDto.isSuccess());
	}


	@Test
	public void generateProductTask() {
		financePlanFacadeService.generateProductTask();
	}


	@Test
	public void bidTenderCollect() {
		HRbidTenderCollectDto hRbidTenderCollectDto = new HRbidTenderCollectDto();
		hRbidTenderCollectDto.setPersonAccount("01190108000005617");
		hRbidTenderCollectDto.setBidNo("wn_test1");
		financePlanFacadeService.bidTenderCollect(hRbidTenderCollectDto);
	}




}