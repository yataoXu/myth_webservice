package com.zdmoney.mapper;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.CustInviteLine;
import com.zdmoney.models.InvestFriendsInfo;
import com.zdmoney.vo.TopInviteVo;

import java.util.List;
import java.util.Map;

public interface CustInviteLineMapper extends JdMapper<CustInviteLine, Long>{

    int updateByCondition(CustInviteLine inviteLine);

    List<CustInviteLine> selectByCondition(Map<String, Object> map);

    int selectInviteNum(String cellphone);

    int selectCurMonthInviteNum(String cellphone);

    int isStaff(Map<String, Object> map);

    int getRankByCellphone(Map<String, Object> map);

    TopInviteVo getInviteCountByCellphone(Map<String, Object> map);

    List<TopInviteVo> getTopInvistLineList(Map<String, Object> map);

    List<String> investFriendsPhone(Map<String, Object> map);

    int isInvest(Map<String, Object> map);

    List<InvestFriendsInfo> investFriendsInfo(Map<String, Object> map);

    List<String> queryInsideStaffInfo();

    List<InvestFriendsInfo> investFriendsDetail(Map<String, Object> map);

    int isInsideStaff(Map<String, Object> map);

    List<InvestFriendsInfo> isomerismFriendInvest(Map<String, Object> map);

    List<InvestFriendsInfo> queryLotteryCount(Map<String, Object> map);

    List<InvestFriendsInfo> queryHelpFriendsKillMonster(Map<String, Object> map);

    List<InvestFriendsInfo> queryHelpFriends(Map<String, Object> map);
}