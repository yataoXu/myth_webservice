/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;


public interface EuroCupFunctionService extends FunctionService {

    /*
     * 900101 竞猜页面
     *
     */
    Result guessMatch(ReqMain reqMain) throws Exception;

    /*
     * 900102 竞猜结果提交
     *
     */
    Result commitMatch(ReqMain reqMain) throws Exception;

    /*
     *900103 上一日比赛中奖用户
     *
     */
    Result previousGameWinner(ReqMain reqMain) throws Exception;

    /*
     * 901001 欧洲杯英雄榜
     *
     */
    Result euroCupHeroList(ReqMain reqMain) throws Exception;

    /*
     * 901002 欧洲杯英雄榜今日是否已投票
     *
     */
    Result euroCupHeroIsVoted(ReqMain reqMain) throws Exception;

    /*
     * 901003 欧洲杯英雄榜投票
     *
     */
    Result euroCupHeroVote(ReqMain reqMain) throws Exception;

    /*
     * 902001 邀请排行榜
     *
     */
    Result contactsList(ReqMain reqMain) throws Exception;
}
