package com.zdmoney.models.financePlan;

import com.zdmoney.constant.ParamConstant;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by gaol on 2017/6/7
 **/
@Data
public class FundDetailDTO {

    /**
     * 主产品ID
     */
    private Long mainProductId;

    /**
     * 资产编号
     */
    private Long fundId;

    /**
     * 用户编号
     */
    private String cmNumber;

    /**
     * 手机号
     */
    private String cmCellPhone;

    /**
     * 资金金额
     */
    private BigDecimal fundAmt;

    /**
     * 封闭期
     */
    private Long closePeriod;

    private int pageSize = ParamConstant.PAGESIZE;

    private int pageNo = 1;
}
