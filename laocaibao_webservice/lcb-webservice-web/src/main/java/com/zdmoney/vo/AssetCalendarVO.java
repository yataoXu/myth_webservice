package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 00250968 on 2017/11/28
 **/
@Data
public class AssetCalendarVO implements Serializable {

    /**
     * 已回款list
     */
    private List receivableList;

    /**
     * 待回款list
     */
    private List waitReceiveList;

    /**
     * 待回款数量
     */
    private Integer waitReceiveCount;

    /**
     * 已回款数量
     */
    private Integer receivableCount;

    /**
     * 已回款总金额
     */
    private BigDecimal totalWaitReceiveAmt;

    /**
     * 待回款总金额
     */
    private BigDecimal totalReceivableAmt;

    /**
     * 当前日期
     */
    @JSONField(format = "yyyy-MM")
    private Date currentTime = new Date();
}
