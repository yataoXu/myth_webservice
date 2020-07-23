package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustomerAddress;
import com.zdmoney.web.dto.CustomerAddressDTO;

import java.util.List;

/**
 * Created by user on 2016/11/10.
 */
public interface CustomerAddressMapper {

    /**
     * 保存收获地址
     * @param customerAddress
     * @return
     */
    Long saveCustomerAddress(CustomerAddress customerAddress);

    /**
     * 更新收获地址
     * @param customerAddress
     * @return
     */
    Long updateCustomerAddress(CustomerAddress customerAddress);

    /**
     * 查询收获地址列表
     * @param customerId
     * @return
     */
    List<CustomerAddressDTO> queryCustomerAddressList(Long customerId);

    /**
     * 删除地址
     * @param id
     * @return
     */
    Long deleteCustomerAddressById(Long id);
}
