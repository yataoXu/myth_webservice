package com.zdmoney.models.flw;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 16:06
 * @Mail : xuyt@zendaimoney.com
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "products")
public class Product {
    private String pid;
    private String title = "";
    private String category = "";
    private String category_title = "";
    private String url = "";
    private int num = 1;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal real_pay_fee = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private String comm_type = "";
    private ProductExtension extension;

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setReal_pay_fee(BigDecimal real_pay_fee) {
        this.real_pay_fee = real_pay_fee.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
