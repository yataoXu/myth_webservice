package com.zdmoney.service.impl;

import com.zdmoney.mapper.OperationsResultStatisticsMapper;
import com.zdmoney.service.OperationsStatisticsService;
import com.zdmoney.vo.OperationsResultStatisticsVo;
import com.zdmoney.webservice.api.dto.sundry.OperationsResultStatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/17.
 */
@Service
public class OperationsStatisticsServiceImpl implements OperationsStatisticsService {

    @Autowired
    private OperationsResultStatisticsMapper operationsStatisticsMapper;

    @Override
    public void insertRecord(OperationsResultStatisticsDto dto) {
        operationsStatisticsMapper.insertRecord(dto);
    }

    @Override
    public int updateRecord(OperationsResultStatisticsDto dto) {
        return operationsStatisticsMapper.updateRecord(dto);
    }

    @Override
    public int updateAllColumns(OperationsResultStatisticsDto dto) {
        return operationsStatisticsMapper.updateAllColumns(dto);
    }

    @Override
    public OperationsResultStatisticsDto selectById(String id) {
        return operationsStatisticsMapper.selectById(id);
    }

    @Override
    public List<OperationsResultStatisticsDto> selectListByMap(Map<String, Object> map) {
        return operationsStatisticsMapper.selectListByMap(map);
    }

    @Override
    public List<OperationsResultStatisticsVo> selectMonthList(Map<String, Object> map) {
        return operationsStatisticsMapper.selectMonthList(map);
    }

    @Override
    public List<String> selectSimpleMonthList(Map<String, Object> map) {
        return operationsStatisticsMapper.selectSimpleMonthList(map);
    }
}
