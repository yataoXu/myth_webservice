package com.zdmoney.controller;/**
 * Created by pc05 on 2017/9/19.
 */

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.Constants;
import com.zdmoney.data.agent.api.base.OpenProcessCode;
import com.zdmoney.data.agent.api.base.OpenResponse;
import com.zdmoney.data.agent.api.dto.bc.BusinessReportDto;
import com.zdmoney.enm.AuditMethodType;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.dto.coupon.CouponDto;
import com.zdmoney.integral.api.dto.coupon.CouponSearchDto;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.marketing.entity.AuthMessage;
import com.zdmoney.marketing.entity.BindMessage;
import com.zdmoney.marketing.entity.RegisterMessage;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderExchange;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.task.BusiTask;
import com.zdmoney.models.task.BusiTaskFlow;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.*;
import com.zdmoney.service.mall.BusiTaskFlowService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-09-19 11:21
 * @email : huangcy01@zendaimoney.com
 **/
@Slf4j
@Controller
@RequestMapping("/audit")
public class AuditCallbackController {

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    private BusiTaskFlowService busiTaskFlowService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiFinancePlanService busiFinancePlanService;

    @Autowired
    private BusiProductService busiProductService;

    @Autowired
    private BusiMallService busiMallService;




    @ResponseBody
    @RequestMapping(value="/callback", method = {RequestMethod.POST})
    public OpenResponse auditCallback(BusinessReportDto brDto){
        log.info("审计回调参数[{}]",brDto);
        if(StringUtils.isBlank(brDto.getNotifyNo()) || StringUtils.isBlank(brDto.getFeatureNo()) || StringUtils.isBlank(brDto.getNotifyContent())
                || StringUtils.isBlank(brDto.getSystemNo())){
            return OpenResponse.handleFail(OpenProcessCode.PARAMETER_ILLEGAL);
        }
        if(StringUtils.equals(brDto.getFeatureNo(),"001")){//注册送红包
            RegisterMessage registerMessage = JSONObject.parseObject(brDto.getNotifyContent(), RegisterMessage.class);
            CouponSearchDto couponSearchDto = new CouponSearchDto();
            couponSearchDto.setType("REG");//发放原因注册
            couponSearchDto.setAccountNo(registerMessage.getCmNumber());
            PageResultDto<CouponDto> couponDtoPageResultDto = couponFacadeService.searchCoupons(couponSearchDto);
            log.info("审计:注册送红包cmNumber [{}] , 记录[{}]",registerMessage.getCmNumber(),couponDtoPageResultDto);
            if(!couponDtoPageResultDto.isSuccess()||couponDtoPageResultDto.getDataList().size()==0){
                return OpenResponse.fail("审计:注册发送红包失败!params:["+ JSONUtils.toJSON(couponSearchDto)+"]");
            }
        }

        if(StringUtils.equals(brDto.getFeatureNo(),"002")){//实名认证送捞财币
            AuthMessage authMessage = JSONObject.parseObject(brDto.getNotifyContent(), AuthMessage.class);
            int authFlow = busiTaskFlowService.findTaskFlow(authMessage.getCmNumber(), Constants.TASK_ID_AUTH);
            log.info("审计:实名认证送捞财宝任务cmNumber [{}] ,流水记录 [{}] 条",authMessage.getCmNumber(),authFlow);
            if(authFlow <= 0){
                return OpenResponse.fail("审计:实名认证送捞财币失败!params:["+ JSONUtils.toJSON(authMessage.getCmNumber())+"]");
            }
        }

        if(StringUtils.equals(brDto.getFeatureNo(), AuditMethodType.AUDIT_BIND_WX.getMethod())){
            return bindWx(brDto);
        }

//        if(StringUtils.equals(brDto.getFeatureNo(), AuditMethodType.AUDIT_ORDER_PAY.getMethod())){
//            return orderPay(brDto);
//        }

        if(StringUtils.equals(brDto.getFeatureNo(), AuditMethodType.AUDIT_coin_EXCHANGE.getMethod())){
            return coinExchange(brDto);
        }

        return OpenResponse.SUCCESS();
    }


