package com.zdmoney.facade;

import com.zdmoney.service.OperationsStatisticsService;
import com.zdmoney.utils.Page;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.sundry.OperationsResultStatisticsDto;
import com.zdmoney.webservice.api.facade.IOperationsStatisticsFacadeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/17.
 */
@Component
@Slf4j
public class OperationsStatisticsFacadeService implements IOperationsStatisticsFacadeService {

    @Autowired
    private OperationsStatisticsService operationsStatisticsService;

    @Override
    public ResultDto create(OperationsResultStatisticsDto model) {
        ResultDto resultDto = ResultDto.SUCCESS();
        try{
            operationsStatisticsService.insertRecord(model);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("保存失败");
        }
        return resultDto;
    }

    @Override
    public ResultDto updateById(OperationsResultStatisticsDto model) {
        ResultDto resultDto = ResultDto.SUCCESS();
        try{
            operationsStatisticsService.updateAllColumns(model);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("保存失败");
        }
        return resultDto;
    }

    @Override
    public ResultDto<OperationsResultStatisticsDto> selectById(String id) {
        ResultDto<OperationsResultStatisticsDto> resultDto = ResultDto.SUCCESS();
        OperationsResultStatisticsDto statisticsDto = operationsStatisticsService.selectById(id);
        resultDto.setData(statisticsDto);
        return resultDto;
    }

    @Override
    public PageResultDto<OperationsResultStatisticsDto> searchList(PageSearchDto searchDto) {
        PageResultDto<OperationsResultStatisticsDto> resultDto = new PageResultDto<>();

        Map<String,Object> map = new HashMap<>();
        Page page = new Page();
        page.setPageNo(searchDto.getPageNo());
        page.setPageSize(searchDto.getPageSize());
        map.put("page",page);
        List<OperationsResultStatisticsDto> list = operationsStatisticsService.selectListByMap(map);
        resultDto.setDataList(list);
        resultDto.setTotalPage(page.getTotalPage());
        resultDto.setTotalSize(page.getTotalRecord());
        return resultDto;
    }

    @Override
    public ResultDto<Boolean> checkExists(String analysisTime, String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("analysisTime",analysisTime);
        map.put("neId",id);
        List<OperationsResultStatisticsDto> archives = operationsStatisticsService.selectListByMap(map);
        boolean exists = false;
        if(archives.size() > 0) exists= true;
        ResultDto<Boolean> resultDto = new ResultDto(exists);
        return resultDto;
    }
}
