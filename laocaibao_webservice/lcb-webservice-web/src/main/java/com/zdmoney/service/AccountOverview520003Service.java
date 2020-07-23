package com.zdmoney.service;

import com.zdmoney.common.Result;
import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.vo.UserUnReceiveAsset;
import com.zdmoney.web.dto.AccountInfoDTO;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by 00225181 on 2015/12/2.
 */
public interface AccountOverview520003Service {

    /*账户总览*/
    Result getOverview(ReqMain reqMain)throws Exception;

    /*账户余额*/
    Result getAccountBalance(ReqMain reqMain)throws Exception;

    /*账户余额*/
    BigDecimal getAccountBalance(CustomerMainInfo customerMainInfo)throws Exception;

    /*积分余额*/
    Integer getIntegralBalance(CustomerMainInfo customerMainInfo)throws Exception;

    /*红包余额*/
    Integer getRedPacketBalance(CustomerMainInfo customerMainInfo)throws Exception;

    /*用户持有资产*/
    UserUnReceiveAsset getHoldAsset(CustomerMainInfo customerMainInfo)throws Exception;

    /*用户持有资产v4.1*/
    UserUnReceiveAsset getHoldAssets(CustomerMainInfo customerMainInfo,BusiOrderTemp busiOrderTemp)throws Exception;

    IntegralProductDto getIntegralProductInfo();

    /*用户昨日收益*/
    BigDecimal getYesterdayIncome(CustomerMainInfo customerMainInfo)throws Exception;

    /*用户累计收益*/
    BigDecimal getTotalIncome(CustomerMainInfo customerMainInfo)throws Exception;

    /*用户历史资产红包积分，已收加息累计收益v4.1*/
    UserUnReceiveAsset getTotalCouponAndIntegralAmt(CustomerMainInfo customerMainInfo)throws Exception;

    /*用户待收加息累计收益v4.1*/
    UserUnReceiveAsset getNoRecieveAmt(CustomerMainInfo customerMainInfo)throws Exception;

    /**
     * 全部提现
     * @param cmNumber
     * @return
     * @throws Exception
     */
    BigDecimal extractAllBalance(String cmNumber) throws Exception;


    /**
     * 510008 5.0 获取账户信息(我的)
     * @param reqMain
     * @return
     * @throws Exception
     */
    AccountInfoDTO getAccountInfo(String userId) throws Exception;
}
