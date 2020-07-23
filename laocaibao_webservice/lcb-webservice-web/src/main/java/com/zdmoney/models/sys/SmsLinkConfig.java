package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author: silence.cheng
 * Date: 2018/8/27 10:02
 */


@Table(name = "SMS_LINK_CONFIG")
@Data
public class SmsLinkConfig extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String appUrl; //0 ：网贷 1：首页  2：页面输入的自定义url

    private String status; //状态0停用1启用

    private String smsUrl; //短信链接


}
