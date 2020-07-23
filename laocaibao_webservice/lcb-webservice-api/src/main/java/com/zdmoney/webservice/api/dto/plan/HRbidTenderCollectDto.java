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
public class HRbidTenderCollectDto implements Serializable {

    /**
     * 用户编号，借款人
     */
    @NotNull(message = "用户编号不能为空")
    private String personAccount;

    /**
     * 标的编号
     */
    @NotNull(message = "标的编号不能为空")
    private String bidNo;



}
