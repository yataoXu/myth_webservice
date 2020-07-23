package com.zdmoney.service.order;

import com.zdmoney.common.Result;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.vo.BusiBeforeOrderVo;
import com.zdmoney.vo.OrderVo;
import websvc.req.ReqMain;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2016/3/16.
 */
public interface OrderService {

    Result order(Long customerId, Long productId, BigDecimal orderAmt,String inviteCode, ReqMain reqMain) throws Exception;

    void checkLimitProduct(CustomerMainInfo mainInfo, Long limitType);

    Result checkBespeakTicket(String loginType, Long customerId, Long productId, BigDecimal orderAmt);

    BusiOrderSub order(OrderVo vo) throws Exception;

    /**
     * 判断下单或支付剩余出借额度
     * @param mainInfo
     * @param orderAmt
     * @return
     */
    Boolean isInsufficient(CustomerMainInfo mainInfo, BigDecimal orderAmt);

}
