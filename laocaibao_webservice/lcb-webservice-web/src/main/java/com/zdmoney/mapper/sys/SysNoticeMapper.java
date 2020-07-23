package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysNotice;
import com.zdmoney.vo.SysMessageVo;

import java.util.List;
import java.util.Map;

public interface SysNoticeMapper extends JdMapper<SysNotice, Long> {

    List<SysNotice> getSysNoticeList(Map<String,Object> map);

    SysMessageVo noticeDetailById(Integer id);

}