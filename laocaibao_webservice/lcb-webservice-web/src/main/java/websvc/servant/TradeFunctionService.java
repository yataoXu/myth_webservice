package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * 前端调用-交易模块
 * TradeFunctionService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-25 16:02
 * Mail: haoc@zendaimoney.com
 */
public interface TradeFunctionService extends FunctionService {

    /*
     *500000 平台销售情况总览
     */
    Result platformHistorySale(ReqMain reqMain) throws Exception;


    /*
     * 500003 下单接口
     *
     */
    Result order(ReqMain reqMain) throws Exception;


    /*
     * 500009 获取充值状态
     *
     */
    Result rechargeInfo(ReqMain reqMain) throws Exception;

    /*
     * 500012 资产明细列表
     *
     */
    Result assetList(ReqMain reqMain) throws Exception;

    /*
     * 500017 查询投资协议
     *
     */
    Result investAgreement(ReqMain reqMain) throws Exception;

    /*
     * 500018 查询债权协议
     *
     */
    Result loanAgreement(ReqMain reqMain) throws Exception;

    /*
     * 500023 查询标的协议模板
     *
     */
    Result loanAgreementTemplate(ReqMain reqMain) throws Exception;


    /*
     * 500022 回款日历接口
     *
     */
    Result getPaymentCalendar(ReqMain reqMain) throws Exception;

    /**
     * 720002 腾讯下单
     *
     * @param reqMain
     */
    Result orderPayTencent(ReqMain reqMain) throws Exception;

    /**
     * 720003 查询用户可用红包
     *
     * @param reqMain
     */
    Result gainAvailCoupon(ReqMain reqMain) throws Exception;


    /**
     * 500050
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryAgreement(ReqMain reqMain) throws Exception;


    /**
     * 540009 查询产品详情模板
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryProductAgreements(ReqMain reqMain) throws Exception;

    /**
     * 查询投资记录
     *
     * @param reqMain
     * @return
     */
    Result queryInvestRecord(ReqMain reqMain) throws Exception;



    Result orderPay(ReqMain reqMain) throws Exception;

    /**
     * 520018
     * 获取用户绑卡信息
     */
    Result getCurrentPayChannel(ReqMain reqMain) throws Exception;

    /**
     * 520020
     * 平台运营数据
     *
     * @return
     * @throws Exception
     */
    Result findOperationDataStatistics(ReqMain reqMain) throws Exception;

    /**
     * 520021
     * 全部提现
     *
     * @return
     * @throws Exception
     */
    Result extractAllBalance(ReqMain reqMain) throws Exception;

    /**
     * 收银台接口
     */
    Result cashierPay(ReqMain reqMain) throws Exception;

    /**
     * 收银台初始化接口
     */
    Result cashierPayInit(ReqMain reqMain) throws Exception;

    /**
     * 优惠券接口 (收银台)
     */
    Result vouchers(ReqMain reqMain) throws Exception;


    /**
     * 521005
     * 续投接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result continuedInverstment(ReqMain reqMain) throws Exception;

    /**
     * 可续投订单列表
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result unableOrderContinuedList(ReqMain reqMain) throws Exception;

    /**
     * 续投订单初始化
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result orderContinuedInit(ReqMain reqMain) throws Exception;


    /**
     * 内部网络校验
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result innerIPAddres(ReqMain reqMain) throws Exception;

    /**
     * 员工专属产品
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result staffProductList(ReqMain reqMain) throws Exception;

}