package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.sundry.OperationsResultStatisticsDto;

/**
 * Created by user on 2018/1/17.
 */
public interface IOperationsStatisticsFacadeService {

    ResultDto create(OperationsResultStatisticsDto model);

    ResultDto updateById(OperationsResultStatisticsDto model);

    ResultDto<OperationsResultStatisticsDto> selectById(String id);

    PageResultDto<OperationsResultStatisticsDto> searchList(PageSearchDto pageSearchDto);

    ResultDto<Boolean> checkExists(String analysisTime, String id);

}
