package com.zdmoney.service;

import com.zdmoney.models.sys.SysHttpMessage;

public interface SysHttpMessageService {
	int insert(SysHttpMessage record);
	
	int updateByPrimaryKeySelective(SysHttpMessage record);
	
	 SysHttpMessage selectByPrimaryKey(Long id);
	 
	 int updateByOrderNumAndStrType(SysHttpMessage record);
}
