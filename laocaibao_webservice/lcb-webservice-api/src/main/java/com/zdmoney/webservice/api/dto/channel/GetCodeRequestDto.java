package com.zdmoney.webservice.api.dto.channel;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by qinz on 2019/2/13.
 * 渠道开户请求
 */
@Data
public class GetCodeRequestDto  implements Serializable {

    /**渠道码 1001-证大前前 1002-挖财 1003-信贷*/
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    /**手机号*/
    @NotBlank(message = "手机号不能为空")
    private String phone;

}
