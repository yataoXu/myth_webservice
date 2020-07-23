package com.zdmoney.web.dto;

import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.product.BusiProduct;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Author: silence.cheng
 * Date: 2018/11/1 15:44
 */

@Setter
@Getter
public class TransferProductDTO {

    private  String transferProductName;

    private BigDecimal productInterest;

    private BusiOrderTemp busiOrder;

    private BusiProduct busiProduct;

    private BigDecimal transferPrice;

    private Date transferDate;

    private Date rate;

    private BusiOrderTemp originOrder;

    private BigDecimal discountRate;

    private BigDecimal originYearRate;


}
