package com.zdmoney.service;

import com.zdmoney.base.BaseTest;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsDto;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsSearchDto;
import com.zdmoney.integral.api.dto.IntegralAccountDto;
import com.zdmoney.integral.api.dto.IntegralAccountSummaryDto;
import com.zdmoney.integral.api.dto.account.*;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xinqigu on 2015/8/31.
 */
//@Ignore
public class IntegralAccountFacadeServiceTest extends BaseTest {

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;

    /**
     * 查询账户积分明细
     * @throws Exception
     */
    //@Test
    public void testAccountIntegralDetails() throws Exception {
        IntegralAccountDetailsSearchDto searchDto = new IntegralAccountDetailsSearchDto();
        searchDto.setAccountNo("909096");
        searchDto.setPageSize(500);
        searchDto.setPageNo(1);
        //searchDto.setAction("IN");//收入
        //searchDto.setAction("OUT");//支出
        //searchDto.setSourceCode("100300");//积分项目编号
        //searchDto.setStartTime();//开始时间
        //searchDto.getEndTime();//结束时间
        PageResultDto<IntegralAccountDetailsDto> resultDto = integralAccountFacadeService.searchDetails(searchDto);
        System.out.println(resultDto.getDataList().size());
        for (IntegralAccountDetailsDto detailsDto : resultDto.getDataList()) {
            System.out.println(resultDto.getPageNo());
            System.out.println(
                    " 积分项目编号=" + detailsDto.getSourceCode() +
                    " 积分项目名称=" + detailsDto.getSourceName() +
                    " 积分生成/消费=" +detailsDto.getAction() +
                    " 时间=" + detailsDto.getActionTime()

            );
        }
    }

    /**
     * 查询账户积分
     * @throws Exception
     */
    //@Test
    public void testAccountIntegral() throws Exception {
        ResultDto<IntegralAccountDto> resultDto = integralAccountFacadeService.getIntegralAccount("310107198201140413");
        if (resultDto.isSuccess()) {
            IntegralAccountDto accountDto = resultDto.getData();
            System.out.println(
                    " 帐号=" + accountDto.getAccountNo() +
                            " 总积分=" + accountDto.getIntegral() +
                            " 可用积分=" + accountDto.getAvailableIntegral() +
                            " 冻结积分=" + accountDto.getFreezingIntegral());
        } else {
            System.out.println(resultDto.getMsg());
        }

    }

    /**
     * 查询账户积分明细和积分账户
     * @throws Exception
     */
    //@Test
    public void testAccountIntegralSummary() throws Exception {
        IntegralAccountDetailsSearchDto searchDto = new IntegralAccountDetailsSearchDto();
        searchDto.setAccountNo("909096");
        //searchDto.setAction("IN");//收入
        //searchDto.setAction("OUT");//支出
        //searchDto.setSourceCode("100300");//积分项目编号
        //searchDto.setStartTime();//开始时间
        //searchDto.getEndTime();//结束时间
        ResultDto<IntegralAccountSummaryDto> resultDto = integralAccountFacadeService.getIntegralAccountSummary(searchDto);
        IntegralAccountSummaryDto summaryDto = resultDto.getData();
        IntegralAccountDto accountDto = summaryDto.getAccountDto();
        System.out.println(
                " 帐号=" + accountDto.getAccountNo() +
                        " 总积分=" + accountDto.getIntegral() +
                        " 可用积分=" + accountDto.getAvailableIntegral() +
                        " 冻结积分=" + accountDto.getFreezingIntegral());

        PageResultDto<IntegralAccountDetailsDto> detailsDtoPageDto = summaryDto.getAccountDetailPageDto();
        for (IntegralAccountDetailsDto detailsDto : detailsDtoPageDto.getDataList()) {
            System.out.println(
                    " 积分项目编号=" + detailsDto.getSourceCode() +
                            " 积分项目名称=" + detailsDto.getSourceName() +
                            " 积分生成/消费=" +detailsDto.getAction() +
                            " 时间=" + detailsDto.getActionTime()

            );
        }
    }

