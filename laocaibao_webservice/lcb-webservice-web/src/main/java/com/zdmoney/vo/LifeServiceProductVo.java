package com.zdmoney.vo;

import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jb sun on 2016/3/17.
 */
@Setter
@Getter
public class LifeServiceProductVo {
    private String productName;
    private String returnValue;
    private String productNo;
    private Integer integral;
    private Float cash;
    private String details;
    private Integer stock;

    public LifeServiceProductVo() {
    }

    public LifeServiceProductVo(IntegralProductDto productDto) {
        this.productName = productDto.getUnitValue();
        this.returnValue = String.valueOf(productDto.getReturnValue());
        this.productNo = productDto.getNo();
        this.integral = productDto.getIntegral();
        this.cash = productDto.getCash() == null ? 0 : productDto.getCash();
        this.details = StringUtils.isEmpty(productDto.getDetails()) ? "" : productDto.getDetails();
        this.stock = productDto.getStock() == null ? 0 : productDto.getStock();
    }

}
