package com.zdmoney.enm;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单枚举类
 */
@Getter
@AllArgsConstructor
public enum OrderGenerateType {
    MAIN_ORDER("C","8888","主订单"),
    SUB_ORDER("C","7888","子订单");
    /**
     * 订单类型
     */
    private String type;

    /**
     * 编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;
}
