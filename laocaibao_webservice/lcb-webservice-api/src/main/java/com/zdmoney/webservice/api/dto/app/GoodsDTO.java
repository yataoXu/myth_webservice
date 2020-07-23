package com.zdmoney.webservice.api.dto.app;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GoodsDTO implements Serializable {
    private int id;
    private String code;
    private String name;
    private String salePrice;
    private String marketPrice;
    private String url;
    private String supplierId;
    private String isHave;
    private String discountpercent;
    private String sort;
    private String productId;
    private String isAdded;
    private String pointPay;
    private String detailUrl;
}
