package com.zdmoney.constant;

import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.utils.PropertiesUtil;
import org.springframework.web.context.ContextLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * 常量及通用配置
 *
 * @author ultrafrog
 * @version 1.0, 2013-10-10
 * @since 1.0
 */
public class AppConstants {
    public static final String SYS_INVITE_CODE = "000000";//系统账户邀请码

    private static ConfigParamBean configParamBean = ContextLoader.getCurrentWebApplicationContext().getBean(ConfigParamBean.class);

    public static class Status {
        public static final String SUCCESS = "0";
        public static final String SUCCESS_MESSAGE = "成功";
        public static final String EXCEPTION_2 = "2";
        public static final String EXCEPTION = "-1";
        public static final String EXCEPTION_1 = "-2";
        public static final String EXCEPTION_3 = "-3";
        public static final String SERVERERROR = "服务故障，请拨打电话：400-821-6888";

        public static final String EXCEPTION_4 = "3";//债券已购买过，不能重复购买(目前针对优益计划产品)

        public static final String DATA_EROR = "1";

        public static final String APPSERVICE_POWER = "ON";

        public static final String APPSERVICE_OFFMSG = PropertiesUtil.getPropertyDealCh("appservice.desc", "/config.properties");

        public static final String POWER_ON = "true";

//		public static final String APPSERVICE_OFF = PropertiesUtil.getApplicationProperty("time_desc");
//
//		public static final String CE_FLOW_BUSINESSTYPE = PropertiesUtil.getApplicationProperty("ce_flow_businessType");
    }

    //国政通检测限制次数
    public static final int GZTCOUNT = 5;

    //交易金额密钥
    public static String AMT_KEY = "laocaibao2015";
    //公司余额密钥
    public static String COMPANY_KEY = "laocaibaoCompany";

    //会员状态
    public static class CmStatus {
        //新建会员
        public static final int NEW_MEMBER = 1;
        //会员认证中
        public static final int MENBER_AUTHENING = 2;
        //普通会员
        public static final int MEMBER_AUTHEN_SUCCESS = 3;
        //会员认证失败
        public static final int MEMBER_AUTHEN_FAILE = 4;
        //冻结会员
        public static final int FROZEN_MEMBER = 5;

        public static final String AuthenBack1 = "该用户状态无须认证";

        public static final String AuthenBack2 = "该用户认证次数已超过限额，不能再认证";
    }

    public static class LoginBack {
        public static final String MODIFY_PASSWORD_SUCCESS = "修改密码成功!";

        public static final String RESET_PASSWORD_SUCCESS = "重置密码成功!";

        public static final String ERROR_DATA_1 = "不存在该会员";

        public static final String ERROR_DATA_2 = "密码输入错误!";

        public static final String ERROR_DATA_3 = "该会员已被冻结!";

        public static final String ERROR_DATA_4 = "手机号不能为空!";

        public static final String ERROR_DATA_5 = "密码不能为空!";

        public static final String ERROR_DATA_6 = "新密码不能为空!";

        public static final String ERROR_DATA_7 = "不能重复登录!";

        public static final String ERROR_DATA_8 = "身份证验证不通过";


        public static final String ERROR_DATA_14 = "该身份证已被注册";


        public static final String ERROR_DATA_9 = "验证码无效或已过期!";

        public static final String ERROR_DATA_10 = "验证码错误!";

        public static final String ERROR_DATA_11 = "邀请码无效!";

        public static final String ERROR_DATA_12 = "操作类型不能为空!";

        public static final String ERROR_DATA_13 = "该手机已被注册!";

        public static final String ERROR_DATA_15 = "请填写正确的旧密码";

        public static final String ERROR_DATA_16 = "不能填写多个邀请码！";

        public static final String ERROR_DATA_17 = "不能填写自己的邀请码";

        public static final String ERROR_DATA_18 = "该用户未绑定银行卡";

        public static final String ERROR_DATA_19 = "该用户未开通个人账户";

        public static final String ERROR_DATA_20 = "账户余额不足,请充值";

        public static final String ERROR_DATA_21 = "您当天已经签到";

        public static final String ERROR_DATA_22 = "账户编号不能为空";

        public static final String ERROR_DATA_23 = "未查询到该用户积分信息";

        public static final String ERROR_DATA_24 = "邀请码不能相互填写";

        public static final String ERROR_DATA_25 = "签到失败";

        public static final String ERROR_DATA_26 = "微信号不能为空";

        public static final String ERROR_DATA_27 = "微信号绑定失败";

        public static final String ERROR_DATA_28 = "当前微信号未绑定账号";

        public static final String ERROR_DATA_29 = "账号未绑定微信号";

        public static final String ERROR_DATA_30 = "三个人不能相互填写邀请码";

        public static final String ERROR_DATA_31 = "此邀请码今日邀请人数已达上限";

        public static final String ERROR_DATA_32 = "用户编号不能为空";

        public static final String ERROR_DATA_33 = "获取红包列表失败";

        public static final String ERROR_DATA_34 = "获取红包信息失败";

    }

    public static class LoginBusiOrder {
        public static final String BUSIORDER_ERROR_1 = "产品ID无效！";
        public static final String BUSIORDER_ERROR_2 = "用户ID无效！";
        public static final String BUSIORDER_ERROR_3 = "orderId无效！";
        public static final String BUSIORDER_ERROR_4 = "客户ID,订单金额,产品ID不能为空！";
        public static final String BUSIORDER_ERROR_5 = "该产品邀请码是必填！";
        public static final String BUSIORDER_ERROR_6 = "该客户未绑定银行卡!";
        public static final String BUSIORDER_ERROR_7 = "客户ID,订单金额,业务类型不能为空！";
        public static final String BUSIORDER_ERROR_8 = "产品限购类型后台设置为空！";

        public static final String BUSIORDER_ERROR_10 = "订单不存在！";
        public static final String BUSIORDER_ERROR_11 = "订单已失效！";
        public static final String BUSIORDER_ERROR_12 = "产品无效！";
        public static final String BUSIORDER_ERROR_13 = "产品已售罄！";
        public static final String BUSIORDER_ERROR_14 = "产品已下架！";
        public static final String BUSIORDER_ERROR_15 = "产品已过期！";
        public static final String BUSIORDER_ERROR_16 = "订单状态不是待付款！";
        public static final String BUSIORDER_ERROR_17 = "产品余额不足！";
        public static final String BUSIORDER_ERROR_18 = "银行账号不能为空";
        public static final String BUSIORDER_ERROR_19 = "该项目仅供首次出借用户专享！";
        public static final String BUSIORDER_ERROR_20 = "该产品不可转让！";
        public static final String BUSIORDER_ERROR_21 = "转让产品订单金额与转让价格不一致！";
        public static final String BUSIORDER_ERROR_22 = "该笔订单不能转让！";
        public static final String BUSIORDER_ERROR_23 = "已购买过微信产品，不能再次购买！";
        public static final String BUSIORDER_ERROR_24 = "请先绑定微信，才能购买微信专享产品！";
        public static final String BUSIORDER_ERROR_BUYING_NOT_PERMITTED = "账户类型不是出借人，无法购买捞财宝产品！";
    }

