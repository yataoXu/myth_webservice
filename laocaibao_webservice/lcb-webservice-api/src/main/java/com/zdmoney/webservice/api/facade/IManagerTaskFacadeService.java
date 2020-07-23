package com.zdmoney.webservice.api.facade;

/**
 * Created by user on 2017/9/13.
 */
public interface IManagerTaskFacadeService {
    void expireRechargeRequest();

    void updateCanceledOrderStatus();

    void updateExpiredProduct();

    void finishCreditTransfer();

    void calculateCustomerRating();

    void sendCustomerRatingChangingMsg();

    void sendNotifyDeptTradeSMS();


    void buyRemainingPartOfWacaiProduct();

    void updateWacaiFundDetail();
}
