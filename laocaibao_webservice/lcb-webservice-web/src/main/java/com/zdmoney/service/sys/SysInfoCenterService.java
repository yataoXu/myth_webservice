/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysInfoCenterMapper;
import com.zdmoney.models.sys.SysInfoCenter;
import com.zdmoney.service.BusiBannerService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.BannerDTO;
import com.zdmoney.web.dto.SysInfoCenterDTO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SysInfoCenterService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-31 11:21
 * Mail: haoc@zendaimoney.com
 */
@Service
public class SysInfoCenterService extends BaseService<SysInfoCenter, Long> {
    private SysInfoCenterMapper getSysInfoCenterMapper() {
        return (SysInfoCenterMapper) baseMapper;
    }

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private BusiBannerService busiBannerService;

    public Page<SysInfoCenterDTO> getSysInfoCenterDTOPage(Integer pageNo, Integer pageSize){
        PageHelper.startPage(pageNo, pageSize);
        List<SysInfoCenter> list = getSysInfoCenterMapper().getSysInfoCenterList(new Date());
        PageInfo<SysInfoCenter> page = new PageInfo<SysInfoCenter>(list);
        List<SysInfoCenterDTO> dtoList = new ArrayList<SysInfoCenterDTO>();
        SysInfoCenter sysInfoCenter = null;
        SysInfoCenterDTO sysInfoCenterDTO = null;
        for (SysInfoCenter aList : list) {
            sysInfoCenter = aList;
            sysInfoCenterDTO = new SysInfoCenterDTO();
            sysInfoCenterDTO.setId(sysInfoCenter.getId());
            sysInfoCenterDTO.setImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenter.getImgUrl());
            sysInfoCenterDTO.setPcImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenter.getPcImgUrl());
            sysInfoCenterDTO.setInfoSource(sysInfoCenter.getInfoSource());
            sysInfoCenterDTO.setPublishDate(DateUtil.getDateFormatString(sysInfoCenter.getPublishDate(), DateUtil.YMDSTR));
            sysInfoCenterDTO.setRemark(sysInfoCenter.getRemark());
            sysInfoCenterDTO.setSummary(sysInfoCenter.getSummary());
            sysInfoCenterDTO.setTitle(sysInfoCenter.getTitle());
            sysInfoCenterDTO.setTopStatus(sysInfoCenter.getTopStatus());
            dtoList.add(sysInfoCenterDTO);
        }
        //分页显示
        Page<SysInfoCenterDTO> sysInfoCenterDTOs = new Page<SysInfoCenterDTO>();
        sysInfoCenterDTOs.setResults(dtoList);
        sysInfoCenterDTOs.setPageNo(page.getPageNum());
        sysInfoCenterDTOs.setPageSize(page.getPageSize());
        sysInfoCenterDTOs.setTotalRecord((int) page.getTotal());
        sysInfoCenterDTOs.setTotalPage(page.getPages());
        return sysInfoCenterDTOs;
    }

    public Page<SysInfoCenterDTO> getSysInfoCenterDTOPageForEdu(Integer pageNo, Integer pageSize){
        PageHelper.startPage(pageNo, pageSize);
        List<SysInfoCenter> list = getSysInfoCenterMapper().getSysInfoCenterListForEdu(new Date());
        PageInfo<SysInfoCenter> page = new PageInfo<SysInfoCenter>(list);
        List<SysInfoCenterDTO> dtoList = new ArrayList<SysInfoCenterDTO>();
        SysInfoCenter sysInfoCenter = null;
        SysInfoCenterDTO sysInfoCenterDTO = null;
        for (SysInfoCenter aList : list) {
            sysInfoCenter = aList;
            sysInfoCenterDTO = new SysInfoCenterDTO();
            sysInfoCenterDTO.setId(sysInfoCenter.getId());
            sysInfoCenterDTO.setImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenter.getImgUrl());
            sysInfoCenterDTO.setPcImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenter.getPcImgUrl());
            sysInfoCenterDTO.setInfoSource(sysInfoCenter.getInfoSource());
            sysInfoCenterDTO.setPublishDate(DateUtil.getDateFormatString(sysInfoCenter.getPublishDate(), DateUtil.YMDSTR));
            sysInfoCenterDTO.setRemark(sysInfoCenter.getRemark());
            sysInfoCenterDTO.setSummary(sysInfoCenter.getSummary());
            sysInfoCenterDTO.setTitle(sysInfoCenter.getTitle());
            sysInfoCenterDTO.setTopStatus(sysInfoCenter.getTopStatus());
            dtoList.add(sysInfoCenterDTO);
        }
        //分页显示
        Page<SysInfoCenterDTO> sysInfoCenterDTOs = new Page<SysInfoCenterDTO>();
        sysInfoCenterDTOs.setResults(dtoList);
        sysInfoCenterDTOs.setPageNo(page.getPageNum());
        sysInfoCenterDTOs.setPageSize(page.getPageSize());
        sysInfoCenterDTOs.setTotalRecord((int) page.getTotal());
        sysInfoCenterDTOs.setTotalPage(page.getPages());
        return sysInfoCenterDTOs;
    }


    public SysInfoCenterDTO getSysInfoCenterDTO(Long sid) {
        SysInfoCenter sysInfoCenter = findOne(sid);
        SysInfoCenterDTO sysInfoCenterDTO = new SysInfoCenterDTO();
        if (sysInfoCenter != null) {
            sysInfoCenterDTO.setId(sysInfoCenter.getId());
            sysInfoCenterDTO.setImgUrl(sysInfoCenter.getImgUrl());
            sysInfoCenterDTO.setInfoSource(sysInfoCenter.getInfoSource());
            sysInfoCenterDTO.setPublishDate(new DateTime(sysInfoCenter.getPublishDate()).toString("MM-dd HH:mm"));
            sysInfoCenterDTO.setRemark(sysInfoCenter.getRemark());
            sysInfoCenterDTO.setSummary(sysInfoCenter.getSummary());
            sysInfoCenterDTO.setTitle(sysInfoCenter.getTitle());
            sysInfoCenterDTO.setTopStatus(sysInfoCenter.getTopStatus());
        }
        return sysInfoCenterDTO;
    }

    public List<BannerDTO> getHeadBanner(){
        List<BannerDTO> dtos = busiBannerService.queryBannerDTOListByType("6"); //资讯页头部banner
        return dtos;
    }

    public SysInfoCenter getRecommendedBanner(){
        List<SysInfoCenter> recommendedBanner = getSysInfoCenterMapper().getRecommendedBanner(new Date());
        if(recommendedBanner.size()>0){
            SysInfoCenter sysInfoCenter = recommendedBanner.get(0);
            sysInfoCenter.setBanner(configParamBean.getImgPath() + "/" + sysInfoCenter.getBanner());
            sysInfoCenter.setImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenter.getImgUrl());
            sysInfoCenter.setPcImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenter.getPcImgUrl());
            return sysInfoCenter;
        }
        return new SysInfoCenter();
    }


    public List<SysInfoCenter> getSysInfoCenter(){

        List<SysInfoCenter> list = getSysInfoCenterMapper().getSysInfoCenterList(new Date());

        return list;
    }
}