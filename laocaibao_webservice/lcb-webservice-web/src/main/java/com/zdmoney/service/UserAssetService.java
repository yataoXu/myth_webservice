package com.zdmoney.service;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2015/12/2.
 * 用户资产接口
 */
public interface UserAssetService {

    /*个人资产接口*/
    Result getUserAsset(ReqMain reqMain) throws Exception;

    /*个人资产接口*/
    Result getUserAssets(ReqMain reqMain) throws Exception;

    /*可用余额*/
    String getAvialableBalance(ReqMain reqMain) throws Exception;

    //统计用户历史收益
    BigDecimal computeHistoryInterest(Long customerId);

    //获取可续约订单列表
    Result unableOrderContinuedList(ReqMain reqMain) throws Exception;

    //获取续约订单初始化
    Result orderContinuedInit(ReqMain reqMain);
}
