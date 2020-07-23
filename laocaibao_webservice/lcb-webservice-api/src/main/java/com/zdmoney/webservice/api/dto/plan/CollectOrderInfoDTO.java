package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * CollectOrderInfoDTO
 *
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/9/25 14:40
 * @Mail: wein@zendaimoney.com
 */
@Data
public class CollectOrderInfoDTO implements Serializable {


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 回应码
     */
    private String code;

    /**
     * 订单金额
     */
    private String msg;

    /**
     * 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     */
    @NotNull(message = "归集类型不能为空")
    private Long type;

    /**
     * 标的边号
     */
    @NotNull(message = "标的编号不能为空")
    private String subjectNo;

    /**
     * 归集批次号
     */
    private String batchNo;

    private List<CollectOrderDetailDTO> collectOrderDetailDTOList;

}
