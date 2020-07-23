/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysAdvertMapper;
import com.zdmoney.models.sys.SysAdvert;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.web.dto.AdvertDTO;
import com.zdmoney.webservice.api.dto.app.Advert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * SysAdvertService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-31 11:30
 * Mail: haoc@zendaimoney.com
 */
@Service
public class SysAdvertService extends BaseService<SysAdvert, Long> {

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    private SysAdvertMapper getSysAdvertMapper() {
        return (SysAdvertMapper) baseMapper;
    }


    @Autowired
    private ConfigParamBean configParamBean;

    public AdvertDTO getAdvertDTO() {
        AdvertDTO dto = new AdvertDTO();
        String key = "advert" + DateUtil.getDateFormatString(new Date(), DateUtil.YMDSTR);
        String val = redisSessionManager.get(key);
        if (StringUtils.isNotEmpty(val)) {
            dto = JSON.parseObject(val, AdvertDTO.class);
        } else {
            boolean hideBanner = sysSwitchService.getSwitchIsOnDiffEdition("hideBanner");
            if (hideBanner) {
                return dto;
            }
            List<SysAdvert> adverts = getSysAdvertMapper().selectAdvertList();
            if (adverts.isEmpty()) {
                return dto;
            }
            SysAdvert advert = adverts.get(0);
            dto.setDetailWebUrl(StringUtils.defaultString(advert.getDetail()));
            if (StringUtils.isEmpty(advert.getImgUrl())) {
                dto.setImgUrl("");
            } else {
                if (advert.getImgUrl().contains("http://")) {
                    dto.setImgUrl(advert.getImgUrl());
                } else {
                    dto.setImgUrl(configParamBean.getImgPath() + "/" + advert.getImgUrl());
                }
            }
            redisSessionManager.put(key, JSON.toJSONString(dto), 5, TimeUnit.MINUTES);
        }
        return dto;
    }
}