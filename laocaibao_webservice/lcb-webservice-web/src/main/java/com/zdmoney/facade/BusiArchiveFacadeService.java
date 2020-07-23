package com.zdmoney.facade;

import com.zdmoney.service.BusiArchiveService;
import com.zdmoney.utils.Page;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.sundry.BusiArchiveDto;
import com.zdmoney.webservice.api.facade.IBusiArchiveFacadeService;
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
public class BusiArchiveFacadeService implements IBusiArchiveFacadeService {

    @Autowired
    private BusiArchiveService busiArchiveService;

    @Override
    public ResultDto create(BusiArchiveDto model) {
        ResultDto resultDto = ResultDto.SUCCESS();
        try{
            busiArchiveService.insertArchive(model);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("保存失败");
        }
        return resultDto;
    }

    @Override
    public ResultDto updateById(BusiArchiveDto model) {
        ResultDto resultDto = ResultDto.SUCCESS();
        try{
            busiArchiveService.updateArchive(model);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("保存失败");
        }
        return resultDto;
    }

    @Override
    public ResultDto<BusiArchiveDto> selectById(String id) {
        ResultDto<BusiArchiveDto> resultDto = ResultDto.SUCCESS();
        try{
            BusiArchiveDto archiveDto = busiArchiveService.selectById(id);
            resultDto.setData(archiveDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("查询失败");
        }
        return resultDto;
    }

    @Override
    public PageResultDto<BusiArchiveDto> searchList(PageSearchDto searchDto) {
        PageResultDto<BusiArchiveDto> resultDto = new PageResultDto<>();

        Map<String,Object> map = new HashMap<>();
        Page page = new Page();
        page.setPageNo(searchDto.getPageNo());
        page.setPageSize(searchDto.getPageSize());
        map.put("page",page);
        List<BusiArchiveDto> list = busiArchiveService.selectListByMap(map);
        resultDto.setDataList(list);
        resultDto.setTotalPage(page.getTotalPage());
        resultDto.setTotalSize(page.getTotalRecord());
        return resultDto;
    }

    @Override
    public ResultDto<Boolean> checkExists(String name, String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("fullName",name);
        map.put("neId",id);
        List<BusiArchiveDto> archives = busiArchiveService.selectListByMap(map);
        boolean exists = false;
        if(archives.size() > 0) exists= true;
        ResultDto<Boolean> resultDto = new ResultDto(exists);
        return resultDto;
    }
}
