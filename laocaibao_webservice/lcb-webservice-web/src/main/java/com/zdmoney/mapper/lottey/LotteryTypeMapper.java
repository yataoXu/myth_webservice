package com.zdmoney.mapper.lottey;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.lottey.LotteryType;
import com.zdmoney.web.dto.LotteryTypeDTO;

import java.util.List;

public interface LotteryTypeMapper extends JdMapper<LotteryType, Long> {

    List<LotteryTypeDTO> selectLotteryType();

}