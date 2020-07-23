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
public class RealNameAuthRequestDto  implements Serializable {

    /**渠道码 1001-证大前前 1002-挖财 1003-信贷*/
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    /**用户*/
    @NotNull(message = "用户id不能为空")
    private Long customerId;

    /**身份证*/
    @NotBlank(message = "身份证号不能为空")
    private String idNum;

    /**姓名*/
    @NotBlank(message = "姓名不能为空")
    private String realName;


}
