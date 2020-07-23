package com.zdmoney.models.zdpay;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gosling
 * @date 2018/8/16
 */
@Data
public class BaseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 存管id
     */
    protected String loginId;

}
