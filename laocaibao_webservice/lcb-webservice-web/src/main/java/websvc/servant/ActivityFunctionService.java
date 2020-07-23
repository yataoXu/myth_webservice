package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;


/**
 * 活动接口
 * <p/>
 * Author: silence.cheng
 * Date: 2016-09-19 11:10
 */
public interface ActivityFunctionService extends FunctionService {


    /*
     * 905001 获取下单排名列表
     *
     */
    Result getOrderPayRankList(ReqMain reqMain) throws Exception;

    /*
     * 905002 用户活动期间订单总金额排名
     *
     */
    Result getOrderTotalAmtRankList(ReqMain reqMain) throws Exception;

    /*
     * 905003 到期弹层提示活动初始化
     *
     */
    Result expireRemindInit(ReqMain reqMain) throws Exception;

    /*
     * 905004 更新活动开始时间
     *
     */
    Result updateFirstShareTime(ReqMain reqMain) throws Exception;

    /**
     * 抓娃娃活动 初始化接口
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result craneMachineInit(ReqMain reqMain) throws Exception;

    /**
     * 获取抓娃娃机会
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result getCraneMachineChance(ReqMain reqMain) throws Exception;

    /**
     * 3.5版本  活动中转接口
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result activityTransfer(ReqMain reqMain) throws Exception;

    /**
     * 黄金刮刮卡
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result scratchCardInit(ReqMain reqMain) throws Exception;

    /**
     * 刮刮卡兑换
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result scratchCardChance(ReqMain reqMain) throws Exception;

    Result getUserLevel(ReqMain reqMain) throws Exception;

    /**
     * 现金券领取
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result gainCash(ReqMain reqMain) throws Exception;
}
