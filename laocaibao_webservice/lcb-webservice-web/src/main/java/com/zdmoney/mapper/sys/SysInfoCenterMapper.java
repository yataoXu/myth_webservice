package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysInfoCenter;

import java.util.Date;
import java.util.List;

public interface SysInfoCenterMapper extends JdMapper<SysInfoCenter, Long> {

    List<SysInfoCenter> getSysInfoCenterList(Date date);

    List<SysInfoCenter> getRecommendedBanner(Date date);

    List<SysInfoCenter> getSysInfoCenterListForEdu(Date date);
}