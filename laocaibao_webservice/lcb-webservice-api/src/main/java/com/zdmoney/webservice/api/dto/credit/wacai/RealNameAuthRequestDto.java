package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 实名认证
 * Created by 46186 on 2019/3/7.
 */
@Data
public class RealNameAuthRequestDto extends BaseRequestDto {

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
