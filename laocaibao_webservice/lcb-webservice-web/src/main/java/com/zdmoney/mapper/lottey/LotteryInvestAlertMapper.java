package com.zdmoney.mapper.lottey;

import com.zdmoney.models.lottey.LotteryInvestAlert;

import java.util.List;
import java.util.Map;

/**
 * Created by pc05 on 2017/4/1.
 */
public interface LotteryInvestAlertMapper {

    /**
     * 根据cmNumber和时间区间查询
     * @param params
     * @return
     */
    List<LotteryInvestAlert> selectAlertBycmNumAndDate(Map<String,Object> params);

    /**
     * 根据cmNumber和时间区间查询数量
     * @param params
     * @return
     */
    int countBycmNumAndDate(Map<String,Object> params);

    /**
     * 插入
     * @param lotteryInvestAlert
     * @return
     */
    int insert(LotteryInvestAlert lotteryInvestAlert);

    /**
     * 更新首次分享时间
     * @param lotteryInvestAlert
     * @return
     */
    int updateShareDateByPrimaryKey(LotteryInvestAlert lotteryInvestAlert);

    long queryExchangeCoinCount();

    long queryCoinCount();
}
