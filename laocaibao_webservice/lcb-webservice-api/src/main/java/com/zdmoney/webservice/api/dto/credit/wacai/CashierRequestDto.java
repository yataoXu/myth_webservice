package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 跳转收银台相关请求类
 * Created by 46186 on 2019/3/7.
 */
@Data
public class CashierRequestDto extends BaseRequestDto{
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
}
