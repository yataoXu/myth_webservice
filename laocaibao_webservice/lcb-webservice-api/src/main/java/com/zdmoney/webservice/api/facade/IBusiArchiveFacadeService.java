package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.sundry.BusiArchiveDto;

/**
 * Created by user on 2018/1/17.
 */
public interface IBusiArchiveFacadeService {

    ResultDto create(BusiArchiveDto model);

    ResultDto updateById(BusiArchiveDto model);

    ResultDto<BusiArchiveDto> selectById(String id);

    PageResultDto<BusiArchiveDto> searchList(PageSearchDto searchDto);

    ResultDto<Boolean> checkExists(String name, String id);

}

