package com.zdmoney.models.zdpay;

import lombok.Data;

/**
 * WithdrawBo
 * 提现返回对象
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/8/20 17:32
 * @Mail: wein@zendaimoney.com
 */
@Data
public class WithdrawBo extends BaseBO {

    /**
     * 	备用字段
     */
    private String reserved;

    /**
     * 商户流水号
     */
    protected String channelOrderNo;

    /**
     * 	收银台流水号
     */
    private String mchntTxnSsn;

    /**
     * 清算日期，yyyyMMdd
     */
    private String settleDate;

    /**
     * 金额，分
     */
    private String payAmt;
}