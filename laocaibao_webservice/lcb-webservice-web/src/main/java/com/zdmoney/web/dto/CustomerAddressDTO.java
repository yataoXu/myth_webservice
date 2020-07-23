package com.zdmoney.web.dto;

import lombok.Data;

/**
 * Created by user on 2016/11/11.
 */
@Data
public class CustomerAddressDTO {
    /**
     * 地址编号
     */
    private Long id;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系方式
     */
    private String cellPhone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 街道地址
     */
    private String street;

    /**
     * 状态
     */
    private Integer status;
}
