package com.zdmoney.service.impl;

import com.zdmoney.mapper.sys.SysLogMapper;
import com.zdmoney.models.sys.SysLog;
import com.zdmoney.models.sys.SysRequestLog;
import com.zdmoney.service.SysLogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SysLogServiceImpl implements SysLogService {
	private static Log log = LogFactory.getLog(SysLogServiceImpl.class);
	
	@Autowired
	SysLogMapper sysLogMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return sysLogMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysLog record) {
		return sysLogMapper.insert(record);
	}

	@Override
	public int insertSelective(SysLog record) {
		return sysLogMapper.insertSelective(record);
	}

	@Override
	public SysLog selectByPrimaryKey(Long id) {
		return sysLogMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysLog record) {
		return sysLogMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SysLog record) {
		return sysLogMapper.updateByPrimaryKey(record);
	}

	@Override
	public void saveSysRequestLog(SysRequestLog reqLog) {
		sysLogMapper.saveSysRequestLog(reqLog);
	}

}
