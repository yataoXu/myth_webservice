package com.zdmoney.web.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by user on 2016/11/11.
 */
@Data
public class BusiBankQuotaDTO {

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 单笔限额
     */
    private BigDecimal singleQuota;

    /**
     * 单日限额
     */
    private BigDecimal dayQuota;

    /**
     * 单月限额
     */
    private BigDecimal monthQuota;

    /**
     * 限额说明
     */
    private String remark;
}
