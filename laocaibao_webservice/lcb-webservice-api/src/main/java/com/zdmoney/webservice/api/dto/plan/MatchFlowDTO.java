package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yangp on 2017/11/17
 **/
@Data
public class MatchFlowDTO implements Serializable {

    /**
     * 理财计划ID
     */
    private Long productId;


    /**
     * 资金编号
     */
    private String capitalCode;

    /**
     * 债权编号
     */
    private String financeId;

    /**
     * 操作状态 0-待处理 1-处理成功 2-处理失败
     */
    private String operStatus;

    /**
     * 撮合开始时间
     */
    private String startDate;

    /**
     * 撮合结束时间
     */
    private String endDate;

    private int pageSize = 20;

    private int pageNo = 1;

}
