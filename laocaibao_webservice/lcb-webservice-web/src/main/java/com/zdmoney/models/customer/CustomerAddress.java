package com.zdmoney.models.customer;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by user on 2016/11/9.
 * 基于2.9 : 收获地址
 */
@Table(name = "CUSTOMER_ADDRESS")
@Data
public class CustomerAddress extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT SEQ_CUSTOMER_ADDRESS.NEXTVAL FROM DUAL")
    private Long id;

    /**
     * 用户ID
     */
    private Long customerId;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