    public static class BusiOrderStatus {
        public static final String BUSIORDER_STATUS_0 = "0";  //付款成功
        public static final String BUSIORDER_STATUS_1 = "1";  //未付款
        public static final String BUSIORDER_STATUS_2 = "2";  //退款失败
        public static final String BUSIORDER_STATUS_3 = "3";  //退款中
        public static final String BUSIORDER_STATUS_4 = "4";  //已退款
        public static final String BUSIORDER_STATUS_5 = "5";  //订单失败
        public static final String BUSIORDER_STATUS_6 = "6";  //订单失效
        public static final String BUSIORDER_STATUS_7 = "7";  //
        public static final String BUSIORDER_STATUS_8 = "8";  //
        public static final String BUSIORDER_STATUS_9 = "9";  //回款完成
        public static final String BUSIORDER_STATUS_10 = "10";  //对外付款完成
        public static final String BUSIORDER_STATUS_11 = "11";  //取消付款
        public static final String BUSIORDER_STATUS_12 = "12";  //订单超时
        public static final String BUSIORDER_STATUS_13 = "13";//划扣中
        public static final String BUSIORDER_STATUS_14 = "14";//分期回款中
        public static final String BUSIORDER_STATUS_15 = "15";//付款中
        public static final String BUSIORDER_STATUS_16 = "16";//转让成功
        public static final String BUSIORDER_STATUS_17 = "17";//撮合起息
        public static final String BUSIORDER_STATUS_18 = "18";//退出中
        public static final String BUSIORDER_STATUS_19 = "19";//退出成功

    }

    public static class RecommendStatus {
        public static final String RECOMMEND_STATUS_0 = "0";  //未实名认证
        public static final String RECOMMEND_STATUS_1 = "1";  //员工
        public static final String RECOMMEND_STATUS_2 = "2";  //员工邀请
        public static final String RECOMMEND_STATUS_3 = "3";  //非员工
        public static final String RECOMMEND_STATUS_4 = "4";  //非员工邀请
        public static final String RECOMMEND_STATUS_5 = "5";    //其他
        public static final String RECOMMEND_STATUS_6 = "6";    //员工邀请员工
    }

    public static class CM_RECHARGE_TYPE {
        public static final String CM_RECHARGE_TYPE_0 = "0"; //充值
        public static final String CM_RECHARGE_TYPE_1 = "1"; //提现
    }

    public static class FeedbackSource {
        public static final String FEEDBACKSOURCE_APP = "1";  //反馈来源  1 APP

    }

    public static class QueryOrder {
        public static final String ERROR_DATA_1 = "不存在该订单";

    }

    public static class ID5Agreement {
        public static final String HTTP = "http://gboss.id5.cn/services/QueryValidatorServices";

        public static final String HTTPS = "https://gboss.id5.cn/services/QueryValidatorServices";
    }

    /***
     * crm 程序返回类型
     *
     * @author liye_z
     * @version 2.0.0   2014-07-23
     **/
    public static class CrmStatus {
        /*返回成功*/
        public static final String SUCCESS = "0000";

        public static final String SUCCESS_MESSAGE = "成功";

        /* 不存在该会员*/
        public static final String EXCEPTION_1 = "0101";
        /* 不存在该会员*/
        public static final String EXCEPTION_MEMBER = "0199";
        /* 重复数据*/
        public static final String EXCEPTION_2 = "0102";
        /* 不存在该记录*/
        public static final String EXCEPTION_3 = "0301";
        /* 不存在该客户*/
        public static final String EXCEPTION_4 = "0201";
        /**
         * 接口返回，代表该客户已删除（或其他状态）
         */
        public static final String EXCEPTION_5 = "0202";
        /**
         * 接口返回，代表该客户不是储备客户，无法删除,修改等操作
         */
        public static final String EXCEPTION_6 = "0203";
        /**
         * 接口返回，代表客户信息不完整
         */
        public static final String EXCEPTION_7 = "0302";

        //----业务类异常返回，返回码  1  开头---//
        /**
         * 接口返回，代表业务订单不存在
         */
        public static final String EXCEPTION_BUSI_1 = "1101";

        /* 程序报错、异常*/
        public static final String EXCEPTION = "9999";

        public static final String ERROR_SERVER = "服务故障，请稍后重试！";
        public static final String ERROR_CONNECTION = "连接服务失败，请稍后重试！";
        public static final String ERROR_SERVER_DONE = "操作异常，请稍后重试！";

        public static final String ERROR_DATA_1 = "不存在该会员！";
        public static final String ERROR_DATA_2 = "重复数据！";
        public static final String ERROR_DATA_3 = "不存在该记录！";

        public static final String ERROR_PARAM = "请求参数不正确!";

        /**
         * 接口接收参数key--产品类型
         */
        public static final String PARAM_KEY_PROTYPE = "proType";

        /**
         * 接口接收参数key--产品ID
         */
        public static final String PARAM_KEY_PROID = "proId";
        /**
         * 接口接收参数key--客户id
         */
        public static final String PARAM_KEY_CUSTOMERID = "customerId";
        /**
         * 接口接收参数key--业务id
         */
        public static final String PARAM_KEY_BUSIID = "busiId";
        /**
         * 接口接收参数key--业务id(删除功能)
         */
        public static final String PARAM_KEY_DELBUSIID = "Id";
        /**
         * 接口接收参数key--产品id
         */
        public static final String PARAM_KEY_PRODUCTID = "productId";
        /**
         * 接口接收参数key--产品id
         */
        public static final String PARAM_KEY_FEPRODUCT = "feProduct";

        public static final String MSG_EXISTENCE_ORDER = "存在未生效订单！";

        /**
         * 接口接收参数key--支付状态
         * （1.未付款 3.已付款）
         */
        public static final String PARAM_KEY_PAY_STATUS = "payStatus";

        /**
         * 接口接收参数key--页码
         */
        public static final String PARAM_KEY_PAGENO = "pageNo";

