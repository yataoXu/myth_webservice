package com.zdmoney.mapper;

import com.zdmoney.models.SysOperateTask;
import org.apache.ibatis.annotations.Param;

public interface SysOperateTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysOperateTask record);

    int insertSelective(SysOperateTask record);

    SysOperateTask selectByPrimaryKey(Long id);

    SysOperateTask selectBySerialNo(@Param("serialNo") String serialNo);

    int updateByPrimaryKeySelective(SysOperateTask record);

    int updateByPrimaryKey(SysOperateTask record);
}