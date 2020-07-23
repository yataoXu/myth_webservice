package com.zdmoney.service.lottery;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.lottey.LotteryInvestAlertMapper;
import com.zdmoney.models.lottey.LotteryInvestAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by pc05 on 2017/4/1.
 */
@Service
public class LotteryInvestAlertService extends BaseService<LotteryInvestAlert,Long>{

    @Autowired
    private LotteryInvestAlertMapper lotteryInvestAlertMapper;

    /**
     * 根据cmNum和时间区间查询记录
     * @param params
     * @return
     */
    public List<LotteryInvestAlert> selectAlertBycmNumAndDate(Map<String,Object> params){
        return lotteryInvestAlertMapper.selectAlertBycmNumAndDate(params);
    }

    /**
     * 根据cmNumber和时间区间查询
     * @param params
     * @return
     */
    public int countBycmNumAndDate(Map<String,Object> params){
        return lotteryInvestAlertMapper.countBycmNumAndDate(params);
    }

    /**
     * 插入
     * @param lotteryInvestAlert
     * @return
     */
    public int insert(LotteryInvestAlert lotteryInvestAlert){
        return lotteryInvestAlertMapper.insert(lotteryInvestAlert);
    }

    /**
     * 更新
     * @param lotteryInvestAlert
     * @return
     */
    public int updateShareDateByPrimaryKey(LotteryInvestAlert lotteryInvestAlert){
        return lotteryInvestAlertMapper.updateShareDateByPrimaryKey(lotteryInvestAlert);
    }

    /**
     * 抓娃娃 所需捞财币数量
     * @return
     */
    public long queryExchangeCoinCount(){
        return lotteryInvestAlertMapper.queryExchangeCoinCount();
    }

    /**
     * 黄金刮刮卡
     * @return
     */
    public long queryCoinCount(){
        return lotteryInvestAlertMapper.queryCoinCount();
    }
}