        /**
         * 接口接收参数key--每页多少条记录数
         */
        public static final String PARAM_KEY_PAGESIZE = "pageSize";
        /**
         * 接口接收参数key--页码默认值
         */
        public static final Integer PAGENO_DEFAULT = 1;

        /**
         * 接口接收参数key--每页多少条记录数默认值
         */
        public static final Integer PAGESIZE_DEFAULT = 15;
        /**
         * 接口接收参数key--债权ID
         */
        public static final String PARAM_KEY_LOANID = "loanId";
        /**
         * 接口接收参数key--优益计划产品ID
         */
        public static final String PRODUCTID_DEFAULT = "100";
        /**
         * 接口接收参数key--总条数total
         */
        public static final String TOTAL_DEFAULT = "0";
        /**
         * 生活消费类订单 流量充值类
         */
        public static final String CE_BUSI_TYPE_FLOW = "2";

    }

    /**
     * 消息推送头信息
     */
    public static class HttpHead {
        /**
         * 项目编号
         */
        public static final String PROJECTNO = "L";
        /**
         * 秘钥
         */
        public static final String SIGN = configParamBean.getPushKey();

        /**
         * webservice地址
         */
//        public static final String ADDRESS = PropertiesUtil.getSysMap().get("pushwsRootUrl") + "/services/DealProcessor?wsdl=DealProcessorService.wsdl";

        /**
         * 注册推送方法代号
         */
        public static final String LOGINPUSH = "200001";

        /**
         * 消息推送方法代号
         */
        public static final String PUSHMESSAGE = "100001";
        /**
         * 注销推送方法代号
         */
        public static final String CANCELLATION = "200002";

        /**
         * 设备类型  安卓
         */
        public static final String ANDROID = "2";

        /**
         * 设备类型  IOS
         */
        public static final String IOS = "1";
        /**
         * 项目组编号
         */
        public static final String GROUPNO = "001";
        /**
         * 项目组名称
         */
        public static final String GROUPNAME = "laocaibao";


    }

    public static String returnSuccess() {
        return "{\"status\":\"0\",\"respDesc\":\"成功\"}";
    }

    public static String returnSuccess(String content) {
        return "{\"status\":\"0\",\"respDesc\":\"成功\",\"validataCode\":\"" + content + "\"}";
    }

    public static String returnSuccessInfos(String infos) {
        return "{\"status\":\"0\",\"respDesc\":\"成功\",\"infos\":" + infos + "}";
    }

    public static String returnSuccessInfos2(String totalPage, String infos) {
        return "{\"status\":\"0\",\"respDesc\":\"成功\",\"totalPage\":\"" + totalPage + "\",\"infos\":" + infos + "}";
    }

    //与第三方支付成功后约定的返回结果 by wangwm
    public static String returnSuccessInfo(String info) {
        return "{\"status\":\"success\",\"respDesc\":\"成功\",\"info\":" + info + "}";
    }

    public static String returnSuccess(String response, String infos) {
        return "{\"status\":\"0\",\"respDesc\":\"成功\",\"" + response + "\":\"" + infos + "\"}";
    }

    public static String returnInfo(String info) {
        return "{\"status\":\"-1\",\"respDesc\":\"" + info + "\"}";
    }

    public static String returnException() {
        return "{\"status\":\"-1\",\"respDesc\":\"系统异常\"}";
    }

    public static String returnException(String error) {
        return "{\"status\":\"-1\",\"respDesc\":\"" + error + "\"}";
    }

    public static String returnException(String error, String response, String infos) {
        return "{\"status\":\"-1\",\"respDesc\":\"" + error + "\",\"" + response + "\":\"" + infos + "\"}";
    }

    public static String returnParamException() {
        return "{\"status\":\"-2\",\"respDesc\":\"请求参数或参数格式不正确! \"}";
    }

    public static String returnJsonException(String error) {
        return "{\"status\":\"-3\",\"respDesc\":\"请求参数或参数格式不正确: " + error + " \"}";
    }

    public static String returnDataException(String error) {
        return "{\"status\":\"-4\",\"respDesc\":\"数据异常:" + error + "\"}";
    }

    public static String returnSuccessInfo(String busitatus, String infos) {
        return "{\"status\":\"0\",\"respDesc\":\"成功\",\"busiStatus\":\"" + busitatus + "\",\"infos\":" + infos + "}";
    }

    public static String returnSuccessBusitatusInfo(String respDesc, String busitatus) {
        return "{\"status\":\"0\",\"respDesc\":\"" + respDesc + "\",\"busiStatus\":\"" + busitatus + "\"}";
    }

    //返回资产总和信息  incomeTotal 累计收益   accountTotal 账户总额   infos 订单列表   Sam.J 14.12.04
    public static String returnSuccessInfoWithTotal(String infos, String incomeTotal, String accountTotal) {
        return "{\"status\":\"0\",\"respDesc\":\"成功\",\"incomeTotal\":\"" + incomeTotal + "\",\"accountTotal\":\"" + accountTotal + "\",\"infos\":" + infos + "}";
    }

    public static String returnExceptions(String content) {
        return "{\"status\":\"1\",\"respDesc\":\"" + content + "\"}";
    }



    public static String toDateStr(Date d) {
        if (d != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return f.format(d);
        }
        return "";
    }

    public static class PayGateway {
        public static String PayUrl = "http://test-cashier.ezendai.com:8188/zdpay_cashier/cashier/toPay";
        public static String PayCheckoutVersion = "2.0";
        public static String PayCallBackURL = "http://172.16.73.71:8089/laocaibao_manager_panjy/busi/cashier/getRechargeResults";

    }

    public static class AccountGateway {
        public static String AccountURL = "http://172.16.73.71:8089/laocaibao_manager_panjy/busi/cashier/getMessage";

        public static String AccountPayUrl = "http://172.16.73.71:8089/laocaibao_manager_panjy/busi/cashier/getRechargeMessage";

        public static String AccountRechargeUrl = "http://172.16.73.71:8089/laocaibao_manager_panjy/busi/cashier/getWithdrawResults";

    }

    public static class BusiAccountPayStatus {
        public static final String PAY_STATUS_0 = "0";  //充值/提现成功
        public static final String PAY_STATUS_1 = "1";  //充值/提现失败
        public static final String PAY_STATUS_2 = "2";  //充值/提现中
        public static final String PAY_STATUS_3 = "3";  //待付款
    }


    /**
     * 证大账户通错误码
     *
     * @author 00232385
     */
    public static class AccountConstants {
        public static final String SUCCESS = "0000"; //成功
        public static final String FAILURE = "9999";  //失败
        public static final String PROJECT_NO = "L";  //账户通对捞财宝定义的项目编号

    }


