package com.zdmoney.webservice.api.dto.product;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 5.0
 */
@Data
public class ProductVO  implements Serializable {

    /**
     * 产品类型
     *
     * 1: 定期
     * 2: 个贷(散标)
     * 3: 理财计划(智投保)
     * 4: 转让产品
     * 5: PC全部
     * 6: 新手标
     * 7: 专区产品
     */
    @NotNull(message = "产品类型不能为空")
    private Integer productType;

    /**
     * 页码
     */
    private Integer pageNo = 1;

    private Long customerId;

    /**
     * 收益率
     * 0: 降序 1: 升序
     */
    private Integer rateSort;

    /**
     * 封闭期
     * 0: 降序 1: 升序
     */
    private Integer termSort;

}
