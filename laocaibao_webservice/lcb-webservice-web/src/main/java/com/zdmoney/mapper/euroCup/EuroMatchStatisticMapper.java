package com.zdmoney.mapper.euroCup;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.euroCup.EuroMatchStatistic;
import com.zdmoney.web.dto.euroCup.EuroGuessResultDTO;
import com.zdmoney.web.dto.euroCup.OneDayWinner;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EuroMatchStatisticMapper extends JdMapper<EuroMatchStatistic, Long> {

    List<EuroGuessResultDTO> getGuessList(Map map);
    List<OneDayWinner> findWinner(@Param("matchDate")String date);
}