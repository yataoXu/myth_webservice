package com.zdmoney.vo.trade;

import com.zdmoney.enm.OrderType;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.product.BusiProduct;
import lombok.Data;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投标参数
 * Created by 00232384 on 2017/7/6.
 */
@Data
public class TenderVo {

    private OrderType orderType = OrderType.COMMON;

    private String voucherNo="";

    private String integralAmt="";

    private String couponNo = "";

    private CustomerMainInfo customerMainInfo;

    private BusiProduct busiProduct;

    //理财计划子订单不用传
    private String platform = "";

    //普通投标传产品的本金 理财计划子单传标的金额
    private BigDecimal bidAmount;
    //普通投标传产品ID,理财计划子订单投标需要传标的编号
    private String bidNo;

    private String debtType; //1新标的 2退出转让

    private String holderType="0";

    private BigDecimal yearRate;

    private Date saleStartDate; //起售日

    private Date saleEndDate; //结售日

    private String initOrderNum;//上家订单编号

    private BigDecimal debtWorth;
}
