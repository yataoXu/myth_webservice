package com.zdmoney.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.BusiBannerMapper;
import com.zdmoney.models.BusiBanner;
import com.zdmoney.service.sys.SysSwitchService;
import com.zdmoney.utils.PropertiesUtil;
import com.zdmoney.vo.BannerVo;
import com.zdmoney.web.dto.BannerDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BusiBannerService extends BaseService<BusiBanner, Long> {

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private ConfigParamBean configParamBean;

    private BusiBannerMapper getBusiBannerMapper() {
        return (BusiBannerMapper) baseMapper;
    }

    public List<BusiBanner> getBusiBannerList(Map<String, Object> map) {
        return getBusiBannerMapper().getBusiBannerList(map);

    }

    public List<BannerDTO> queryBannerDTOListByType(String type) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("type", type);
        List<BannerVo> bannerVoList = getBusiBannerMapper().queryBanner(map);
        List<BannerDTO> dtos = Lists.newArrayList();
        String path = configParamBean.getBannerPath();
        for (BannerVo vo : bannerVoList) {
            BannerDTO dto = new BannerDTO();
            dto.setImgName(StringUtils.defaultString(path + "/" + vo.getImgName()));
            dto.setUrl(StringUtils.defaultString(vo.getUrl()));
            dto.setId(vo.getId() == null ? 0L : vo.getId());
            dto.setTitle(StringUtils.defaultIfEmpty(vo.getTitle(),""));
            dtos.add(dto);
        }
        return dtos;
    }

    public List<BannerDTO> getBannerDTOList(String type) {
        List<BannerDTO> dtoList = Lists.newArrayList();
        boolean hideBanner = sysSwitchService.getSwitchIsOnDiffEdition("hideBanner");
        if (!hideBanner) {
            dtoList = queryBannerDTOListByType(type);
        }
        return dtoList;
    }
}
