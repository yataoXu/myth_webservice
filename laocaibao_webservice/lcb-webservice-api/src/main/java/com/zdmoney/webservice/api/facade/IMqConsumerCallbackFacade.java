package com.zdmoney.webservice.api.facade;


import com.zdmoney.webservice.api.common.dto.ResultDto;

/**
 * Created by user on 2018/1/19.
 */
public interface IMqConsumerCallbackFacade {
    ResultDto<Boolean> sendWelfare(String data);
}
