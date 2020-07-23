package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * Created by qinz on 2019/3/7.
 */
@Data
public class RegisterRequestDto extends GetInfoRequestDto{

    /**标的编号*/
    @NotBlank(message = "标的编号不能为空")
    private String subjectNo;

    /**姓名*/
    @NotBlank(message = "姓名不能为空")
    private String name;

}