    public static String APPLICATION_CONTEXT_NAME = "LCB";

    public static String APP_TYPE_ANDROID = "ANDROID";

    public static String APP_TYPE_IOS = "IOS";

    public static String APP_TYPE_WEIXIN = "WECHAT";

    public static String APP_TYPE_WEB = "WEB";

    public static String APP_TYPE_MANAGE="MANAGE";

    public static String APP_TYPE_CREDIT = "CREDIT";

    public static String INTEGRAL_STATUS_SUCCESS = "0000";

    public static String INTEGRAL_STATUS_FAIL = "1111";

    public static String getAppType(String str) {
        if (str.toUpperCase().contains(APP_TYPE_IOS)) {
            return APP_TYPE_IOS;
        } else if (str.toUpperCase().contains(APP_TYPE_ANDROID)) {
            return APP_TYPE_ANDROID;
        } else if (str.toUpperCase().contains(APP_TYPE_WEIXIN)) {
            return APP_TYPE_WEIXIN;
        } else if (str.toUpperCase().contains(APP_TYPE_WEB)) {
            return APP_TYPE_WEB;
        } else if (str.toUpperCase().contains(APP_TYPE_MANAGE)) {
            return APP_TYPE_MANAGE;
        }else {
            return "";
        }
    }

    public static class CustInviteLineStatusContants {
        public static final String RIGISTER = "0";//注册
        public static final String VALID = "1";//实名认证
        public static final String INVEST = "2";//投资
    }

    public static class PayChannelCodeContants {
        public static final String LIANLIAN = "1004";
        public static final String LIANDONG = "1014";
        public static final String TPP = "TPP";
        public static final String HUARUI_BANK="HRB";//华瑞银行
        public static final String FUIOU = "FUIOU";//富友
    }

    public static class PayMessage {
        public static final String PAYURL = "payUrl";
        public static final String PAYPLATFORMCODE = "payPlatformCode";
        public static final String PAYMERCHANTNO = "payMerchantNo";
        public static final String PAYPRIVATEKEY = "payPrivateKey";
        public static final String PAYVESION = "payVesion";
        public static final String AGREEMENTURL = "agreementUrl";
    }

    public static class PayStatusContants {
        public static final String BUY_SUCCESS = "0";//申购成功
        public static final String SELL_OUT = "1";//产品已售罄
        public static final String OVERDUE = "2";//产品已过期
        public static final String OFF_SHELF = "3";//产品已下架
        public static final String BALANCE_UN_ENOUGH = "4";//产品余额不足
        public static final String NOT_PAYMENT = "5";//订单不是待付款
        public static final String NOT_NEW_HAND = "6";//非新手购买新手标
        public static final String NOT_ENOUGH_AMOUNT = "7";//账户余额不足
        public static final String EXSIST_ORDER_PAY_FLOW = "8";

        public static final String SUCCESS = "0";//申购成功
        public static final String FAIL = "1";//申购失败

        private static Map<String, String> payStatus = Maps.newTreeMap();

        static {
            payStatus.put("0", "申购成功");
            payStatus.put("1", "产品已售罄");
            payStatus.put("2", "产品已过期");
            payStatus.put("3", "产品已下架");
            payStatus.put("4", "产品余额不足");
            payStatus.put("5", "订单不是待付款");
            payStatus.put("6", "非新手购买新手标产品");
            payStatus.put("7", "账户余额或积分不足");
            payStatus.put("8","该订单已存在流水，不能重复操作！");
        }

        public static String getPayStatusDesc(String status) {
            return payStatus.get(status);
        }
    }

    public static class TradeStatusContants {
        //交易类型，0:充值，1:提现，2：提现退款
        public static final String RECHARGEING = "0";//0:充值
        public static final String WITHDRAWING = "1";//1:提现
        public static final String WITHDRAW = "2";//2：提现退款

        public static final String INIT = "0";//初始
        public static final String PROCESS_SUCCESS = "1";//处理成功
        public static final String PROCESSING = "2";//处理中
        public static final String PROCESS_FAIL = "3";//处理失败
        public static final String UNPROCESS = "4";//待处理
        public static final String WITHDRAW_FROZEN="5";//提现冻结
        public static final String WITHDRAW_REFUND_START="6";//提现退款中
        public static final String WITHDRAW_REFUND_FAIL="7";//提现退款失败
        public static final String WITHDRAW_REFUND_SUCCESS="8";//提现退款成功
    }

    public static class AccountFlowTypeDesc {
        //0-充值 1-回款 2-退款 3-提现 4-购买
        private static Map<String, String> flowStatus = Maps.newTreeMap();

        static {
            flowStatus.put("0", "充值");
            flowStatus.put("1", "回款");
            flowStatus.put("2", "退款");
            flowStatus.put("3", "提现");
            flowStatus.put("4", "购买");
        }

        public static String geFlowStatusDesc(String status) {
            return flowStatus.get(status);
        }
    }


    public static final String OUT_BANK_LIMIT_MSG = "已超出该卡最大支付限额，建议更换修改金额后，进行购买。银行卡支付限额由银行设定，捞财宝无法提升限额。";

    /**
     * 充值状态
     */
    public static class RechargeStatus {
        public static final String Recharge_STATUS_0 = "0";  //充值中
        public static final String Recharge_STATUS_1 = "1";  //充值成功
        public static final String Recharge_STATUS_2 = "2";  //充值中
        public static final String Recharge_STATUS_3 = "3";  //充值失败
    }

    public static class MallTaskStatus {
        public static final String TASK_TYPE_1 = "1";//新手任务
        public static final String TASK_TYPE_2 = "2";//限时任务
        public static final String TASK_TYPE_3 = "3";//多捞多得
        public static final String TASK_TYPE_4 = "4";//普通任务
        public static final String TASK_TYPE_5 = "5";//邀请好友
        public static final String TASK_TYPE_6 = "6";//每日任务
        public static final String TASK_TYPE_10 = "10";//风险测试
        public static final String TASK_TYPE_11 = "11";//累计任务
        public static final String ACTION_STATUS_1 = "1";//去认证
        public static final String ACTION_STATUS_2 = "2";//去投资
        public static final String ACTION_STATUS_3 = "3";//去绑定
        public static final String ACTION_STATUS_4 = "4";//去签到
        public static final String ACTION_STATUS_5 = "5";//去邀请
        public static final String ACTION_STATUS_6 = "6";//去领取奖励
        public static final String ACTION_STATUS_7 = "7";//去玩吧
        public static final String ACTION_STATUS_9 = "9";//已完成
        public static final Long TASK_ID_SIGN = 1L;//每日签到
        public static final Long TASK_ID_INVITATION = 2L;//邀请好友
        public static final Long TASK_ID_AUTH = 3L;//新手有礼--实名认证
        public static final Long TASK_ID_FIRST_INVEST = 4L;//小试牛刀--首笔投资
        public static final Long TASK_ID_24H_INVEST = 5L;//小试牛刀--24内投资
        public static final Long TASK_ID_BIND_WEICHAT = 6L;//绑定微信
        public static final Long TASK_ID_LIMIT_INVEST = 7L;//限时任务
        public static final Long TASK_ID_MONTH_INVEST = 8L;//每月任务
        public static final Short TASK_UNRECEIVE_STATUS = 0;//未领取
        public static final Short TASK_RECEIVE_STATUS = 1;//已领取

