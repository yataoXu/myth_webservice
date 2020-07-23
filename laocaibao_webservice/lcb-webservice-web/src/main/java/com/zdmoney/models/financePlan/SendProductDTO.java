package com.zdmoney.models.financePlan;

import lombok.Data;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gaol on 2017/6/12
 **/
@Data
public class SendProductDTO {

    /**
     * 理财计划产品编号
     */
    private String productCode;

    /**
     * 项目本金
     */
    private BigDecimal productPrincipal;

    /**
     * 最小匹配金额
     */
    private BigDecimal minMatchAmount;

    /**
     * 约定交割日（发起时间）
     */
    private Date settleDate;

    public SendProductDTO validate(){
        Validate.notBlank(productCode,"productCode must not be blank");
        Validate.notNull(productPrincipal,"productPrincipal must not be blank");
        Validate.notNull(minMatchAmount, "minMatchAmount must not be Null");
        return this;
    }
}
