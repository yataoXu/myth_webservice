package com.zdmoney.service.lottery;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.lottey.LotterySignMapper;
import com.zdmoney.models.lottey.LotterySign;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 00225181 on 2016/1/5.
 */
@Service
public class LotterySignService extends BaseService<LotterySign, Long> {

    private LotterySignMapper getLotterySignMapper() {
        return (LotterySignMapper) baseMapper;
    }

    /**
     *  @Description: 查询当前有效的累计登陆天数
     *  @author huangcy
     *  @date 2017/8/1
    */
    public LotterySign findByDate(){
        Map<String, Object> params = new HashMap<>();
        params.put("nowDate",new Date());
        return getLotterySignMapper().findByDate(params);
    }
}