        public static final String TASK_RISK_TEST = "10";
    }

    public static class VoucherStatus {
        //AVAIL-1未使用 CONSUMED-2已使用 OVERDUE-3已过期
        private static Map<String, Integer> voucherStatus = Maps.newTreeMap();

        static {
            voucherStatus.put("AVAIL", 1);
            voucherStatus.put("CONSUMED", 2);
            voucherStatus.put("OVERDUE", 3);
        }

        public static Integer getVoucherStatus(String status) {
            return voucherStatus.get(status);
        }
    }

    /**
     * 充值订单状态
     */
    public static class OrderExchargeStatus {
        //0-初始 1-消费捞财币失败 2-消费捞财币成功 3-兑换商品失败 4-兑换商品成功
        public static final String STATUS_0 = "0";  //初始
        public static final String STATUS_1 = "1";  //消费捞财币失败
        public static final String STATUS_2 = "2";  //消费捞财币成功
        public static final String STATUS_3 = "3";  //兑换商品失败
        public static final String STATUS_4 = "4";  //兑换商品成功
    }

    public static class PRODUCT_TYPE {
        public static final String TYPE_1 = "1"; //红包
        public static final String TYPE_2 = "2"; //加息券
        public static final String TYPE_3 = "3"; //预约券
        public static final Integer PRODUCT_TYPE_0 = 0; // app全部产品
        public static final Integer PRODUCT_TYPE_1 = 1; // 定期
        public static final Integer PRODUCT_TYPE_2 = 2; // 个贷
        public static final Integer PRODUCT_TYPE_3 = 3; // 理财计划
        public static final Integer PRODUCT_TYPE_4 = 4; // 转让产品
        public static final Integer PRODUCT_TYPE_5 = 5; // pc全部产品
        public static final Integer PRODUCT_TYPE_6 = 6; // 新手标产品
    }

    public static class PRODUCT_SORT {
        public static final String PRODUCT_SORT_0 = "0"; // 默认排序
        public static final String PRODUCT_SORT_1 = "1"; // 收益率排序
        public static final String PRODUCT_SORT_2 = "2"; // 封闭期

    }

    /**
     * 订单类型
     */
    public static class ORDER_TYPE {
        public static final String ORDER_TYPE_ALL = "0"; // 全部
        public static final String ORDER_TYPE_FINPLAN = "1"; // 智投计划
        public static final String ORDER_TYPE_PERSONAL_SUBJECT = "2"; // 散标(个贷+标的)
        public static final String ORDER_TYPE_NORMAL = "3"; // 定期
        public static final String ORDER_TYPE_TRANSFER = "4"; // 转让
    }
    /** 验证码类型*/
    public static class ValidateCode {
        public static final Integer REGISTER = 0;//0--注册
        public static final Integer RESET_PASSWORD = 1;//1--重置密码
        public static final Integer TPP_DEDUCT = 2;//2--TPP划扣
        public static final Integer WECHAT_REGISTER = 3;//3--微信注册
        public static final Integer WECHAT_BIND = 4;// 4--微信绑定
        public static final Integer SET_PAY_PASSWORD = 5;// 5--设置交易密码
        public static final Integer RESET_PAY_PASSWORD = 6;// 6--重置交易密码
        public static final Integer MERCHANT_REGISTOR = 7;//商户用户注册
        public static final Integer HEROLIST_VOTE = 8;//英雄榜投票
        public static final Integer CELLPHONE_MODIFY = 9;//pc修改手机号
        public static final Integer RECHARGE = 10;//充值
        public static final Integer BIND_CARD = 11;//绑卡
        public static final Integer CREDIT = 12;//credit
    }

    public static class ProductLimitType {
        public static final Long COMMON = 0L;//普通产品
        public static final Long LIMIT = 1L;//限购产品
        public static final Long NEW_HAND = 2L;//新手标
        public static final Long WECHAT = 3L;//微信专享
        public static final Long CHANNEL = 4L;//渠道产品
        public static final Long BESPEAK = 5L;//预约产品
    }

    public static class ContractType{
        public static final String LOAN = "LOAN";//债权
        public static final String SUBJECT = "SUBJECT";//标的
    }
    public static class OrderProductType{
        public static final Integer COMMON = 1;//优选
        public static final Integer CONTRACT = 2;//智选
        public static final Integer PERSONAL= 3;//个贷
        public static final Integer FINANCE_PLAN= 4;//理财计划
        public static final Integer FINANCE_PLAN_SUB= 5;//理财计划子产品
    }

    public static class OrderPaymentStatus{
        public static final String RECEIVE = "receive";//已回款
        public static final String UNRECEIVE = "unReceive";//未回款
    }

    /**
     * 小组队员状态
     */
    public static class TeamMemberStatus {
        public static final String TEAM_MEMBER_NORMAL = "1";  //正常
        public static final String TEAM_MEMBER_LEAVE = "0";  //离队
    }

    /**
     * 小组队员申请状态
     */
    public static class TeamApplyStatus {
        public static final String TEAM_APPLY_AUDITING = "0";  //审核中
        public static final String TEAM_APPLY_PASS = "1";  //已审核
        public static final String TEAM_APPLY_REJECT = "2";  //已拒绝
    }


    public static class BuyWechatStatus{
        public static final Integer NOTBUY = 0;  //未购买
        public static final Integer BUY = 1;  //已审核
    }