    /**
     * 微信绑定
     */
     private OpenResponse bindWx(BusinessReportDto brDto){
         BindMessage bindMessage = JSONObject.parseObject(brDto.getNotifyContent(), BindMessage.class);
         int bindWxFlow = busiTaskFlowService.findTaskFlow(bindMessage.getCmNumber(),Constants.TASK_ID_BIND_WEICHAT);
         if(bindWxFlow <= 0){
             return  OpenResponse.fail("审计:绑定微信失败!params:[" + JSONUtils.toJSON(bindMessage.getCmNumber()) + "]");
         }
         log.info("*******************审计:绑定微信回调成功,用户：｛｝", bindMessage.getCustomerId());
         return OpenResponse.SUCCESS();
     }

    /**
     * 下单支付
     * 1,是否通知marketing，生成投资任务
     * 2，是否通知标的生成下单
     *
     * @param brDto
     * @return
     */
    private OpenResponse orderPay(BusinessReportDto brDto) {
        String  orderId = JSONObject.toJSONString(brDto.getNotifyContent());
        if (orderId == null) {
            return OpenResponse.fail("审计回调解析失败，返回信息 ："+brDto.getNotifyContent());
        }
        BusiOrderSub order = busiFinancePlanService.selectOrderSubByPrimaryKey(Long.parseLong(orderId));
        CustomerMainInfo customerMainInfo = customerMainInfoService.findAuthCustomerById(order.getCustomerId());
        //下单是否生成投资流水
        List<Long> taskFlowIds = Arrays.asList(1L,3L);
        List<BusiTaskFlow> orderTaskFlows = busiTaskFlowService.findOrderTaskFlow(taskFlowIds,customerMainInfo.getCmNumber());
        StringBuilder sbMsg= new StringBuilder();
        if (CollectionUtils.isEmpty(orderTaskFlows)){
            sbMsg.append("审计:发送下单生成新手，多劳多得任务流水失败!params:["+ JSONUtils.toJSON(customerMainInfo.getCmNumber())+"]");
        }
        List<BusiTask> taskList = busiTaskFlowService.findValidListByActionType(Constants.TASK_ACTION_TYPE_ORDER);
        List taskEndTimes= Lists.newArrayList();
        if (!CollectionUtils.isEmpty(taskList)){
            for (BusiTask busiTask : taskList) {
                if (AppConstants.MallTaskStatus.TASK_TYPE_2.equals(busiTask.getTaskType())){// 2：限时任务
                    Date taskEndTime=  busiTask.getTaskEndTime();
                    taskEndTimes.add(taskEndTime);
                }
            }
        }

        //有效限时任务
        for (int i=0;i<taskEndTimes.size();i++){
            List<BusiTaskFlow>  limitTimeTaskFlows=busiTaskFlowService.findLimitTimeTaskFlow(customerMainInfo.getCmNumber(), (Date) taskEndTimes.get(i),2L);
            if (CollectionUtils.isEmpty(limitTimeTaskFlows)){
                sbMsg.append("用户：" + customerMainInfo.getCmNumber() + "生成限时任务流水失败， 限时任务结束时间："+ taskEndTimes.get(i)+"   |");
            }
        }
        if (sbMsg!=null){
            return OpenResponse.fail("审计:发送下单生成投资任务流水失败!"+JSONUtils.toJSON(sbMsg.toString()));
        }
        //是否通知标的
        BusiDebtTransfer transfer = busiDebtTransferService.getTransferByProductId(order.getProductId());
        BusiProductSub busiProductSub =busiProductService.getBusiProductSubById(order.getProductId());
        //asyncService.nodifySubjectByOrderPay(customerMainInfo, order, busiProductSub, transfer.getTransferNo());
        log.info("*******************审计:发送下单生成投资任务流水成功,用户：｛｝", customerMainInfo.getId());
        return OpenResponse.SUCCESS();
    }

    /**
     * 捞财币商城兑换
     */
    private OpenResponse coinExchange(BusinessReportDto brDto){
        BusiOrderExchange  busiOrderExchange=busiMallService.getBusiOrderExchangeInfo(JSONObject.parse(brDto.getNotifyContent()).toString());
        if (busiOrderExchange == null){
            return OpenResponse.fail("审计:捞财币商城兑换失败!params:["+ brDto.getNotifyContent()+"]");
        }
        log.info("*******************审计:捞财币商城兑换回调成功,用户：｛｝", busiOrderExchange.getCustomerId());
        return OpenResponse.SUCCESS();
    }


    /**
     * message 系统回调
     * @return
     */
    @RequestMapping("/messageCallBack")
    @ResponseBody
    public Integer MessageCallBack(){
        return Integer.valueOf(200);
    }
}
