package com.zdmoney.service.sys;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysMessageLogMapper;
import com.zdmoney.models.sys.SysMessageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SysMessageLogService extends BaseService<SysMessageLog, Long> {

    private SysMessageLogMapper getSysMessageLogMapper() {
        return (SysMessageLogMapper) baseMapper;
    }

}