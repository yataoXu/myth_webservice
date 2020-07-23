package com.zdmoney.service;

import com.zdmoney.models.sys.SysRequestLog;
import org.springframework.stereotype.Service;

import com.zdmoney.models.sys.SysLog;

@Service("sysLogService")
public interface SysLogService {
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