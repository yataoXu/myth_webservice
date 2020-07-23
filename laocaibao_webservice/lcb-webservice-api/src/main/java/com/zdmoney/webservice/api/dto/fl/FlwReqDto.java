package com.zdmoney.webservice.api.dto.fl;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @ Author : Evan.
 * @ Description : 返利网请求dto
 * @ Date : Crreate in 2018/10/26 15:51
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class FlwReqDto implements Serializable {
    @NotEmpty(message = "时间不能为空")
    private String begin_date;

    @NotEmpty(message = "时间不能为空")
    private String end_date;

    private Integer update =1;
}
