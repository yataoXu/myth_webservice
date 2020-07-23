package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 挖财--获取注册验证码
 * Created by qinz on 2019/3/7.
 */
@Data
public class GetVCodeRequestDto extends BaseRequestDto {

    /**手机号*/
    @NotBlank(message = "手机号不能为空")
    private String phone;

}
