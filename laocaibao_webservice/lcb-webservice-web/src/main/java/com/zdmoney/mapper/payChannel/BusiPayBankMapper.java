package com.zdmoney.mapper.payChannel;

import com.zdmoney.models.payChannel.BusiPayBank;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusiPayBankMapper extends Mapper<BusiPayBank> {
    List<BusiPayBank> selectValidBankList();
}