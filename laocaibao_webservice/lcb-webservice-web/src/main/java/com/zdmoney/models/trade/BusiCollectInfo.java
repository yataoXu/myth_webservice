package com.zdmoney.models.trade;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "BUSI_COLLECT_INFO")
public class BusiCollectInfo {
    /**
     * 序列
     */
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 回应码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 订单金额
     */
    @Column(name = "MSG")
    private String msg;

    /**
     * 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     */
    @Column(name = "TYPE")
    private Long type;

    /**
     * 标的边号
     */
    @Column(name = "SUBJECT_NO")
    private String subjectNo;

    /**
     * 归集批次号
     */
    @Column(name = "BATCH_NO")
    private String batchNo;

}