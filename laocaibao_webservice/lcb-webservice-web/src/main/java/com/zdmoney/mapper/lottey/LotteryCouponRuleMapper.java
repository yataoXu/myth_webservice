package com.zdmoney.mapper.lottey;

import com.zdmoney.models.lottey.LotteryCouponRule;

/**
 * Created by pc05 on 2017/4/1.
 */
public interface LotteryCouponRuleMapper {
    /**
     * 查询单条
     * @param id
     * @return
     */
    LotteryCouponRule selectByPrimaryKey(Long id);
}
