package com.zdmoney.models.zdpay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Author: silence.cheng
 * Date: 2018/9/25 17:18
 */
@Data
public class UserAuthDto {

    BigDecimal authTotalAmt= new BigDecimal(0);//授权出借额度
    BigDecimal totalPayPlanAmt= new BigDecimal(0);//智投宝订单总金额
    Date authPeriod;//授权出借期限
    BigDecimal payAmt = new BigDecimal(0);//授权缴费额度
    Date payPeriod ;//授权缴费期限

}
