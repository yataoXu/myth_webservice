package com.zdmoney.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.mapper.BusiImgMapper;
import com.zdmoney.models.BusiImg;
import com.zdmoney.service.BusiImgService;
@Service
public class BusiImgServiceImpl implements BusiImgService {

	@Autowired
	BusiImgMapper busiImgMapper;
	@Override
	public int deleteByPrimaryKey(Short id) {
		return busiImgMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(BusiImg record) {
		return busiImgMapper.insert(record);
	}

	@Override
	public int insertSelective(BusiImg record) {
		return busiImgMapper.insertSelective(record);
	}

	@Override
	public BusiImg selectByPrimaryKey(Long id) {
		return busiImgMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BusiImg record) {
		return busiImgMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(BusiImg record) {
		return busiImgMapper.updateByPrimaryKeyWithBLOBs(record);
	}

}
