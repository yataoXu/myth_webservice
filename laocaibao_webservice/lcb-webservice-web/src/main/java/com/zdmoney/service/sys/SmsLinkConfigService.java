/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.SmsLinkConfigMapper;
import com.zdmoney.mapper.sys.SysStaffInfoMapper;
import com.zdmoney.models.sys.SmsLinkConfig;
import com.zdmoney.models.sys.SysStaffInfo;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.secure.utils.ThreeDesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmsLinkConfigService extends BaseService<SmsLinkConfig, Long> {

    private SmsLinkConfigMapper getSmsLinkConfigMapper() {
        return (SmsLinkConfigMapper) baseMapper;
    }

    public  SmsLinkConfig getSmsLink(){

        Example example = new Example(SmsLinkConfig.class);
        example.createCriteria().andEqualTo("status", "1");
        List<SmsLinkConfig> smsLinkConfigs = getSmsLinkConfigMapper().selectByExample(example);
        SmsLinkConfig linkConfig = null;
        if (CollectionUtils.isNotEmpty(smsLinkConfigs)){
            linkConfig =smsLinkConfigs.get(0);
        }
        return linkConfig;
    }
}