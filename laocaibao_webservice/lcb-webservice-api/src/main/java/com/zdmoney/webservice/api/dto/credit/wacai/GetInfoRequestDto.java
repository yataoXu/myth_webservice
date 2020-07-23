package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by 46186 on 2019/3/7.
 */
@Data
public class GetInfoRequestDto extends BaseRequestDto {

    /**用户*/
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**身份证*/
    @NotBlank(message = "身份证号不能为空")
    private String idNum;
}
