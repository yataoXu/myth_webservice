package com.zdmoney.mapper.sys;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.sys.SysAnnouncement;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ******声明*************
 *
 * 版权所有：zendaimoney
 *
 * 项目名称：laocaibao_webservice
 * 类    名称：SysAnnouncementMapper
 * 功能描述：
 *
 * 创建人员：CJ
 * 创建时间：2015年6月28日
 * @version
 ********修改记录************
 * 修改人员：
 * 修改时间：
 * 修改描述：
 */
public interface SysAnnouncementMapper extends JdMapper<SysAnnouncement, Long> {

    List<SysAnnouncement> getSysAnnouncementList(Map<String,Object> map);//根据查询参数获取列表

    Date selectMaxDate(@Param("msgDate")String msgDate);
}