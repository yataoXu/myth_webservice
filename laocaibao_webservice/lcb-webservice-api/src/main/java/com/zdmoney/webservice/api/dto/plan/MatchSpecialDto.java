package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 00232384 on 2017/7/20.
 */
@Data
public class MatchSpecialDto implements Serializable {
    private static final long serialVersionUID = -141957948805595015L;
    private String matchOrderCode;
    private String capitalCode;
    private String customerCode;
    private BigDecimal capitalAmount;
    private String orderNo;
}
