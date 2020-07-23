package com.zdmoney.mapper;

import com.zdmoney.vo.BusiArchiveVo;
import com.zdmoney.webservice.api.dto.sundry.BusiArchiveDto;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/16.
 */
public interface BusiArchiveMapper {

    void insertArchive(BusiArchiveDto model);

    BusiArchiveDto selectById(String id);

    List<BusiArchiveDto> selectListByMap(Map<String, Object> map);

   int updateArchive(BusiArchiveDto model);

    List<BusiArchiveVo> selectSimpleList(Map<String, Object> map);
}
