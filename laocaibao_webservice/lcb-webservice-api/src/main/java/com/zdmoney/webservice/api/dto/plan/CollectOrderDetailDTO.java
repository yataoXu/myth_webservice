package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;

/**
 * CollectOrderDetailDTO
 *
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/9/22 14:28
 * @Mail: wein@zendaimoney.com
 */
@Data
public class CollectOrderDetailDTO implements Serializable {

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 返回code
     */
    private String code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     */
    private Long type;

    /**
     * 标的编号
     */
    private String subjectNo;

    /**
     * 归集批次号
     */
    private String batchNo;
}
