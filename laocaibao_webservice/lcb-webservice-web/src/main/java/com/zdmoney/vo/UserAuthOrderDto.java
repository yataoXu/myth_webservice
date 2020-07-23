package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2015/12/2.
 */
@Getter
@Setter
public class UserAuthOrderDto {


    /*智投宝订单总金额*/
    private BigDecimal totalPlanAmt = new BigDecimal(0);

    /*散标（个贷）订单总金额*/
    private BigDecimal totalLoanAmt = new BigDecimal(0);

    private BigDecimal totalAmt = new BigDecimal(0);//授权出借需要额度

    /*结息日*/
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;

    //授权出借、缴费期限
    private  Date authDate;

    //缴费额度
    private BigDecimal authFee = new BigDecimal(0);
}
