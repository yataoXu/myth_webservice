package com.zdmoney.webservice.api.dto.credit;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by user on 2017/10/23.
 */
public class GettingVCRequestDto extends BaseRequestDto{

    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    public GettingVCRequestDto(String phone,String channelCode,String sign) {
        super(sign);
        this.phone = phone;
        this.channelCode = channelCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public boolean checkSignature() {
        return true;
    }
}
