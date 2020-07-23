package com.zdmoney.webservice.api.dto.app;

import lombok.Data;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author gaol
 * @create 2019-03-20
 */
@Data
public class IndexVO implements Serializable {

    private String customerId;

    /**
     * 请求来源
     * 0: app
     * 1: pc
     */
    private String source = "0";
}
