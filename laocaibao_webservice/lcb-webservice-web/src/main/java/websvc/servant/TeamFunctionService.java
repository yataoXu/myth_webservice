/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * TeamFunctionService
 * <p/>
 * Author: ZhouYj
 * Date: 2016-03-30
 */
public interface TeamFunctionService extends FunctionService {

    /*
     * 820001 邀请好友当日获取积分，累计获取积分，累计人数
     *
     */
    Result inviteFriendsIntegralRecord(ReqMain reqMain) throws Exception;

    /*
     * 820002 捞财币兑换刮刮卡
     *
     */
    Result lcCoin2GuaGuaKa(ReqMain reqMain) throws Exception;

    /*
     * 820003 创建队伍
     *
     */
    Result createTeam(ReqMain reqMain) throws Exception;

    /*
     * 820004 加入队伍申请接口
     *
     */
    Result joinTeam(ReqMain reqMain) throws Exception;

    /*
     * 820005 查看队伍申请
     *
     */
    Result checkTeamApply(ReqMain reqMain) throws Exception;

    /*
     * 820006 邀请好友
     *
     */
    Result inviteFriends(ReqMain reqMain) throws Exception;

    /*
     * 820007 领取团队任务奖励
     *
     */
    Result receiveTeamAward(ReqMain reqMain) throws Exception;

    /*
     * 820008 查看团队任务
     *
     */
    Result checkTeamTask(ReqMain reqMain) throws Exception;

    /*
     * 820009 队长审核
     *
     */
    Result captainAudit(ReqMain reqMain) throws Exception;

    /*
     * 820010 生成创建队伍页面
     *
     */
    Result goCreateTeam(ReqMain reqMain) throws Exception;

    /*
     * 820011 土豪列表
     *
     */
    Result tuHaoList(ReqMain reqMain) throws Exception;

    /**
     * 820012 已注册申请加入
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result toLoginApply(ReqMain reqMain) throws Exception;

    /**
     * 820013 未注册，去注册后申请加入
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result toRegisterApply(ReqMain reqMain) throws Exception;


    /**
     * 820014 检查用户是否已加入过队伍
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result checkIsMember(ReqMain reqMain)throws Exception;

    /**
     * 820015 排行榜
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result getRankList(ReqMain reqMain)throws Exception;

    /**
     * 820017 是否是内部员工
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result isInsideStaff(ReqMain reqMain) throws Exception;

    /**
     * 820018 查询2016年度账单
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryAnnualBill2016(ReqMain reqMain) throws Exception;
}
