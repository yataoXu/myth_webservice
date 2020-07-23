package com.zdmoney.webservice.api.dto.product;

import com.zdmoney.webservice.api.common.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gaol
 * @create 2019-03-14
 */
@Data
public class BusiProductListDTO implements Serializable {

    private Page<BusiProductInfo> productList;

    private Introduction introduction;

    @Data
    public static class Introduction implements Serializable{

        private Long id;

        /**
         * 产品介绍图片
         */
        private String img;

        /**
         * 产品介绍URL
         */
        private String url;
    }
}
