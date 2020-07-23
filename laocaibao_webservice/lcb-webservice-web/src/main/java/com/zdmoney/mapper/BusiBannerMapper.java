package com.zdmoney.mapper;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.BusiBanner;
import com.zdmoney.vo.BannerVo;

import java.util.List;
import java.util.Map;



public interface BusiBannerMapper extends JdMapper<BusiBanner, Long> {
    List<BusiBanner> getBusiBannerList(Map<String, Object> params);

    List<BannerVo> queryBanner(Map<String,Object> map);
    
}