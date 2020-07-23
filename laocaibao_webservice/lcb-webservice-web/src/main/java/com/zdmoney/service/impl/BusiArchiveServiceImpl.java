package com.zdmoney.service.impl;

import com.zdmoney.mapper.BusiArchiveMapper;
import com.zdmoney.service.BusiArchiveService;
import com.zdmoney.vo.BusiArchiveVo;
import com.zdmoney.webservice.api.dto.sundry.BusiArchiveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/16.
 */
@Service
public class BusiArchiveServiceImpl implements BusiArchiveService {

    @Autowired
    private BusiArchiveMapper busiArchiveMapper;

    @Override
    public void insertArchive(BusiArchiveDto model) {
        busiArchiveMapper.insertArchive(model);
    }

    @Override
    public BusiArchiveDto selectById(String id) {
        return busiArchiveMapper.selectById(id);
    }

    @Override
    public List<BusiArchiveDto> selectListByMap(Map<String, Object> map) {
        return busiArchiveMapper.selectListByMap(map);
    }

    @Override
    public int updateArchive(BusiArchiveDto model) {
        return busiArchiveMapper.updateArchive(model);
    }

    @Override
    public List<BusiArchiveVo> selectSimpleList(Map<String, Object> map) {
        return busiArchiveMapper.selectSimpleList(map);
    }
}
