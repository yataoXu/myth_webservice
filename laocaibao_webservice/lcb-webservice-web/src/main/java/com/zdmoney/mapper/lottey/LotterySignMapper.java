package com.zdmoney.mapper.lottey;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.lottey.LotterySign;

import java.util.Map;

public interface LotterySignMapper extends JdMapper<LotterySign, Long> {

    LotterySign findByDate(Map<String, Object> params);
}