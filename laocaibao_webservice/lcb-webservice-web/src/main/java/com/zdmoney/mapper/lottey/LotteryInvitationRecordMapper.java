package com.zdmoney.mapper.lottey;

import com.zdmoney.models.lottey.LotteryInvitationRecord;

import java.util.Map;

/**
 * Created by pc05 on 2017/4/1.
 */
public interface LotteryInvitationRecordMapper {

    /**
     * 插入
     * @param lotteryInvitationRecord
     * @return
     */
    int insert(LotteryInvitationRecord lotteryInvitationRecord);

    /**
     * 根据cmNumber和typeNo查询数量
     * @param params
     * @return
     */
    int countByCmNumAndTypeNo(Map<String,Object> params);
}
