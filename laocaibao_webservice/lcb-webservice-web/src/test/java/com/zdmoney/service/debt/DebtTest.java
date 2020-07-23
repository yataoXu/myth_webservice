package com.zdmoney.service.debt;

import com.alibaba.fastjson.JSON;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.match.MatchPvReqDto;
import com.zdmoney.assets.api.facade.subject.ILcbFinancialPlanFacadeService;
import com.zdmoney.base.BaseTest;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.service.BusiFinancePlanService;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.plan.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gaol on 2017/6/7
 **/
public class DebtTest extends BaseTest {

    @Autowired
    BusiFinancePlanService busiFinancePlanService;

    @Autowired
    private ILcbFinancialPlanFacadeService lcbFinancialPlanFacadeService;

    /**
     * 债权详情 列表
     */
    @Test
    public void debtTest(){
        DebtDetailReqDTO debtDetailDTO = new DebtDetailReqDTO();
        debtDetailDTO.setMainProductId(9460L);
        PageResultDto<BusiDebtDetailVO> page = busiFinancePlanService.queryDeptProductDetail(debtDetailDTO);
        System.out.println(page.toString());
    }

    /**
     * 资金详情 列表
     */
    @Test
    public void fundTest(){
        FundDetailReqDTO fundDetailDTO = new FundDetailReqDTO();
        PageResultDto<BusiFundDetailVO> page = busiFinancePlanService.queryFundDetail(fundDetailDTO);
        System.out.println(page.toString());
    }

    /**
     * 查询特殊理财人编号
     */
    @Test
    public void SpecialFinancePeopleCodeTest(){
        SpecialFinancialPlannerVO specialFinancialPlannerVO = busiFinancePlanService.querySpecialFinancePeopleCode();
        System.out.println(">>>>>special finance people code :" + JSON.toJSONString(specialFinancialPlannerVO));
    }

    @Test
    public void queryPvTest(){
        MatchPvReqDto matchPvReqDto = new MatchPvReqDto();
        matchPvReqDto.setPartnerNo(AppConstants.PARTNER_NO);
        matchPvReqDto.setSearchDate(new Date());
        AssetsResultDto<BigDecimal> resultDto = lcbFinancialPlanFacadeService.queryPv(matchPvReqDto);
        if (resultDto.isSuccess()) {
            System.out.println("//////////////////////////////////////////////////////");
        }
    }

}
