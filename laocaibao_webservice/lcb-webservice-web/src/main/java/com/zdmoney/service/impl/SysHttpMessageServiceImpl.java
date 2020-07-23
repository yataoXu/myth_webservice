package com.zdmoney.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.mapper.sys.SysHttpMessageMapper;
import com.zdmoney.models.sys.SysHttpMessage;
import com.zdmoney.service.SysHttpMessageService;

@Service
public class SysHttpMessageServiceImpl implements SysHttpMessageService{

	@Autowired
	private SysHttpMessageMapper sysHttpMessageMapper;
	
	public int insert(SysHttpMessage record){
		return sysHttpMessageMapper.insert(record);
	}
	
	public int updateByPrimaryKeySelective(SysHttpMessage record){
		return sysHttpMessageMapper.updateByPrimaryKeySelective(record);
	}
	
	public  SysHttpMessage selectByPrimaryKey(Long id){
		return sysHttpMessageMapper.selectByPrimaryKey(id);
	}
	
	public int updateByOrderNumAndStrType(SysHttpMessage record){
		return sysHttpMessageMapper.updateByOrderNumAndStrType(record);
	}
}
