package com.zdmoney.webservice.api.dto.channel;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by qinz on 2019/2/13.
 * 渠道注册请求参数
 */
@Data
public class RegisterRequestDto implements Serializable{

    /**渠道码 1001-证大前前 1002-挖财 1003-信贷*/
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    /**手机号*/
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**验证码*/
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @Override
    public String toString() {
        return "RegisterRequestDto{" +
                "channelCode='" + channelCode + '\'' +
                ", phone='" + phone + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
