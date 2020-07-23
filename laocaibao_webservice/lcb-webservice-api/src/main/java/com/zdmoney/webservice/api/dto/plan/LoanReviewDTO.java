package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * LoanReviewDTO
 *
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/9/26 14:02
 * @Mail: wein@zendaimoney.com
 */
@Data
public class LoanReviewDTO implements Serializable {

    /**
     * 标的编号
     */
    @NotNull(message = "标的编号不能为空")
    private String bidNo;
}
