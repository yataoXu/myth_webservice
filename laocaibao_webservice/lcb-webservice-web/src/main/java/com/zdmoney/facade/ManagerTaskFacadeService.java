package com.zdmoney.facade;


import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.Constants;
import com.zdmoney.service.BusiOrderService;
import com.zdmoney.service.CustRatingChangingRecordService;
import com.zdmoney.service.IAggregationService;
import com.zdmoney.service.TradeService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.DateUtils;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.QueryOrderReqDto;
import com.zdmoney.webservice.api.facade.IManagerTaskFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by user on 2017/9/6.
 */
@Component
@Slf4j
public class ManagerTaskFacadeService implements IManagerTaskFacadeService {

    @Autowired
    private IAggregationService aggregationService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private BusiDebtTransferService debtTransferService;

    @Autowired
    private CustRatingChangingRecordService custRatingChangingRecordService;

    @Override
    public void expireRechargeRequest() {
        log.info("充值过期任务开始…………");
        try {
            aggregationService.expireRechargeRequest();
        } catch (Exception e) {
            log.error(e.getMessage());
            //SysLogUtil.insertSysLog(e.getMessage(),"充值过期任务","ExpireTask");
            MailUtil.sendMail("充值过期任务异常", e.getMessage());
            return ;
        }
        MailUtil.sendMail("充值过期任务完毕", "完成时间：" + DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
        log.info("充值过期任务结束…………");
    }

    @Override
    public void updateCanceledOrderStatus() {
        log.info("取消付款任务开始…………");
        ResultDto<Integer> resultDto = null;
        try {
            resultDto = aggregationService.updateCanceledOrderStatus();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            MailUtil.sendMail("取消付款任务产生异常", "信息"+e.getMessage());
        }
        if(resultDto != null && resultDto.isSuccess()){
            MailUtil.sendMail("取消付款任务完毕", "完成时间：" + DateUtil.timeFormat(new Date(), DateUtil.fullFormat)+",成功取消付款："+resultDto.getData()+"条记录。");
        }else if(resultDto != null && !resultDto.isSuccess()){
            MailUtil.sendMail("取消付款任务失败", "信息"+resultDto.getMsg());
        }
        log.info("取消付款任务结束…………");
    }

    @Override
    public void updateExpiredProduct() {
        log.info("----过期未卖光产品重置任务开始-------");
        ResultDto<Integer> resultDto = null;
        try {
            resultDto = aggregationService.updateExpiredProduct();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            MailUtil.sendMail("过期未卖光产品重置任务产生异常", "信息"+e.getMessage());
        }
        if(resultDto != null && !resultDto.isSuccess()){
            MailUtil.sendMail("过期未卖光产品重置任务失败", "信息"+resultDto.getMsg());
        }
        log.info("-------过期未卖光产品重置任务结束-------");
    }


    @Override
    public void finishCreditTransfer() {
        log.info("----------交割债权定时任务开始----------");
        try{
            tradeService.finishCreditTransfer();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        log.info("----------交割债权定时任务结束----------");
    }

    @Override
    public void calculateCustomerRating() {
        log.info("----------用户会员等级定时任务开始----------");
        custRatingChangingRecordService.checkAndUpdateCustomerRating(null);
        log.info("----------用户会员等级定时任务结束----------");
    }

    @Override
    public void sendCustomerRatingChangingMsg() {
        custRatingChangingRecordService.sendCustomerRatingChangingMsg();
    }

    @Autowired
    private BusiOrderService orderService;

    @Override
    public void buyRemainingPartOfWacaiProduct() {
        log.info("----------挖财兜底定时任务开始----------");
        orderService.buyRemainingPartOfWacaiProduct();
        log.info("----------挖财兜底定时任务结束----------");
    }

    @Override
    public void sendNotifyDeptTradeSMS() {
        log.info("--------发送给借款人短信任务开始------------");
        debtTransferService.sendNotifyDeptTradeSMS();
        log.info("----------发送给借款人短信任务结束----------");
    }

    public void updateWacaiFundDetail(){
        log.info("--------更新挖财资金状态任务开始------------");
        orderService.updateWacaiFundDetail();
        log.info("----------更新挖财资金状态任务结束----------");
    }

}