    public static class TeamTaskStatus{
        public static final  String TASK_TYPE_NEW_HAND = "1";//新手任务
        public static final  String TASK_TYPE_MONTHLY = "2";//每月任务
        public static final  String TASK_TYPE_DAILY = "3";//每日任务
        public static final  String TASK_TYPE_NOT_LIMIT = "4";//不限次任务
        public static final  String TASK_RECEIVE_NO ="0";//领取中
        public static final  String TASK_RECEIVE_YES ="1";//已领取
        public static final  String ACTION_STATUS_AUTH="1";//去认证
        public static final  String ACTION_STATUS_INVEST="2";//去投资
        public static final  String ACTION_STATUS_BIND="3";//去绑定
        public static final  String ACTION_STATUS_SIGN="4";//去签到
        public static final  String ACTION_STATUS_INVITE="5";//去邀请
        public static final  String ACTION_STATUS_RECEIVE="6";//去领取
        public static final  String ACTION_STATUS_FINISHED="7";//已完成
        public static final  String TASK_REWARD_COIN="1";//捞财币
        public static final  String TASK_REWARD_VOUCHER="2";//加息券
    }

    /**
     * 队伍状态
     */
    public static class TeamStatus{
        public static final  Short TEAM_STATUS_OPEN = 1; //启用
        public static final  Short TEAM_STATUS_CLOSE= 0;//禁用
    }

    public static class PaymentPlanStatus {
        public static final  String UNRETURN = "1";//未回款
        public static final  String  RETURNING="2";//回款中
        public static final  String  RETURNED="3" ;//已回款
        public static final  String  RETURNED_TRANSFER="4" ;//已转让
        public static final  String  RETURNED_TRANSFER_UP="5" ;//上家已回款
        public static final  String  RETURNING_TRANSFER_UP="6" ;//上家回款中  --No Use
        public static final  String  RETURNED_AHEAD="8" ;//已回款(提前结清到账)
    }

    public static class SubjectStatus {
        public static final String NOTIFY_FAILURE = "0"; //未通知或通知失败
        public static final String NOTIFY_SUCCESS = "1"; //通知成功
    }

    /**
     * 商户类型
     */
    public static class MerchantType {
        public static final String MERCHANT = "1";//商户
        public static final String CHANNEL = "2";//渠道
    }

    /**
     * 注册渠道
     */
    public static class RegisterChannel {
        public static final String ZHIZHUWANG001 = "zhizhuwang001";//蜘蛛网
        public static final String EURO = "euro";//欧洲杯
    }

    /**
     * 产品转让标志
     */
    public static class ProductTransferStatus {
        public static final String COMMON_PRODUCT = "0";//固收产品
        public static final String TRANSFER_PRODUCT = "1";//转让产品
    }

    /**
     * 用户有效标识
     */
    public static class CustomerValidStatus {
        public static final int CUSTOMER_VALID = 0;//有效
        public static final int CUSTOMER_INVALID = 1;//失效
    }

    /**
     * 转让单转让状态转让状态
     */
    public static class DebtTransferStatus {
        public static final String TRANSFER_INIT = "0";//转让初始
        public static final String TRANSFER_SUCCESS = "1";//已转让
        public static final String TRANSFER_DURING = "2";//转让中
        public static final String TRANSFER_FAILURE = "3";//转让失败
        public static final String TRANSFER_OFFLINE = "4";//审核失败
        public static final String TRANSFER_FINISH = "5";//转让成功
    }

    /**
     * 产品上下架状态
     */
    public static class ProductUpLowStatus {
        public static final String READY_FOR_SALE = "0";//待上架
        public static final String PRODUCT_UP = "1";//上架
        public static final String NOT_ON_SALE = "-1";//下架
    }

    /**
     * 产品审核状态
     */
    public static class ProductAuditStatus {
        public static final String PRODUCT_AUDIT_INIT = "0";//未审核
        public static final String PRODUCT_AUDIT_PASS = "1";//审核
    }

    /**
     * 产品转让状态
     */
    public static class ProductTransferProperty {
        public static final String CAN_TRANSFER = "1";//可转让
        public static final String CAN_NOT_TRANSFER= "0";//不可转让
    }

    /**
     * 产品转让状态
     */
    public static class TransferDebtStatus {
        public static final String TRANSFER_SETTLE_NO = "0";//未交割
        public static final String TRANSFER_SETTLE_YES= "1";//已交割
    }

    /**
     * 理财计划申请退出标识
     */
    public static class FinPlanTips {
        public static final String FIN_PLAN_INVEST = "申购成功";
        public static final String FIN_PLAN_INVEST_VILIDATE= "出借中";
        public static final String FIN_PLAN_EXIT= "退出中";
        public static final String FIN_PLAN_EXIT_END= "已回款";
        public static final String FIN_PLAN_RETURN_AMT= "已退款";
        public static final String FIN_PLAN_RETURN_APPLY= "提前退出申请中";
        public static final String FIN_PLAN_EXIT_APPLY_SUCCESS= "退出成功-已回款";


    }

    /**
     * 订单转让状态判断
     */
    public static class HasTransferStatus {
        public static final String HAS_TRANSFER_NORMAL = "0";//普通产品
        public static final String HAS_TRANSFER_SHOW_BUTTON= "1";//显示转让按钮(一次没转让过)
        public static final String HAS_TRANSFER_DAY= "2";//显示转让天数
        public static final String HAS_TRANSFER_CANCEL_BUTTON= "3";//撤销转让按钮
        public static final String HAS_TRANSFER_HIDE_BUTTON= "4";//撤销转让按钮隐藏

        public static final String HAS_TRANSFER_RESHOW_BUTTON="5";//显示继续按钮(转让过)

        public static final String HAS_TRANSFER_SUCCESS="6"; //转让成功
        public static final String HAS_NO_TRANSFER_END="7";//不可转让产品，投资结束
        public static final String HAS_TRANSFER_NOLOG_END="8";//可转让，但未转让过，投资结束
        public static final String HAS_TRANSFER_FAIL_END="9";//可转让，转让失败n，不转让，投资结束
        public static final String HAS_TRANSFER_PAY = "10";//去支付

        public static final String FIN_PLAN_SHOW_EXIT_BUTTON = "11";//显示提前申请退出按钮
        public static final String FIN_PLAN_HIDDEN_EXIT_BUTTON = "12";//隐藏提前申请退出按钮
        public static final String FIN_PLAN_HIDDEN_CHARGEFEE = "13";//提前退出申请中，不显示手续费
        public static final String FIN_PLAN_SHOW_CHARGEFEE = "14";//提前退出申请成功，显示手续费





        //      public static final String HAS_SPECIAL_TRANSFER="6";//普通可转让正常到期（包含转让失败在1天之外恢复可转让）
  //      public static final String HAS_TRANSFER_END="7";//普通可转让产品转让成功，投资结束

//        public static final String HAS_TRANSFER_FAIL_BEFOREDAY= "5";//转让失败一天之内  -->1
//        public static final String HAS_TRANSFER_FAIL_AFTERDAY= "6";//转让失败一天之外   -->0
//        public static final String HAS_TRANSFER_ORDER= "7";//购买的产品已转让过   ---> 1
//        public static final String HAS_NO_TRANSFER_ORDER= "8";//购买的产品没转让过   0
    }

