package com.zdmoney.mapper;

import com.zdmoney.models.UcMonitorBak;

@Deprecated
public interface UcMonitorBakMapper {
    int insert(UcMonitorBak record);

    int insertSelective(UcMonitorBak record);
}