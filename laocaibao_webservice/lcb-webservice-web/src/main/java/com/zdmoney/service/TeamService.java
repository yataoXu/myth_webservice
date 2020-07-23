package com.zdmoney.service;

import com.zdmoney.common.Result;
import com.zdmoney.models.customer.CustomerMainInfo;
import websvc.models.*;
import websvc.req.ReqMain;

import javax.annotation.Resource;


public interface TeamService {

	/**
	 * 邀请好友当日获取积分，累计获取积分，累计人数
	 * @param model_820001
	 * @return
     */
	Result inviteFriendsIntegralRecord(Model_820001 model_820001);

	/**
	 * 生成创建队伍页面
	 * @return
     */
	Result goCreateTeam();

	/**
	 * 创建队伍
	 * @param model_820003
	 * @return
     */
	Result createTeam(Model_820003 model_820003);

	/**
	 * 加入队伍申请接口
	 * @param model_820004
	 * @return
     */
	Result joinTeam(Model_820004 model_820004);

	/**
	 * 申请列表
	 * @param model_820005
	 * @return
     */
	Result checkTeamApply(Model_820005 model_820005);

	/**
	 * 土豪列表
	 * @param model_820011
	 * @return
     */
	Result tuHaoList(Model_820011 model_820011);

	/**
	 * 邀请好友
	 * @param model_820006
	 * @return
     */
	Result inviteFriends(Model_820006 model_820006);

	/**
	 * 已注册申请加入
	 * @param model_820012
	 * @return
	 * @throws Exception
     */
	Result toLoginApply(Model_820012 model_820012) throws Exception;

	/**
	 * 未注册，去注册后申请加入
	 * @param reqMain
	 * @return
	 * @throws Exception
     */
	Result toRegisterApply(ReqMain reqMain) throws Exception;

	/**
	 * 排行榜
	 * @param model_820015
	 * @return
	 */
	Result teamRank(Model_820015 model_820015);

    /**
     * 查询邀请用户首次投资详情
     * @param model_820016
     * @return
     */
    Result investFriendsInfo(Model_820016 model_820016);

    /**
     * 是否是内部员工
     * @param model_820017
     * @return
     */
    Result isInsideStaff(Model_820017 model_820017);

    /**
     * 查询2016年度账单
     * @param model_820018
     * @return
     */
    Result queryAnnualBill2016(Model_820018 model_820018);
}
