package com.zdmoney.models.customer;

import lombok.Data;

/**
 * 用户实名认证通道信息
 * Created by gaol on 2017/1/18
 **/
@Data
public class CustomerAuthChannel {

    private Integer id;

    private String channelName;

    private Integer status;
}