    /**
     * 订单转让方向
     */
    public static class OrderTransferDirect {
        public static final String ORDER_TRANSFER_TRANSFER= "1";//转让方
        public static final String ORDER_TRANSFER_TRANSFEREE= "2";//受让方
    }

    /**
     * 订单显示状态
     */
    public static class TransferShowStatus {
        public static final String SHOW_INIT= "0";//初始状态
        public static final String SHOW_TRANSFERING= "1";//转让中
        public static final String SHOW_TRANSFERED= "2";//已转让
        public static final String SHOW_BEFORE_TRANSFER= "3";//转让前
    }

    /**
     * 订单显示状态
     */
    public static class OrderTransferStatus {
        public static final String ORDER_NORMAL= "0";//普通订单
        public static final String ORDER_TRANSFER= "1";//转让订单
    }

    public static class MemberType{
        public static final Integer ADVISOR=1; //理财师
        public static final Integer ADVISOR_MEMBER=2;//理财师客户
        public static final Integer NORMAL=3;//普通会员
    }


    public static class WelfareType{
        public static final String  INTEGRAL="1";
        public static final String  COUPON="2";
        public static final String  VOUCHER="3";
    }

    public static class RegisterSource{
        public static final Integer LCB=0; //捞财宝
        public static final Integer CREDIT=1;//信贷
    }

    public static class PersonalLoan{
        public static final Integer NOT_PERSONAL_LOAN = 0;  //非个贷
        public static final Integer PERSONAL_LOAN = 1;  //个贷
    }

    /**
     * 订单回款类型
     */
    public static class OrderPaymentType{

        public static final String DEBX ="0";//等额本息

        public static final String ONE_TIME="1";//一次性还本付息

        public static final String XXHB ="2";//先息后本

    }


    /**
     * 推送债权信息
     */
    public static class FinanceOrderPushType{

        public static final String ORDER_PUSH_TYPE_1 ="1";//债权到期转让

        public static final String ORDER_PUSH_TYPE_2="2";//提前退出转让

    }

    public static class authChannel{
        public static final Integer GZT = 1;  // 国政通
        public static final Integer AR = 2;  // 安融
        public static final Integer SH = 3;  // 算话
    }

    /**产品类型*/
    public static class ProductSubjectType{
        public static final String SUBJECT_YX = "1";//优选
        public static final String SUBJECT_ZX = "2";//智选
        public static final String SUBJECT_GD = "3";//个贷
        public static final String FINANCE_PLAN = "4";//理财计划
        public static final String FINANCE_PLAN_SUB="5";//理财计划子产品
    }

    public static class CmOpenAccountFlag{
        public static final String  UNOPEN="0";
        public static final String  OPEN="1";
    }

    public static class BusiTradeFlowStatus{
        public static final String  DEAL_INIT="0";
        public static final String  DEAL_SUCCESS="1";
        public static final String  DEAL_DURING="2";
        public static final String  DEAL_FAIL="3";
    }

    public static class riskAnswerResult{
        public static final long RISK_ANSWER_RESULT_A = 10;
        public static final long RISK_ANSWER_RESULT_B = 8;
        public static final long RISK_ANSWER_RESULT_C = 5;
        public static final long RISK_ANSWER_RESULT_D = 3;
        public static final long RISK_ANSWER_RESULT_E = 0;
    }

    public static final long RISK_TEST_AWARD = 1000;

    public static class riskType{
        public static final String RISK_TYPE_A = "保守型";
        public static final String RISK_TYPE_B = "稳健型";
        public static final String RISK_TYPE_C = "平衡型";
        public static final String RISK_TYPE_D = "积极型";
        public static final String RISK_TYPE_E = "激进型";
        public static final String RISK_TYPE_NO = "测试赢积分";
    }

    public static class FinancePlan{
        public static final String  DEBT_TYPE1="1";//新标的
        public static final String  DEBT_TYPE2="2";//退出转让

        //还款类型
        public static final String REPAY_TYPE0 = "0";//等额本息
        public static final String REPAY_TYPE1 = "1";//一次性还本付息
        public static final String REPAY_TYPE2 = "2";//先息后本
        public static final String REPAY_TYPE3 = "3";// 等额本金
        public static final String REPAY_TYPE4 = "4";// 其他

        public static final String  FUND_TYPE0="0";//新出借
        public static final String  FUND_TYPE1="1";//复投
        public static final String  FUND_TYPE2="2";//异常替换
        public static final String  FUND_TYPE4="4";//兜底资金

        public static final String FUND_STATUS_1 = "1"; // 状态 1-待撮合 2-撮合中 3-已撮合
        public static final String FUND_STATUS_2 = "2"; // 撮合中
        public static final String FUND_STATUS_3 = "3"; // 已撮合
        public static final String FUND_MATCHING_FAILED = "4"; // 撮合失败

        public static final String DEBT_STATUS_1 = "1"; // 状态 1-待撮合 2-暂停撮合 3-已撮合 4-提前结清 5-异常 6-结标
        public static final String DEBT_STATUS_3 = "3"; // 已撮合
    }

    // 特殊理财人
    public static final String SPECIAL_FINANCE_PEOPLE = "specialFinancePeople";

    // 子订单状态
    public static class ORDER_SUB_STATUS {
        public static final String  ORDER_STATUS_1 = "1"; // 持有中
        public static final String  ORDER_STATUS_2 = "2"; // 已结束
    }

    public static class PRODUCT_PLAN_STATUS {
        // 计划状态  0:未匹配  1:匹配中 2:已匹配 3:匹配失败 4:募集中 5:满标-撮合中 6:兜底满标-撮合中 7:已撮合-起息 8:未满标-待撮合 9:未满标-撮合中 10:已解散 11:已结束 12:撮合-待起息
        public static final Integer PRODUCT_STATUS_0 = 0;
        public static final Integer PRODUCT_STATUS_1 = 1;
        public static final Integer PRODUCT_STATUS_2 = 2;
        public static final Integer PRODUCT_STATUS_3 = 3;
        public static final Integer PRODUCT_STATUS_4 = 4;
        public static final Integer PRODUCT_STATUS_5 = 5;
        public static final Integer PRODUCT_STATUS_6 = 6;
        public static final Integer PRODUCT_STATUS_7 = 7;
        public static final Integer PRODUCT_STATUS_8 = 8;
        public static final Integer PRODUCT_STATUS_9 = 9;
        public static final Integer PRODUCT_STATUS_10 = 10;
        public static final Integer PRODUCT_STATUS_11 = 11;
        public static final Integer PRODUCT_STATUS_12 = 12;
    }