    /**
     * 第一笔交易获取积分
     * @throws Exception
     */
    @Test
    public void testFirstOrderIntegral() throws Exception {
        IntegralFirstOrderDto firstOrderDto = new IntegralFirstOrderDto();
        firstOrderDto.setAccountNo("909096");//账户号
        firstOrderDto.setPlatform("ANDROID");//平台
        firstOrderDto.setChannel("LCB");//捞财宝
        firstOrderDto.setOrderNo("order_123456");//第一笔交易的订单号
        ResultDto<IntegralResDto> result = integralAccountFacadeService.firstOrderGetIntegral(firstOrderDto);
        Thread.sleep(5000);
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
    //@Test
    public void testFirstOrderRefundIntegral() throws Exception {
        IntegralFirstOrderRefundDto firstOrderRefundDto = new IntegralFirstOrderRefundDto();
        firstOrderRefundDto.setAccountNo("909096");//账户号
        firstOrderRefundDto.setPlatform("ANDROID");//平台
        firstOrderRefundDto.setChannel("LCB");//捞财宝
        firstOrderRefundDto.setOrderNo("order_1234");//第一笔交易的订单号
        ResultDto<IntegralResDto> result = integralAccountFacadeService.firstOrderDeductionIntegral(firstOrderRefundDto);
        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("第一笔交易扣除积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

    /**
     * 邀请好友投资返利获取积分
     * @throws Exception
     */
    //@Test
    public void testIncomeInviteFriendIntegral() throws Exception {
        IntegralRebateDto rebateDto = new IntegralRebateDto();
        rebateDto.setAccountNo("909096");//邀请人
        rebateDto.setInviteeAccountNo("909092");//被邀请人
        rebateDto.setPlatform("IOS");
        rebateDto.setChannel("LCB");//捞财宝
        rebateDto.setOrderNo("ORDER_002");//产生返利的业务订单
        rebateDto.setValue(2.05f);//投资返利
        ResultDto<IntegralResDto> result = integralAccountFacadeService.rebateGetIntegral(rebateDto);
        Thread.sleep(10000);
        if (result.isSuccess()) {
            System.out.println("邀请好友投资返利获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

    /**
     * 投资收益获取积分
     * @throws Exception
     */
    //@Test
    public void testIncomeIntegral() throws Exception {
        IntegralInvestmentIncomeDto rateDto = new IntegralInvestmentIncomeDto();
        rateDto.setAccountNo("909096");//账户号
        rateDto.setPlatform("ANDROID");
        rateDto.setChannel("LCB");//捞财宝
        rateDto.setOrderNo("LCB_001");//业务订单
        rateDto.setValue(27.05f);//投资收益
        ResultDto<IntegralResDto> result = integralAccountFacadeService.investmentIncomeGetIntegral(rateDto);
        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("投资收益获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

    /**
     * 签到
     * @throws Exception
     */
    //@Test
    public void testSignIntegral() throws Exception {
        IntegralSignDto signDto = new IntegralSignDto();
        signDto.setAccountNo("909096");
        signDto.setPlatform("IOS");
        signDto.setChannel("LCB");
        ResultDto<IntegralResDto> result = integralAccountFacadeService.signGetIntegral(signDto);
        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("签到获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

    /**
     * 实名认证
     * @throws Exception
     */
    //@Test
    public void testAuthIntegral() throws Exception {
        IntegralAuthDto authDto = new IntegralAuthDto();
        authDto.setAccountNo("909096");
        authDto.setPlatform("ANDROID");
        authDto.setChannel("LCB");
        ResultDto<IntegralResDto> result = integralAccountFacadeService.authGetIntegral(authDto);
        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("实名认证获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

    /**
     * 注册获取积分
     * @throws Exception
     */
    //@Test
    public void testRegistIntegral() throws Exception {
        IntegralRegistDto registDto = new IntegralRegistDto();
        registDto.setAccountNo("909096");
        registDto.setPlatform("ANDROID");
        registDto.setChannel("LCB");
        ResultDto<IntegralResDto> result = integralAccountFacadeService.registGetIntegral(registDto);
        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("注册获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

    /**
     * 邀请好友注册获取积分
     * @throws Exception
     */
    //@Test
    public void testInviteFriendIntegral() throws Exception {
        IntegralInviteRegistDto inviteRegistDto = new IntegralInviteRegistDto();
        inviteRegistDto.setAccountNo("909096");
        inviteRegistDto.setInviteeAccountNo("909092");//被邀请人
        inviteRegistDto.setPlatform("ANDROID");
        inviteRegistDto.setChannel("LCB");
        ResultDto<IntegralResDto> result = integralAccountFacadeService.invitationRegistGetIntegral(inviteRegistDto);
        Thread.sleep(5000);
        if (result.isSuccess()) {
            System.out.println("邀请好友注册获取积分=" + result.getData().getIntegral());
        } else {
            System.out.println(result.getMsg());
        }
    }

}