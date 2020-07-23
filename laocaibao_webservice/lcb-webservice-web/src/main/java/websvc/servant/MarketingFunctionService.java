/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * MarketingFunctionService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-25 15:18
 * Mail: haoc@zendaimoney.com
 */
public interface MarketingFunctionService extends FunctionService {


    /*
     * 400010 积分信息
     *
     */
    Result integralInfo(ReqMain reqMain) throws Exception;

    /*
     * 400013 红包信息
     *
     */
    Result couponInfo(ReqMain reqMain) throws Exception;


    /*
     * 400016 邀请红包详情
     *
     */
    Result invitationCouponInfo(ReqMain reqMain) throws Exception;

    /*
     * 500015 获取活动列表
     *
     */
    Result lotteryTypePage(ReqMain reqMain) throws Exception;

    /*
     * 500019 生活服务获取产品列表
     *
     */
    Result lifeProductList(ReqMain reqMain) throws Exception;

    /*
     * 500020 生活服务下单
     *
     */
    Result lifeRecharge(ReqMain reqMain) throws Exception;

    /*
     * 500021 生活服务交易记录
     *
     */
    Result lifeRechargeRecord(ReqMain reqMain) throws Exception;

    /*
     * 700001 我的积分
     *
     */
    Result myIntegral(ReqMain reqMain) throws Exception;

    /*
     * 700002 积分账户余额
     *
     */
    Result integralBalance(ReqMain reqMain) throws Exception;

    /*
     * 700005 分享好友送刮刮卡
     *
     */
    Result shareFriend(ReqMain reqMain) throws Exception;

    /*
     * 700006 查询刮刮卡次数
     *
     */
    Result myLotteryCard(ReqMain reqMain) throws Exception;


    /*
     * 800008 加息券查询
     *
     */
    Result voucherList(ReqMain reqMain) throws Exception;
    /*
     * 800018 预约券查询
     *
     */
    Result bespeakList(ReqMain reqMain) throws Exception;

    /**
     * 908001 现金福利信息
     */
    Result cashInfo(ReqMain reqMain) throws Exception;
}