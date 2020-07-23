package com.zdmoney.mapper;

import com.zdmoney.models.UcMonitor;

@Deprecated
public interface UcMonitorMapper {
    int insert(UcMonitor record);

    int insertSelective(UcMonitor record);
}