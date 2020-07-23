package com.zdmoney.mapper.sys;

import com.zdmoney.models.sys.SysLog;
import com.zdmoney.models.sys.SysRequestLog;

public interface SysLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    /**
     * 保存所有请求日志
     * @param reqLog
     */
    void saveSysRequestLog(SysRequestLog reqLog);
}