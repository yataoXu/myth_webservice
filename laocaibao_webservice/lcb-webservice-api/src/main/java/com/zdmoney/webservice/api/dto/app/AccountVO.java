package com.zdmoney.webservice.api.dto.app;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author gaol
 * @create 2019-03-25
 */
@Data
public class AccountVO implements Serializable {

    @NotEmpty(message = "用户ID不能为空")
    private String customerId;
}
