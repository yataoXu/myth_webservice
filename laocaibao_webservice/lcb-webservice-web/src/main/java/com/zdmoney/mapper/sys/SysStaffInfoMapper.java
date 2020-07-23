package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysStaffInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysStaffInfoMapper extends JdMapper<SysStaffInfo, Long> {

    List<SysStaffInfo> selectByCmIdNum(@Param("staffIdnum") String cmIdNum);

}