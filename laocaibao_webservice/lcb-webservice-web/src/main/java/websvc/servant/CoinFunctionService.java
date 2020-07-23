/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * CoinFunctionService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-25 16:00
 * Mail: haoc@zendaimoney.com
 */
public interface CoinFunctionService extends FunctionService {

    /*
     * 800001 捞财币商品查询
     *
     */
    Result coinProductList(ReqMain reqMain) throws Exception;

    /*
     * 800002 捞财币商品详情
     *
     */
    Result coinProductDetail(ReqMain reqMain) throws Exception;

    /*
     * 800003 捞财币领取接口
     *
     */
    Result coinGet(ReqMain reqMain) throws Exception;

    /*
     * 800004 捞财币兑换记录接口
     *
     */
    Result coinTradeLogList(ReqMain reqMain) throws Exception;

    /*
     * 800006 兑换商品
     *
     */
    Result exchange(ReqMain reqMain) throws Exception;

    /*
     * 800017 使用上期捞财币兑换商品
     *
     */
    Result exchangeLast(ReqMain reqMain) throws Exception;

    /*
     * 800009 捞财币明细
     *
     */
    Result coinDetail(ReqMain reqMain) throws Exception;

    /*
     * 810001 用户任务
     *
     */
    Result taskList(ReqMain reqMain) throws Exception;

    /*
     * 810002 领取捞财币
     *
     */
    Result receiveTaskReward(ReqMain reqMain) throws Exception;

    /*
     * 810003 多捞多得列表
     *
     */
    Result monthTaskList(ReqMain reqMain) throws Exception;
}
