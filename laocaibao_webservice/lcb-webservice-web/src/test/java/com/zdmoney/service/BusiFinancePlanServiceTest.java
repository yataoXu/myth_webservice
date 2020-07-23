package com.zdmoney.service;


import com.zdmoney.base.BaseTest;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.plan.MatchSpecialReqDto;
import com.zdmoney.webservice.api.dto.plan.SpecialTransferDebtDTO;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 00232384 on 2017/7/13.
 */
public class BusiFinancePlanServiceTest extends BaseTest{

    @Autowired
    private  BusiFinancePlanService busiFinancePlanService;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @org.junit.Test
    public void buyBack() throws Exception {
        /*boolean flag = busiFinancePlanService.buyBack("ZDAT_BD_00000662");
        Assert.assertTrue(flag);*/
    }

    @org.junit.Test
    public void matchSpecialResult(){
        MatchSpecialReqDto dto = new MatchSpecialReqDto();
        dto.setBatchNo("1234");
        dto.setProductId(9741L);
        dto.setSuperfluousAmount(new BigDecimal("3000"));
        ResultDto resultDto = busiFinancePlanService.matchSpecialResultOrder(dto);
        Assert.assertTrue(resultDto.isSuccess());
    }

    @org.junit.Test
    public void specialTransferTest(){
        List<SpecialTransferDebtDTO> specialTransferDebtList = new ArrayList<>();
        SpecialTransferDebtDTO std = new SpecialTransferDebtDTO();
        std.setMainOrderId(2147699L);
        specialTransferDebtList.add(std);
        busiFinancePlanService.specialTransfer(specialTransferDebtList);
    }

    @org.junit.Test
    public void batchUpdateOrderSubStatusTest(){
        List<Long> orderIdList = new ArrayList<>();
        orderIdList.add(1000001253L);
        busiOrderSubMapper.batchUpdateOrderSubStatus(orderIdList);
    }

}