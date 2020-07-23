package com.zdmoney.vo;

import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.enm.OrderType;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.webservice.api.dto.plan.MatchSucResult;
import lombok.Data;
import websvc.req.ReqMain;

import java.math.BigDecimal;


/**
 * 下单公共参数
 * Created by 00232384 on 2017/7/6.
 */
@Data
public class OrderVo {

    /**
     * 普通订单 理财计划子订单 理财计划转让订单
     */
    private OrderType orderType = OrderType.COMMON;

    /**
     * 用户ID
     */
    private Long customerId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmt;

    /**
     * 请求参数
     */
    private ReqMain reqMain;

    /**
     * 理财计划主单
     */
    private BusiOrderSub planOrder;

    /**
     * 撮合结果
     */
    private MatchSucResult matchSucResult;

    /**产品**/
    private BusiProductSub busiProductSub;

    /**
     * 转让人订单
     */
    private BusiOrderSub originOrder;

    /**
     持有人类型 0普通客户 1-特殊理财人 2-特殊理财人-异常回购*/
    private String holdType= OrderConstants.OrderHoldType.HOLD_COMMON;


}
