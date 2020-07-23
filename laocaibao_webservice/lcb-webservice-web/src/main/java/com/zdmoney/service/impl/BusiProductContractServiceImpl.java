package com.zdmoney.service.impl;

import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.service.BusiProductContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by user on 2019/3/5.
 */
@Service
public class BusiProductContractServiceImpl implements BusiProductContractService {

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;

    @Override
    public BusiProductContract selectBySubjectNo(String subjectNo) {
        Example example = new Example(BusiProductContract.class);
        example.createCriteria().andEqualTo("subjectNo", subjectNo);
        List<BusiProductContract> busiProductContracts = busiProductContractMapper.selectByExample(example);
        if(!busiProductContracts.isEmpty()) return busiProductContracts.get(0);
        return null;
    }
}
