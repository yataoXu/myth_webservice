package com.zdmoney.service.impl;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.mapper.BusiBrandMapper;
import com.zdmoney.webservice.api.dto.busi.BusiBrandDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zdmoney.service.IBusiBrandService;

import java.util.List;
import java.util.Map;

/**
 *
 * @date 2019-03-15 17:44:39
 */
@Service("busiBrandService")
public class BusiBrandServiceImpl implements IBusiBrandService {

    @Autowired
    private BusiBrandMapper busiBrandMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    @Override
    public BusiBrandDto getBusiBrand(String displayDate) {
        BusiBrandDto busiBrandDto = busiBrandMapper.getBusiBrand(displayDate);
        if (busiBrandDto != null && StringUtils.isNotEmpty(busiBrandDto.getImgUrl())) {
            busiBrandDto.setImgUrl(configParamBean.getImgPath() + "/" + busiBrandDto.getImgUrl());
        }
        return busiBrandDto;
    }
}
