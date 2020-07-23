package com.zdmoney.mapper.sys;

import com.zdmoney.models.sys.SysHttpMessage;

public interface SysHttpMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysHttpMessage record);

    int insertSelective(SysHttpMessage record);

    SysHttpMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysHttpMessage record);

    int updateByPrimaryKey(SysHttpMessage record);
    
    int updateByOrderNumAndStrType(SysHttpMessage record);
}