package com.zdmoney.mapper.euroCup;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.euroCup.EuroMatchSchedule;
import com.zdmoney.web.dto.euroCup.EuroMatchScheduleDTO;

import java.util.Date;
import java.util.List;

public interface EuroMatchScheduleMapper extends JdMapper<EuroMatchSchedule, Long> {

    Date getLastMatchDate(Date currentDate);

    Date getNextMatchDate(Date currentDate);

    List<EuroMatchScheduleDTO> getNextScheduleList(String nextDateStr);
}