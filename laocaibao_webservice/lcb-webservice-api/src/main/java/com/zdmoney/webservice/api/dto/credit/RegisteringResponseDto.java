package com.zdmoney.webservice.api.dto.credit;

/**
 * Created by user on 2017/10/19.
 */
public class RegisteringResponseDto extends BaseResponseDto {
    private String customerId;

    public RegisteringResponseDto(String customerId) {
        super(CreditResponse.SUCCESS);
        this.customerId = customerId;
    }

    public RegisteringResponseDto(CreditResponse response) {
        super(response);
    }

    public RegisteringResponseDto(CreditResponse response, String msg) {
        super(response, msg);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
