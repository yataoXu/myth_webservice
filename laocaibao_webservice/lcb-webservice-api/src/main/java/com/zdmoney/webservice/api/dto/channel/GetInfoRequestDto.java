package com.zdmoney.webservice.api.dto.channel;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by 46186 on 2019/2/14.
 * 获取用户信息请求参数
 */
@Data
public class GetInfoRequestDto  implements Serializable {
    /**渠道码 1001-证大前前 1002-挖财 1003-信贷*/
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    /**用户*/
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**身份证*/
    @NotBlank(message = "身份证号不能为空")
    private String idNum;
}
