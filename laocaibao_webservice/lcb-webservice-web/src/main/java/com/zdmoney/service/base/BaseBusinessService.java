/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.base;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.customer.CustomerWhiteListMapper;
import com.zdmoney.marketing.entity.Message;
import com.zdmoney.marketing.utils.BusinessKeyGenerator;
import com.zdmoney.models.customer.CustomerAuthChannel;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.customer.CustomerWhiteList;
import com.zdmoney.component.mq.Producer;
import com.zdmoney.mq.client.group.MqGroup;
import com.zendaimoney.laocaibao.wx.api.dto.WeiChantDto;
import com.zendaimoney.laocaibao.wx.api.facade.IWechatFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseBusinessService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-31 15:27
 * Mail: haoc@zendaimoney.com
 */
@Slf4j
public abstract class BaseBusinessService<T extends AbstractEntity, ID extends Serializable> extends BaseService<T, ID> {

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private CustomerWhiteListMapper customerWhiteListMapper;

    @Autowired
    private IWechatFacadeService wechatFacadeService;

    @Autowired
    private Producer producer;

    @Autowired
    private MessageSource messageSource;

    protected CustomerMainInfo checkExistByCustomerId(Long customerId) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(customerId);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        return mainInfo;
    }

    protected CustomerMainInfo checkExistByCmNumber(String customerNumber) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectBycmNumber(customerNumber);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        return mainInfo;
    }

    public CustomerMainInfo checkExistByCellphone(String cellphone) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPhone(cellphone);
        if (mainInfo == null) {
            throw new BusinessException("cellphone.not.exist");
        }
        return mainInfo;
    }

    public void checkNotRegisterByCellphone(String cellphone) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPhone(cellphone);
        if (mainInfo != null) {
            throw new BusinessException("cellphone.registered");
        }
    }

    /**
     * 校验手机号是否在白名单中
     * @param phone
     */
    public void checkWhiteListByPhone(String phone) {
        List<CustomerWhiteList> whiteInfos = customerWhiteListMapper.selectByPhone(phone);
        if (whiteInfos == null || whiteInfos.size() == 0) {
            throw new BusinessException("cellphone.not.whitelist");
        }
    }


    public void sendWxTemplateMsg(String openId, String type, Map<String, String> map){
        try {
            WeiChantDto weiChantDto = new WeiChantDto();
            weiChantDto.setOpenId(openId);
            weiChantDto.setParam(map);
            weiChantDto.setTmlShortId(type);
            com.zendaimoney.laocaibao.wx.api.dto.ResultDto resultDto = wechatFacadeService.sendTemplateMsg(weiChantDto);
            if (resultDto.isSuccess()) {
                log.info(resultDto.getMsg());
            } else {
                log.error("微信推送失败：{} | openId: {} | map: {}" , new Object[] {resultDto.getMsg(), openId, JSON.toJSONString(map)});
            }
        } catch (Exception e) {
            log.error("微信推送异常：{} | openId: {} | map: {}" , new Object[] {e.getMessage(), openId, JSON.toJSONString(map)});
        }
    }


    public void sendRocketMqMsg(BusiTypeEnum typeEnum, Message message) {
        String key = BusinessKeyGenerator.getKey(typeEnum.name());
        String json = JSON.toJSONString(message);
        log.info("order send mq:key=" + key + ",msg=" + json);
        try {
            producer.sendMsg(MqGroup.LCB_GROUP.name(), key, json);
        } catch (Exception e) {
            log.error("发送rocketMq消息异常：{} ", e);
        }
    }

    protected String getMessage(String code){
        return messageSource.getMessage(code, null, null);
    }

    protected String getMessage(String code, Object... args){
        return messageSource.getMessage(code, args, null);
    }

    protected CustomerAuthChannel queryAuthChannelInfo(){
        return customerMainInfoMapper.queryAuthChannelInfo();
    }
}