    public static final String PARTNER_NO = "LCB";

    // 公司红包户
    public static final String ACCOUNT_GSHB = "account_gshb";

    public static final String REPAYMENTGUARANTEE = "借款人以其收入担保";
    public static final String REPAYSOURCE = "借款人收入";
    public static final String COOPERATIVEDESC = "上海证大投资咨询有限公司作为证大财富新金融服务平台的借款服务端，开展信用贷款业务多年，为行业龙头企业之一。拥有业内领先的风控体系，20多年风控管理经验，专业风控团队300多人。秉持着小额分散的原则，多家第三方征信合作机构大数据支持，运用MD5加密算法等先进风控技术，准确快速的完成对借款人身份、借款资料、信息真实性及客户签约信息的核实工作。";

    // 撮合批次信息 状态
    public static class BUSI_MATCH_RESULT_INFO_STATUS {
        public static final String  NON_MATCH = "0"; // 待撮合
        public static final String  MATCH_END = "1"; // 撮合结束
        public static final String  MATCHMAKING = "2"; // 撮合中
    }

    // 债权匹配异常状态
    public static class BUSI_ABNORMAL_ORDER_HANDLE_STATUS {
        public static final String  HANDLE = "0"; // 待撮合
        public static final String  HANDLED = "1"; // 撮合结束
        public static final String  HANDLING = "2"; // 撮合中
    }

    /**
     * 系统参数
     */
    public static class SYSPARAMS{
        public static final String FORT_ACCOUNT = "fort_account";//堡垒户
    }


    //用户类型
    public static final String USER_TYPE_NORMAL = "0";//个人
    public static final String USER_TYPE_ORGAN = "1"; //组织机构

    //产品福利提示
    public static final String PRODUCT_NO_WELFARE = "该产品不支持使用福利";

    // 支付类型
    public static class PAY_TYPE {
        public static final String  QUICK_PAY = "4001"; // 快捷支付
        public static final String  EBANK_PAY = "4002"; // 网银支付
    }

    // 客户类型
    public static class CustomerType{
        public static final String CUSTOMER_TYPE_0 = "0"; // 个人
        public static final String CUSTOMER_TYPE_1 = "1"; // 机构
        public static final String CUSTOMER_TYPE_2 = "2"; // 平台
    }

    public static final String ACCOUNT_CHARGEING_FEE_ERROR = "6666";

    // 日志处理状态
    public static class REQ_LOG{
        public static final Integer REQ_STATUS_1 = 1; // 新建
        public static final Integer REQ_STATUS_2 = 2; // 处理中
        public static final Integer REQ_STATUS_3 = 3; // 成功
        public static final Integer REQ_STATUS_4 = 4; // 失败
    }

    // 现金券配置类型
    public static class CASH_TYPE {
        public static final String  CASH_REG = "1"; // 注册
        public static final String  CASH_REPAY_AMT = "2"; // 回款
        public static final String  CASH_INVEST = "3"; // 投资
        public static final String  CASH_ACTIVITY = "4"; // 活动
        public static final String  CASH_MONTH_GIFT = "5"; // 月度礼包

    }


    // 赠送积分
    public static class GIVE_INTEGRAL {
        public static final String  SIGN = "201812"; // 签到
        public static final String  RISK_TEST = "201813"; // 风险测评
    }

    /**
     * 登录类型
     */
    public static class LOGIN_TYPE{
        public static final Integer LOGIN_TYPE_PWD = 1; // 密码登录
        public static final Integer LOGIN_TYPE_CODE = 2; // 验证码登录
    }

    /**
     * 提现类型
     */
    public static class WITHDRAW_TYPE{
        public static final Integer WITHDRAW_TYPE_1 = 1; // 普通提现
        public static final Integer WITHDRAW_TYPE_2 = 2; // 快速提现
    }

    /**
     * 会员等级
     */
    public static class MEMBER_LEVEL{
        public static final Integer MEMBER_LEVEL_1 = 1; // 铁象
        public static final Integer MEMBER_LEVEL_3 = 3; // 铜象
        public static final Integer MEMBER_LEVEL_5 = 5; // 银象
        public static final Integer MEMBER_LEVEL_7 = 7; // 金象
        public static final Integer MEMBER_LEVEL_9 = 9; // 白金象
        public static final Integer MEMBER_LEVEL_11 = 11; // 钻石象
        public static final Integer MEMBER_LEVEL_13 = 13; // 无极象
    }
    /**
     * 会员礼包等级
     */
    public static class MEMBER_LEVEL_GIFT{
        public static final String IRON_ELEPHANT_GIFT = "1,2"; // 铁象
        public static final String COPPER_ELEPHANT_GIFT = "1,2,3,4,5"; // 铜象
        public static final String SILVER_ELEPHANT_GIFT = "1,2,3,4,5"; // 银象
        public static final String GOLD_ELEPHANT_GIFT = "1,2,3,4,5,6"; // 金象
        public static final String PLATINUM_ELEPHANT_GIFT = "1,2,3,4,5,6,7"; // 白金象
        public static final String DIAMOND_ELEPHANT_GIFT = "1,2,3,4,5,6,7"; // 钻石象
        public static final String INFINATE_ELEPHANT_GIFT = "1,2,3,4,5,6,7"; // 无极象
    }

    public enum CreditSource{
        WACAI("挖财"),TZZX("投资咨询");
        CreditSource(String descr){
            this.descr = descr;
        }
        private String descr;
    }

    /**
     * banner类型
     */
    public static class BANNER_TYPE{
        public static final Integer BANNER_TYPE_1 = 1; // APP首页
        public static final Integer BANNER_TYPE_2 = 2; // APP注册
        public static final Integer BANNER_TYPE_3 = 3; // H5注册
        public static final Integer BANNER_TYPE_4 = 4; // 首页弹窗
        public static final Integer BANNER_TYPE_5 = 5; // 申购成功
        public static final Integer BANNER_TYPE_7 = 7; // PC首页

        public static final String BANNER_TYPE_8 = "8"; // APP网贷页面—智投宝
        public static final String BANNER_TYPE_9 = "9"; // APP网贷页面—散标
        public static final String BANNER_TYPE_10 = "10"; // APP网贷页面—转让
        public static final String BANNER_TYPE_11 = "11"; // APP发现页面
    }
}
