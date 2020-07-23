package com.zdmoney.service.sys;

import com.google.common.collect.Lists;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysHeadMapper;
import com.zdmoney.models.sys.SysHead;
import com.zdmoney.web.dto.HeadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by 00225181 on 2016/1/4.
 */
@Service
@Slf4j
public class SysHeadService extends BaseService<SysHead, Long> {

    private SysHeadMapper getSysHeadMapper() {
        return (SysHeadMapper) baseMapper;
    }

    public List<HeadDTO> findHeadDtoByNewsType(String newsType) {
        List<HeadDTO> dtoList = Lists.newArrayList();
        Example example = new Example(SysHead.class);
        Date nowDate = new Date();
        example.createCriteria()
                .andEqualTo("status", "1")
                .andEqualTo("auditStatus", "1")
                .andEqualTo("newsType", newsType)
                .andLessThanOrEqualTo("startDate", nowDate)
                .andGreaterThanOrEqualTo("endDate", nowDate);
        example.setOrderByClause("SORT desc,CREATE_DATE desc");
        List<SysHead> sysHeadList = getSysHeadMapper().selectByExample(example);
        for (SysHead sysHead : sysHeadList) {
            HeadDTO dto = new HeadDTO();
            dto.setContent(sysHead.getContent());
            dtoList.add(dto);
        }
        return dtoList;
    }

}
