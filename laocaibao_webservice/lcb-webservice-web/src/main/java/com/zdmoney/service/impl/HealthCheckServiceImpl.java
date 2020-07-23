package com.zdmoney.service.impl;

import com.zdmoney.mapper.TSysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.service.HealthCheckService;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {
	
	@Autowired
	private TSysUserMapper tSysUserMapper;
	
	@Override
	public Integer testConnection() {
		return tSysUserMapper.checkHealth();
	}

}
