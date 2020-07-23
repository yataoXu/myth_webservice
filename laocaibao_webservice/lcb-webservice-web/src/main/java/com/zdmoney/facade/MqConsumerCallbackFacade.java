package com.zdmoney.facade;

import com.zdmoney.integral.api.dto.coupon.CouponPublishDto;
import com.zdmoney.integral.api.dto.coupon.CouponResDto;
import com.zdmoney.integral.api.dto.coupon.CouponShareActiveDto;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.facade.IMqConsumerCallbackFacade;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by user on 2018/1/19.
 */
@Slf4j
public class MqConsumerCallbackFacade implements IMqConsumerCallbackFacade {

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    private WelfareService welfareService;

    /**
     * consume message and send registering coupon
     * the message body is the customer's cmnumber
     * @param data
     */
    private boolean processRegistrationCoupon(String data){
        //JSONObject json = JSONObject.fromObject(data);
        //check if the cmNumber is blank
        boolean success = true;
        if(StringUtils.isBlank(data)){
            log.error("消息内容为空:",new RuntimeException("用户编号为空"));
            return success;
        }
        JSONObject json = JSONObject.fromObject(data);
        String cmNumber = json.getString("cmNumber");
        CouponPublishDto couponPublishDto = new CouponPublishDto();
        couponPublishDto.setAccountNo(cmNumber);
        couponPublishDto.setSource("REG");
        try {
            com.zdmoney.integral.api.common.dto.ResultDto<List<CouponResDto>> resultDto = welfareService.publishCouponBySource(couponPublishDto);
            if(resultDto == null || !resultDto.isSuccess()){
                success = false;
                log.error("cmNumber:｛｝", couponPublishDto.getAccountNo() + ",注册赠送红包失败",resultDto.getMsg());
                //no need to rethrow exception,because we can't deal with it,and we already log erro message here //throw new BusinessException("注册赠送红包失败");
            }else
                log.info("cmNumber:{}", couponPublishDto.getAccountNo() + ",注册赠送红包成功");
        } catch (Exception e) {
            success = false;
            log.error("[cmNumber: {}] 调用积分接口发送红包出现异常，{}", couponPublishDto.getAccountNo(), e);
        }
        String redNo = null;
        try{
            Object t = json.get("redNo");
            if(t != null) redNo = t.toString();
        }catch(Exception e){
            log.info("redNo 为空");
        }
        if (StringUtils.isNotEmpty(redNo)) {
            CouponShareActiveDto activeDto = new CouponShareActiveDto();
            activeDto.setAccountNo(cmNumber);
            activeDto.setNo(redNo);
            try {
                com.zdmoney.integral.api.common.dto.ResultDto resultDto = couponFacadeService.activeCoupon(activeDto);
                if (!resultDto.isSuccess()) {
                    success = false;
                    log.error("开启邀请注册红包 [cmNumber: {}] 失败： 调用失败 {}", cmNumber, resultDto.getMsg());
                }
            } catch (Exception e) {
                success = false;
                log.error("开启邀请注册红包 [cmNumber: {}] 失败： 系统异常 {}", cmNumber, e);
            }
        }
        return success;
    }
    @Override
    public ResultDto<Boolean> sendWelfare(String data) {
        boolean result = processRegistrationCoupon(data);
        ResultDto<Boolean> resultDto = new ResultDto<>(Boolean.valueOf(result));
        return resultDto;
    }
}
