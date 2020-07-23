package com.zdmoney.web.dto;

import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiProductVO;

/**
 * Created by 00225181 on 2016/4/1.
 */
public class ProductListDTO {
    private Page<BusiProductVO> products;

    public Page<BusiProductVO> getProducts() {
        return products;
    }

    public void setProducts(Page<BusiProductVO> products) {
        this.products = products;
    }
}
