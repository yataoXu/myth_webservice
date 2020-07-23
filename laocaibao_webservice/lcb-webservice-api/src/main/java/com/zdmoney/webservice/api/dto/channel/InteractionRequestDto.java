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
public class InteractionRequestDto  implements Serializable {

    /**渠道码 1001-证大前前 1002-挖财 1003-信贷*/
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    /**用户*/
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**身份证*/
    @NotBlank(message = "身份证号不能为空")
    private String idNum;

    /**前端回调地址*/
    @NotBlank(message = "前端回调地址不能为空")
    private String pageUrl;

    /**PC或APP*/
    @NotBlank(message = "客户端类型不能为空")
    private String clientType;

    @Override
    public String toString() {
        return "InteractionRequestDto{" +
                "channelCode='" + channelCode + '\'' +
                ", phone='" + phone + '\'' +
                ", idNum='" + idNum + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}
