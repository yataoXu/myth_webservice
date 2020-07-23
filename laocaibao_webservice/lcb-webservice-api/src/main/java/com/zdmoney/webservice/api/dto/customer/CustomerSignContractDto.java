package com.zdmoney.webservice.api.dto.customer;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class CustomerSignContractDto implements Serializable {

    @NotBlank(message="用户编号不能为空!")
    private String cmNumber;//用户编号

}