package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gaol on 2017/6/17
 **/
@Data
public class MatchResultDto implements Serializable {

    /**
     * 理财计划ID
     */
    private Long productId;

    /**
     * 撮合债权金额
     */
    private BigDecimal matchAmount;

    /**
     * 已撮合结果集
     */
    private List<MatchSucResult> matchSucResultList;

    /**
     * 提前结清配备异常
     */
    private List<MatchSucResult> abnormalMatchSucResult;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 唯一流水号
     */
    private String serialNo;

    /**
     * 撮合详细数量
     */
    private int matchSucSize;
}
