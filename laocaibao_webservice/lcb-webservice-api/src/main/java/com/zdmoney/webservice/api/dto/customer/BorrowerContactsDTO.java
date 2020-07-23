package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author hanxn
 * @date 2019/3/11
 */
@Setter
@Getter
public class BorrowerContactsDTO implements Serializable {
    /**
     *
     联系人名称
     */
    private String contactsName;

    /**
     * 联系人手机
     */
    private String contactsPhone;

    /**
     * 联系人关系
     */
    private String contactsRelation;

}
