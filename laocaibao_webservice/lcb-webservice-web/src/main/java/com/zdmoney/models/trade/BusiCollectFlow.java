package com.zdmoney.models.trade;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "BUSI_COLLECT_FLOW")
public class BusiCollectFlow {
    /**
     * 序列
     */
    @Column(name = "ID")
    private Long id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 客户ID
     */
    private String code;

    /**
     * 订单金额
     */
    private String msg;

    /**
     * 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     */
    private Long type;

    /**
     * 标的边号
     */
    private String subjectNo;

    /**
     * 归集批次号
     */
    private String batchNo;
}