package com.zdmoney.mapper.bank;

import com.zdmoney.models.bank.BusiBankLimit;

import java.util.List;

public interface BusiBankLimitMapper {
    List<BusiBankLimit> selectByCondition(BusiBankLimit bankLimit);
}