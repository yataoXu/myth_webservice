package com.zdmoney.webservice.api.dto.plan;

import com.zdmoney.webservice.api.dto.enm.SuperfluousType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Leon
 **/
@Data
public class MatchSpecialReqDto implements Serializable {

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

    /**
     * 待兜底撮合列表
     */
    private List<MatchSucResult> waitIngOutResultList;

    /**
     * 兜底类型
     */
    private SuperfluousType superfluousType;

}
