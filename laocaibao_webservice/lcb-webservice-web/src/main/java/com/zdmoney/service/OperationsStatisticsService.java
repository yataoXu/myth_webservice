package com.zdmoney.service;

import com.zdmoney.vo.OperationsResultStatisticsVo;
import com.zdmoney.webservice.api.dto.sundry.OperationsResultStatisticsDto;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/17.
 */
public interface OperationsStatisticsService {

    void insertRecord(OperationsResultStatisticsDto dto);

    int updateRecord(OperationsResultStatisticsDto dto);

    int updateAllColumns(OperationsResultStatisticsDto dto);

    OperationsResultStatisticsDto selectById(String id);

    List<OperationsResultStatisticsDto> selectListByMap(Map<String, Object> map);

    List<OperationsResultStatisticsVo> selectMonthList(Map<String, Object> map);

    List<String> selectSimpleMonthList(Map<String, Object> map);
}
