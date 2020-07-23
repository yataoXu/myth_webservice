/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.constant;

import com.google.common.collect.ImmutableMap;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.integral.api.dto.coupon.enm.CouponSource;

import java.util.Map;

/**
 * Constants
 * <p/>
 * Author: Hao Chen
 * Date: 2016-02-27 11:54
 * Mail: haoc@zendaimoney.com
 */
public class Constants {

    // 任务类型：一次性
    public static final String TASK_TYPE_ONCE = "1";
    // 任务类型：限时
    public static final String TASK_TYPE_TIME_LIMITED = "2";
    // 任务类型：每月
    public static final String TASK_TYPE_MONTHLY = "3";
    // 任务类型：每日
    public static final String TASK_TYPE_DAILY = "4";
    // 任务类型：无限
    public static final String TASK_TYPE_UNLIMITED = "5";

    // 动作类型：认证
    public static final String TASK_ACTION_TYPE_AUTH = "1";
    // 动作类型：投资
    public static final String TASK_ACTION_TYPE_ORDER = "2";
    // 动作类型：绑定
    public static final String TASK_ACTION_TYPE_BIND = "3";
    // 动作类型：签到
    public static final String TASK_ACTION_TYPE_SIGN = "4";
    // 动作类型：起息
    public static final String TASK_ACTION_TYPE_INTEREST = "5";
    // 动作类型：入队
    public static final String TASK_ACTION_TYPE_JOIN = "6";

    // 动作类型：绑定银行卡
    public static final String TASK_ACTION_TYPE_BIND_CARD = "7";

    // 任务状态：完成，待领取
    public static final String TASK_FLOW_STATUS_TO_OBTAIN = "0";
    // 任务状态：结束，已领取
    public static final String TASK_FLOW_STATUS_OVER = "1";
    // 任务状态：领取中
    public static final String TASK_FLOW_STATUS_RECEIVING = "2";

    public static final Long TASK_ID_SIGN = 1L;//每日签到

    public static final Long TASK_ID_INVITATION = 2L;//邀请好友

    public static final Long TASK_ID_AUTH = 3L;//新手有礼--实名认证

    public static final Long TASK_ID_FIRST_INVEST = 4L;//小试牛刀--首笔投资

    public static final Long TASK_ID_24H_INVEST = 5L;//小试牛刀--24内投资

    public static final Long TASK_ID_BIND_WEICHAT = 6L;//绑定微信

    public static final String TASK_LIMIT_TYPE_SINGLE = "1"; // 单笔

    public static final String TASK_LIMIT_TYPE_MULTIPLE = "2"; // 累积

    public static final String TEAM_TASK_REWARD_COIN = "1";

    public static final String TEAM_TASK_REWARD_ADD_INTEREST_COUPON = "2";

    public static final String STATUS_VALID = "1";

    public static final String STATUS_INVALID = "0";

    public static final String APP_OPERATOR = "SYSTEM";

    public static final String APP_SOURCE = "MARKETING";


    // 组队任务类型：新手
    public static final String TEAM_TASK_TYPE_BEGINNER = "1";
    // 组队任务类型：每月
    public static final String TEAM_TASK_TYPE_MONTHLY = "2";
    // 组队任务类型：每日
    public static final String TEAM_TASK_TYPE_DAILY = "3";
    // 组队任务类型：无限
    public static final String TEAM_TASK_TYPE_UNLIMITED = "4";

    public static final Map<String, String> COUPON_SOURCE_MAP = ImmutableMap.of(
            BusiTypeEnum.REGISTER.getCode(), CouponSource.REG.name(),
            BusiTypeEnum.ORDER.getCode(), CouponSource.INVEST.name(),
            BusiTypeEnum.BIND.getCode(), CouponSource.BIND.name()
    );

    public static final String ACTIVITY_LOTTERY_TYPE = "activityLotteryType";

    public static final String BUSI_TASK_AUTH_ID = "3";

    public static class Account{
        public static final String STATUS_REQUEST_COMMITED = "0";//提现申请
        public static final String STATUS_FINISHED = "1";//处理成功
        public static final String STATUS_DEALING = "2";//处理中
        public static final String STATUS_FAILED = "3";//处理失败
        public static final String STATUS_MONEY_BACK_TO_ACCOUNT = "4";//退回账户
        public static final String STATUS_BLOCKED = "5";//提现冻结

        public static final String TRD_TYPE_RECHARGE = "0";//充值
        public static final String TRD_TYPE_WITHDRAW = "1";//提现
        public static final String TRD_TYPE_REFUND = "2";//退款

        public static final String TYPE_0 = "0";//充值成功
        public static final String TYPE_1 = "1";//回款
        public static final String TYPE_2 = "2";//退款
        public static final String TYPE_3 = "3";//提现申请
        public static final String TYPE_4 = "4";//投资
        public static final String TYPE_5 = "5";//退回账户
        public static final String TYPE_7 = "7";//受让
        public static final String TYPE_9 = "9";//手续费支出
    }

    public static final String APPLICANT_TYPE_MANAGER = "0";//提现申请人类型-manager系统

    public static final String APPLICANT_TYPE_INVESTOR = "1";//提现申请人类型-理财人


    // 产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
    public static final Integer PRODUCT_RANK_10 = 10;
    public static final Integer PRODUCT_RANK_9 = 9;
    public static final Integer PRODUCT_RANK_8 = 8;
    public static final Integer PRODUCT_RANK_7 = 7;
    public static final Integer PRODUCT_RANK_6 = 6;

    // 购买条件 A-保守型 B-稳健型 C-平衡型 D-积极型 E-激进型
    public static final String PURCHASE_CONDITION_10 = "A;B;C;D;E";
    public static final String PURCHASE_CONDITION_9 = "A;B;C;D;E";
    public static final String PURCHASE_CONDITION_8 = "B;C;D;E";
    public static final String PURCHASE_CONDITION_7 = "C;D;E";
    public static final String PURCHASE_CONDITION_6 = "D;E";

    public static class MemberLevel{
        //铁象会员
        public static final String LEVEL_0 = "0";
        //铜像会员
        public static final String LEVEL_1 = "1";
        //银象会员
        public static final String LEVEL_2 = "2";
        //金象会员
        public static final String LEVEL_3 = "3";
        //白金象会员
        public static final String LEVEL_4 = "4";
        //钻石象会员
        public static final String LEVEL_5 = "5";
        //无极象会员
        public static final String LEVEL_6 = "6";
    }


    //放款状态  0：放款初审中，1：放款初审成功，2：放款初审失败，3：放款复审中，4：放款复审成功，5:放款复审失败
    public static final Integer ORDER_LOAN_STATUS_0 = 0;
    public static final Integer ORDER_LOAN_STATUS_1 = 1;
    public static final Integer ORDER_LOAN_STATUS_2 = 2;
    public static final Integer ORDER_LOAN_STATUS_3 = 3;
    public static final Integer ORDER_LOAN_STATUS_4 = 4;
    public static final Integer ORDER_LOAN_STATUS_5 = 5;


    public static final String THRESHOLD_ON_SINGLE_PRODUCT_KEY = "threshold_on_single_product";

    public static final String DAILY_MONEY_SPENT_THRESHOLD_KEY = "daily_money_spent_threshold";

    public static final String START_BUYING_REMAINING_AFTER = "start_buying_remaining_after";

}