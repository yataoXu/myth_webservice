package com.zdmoney.service;

import com.zdmoney.mapper.sys.SysNoticeMapper;
import com.zdmoney.models.sys.SysNotice;
import com.zdmoney.service.base.BaseBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yangp
 * @ClassName: SysNoticeService
 * @Description: 紧急公告接口
 * @date 2015年9月1日 下午6:05:01
 */
@Service
public class SysNoticeService extends BaseBusinessService<SysNotice, Long> {

    private SysNoticeMapper getSysNoticeMapper() {
        return (SysNoticeMapper) baseMapper;
    }

    public List<SysNotice> getSysNoticeList(Map<String, Object> map) {
        return getSysNoticeMapper().getSysNoticeList(map);
    }

}