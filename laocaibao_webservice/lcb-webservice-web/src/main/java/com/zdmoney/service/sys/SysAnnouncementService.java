/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.sys.SysAnnouncementMapper;
import com.zdmoney.models.sys.SysAnnouncement;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.SysAnnouncementDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SysAnnouncementService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-30 17:32
 * Mail: haoc@zendaimoney.com
 */
@Service
public class SysAnnouncementService extends BaseService<SysAnnouncement, Long> {

    private SysAnnouncementMapper getSysAnnouncementMapper() {
        return (SysAnnouncementMapper) baseMapper;
    }


    public Date selectMaxDate(String msgDate) {
        return getSysAnnouncementMapper().selectMaxDate(msgDate);
    }

    public Page getSysAnnouncementPage(int pageNo, int pageSize, Map<String, Object> requestMap) {
        //根据参数获取系统消息列表
        PageHelper.startPage(pageNo, pageSize);
        //根据参数获取系统消息列表
        List<SysAnnouncement> list = getSysAnnouncementMapper().getSysAnnouncementList(requestMap);
        com.github.pagehelper.Page<SysAnnouncement> dtoPage = (com.github.pagehelper.Page<SysAnnouncement>) list;
        //判断必填字段是否非空
        List<SysAnnouncementDTO> dtoList = new ArrayList<SysAnnouncementDTO>();
        for (SysAnnouncement sys : list) {
            SysAnnouncementDTO dto = new SysAnnouncementDTO();
            dto.setAnnouncementType(StringUtils.defaultString(sys.getAnnouncementType().toString()));
            dto.setContent(StringUtils.defaultString(sys.getContent()));
            dto.setId(StringUtils.defaultString(sys.getId().toString()));
            dto.setPubDate(AppConstants.toDateStr(sys.getPubDate()));
            dto.setTitle(StringUtils.defaultString(sys.getTitle()));
            dto.setPubMan(StringUtils.defaultString(sys.getPubMan()));
            dtoList.add(dto);
        }
        return LaocaiUtil.convertPage(dtoPage, dtoList);
    }

    public Map<String, Object> unreadSysAnnouncement(String msgDateStr) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("confirm", "0");
        if (StringUtils.isEmpty(msgDateStr)) {
            return map;
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime msgDate = DateTime.parse(msgDateStr, formatter);
        Date maxDateStr = getSysAnnouncementMapper().selectMaxDate(msgDateStr);
        if (maxDateStr != null && maxDateStr.after(msgDate.toDate())) {
            map.put("confirm", "1");
        }
        return map;
    }

}