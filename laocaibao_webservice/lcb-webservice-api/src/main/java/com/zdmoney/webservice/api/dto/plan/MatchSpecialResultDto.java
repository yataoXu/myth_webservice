package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Leon
 **/
@Data
public class MatchSpecialResultDto implements Serializable {

    /**
     * 理财计划ID
     */
    private Long productId;

    /**
     * 需兜底资金
     */
    private BigDecimal superfluousAmount;

    /**
     * 批次号
     */
    private String batchNo;


    private  List<MatchSpecialDto> resultList;

}
