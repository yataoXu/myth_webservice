package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysSwitch;

import java.util.List;

public interface SysSwitchMapper extends JdMapper<SysSwitch, Long> {

    List<SysSwitch> getAllParameter();

    List<SysSwitch> selectByType(String type);

}