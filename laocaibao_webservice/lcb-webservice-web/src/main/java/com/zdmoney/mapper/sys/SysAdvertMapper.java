package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysAdvert;

import java.util.List;

public interface SysAdvertMapper extends JdMapper<SysAdvert, Long> {
    List<SysAdvert> selectAdvertList();
}