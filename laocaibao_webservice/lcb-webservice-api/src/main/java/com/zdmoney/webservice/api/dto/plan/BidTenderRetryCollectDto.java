package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * HRbidTenderCollectDto
 *
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/9/21 18:38
 * @Mail: wein@zendaimoney.com
 */
@Data
public class BidTenderRetryCollectDto implements Serializable {

    /**
     * 用户编号
     */
    private String personAccount;

    /**
     * 标的编号
     */
    @NotNull(message = "标的编号不能为空")
    private String bidNo;

    /**
     * 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     */
    @NotNull(message = "归集类型不能为空")
    private int type;

}
