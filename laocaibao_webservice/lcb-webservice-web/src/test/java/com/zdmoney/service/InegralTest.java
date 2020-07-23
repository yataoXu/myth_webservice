package com.zdmoney.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.zdmoney.integral.api.dto.coupon.CouponAccountDto;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zdmoney.base.BaseTest;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsDto;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsSearchDto;
import com.zdmoney.integral.api.dto.IntegralAccountDto;
import com.zdmoney.integral.api.dto.account.IntegralFirstOrderDto;
import com.zdmoney.integral.api.dto.account.IntegralFirstOrderRefundDto;
import com.zdmoney.integral.api.dto.account.IntegralRebateDto;
import com.zdmoney.integral.api.dto.account.IntegralResDto;
import com.zdmoney.integral.api.dto.order.IntegralExchangeDto;
import com.zdmoney.integral.api.dto.order.IntegralExchangeResDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderConsumeDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderRefundDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderSearchDto;
import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import com.zdmoney.integral.api.dto.product.IntegralProductSearchDto;
import com.zdmoney.integral.api.dto.product.IntegralProductTrialDto;
import com.zdmoney.integral.api.dto.product.IntegralProductTrialResDto;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.integral.api.facade.IIntegralOrderFacadeService;
import com.zdmoney.integral.api.facade.IIntegralProductFacadeService;

public class InegralTest extends BaseTest{


	@Autowired
	private IIntegralAccountFacadeService integralAccountFacadeService;
    @Autowired
    private ICouponFacadeService couponFacadeService;
    /**
     * 查询账户积分
     * @throws Exception
     */
//    @Test
    public void testAccountIntegral() throws Exception {
        ResultDto<IntegralAccountDto> resultDto = integralAccountFacadeService.getIntegralAccount("909092");
        IntegralAccountDto accountDto = resultDto.getData();
        System.out.println(
                "帐号=" + accountDto.getAccountNo() +
                " 总积分=" + accountDto.getIntegral() +
                " 可用积分=" + accountDto.getAvailableIntegral() +
                " 冻结积分=" + accountDto.getFreezingIntegral());
    }
    
    /**
     * 查询账户积分明细
     * @throws Exception
     */
    @Test
    public void testAccountIntegralDetails() throws Exception {
        IntegralAccountDetailsSearchDto searchDto = new IntegralAccountDetailsSearchDto();
        searchDto.setAccountNo("909094");
        //searchDto.setAction("IN");//收入
        //searchDto.setAction("OUT");//支出
        //searchDto.setSourceCode("100300");//积分项目编号
        PageResultDto<IntegralAccountDetailsDto> resultDto = integralAccountFacadeService.searchDetails(searchDto);
        System.out.println(resultDto.getDataList().size());
        for (IntegralAccountDetailsDto detailsDto : resultDto.getDataList()) {
            System.out.println(
            		"积分="+detailsDto.getIntegral()+
                    "积分项目编号=" + detailsDto.getSourceCode() +
                    " 积分项目名称=" + detailsDto.getSourceName() +
                            " 积分生成/消费" +detailsDto.getAction() +
                            " 时间=" + detailsDto.getActionTime()
            );
        }
    }

    /**
     * 第一笔交易获取积分
     * @throws Exception
     */
//    @Test
    public void testFirstOrderIntegral() throws Exception {
    	 IntegralFirstOrderDto firstOrderDto = new IntegralFirstOrderDto();
         firstOrderDto.setAccountNo("909094");//账户号
         firstOrderDto.setPlatform("IOS");//平台
         firstOrderDto.setChannel("LCB");//捞财宝
         firstOrderDto.setNo("100700");//积分项目编码(第一次交易)
         firstOrderDto.setOrderNo("order_123");//第一笔交易的订单号
         ResultDto<IntegralResDto> result = integralAccountFacadeService.firstOrderGetIntegral(firstOrderDto);
//         Thread.sleep(5000);
         if (result.isSuccess()) {
             System.out.println("第一笔交易获取积分=" + result.getData().getIntegral());
         } else {
             System.out.println(result.getMsg());
         }

    }
    
