package com.zdmoney.facade;

import com.zdmoney.assets.api.dto.wacai.WacaiNotifyStatus;
import com.zdmoney.assets.api.dto.wacai.WacaiWithdrawReqDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.base.JUnitActionBase;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.credit.wacai.*;
import com.zdmoney.webservice.api.facade.IWaCaiFacadeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by 46186 on 2019/3/8.
 */
public class WaCaiFacadeServiceTest  extends JUnitActionBase {

    private IWaCaiFacadeService waCaiFacadeService = JUnitActionBase.context.getBean(IWaCaiFacadeService.class);

    @Autowired
    private ILCBSubjectFacadeService lCBSubjectFacadeService;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Test
    public void testWithdraw(){
        WithdrawRequestDto dto = new WithdrawRequestDto();
        dto.setChannelCode("1002");
        dto.setWithdrawType("2");
        dto.setIdNum("320311198302284915");
        dto.setClientType("APP");
        dto.setPhone("17688888888");
        dto.setPageUrl("www.baidu.com");
        dto.setBorrowNo("a123456789abcd");
        dto.setAmount("100");

        ResultDto<String> resultDto =  waCaiFacadeService.withdraw(dto);
        System.out.println(resultDto.getCode()+resultDto.getMsg());
        System.out.println(resultDto.getData());
    }

    @Test
    public void testOpenAccount(){
        OpenAuthRequestDto dto = new OpenAuthRequestDto();
        dto.setPhone("17600007384");
        dto.setIdNum("320826199311273018");
        dto.setChannelCode("1002");
        dto.setReqType("0");
        dto.setPageUrl("www.abcd.com?key1=val1&key2=val2");
        dto.setClientType("APP");
        ResultDto<String> res = waCaiFacadeService.openAccount(dto);
        System.out.println(res.getCode());
        System.out.println(res.getMsg());
        System.out.println(res.getData());
    }

    @Test
    public void testGetInfo(){
        GetInfoRequestDto req = new GetInfoRequestDto();
        req.setChannelCode("1002");
        req.setIdNum("123456");
        req.setPhone("123456789");
        ResultDto s = waCaiFacadeService.getUserInfo(req);
        System.out.println(s.toString());
    }

    @Test
    public void testGetInfowithIdCardNO(){
        ResultDto<UserInfoDto> res = waCaiFacadeService.getUserInfoWithIdCard("320311198302284915","1002");
        System.out.println(res.getCode());
        System.out.println(res.getMsg());
        System.out.println(res.getData()==null?null:res.getData().toString());

    }

    @Test
    public void testNotifyAsstes(){
        Long flowId = 249874L;
        System.out.println("提现申请回调标的----start:tradeFlowId= " + flowId);
        BusiTradeFlow flow = new BusiTradeFlow();
        flow.setStatus("3");
        flow.setFlowNum("W8888134127201801251626540306");
        WacaiWithdrawReqDto dto = new WacaiWithdrawReqDto();
        dto.setStatusChangeDate(new Date());
        dto.setTransNo(flow.getFlowNum());
        String status = flow.getStatus();
        if (AppConstants.TradeStatusContants.PROCESS_SUCCESS.equals(status)) {
            dto.setWacaiNotifyStatus(WacaiNotifyStatus.WITHDRAW_SUCCESS);
        }else {
            dto.setWacaiNotifyStatus(WacaiNotifyStatus.CONFIRM_FAILED);
        }
        String reason = "";
        switch (status){
            case AppConstants.TradeStatusContants.INIT : reason+="初始";break;
            case AppConstants.TradeStatusContants.PROCESS_SUCCESS : reason+="处理成功";break;
            case AppConstants.TradeStatusContants.PROCESSING : reason+="处理中";break;
            case AppConstants.TradeStatusContants.PROCESS_FAIL : reason+="处理失败";break;
            case AppConstants.TradeStatusContants.UNPROCESS : reason+="待处理";break;
            case AppConstants.TradeStatusContants.WITHDRAW_FROZEN : reason+="提现冻结";break;
            case AppConstants.TradeStatusContants.WITHDRAW_REFUND_START : reason+="提现退款中";break;
            case AppConstants.TradeStatusContants.WITHDRAW_REFUND_FAIL : reason+="提现退款失败";break;
            case AppConstants.TradeStatusContants.WITHDRAW_REFUND_SUCCESS : reason+="提现退款成功";break;
        }
        dto.setReason(reason);
        lCBSubjectFacadeService.wacaiWithdrawNotify(dto);
        System.out.println("提现申请回调标的----end");
    }
    @Test
    public void alarmAmountWacaiTask(){
        waCaiFacadeService.alarmAmountWacaiTask();
    }


}