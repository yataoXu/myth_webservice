package com.zdmoney.service;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;

/**
 * Created by 00225181 on 2015/12/2.
 * 2.0获取支付信息
 */
public interface PayInfo520002Service {

    Result getPayInfo(ReqMain reqMain)throws Exception;


    /**
     * 优惠券接口 (收银台)
     */
    Result vouchers(ReqMain reqMain)throws Exception;
}
