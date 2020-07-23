package com.zdmoney.webservice.api.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.UserBindCardDTO;
import com.zdmoney.webservice.api.dto.plan.*;
import com.zdmoney.webservice.api.dto.sundry.OperationsResultStatisticsDto;
import net.sf.json.util.JSONUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appContext_webservice_api.xml")
public class IFinancePlanFacadeServiceTest {

    @Autowired
    private IFinancePlanFacadeService financePlanFacadeService;

    @Autowired
    private ICreditFacadeService creditFacadeService;

    @Autowired
    private IManagerFacadeService managerFacadeService;

    @Autowired
    private ICustomerInfoFacadeService customerInfoFacadeService;

    @Test
    public void testGainSpFinPlanner() {
        ResultDto resultDto = financePlanFacadeService.gainSpecialFinancialPlannerInfo();
        SpecialFinancialPlannerVO sp = (SpecialFinancialPlannerVO)resultDto.getData();
        System.out.println(">>>>>>>" +sp.getRealName());
    }

    @Test
    public void testGainSpFinPlanners(){
        ResultDto resultDto = financePlanFacadeService.gainSpecialFinancialPlannerInfoList();
        System.out.println(">>>>>>>" + JSON.toJSONString(resultDto));
    }

    @Test
    public void debtMatch() {
        /*DebtDetailReqDTO reqDTO = new DebtDetailReqDTO();
        List<DebtDetailReqDTO> debtDetailDTOList = new ArrayList<DebtDetailReqDTO>();
        reqDTO.setDebtNo("ZDAT_BD_0000188541");
        reqDTO.setDebtType("1");
        reqDTO.setMainProductId(9580L);
        reqDTO.setBorrowerIdNum("20170626P");
        reqDTO.setBorrowerNumber("20170626P");
        reqDTO.setPaymentManner("1");
        reqDTO.setBorrowerDate(null);
        debtDetailDTOList.add(reqDTO);*/

        DebtMatchReqDTO debtMatchReqDTO = new DebtMatchReqDTO();
        /*debtMatchReqDTO.setProductId(9460L);
        debtMatchReqDTO.setStatus("1");
        debtMatchReqDTO.setProductPrincipal(BigDecimal.valueOf(1000));
        debtMatchReqDTO.setDebtDetailDTOList(debtDetailDTOList);*/
        File file = new File("C:\\Users\\user\\Desktop\\302.txt");
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            debtMatchReqDTO = JSONObject.parseObject(line,DebtMatchReqDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResultDto resultDto = financePlanFacadeService.debtMatch(debtMatchReqDTO);
        Assert.assertTrue(resultDto.isSuccess());

    }

    @Test
    public void buyBackTest() {
        String subNo = "ZDAT_BD_00002416";
        ResultDto resultDto = financePlanFacadeService.buyBack(subNo);
        System.out.println(resultDto.isSuccess());
    }

    @Test
    public void sendProductTest() {
        SendProductReqDTO sendProductDTO = new SendProductReqDTO();
        sendProductDTO.setYearRate(new BigDecimal(0.3));
        sendProductDTO.setCloseDay(60);
        sendProductDTO.setMinMatchAmount(new BigDecimal(45000));
        sendProductDTO.setProductCode("9582");
        sendProductDTO.setProductPrincipal(new BigDecimal(50000));
        sendProductDTO.setSettleDate(new Date());
        ResultDto resultDto = financePlanFacadeService.sendProduct(sendProductDTO);
        System.out.println(resultDto.isSuccess());
    }

    @Test
    public void sendDebtInfoTest() {
        List<DebtQueueDTO> debtQueueList = new ArrayList<DebtQueueDTO>();

        DebtQueueDTO debtQueue1 = new DebtQueueDTO();
        debtQueue1.setOrderNo("C8888838345201707241113089754");
        debtQueue1.setDebtType("1");
        debtQueueList.add(debtQueue1);

        debtQueue1.setOrderNo("C8888838364201707241113073546");
        debtQueue1.setDebtType("1");
        debtQueueList.add(debtQueue1);

        ResultDto resultDto = financePlanFacadeService.sendDebtInfo(debtQueueList);
        System.out.println(resultDto.isSuccess());
    }

    @Test
    public void matchResultOrder() {
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

       /* MatchSucResult matchSucResult1=new MatchSucResult();
        matchSucResult1.setCapitalAmount(new BigDecimal(3701.08));
        matchSucResult1.setCapitalCode("628");
        matchSucResult1.setCapitalType("0");
        matchSucResult1.setFinanceId("2076");
        matchSucResult1.setLedgerId("01170707000002869");
        matchSucResult1.setPriority("3");
        matchSucResult1.setStatus("03000001");
        matchSucResult1.setCreateTime(new Date());
        matchSucResult1.setEarningsRate(new BigDecimal(0.105));
        matchSucResult1.setTotalTerm("19");
        matchSucResult1.setProductCode("22465");
        matchSucResult1.setSubjectNo("ZDAT_BD_00002572");
        matchSucResult1.setSubjectAmt("5000");
        matchSucResult1.setLoanCustomerNo("01170707000002869");
        matchSucResult1.setLoanCustomerName("董倩如");
        matchSucResult1.setDebtType("2");
        matchSucResult1.setMatchOrderCode("2017071916523401863");
        matchSucResult1.setBorrowerDate(new Date());
        matchSucResult1.setManFinanceId("2045");
        matchSucResult1.setInitOrderNum("C8888838364201707241113073546");
        matchSucResultList.add(matchSucResult1);*/
        matchResultDto.setMatchSucResultList(matchSucResultList);

        ResultDto resultDto = financePlanFacadeService.matchResultOrder(matchResultDto);
        System.out.println(resultDto.isSuccess());
    }

    /**
     * 测试撮合订单定时任务
     */
    @Test
    public void matchResultOrderTaskTest() {
        financePlanFacadeService.matchResultOrderTask();
    }


    @Test
    public void sendRevokeProductTest() {
        String productId = "22375";
        financePlanFacadeService.sendRevokeProduct(productId);
    }


    @Test
    public void specialMatch() {
        MatchSpecialReqDto reqDto = new MatchSpecialReqDto();
        reqDto.setBatchNo("44444");
        reqDto.setProductId(22459L);
        reqDto.setSuperfluousAmount(new BigDecimal("44444"));
        List<MatchSucResult> waitIngOutResultList = Lists.newArrayList();
        MatchSucResult matchSucResult = new MatchSucResult();
        matchSucResult.setMatchOrderCode("44444");
        waitIngOutResultList.add(matchSucResult);
        reqDto.setWaitIngOutResultList(waitIngOutResultList);
        ResultDto<MatchSpecialResultDto> result = financePlanFacadeService.matchSpecialResultOrder(reqDto);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void specialBuyBackOrderTest() {
        MatchResultDto matchResultDto = new MatchResultDto();
        matchResultDto.setMatchAmount(new BigDecimal(5001.45));
        matchResultDto.setProductId(22465L);
        matchResultDto.setBatchNo("CH_201707241718593887");

        List<MatchSucResult> matchSucResultList = new ArrayList<MatchSucResult>();
        MatchSucResult matchSucResult = new MatchSucResult();
        matchSucResult.setCapitalAmount(new BigDecimal(1300.37));
        matchSucResult.setCapitalCode("628");
        matchSucResult.setCapitalType("0");
        matchSucResult.setFinanceId("2075");
        matchSucResult.setLedgerId("01170707000002869");
        matchSucResult.setPriority("3");
        matchSucResult.setStatus("03000001");
        matchSucResult.setCreateTime(new Date());
        matchSucResult.setEarningsRate(new BigDecimal(0.105));
        matchSucResult.setTotalTerm("19");
        matchSucResult.setProductCode("22465");
        matchSucResult.setSubjectNo("ZDAT_BD_00002572");
        matchSucResult.setSubjectAmt("5000");
        matchSucResult.setLoanCustomerNo("01170707000002869");
        matchSucResult.setLoanCustomerName("董倩如");
        matchSucResult.setDebtType("2");
        matchSucResult.setManFinanceId("2046");
        matchSucResult.setMatchOrderCode("2017072417185902556");
        matchSucResult.setBorrowerDate(new Date());
        matchSucResult.setInitOrderNum("C8888838345201707241113089754");
        matchSucResult.setInterest(new BigDecimal(4545));
        matchSucResultList.add(matchSucResult);

        matchResultDto.setMatchSucResultList(matchSucResultList);
        ResultDto resultDto = financePlanFacadeService.specialBuyBackOrder(matchResultDto);
        System.out.println(resultDto.isSuccess());
    }

    @Test
    public void earlyOutTest() {
        financePlanFacadeService.earlyOut();
    }

    @Test
    public void specialTransferTest() {
        List<SpecialTransferDebtDTO> specialTransferDebtList = new ArrayList<SpecialTransferDebtDTO>();

        SpecialTransferDebtDTO stf1 = new SpecialTransferDebtDTO();
        stf1.setMainOrderId(2148283L);
        specialTransferDebtList.add(stf1);

        ResultDto resultDto = financePlanFacadeService.specialTransfer(specialTransferDebtList);
        System.out.println(resultDto.toString());
    }

    @Test
    public void creditTest() {
        ResultDto<String> resultDto = creditFacadeService.getUserInfoByIdNum("133961");
        System.out.println(resultDto.getMsg());
    }

    @Test
    public void realNameTest() {
        ResultDto resultDto = creditFacadeService.realNameAuth(133967L, "320902195511057076", "刘毅");
        System.out.println(resultDto.getMsg());
    }

    @Test
    public void bankTest() {
        UserBindCardDTO userBindCardDTO = new UserBindCardDTO();
        userBindCardDTO.setCustomerId(603358L);
        userBindCardDTO.setCellphone("18000400002");
        userBindCardDTO.setBankCard("6228480588345121555");
        userBindCardDTO.setBankCode("807118");
        ResultDto resultDto = creditFacadeService.BindCard(userBindCardDTO);
        System.out.println(resultDto.getMsg());
    }

    @Test
    public void regedit() {
        ResultDto resultDto = managerFacadeService.register("17612169999");
        System.out.println(JSON.toJSONString(resultDto));
    }

    @Test
    public void testlistUserInfos() {
        List<Long> ids = Arrays.asList(607396L, 602782L, 607548L, 603202L, 607395L);
        ResultDto resultDto = customerInfoFacadeService.listUserInfos(ids);
        System.out.println(">>>>>>>" + JSON.toJSONString(resultDto));
    }
}