    /**
     * 第一笔交易退款扣除积分
     * @throws Exception
     */
//    @Test
    public void testFirstOrderRefundIntegral() throws Exception {
        IntegralFirstOrderRefundDto firstOrderRefundDto = new IntegralFirstOrderRefundDto();
        firstOrderRefundDto.setAccountNo("909094");//账户号
        firstOrderRefundDto.setPlatform("IOS");//平台
        firstOrderRefundDto.setChannel("LCB");//捞财宝
        firstOrderRefundDto.setNo("100800");//积分项目编码(第一次交易退款)
        firstOrderRefundDto.setOrderNo("order_123");//第一笔交易的订单号
        ResultDto<IntegralResDto> result = integralAccountFacadeService.firstOrderDeductionIntegral(firstOrderRefundDto);
//        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("第一笔交易扣除积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }


    /**
     * 投资收益获取积分
     * @throws Exception
     */
//    @Test
    public void testIncomeIntegral() throws Exception {
//        IntegralRateDto rateDto = new IntegralRateDto();
//        rateDto.setAccountNo("909092");//账户号
//        rateDto.setPlatform("IOS");
//        rateDto.setChannel("LCB");//捞财宝
//        rateDto.setNo("100500");//积分项目编码（固定）
//        rateDto.setOrderNo("LCB_001");//业务订单
//        rateDto.setValue(27.05f);//投资收益
//        ResultDto<IntegralResDto> result = integralAccountFacadeService.rateGetIntegral(rateDto);
//        System.out.println("获取积分=" + result.getData().getIntegral());
    }

    /**
     * 邀请好友投资收益获取积分
     * @throws Exception
     */
//    @Test
    public void testIncomeInviteFriendIntegral() throws Exception {
    	IntegralRebateDto rebateDto = new IntegralRebateDto();
        //909093邀请了909092，909092获利后给邀请人909093返利
        rebateDto.setAccountNo("909094");//邀请人
        rebateDto.setInviteeAccountNo("909092");//被邀请人
        rebateDto.setPlatform("IOS");
        rebateDto.setChannel("LCB");//捞财宝
        rebateDto.setNo("100600");//积分项目编码（固定）
        rebateDto.setOrderNo("ORDER_001");//产生返利的业务订单
        rebateDto.setValue(7.05f);//投资返利
        ResultDto<IntegralResDto> result = integralAccountFacadeService.rebateGetIntegral(rebateDto);
//        Thread.sleep(10000);
        if (result.isSuccess()) {
            System.out.println("邀请好友投资返利获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }

    }
    
    /**
     * 签到
     * @throws Exception
     */
//    @Test
    public void testSignIntegral() throws Exception {
//        IntegralTimeDto timeDto = new IntegralTimeDto();
//        timeDto.setAccountNo("909093");
//        timeDto.setPlatform("IOS");
//        timeDto.setChannel("LCB");
//        timeDto.setNo("100100");//
//        ResultDto<IntegralResDto> result = integralAccountFacadeService.timeGetIntegral(timeDto);
//        System.out.println("签到,获取积分=" + result.getData().getIntegral());
    }

    /**
     * 实名认证
     * @throws Exception
     */
//    @Test
    public void testAuthIntegral() throws Exception {
//        IntegralTimeDto timeDto = new IntegralTimeDto();
//        timeDto.setAccountNo("909093");
//        timeDto.setPlatform("IOS");
//        timeDto.setChannel("LCB");
//        timeDto.setNo("100300");
//        ResultDto<IntegralResDto> result = integralAccountFacadeService.timeGetIntegral(timeDto);
//        System.out.println("实名认证,获取积分=" + result.getData().getIntegral());
    }

    /**
     * 注册获取积分
     * @throws Exception
     */
//    @Test
    public void testRegistIntegral() throws Exception {
//        IntegralTimeDto timeDto = new IntegralTimeDto();
//        timeDto.setAccountNo("909093");
//        timeDto.setPlatform("IOS");
//        timeDto.setChannel("LCB");
//        timeDto.setNo("100200");
//        ResultDto<IntegralResDto> result = integralAccountFacadeService.timeGetIntegral(timeDto);
//        System.out.println("注册,获取积分=" + result.getData().getIntegral());
    }

    /**
     * 邀请好友获取积分
     * @throws Exception
     */
//    @Test
    public void testInviteFriendIntegral() throws Exception {
//        IntegralInvitationDto invitationDto = new IntegralInvitationDto();
//        //909092邀请了909091
//        invitationDto.setAccountNo("909093");
//        invitationDto.setInviteeAccountNo("909092");//被邀请人
//        invitationDto.setPlatform("IOS");
//        invitationDto.setChannel("LCB");
//        invitationDto.setNo("100400");
//        ResultDto<IntegralResDto> result = integralAccountFacadeService.invitationGetIntegral(invitationDto);
//        System.out.println("邀请好友,获取积分=" + result.getData().getIntegral());
    }
    
    @Autowired
    private IIntegralProductFacadeService integralProductFacadeService;

    /**
     * 积分的基础信息，获取积分兑换比例
     */
//    @Test
    public void testGetIntegralProductByNo() throws Exception {
        ResultDto<IntegralProductDto> resultDto = integralProductFacadeService.getIntegralProductByNo("00001");
        System.out.println(resultDto.getCode());

    }

    /**
     * 积分商品列表的查询
     * 
     */
//    @Test
    public void testSearch() throws Exception {
        IntegralProductSearchDto searchDto = new IntegralProductSearchDto();
        searchDto.setNo("0000");
        PageResultDto<IntegralProductDto> resultDto = integralProductFacadeService.search(searchDto);
        System.out.println(resultDto.getCode());
    }

    /**
     * 积分转钱的试算
     * 
     */
//    @Test
    public void testTrial() throws Exception {
        IntegralProductTrialDto trialDto = new IntegralProductTrialDto();
        trialDto.setAccountNo("909090");
        trialDto.setIntegral(8000);
        trialDto.setProductNo("00001");
        ResultDto<IntegralProductTrialResDto> resultDto = integralProductFacadeService.trial(trialDto);
        System.out.println(resultDto.getCode());
    }
    
    @Autowired
    private IIntegralOrderFacadeService integralOrderFacadeService;

/**
 * 查询积分订单
 * @throws Exception
 */
//    @Test
    public void testSearch2() throws Exception {
        IntegralOrderSearchDto searchDto = new IntegralOrderSearchDto();
        PageResultDto<IntegralOrderDto> resultDto = integralOrderFacadeService.search(searchDto);
        System.out.println(resultDto.getCode());
    }

    /**
     * 根据积分订单的订单号查询积分订单
     * @throws Exception
     */
//    @Test
    public void testGetByOrderNo() throws Exception {
        ResultDto<IntegralOrderDto> resultDto = integralOrderFacadeService.getByOrderNo("909090801000010578975120");
        System.out.println(resultDto.getCode());
    }

    /**
     * 根据积分订单号列表查询积分订单
     * @throws Exception
     */
//    @Test
    public void testGetByOrderNos() throws Exception {
        List<String> orderNos = new ArrayList<String>();
        orderNos.add("909090801000010578975120");
        orderNos.add("909091311000010649878253");
        ResultDto<List<IntegralOrderDto>> resultDto = integralOrderFacadeService.getByOrderNos(orderNos);
        System.out.println(resultDto.getCode());
    }

    /*
     * 根据渠道订单号查询订单
     */
//    @Test
    public void testGetByChannelOrderNo() throws Exception {
        ResultDto<IntegralOrderDto> resultDto = integralOrderFacadeService.getByChannelOrderNo("9999999999999995999");
        System.out.println(resultDto.getCode());
    }

    /**
     * 批量--根据订单号查询积分
     * @throws Exception
     */
//    @Test
    public void testGetByChannelOrderNos() throws Exception {
        List<String> orderNos = new ArrayList<String>();
        orderNos.add("9999999999999995999");
        orderNos.add("9999999999999999699");
        ResultDto<List<IntegralOrderDto>> resultDto = integralOrderFacadeService.getByChannelOrderNos(orderNos);
        System.out.println(resultDto.getCode());
    }

    /*
     * 积分兑换钱
     */
//    @Test
    public void testExchange() throws Exception {
        IntegralExchangeDto exchangeDto = new IntegralExchangeDto();
        exchangeDto.setAccountNo("909090");
        exchangeDto.setChannelOrderNo("000000011111111110000000002");
        exchangeDto.setProductNo("00001");
        exchangeDto.setIntegral(200);
        exchangeDto.setChannel("LCB");
        exchangeDto.setPlatform("ANDROID");
        ResultDto<IntegralExchangeResDto> resultDto = integralOrderFacadeService.exchange(exchangeDto);
        System.out.println(resultDto.getCode());

    }

    /*
     * 积分消费
     */
//    @Test
    public void testConsume() throws Exception {
        IntegralOrderConsumeDto consumeDto = new IntegralOrderConsumeDto();
        consumeDto.setChannelOrderNo("000000011111111110000000001");//订单号
        consumeDto.setOrderNo("909090682000011093142867");//积分订单号
        consumeDto.setProductSerialNo("000014779090901093142867");//产品序列号IntegralExchangeResDto
        ResultDto resultDto = integralOrderFacadeService.consume(consumeDto);
        System.out.println(resultDto.getCode());

    }

    /*
     * 失败订单，退还积分
     */
//    @Test
    public void testRefund() throws Exception {
        IntegralOrderRefundDto refundDto = new IntegralOrderRefundDto();
        refundDto.setChannelOrderNo("000000011111111110000000002");//订单号
        refundDto.setOrderNo("909090256000011093349763");//积分订单号
//        refundDto.setProductSerialNo("000012879090901093349763");//产品序列号IntegralExchangeResDto
        ResultDto resultDto = integralOrderFacadeService.refund(refundDto);
        System.out.println(resultDto.getCode());
    }
    @Test
    public void testconp() throws Exception{
        try {

            ResultDto<CouponAccountDto> resultDto=couponFacadeService.getCouponAmountByAccountNo("111111");
            if(resultDto.isSuccess()){
                System.out.println(resultDto.getData());
            }
            else{
                System.out.println("错误信息"+resultDto.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
