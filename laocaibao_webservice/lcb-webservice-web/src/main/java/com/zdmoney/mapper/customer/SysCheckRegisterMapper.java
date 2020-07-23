package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.SysCheckRegister;
import tk.mybatis.mapper.common.Mapper;

public interface SysCheckRegisterMapper extends Mapper<SysCheckRegister> {

    Integer countForRegisterPhone(SysCheckRegister sysCheckRegister);

}