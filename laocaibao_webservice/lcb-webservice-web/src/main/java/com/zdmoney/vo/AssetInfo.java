package com.zdmoney.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 00250968 on 2017/11/29
 **/
@Data
public class AssetInfo implements Serializable {

    private BigDecimal principal;

    private BigDecimal interset;

    /**
     * 1: 定期
     * 2: 转让
     * 3: 散标
     * 4: 理财计划
     */
    private Integer productType;

    private BigDecimal totalPrincipalInterest;

}
