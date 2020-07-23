package com.zdmoney.facade;

import com.alibaba.fastjson.JSON;
import com.zdmoney.base.JUnitActionBase;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.product.BusiSubjectDto;
import com.zdmoney.webservice.api.facade.ISubjectinfoFacadeService;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubjectinfoFacadeServiceTest extends JUnitActionBase {

//	@Autowired
	ISubjectinfoFacadeService subjectInfoFacadeService =JUnitActionBase.context.getBean(ISubjectinfoFacadeService.class);


	@Test
	public void pushBidForWacai() {
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );

		try{

			BusiSubjectDto busiSubjectDto = new BusiSubjectDto();
			busiSubjectDto.setSubjectNo("wn_test111");
			busiSubjectDto.setLastExpire(new Date());
			busiSubjectDto.setPayEndTime("不得迟于截止日期16:00前");
			busiSubjectDto.setCollectAmount(new BigDecimal(2000));
			busiSubjectDto.setSaleStartDate(sdf.parse("2019-01-01"));
			busiSubjectDto.setSaleEndDate(sdf.parse("2019-02-02"));
			busiSubjectDto.setInterestStartDate(new Date());
			busiSubjectDto.setInterestEndDate(sdf.parse("2019-12-12"));
			busiSubjectDto.setYearRate(new BigDecimal(0.12));
			busiSubjectDto.setRepaymentTerms(2);
			busiSubjectDto.setRepayType("AVERAGE_CAPITAL_INTEREST");
			busiSubjectDto.setBorrowerName("猪八戒");
			busiSubjectDto.setIdNo("320311198302284915");
			busiSubjectDto.setProductInterest(new BigDecimal(6565665.32));
			busiSubjectDto.setCmNumber("01190108000005617");
			busiSubjectDto.setCreateDate(new Date());
			busiSubjectDto.setLiabilitiesRate(new BigDecimal(6565665.32));
			busiSubjectDto.setBorrowerType("PERSONAL");
			busiSubjectDto.setBorrowUse("借钱借钱借钱借钱借钱借钱");
			ResultDto resultDto = subjectInfoFacadeService.pushBidForWacai(busiSubjectDto);
			System.out.println(JSON.toJSONString(resultDto));
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	@Test
	public void searchWacaiBidBySubjectNo() {
		try{
			ResultDto resultDto = subjectInfoFacadeService.searchWacaiBidBySubjectNo("wn_tes1");
			System.out.println(JSON.toJSONString(resultDto));
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Test
	public void searchOrderSubsBySubjectNo() {
		try{
			ResultDto resultDto = subjectInfoFacadeService.searchOrderSubsBySubjectNo("ZDAT_WCBD_0000111");
			System.out.println(JSON.toJSONString(resultDto));
		}catch (Exception e){
			e.printStackTrace();
		}

	}



}