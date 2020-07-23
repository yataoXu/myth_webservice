package com.zdmoney.webservice.api.dto.credit;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by user on 2017/10/19.
 */
public class RegisteringRequestDto extends BaseRequestDto{

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "身份证号不能为空")
    private String idNo;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    public boolean checkSignature() {
        return true;
    }
    public RegisteringRequestDto(String phone, String name, String idNo, String verifyCode,String sign) {
        super(sign);
        this.phone = phone;
        this.name = name;
        this.idNo = idNo;
        this.verifyCode = verifyCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getIdNo() {
        return idNo;
    }

    public String getVerifyCode() {
        return verifyCode;
    }
}
