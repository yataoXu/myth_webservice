package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysParameter;

import java.util.List;
import java.util.Map;

public interface SysParameterMapper extends JdMapper<SysParameter, Long> {

    List<SysParameter> getSysParameterWithoutCache(String prType);

    List<SysParameter> getDefaultSysParameterWithoutCache(String prType);

    SysParameter getSysParameterPrs(Map<String, Object> map);
}