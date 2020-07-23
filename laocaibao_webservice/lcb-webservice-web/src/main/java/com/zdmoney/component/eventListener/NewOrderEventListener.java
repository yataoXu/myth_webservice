package com.zdmoney.component.eventListener;

import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.match.api.IMatchApi;
import com.zdmoney.match.dict.MatchMessage;
import com.zdmoney.match.dto.MatchApiResult;
import com.zdmoney.match.dto.MatchPart;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.utils.ApplicationEventSupport;
import com.zdmoney.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by user on 2019/2/26.
 */
@Component
public class NewOrderEventListener implements ApplicationListener<ApplicationEventSupport.NewOrderEvent>{

    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(NewOrderEventListener.class);

    @Autowired
    private IMatchApi matchApi;

    @Autowired
    private BusiOrderMapper busiOrderMapper;

    @Override
    @Async("matchMainOrderThreadExecutor")
    public void onApplicationEvent(ApplicationEventSupport.NewOrderEvent event) {
        // 根据orderId 获得订单
        BusiOrder busiOrder = busiOrderMapper.getOrderInfo(event.getSource());

        //调用撮合系统 数据拼接
        MatchPart fund = new MatchPart();
        fund.setAppId(System.getProperty("app.id"));
        fund.setPoolId(busiOrder.getProductId().toString());
        fund.setMatchId(busiOrder.getOrderId());
        fund.setMatchValue(busiOrder.getOrderAmt());
        fund.setCreateTime(busiOrder.getConfirmPaymentDate());
        try {
            // 发起撮合
            MatchApiResult matchApiResult = matchApi.matchResource(fund);
            if(!MatchMessage.SUCCESS.getCode().equals(matchApiResult.getCode())){
                MailUtil.sendMail("发起撮合失败", "订单编号："+busiOrder.getOrderId()+",失败原因：" +matchApiResult.getCode()+ matchApiResult.getMessage());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            MailUtil.sendMail("发起撮合发生异常", "订单编号："+busiOrder.getOrderId()+",异常：" + e.getMessage());
        }
    }
}
