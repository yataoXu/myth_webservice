package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by 46186 on 2019/3/14.
 */
@Data
public class OpenAuthRequestDto extends CashierRequestDto {

    /**
     * 开户0/授权1
     */
    @NotBlank(message = "请求类型不能为空")
    private String reqType;

}
