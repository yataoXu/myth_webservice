package com.zdmoney.webservice.api.dto.Asset;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by 00250968 on 2017/10/23
 **/
@Data
public class UserBindCardDTO implements Serializable {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotBlank(message = "银行代码不能为空!")
    private String bankCode;

    @NotBlank(message = "银行卡号不能为空!")
    private String bankCard;

    @NotBlank(message = "银行预留手机号不能为空!")
    private String cellphone;

}
