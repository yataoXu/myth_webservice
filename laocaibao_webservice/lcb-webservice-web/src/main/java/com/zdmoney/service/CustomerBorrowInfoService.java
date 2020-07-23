package com.zdmoney.service;


import com.zdmoney.common.service.BaseService;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.CustomerBorrowInfoMapper;
import com.zdmoney.models.CustomerBorrowInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websvc.models.Model_420005;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;


@Service
@Slf4j
public class CustomerBorrowInfoService extends BaseService<CustomerBorrowInfo, Long> {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;


    private CustomerBorrowInfoMapper getCustomerBorrowInfoMapper() {
        return (CustomerBorrowInfoMapper) baseMapper;

    }

    @Transactional
    public void saveCustomerBorrowInfo(Model_420005 cdtModel){
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(cdtModel.getCmId());
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        if (cdtModel.getBorrowAmt().compareTo(new BigDecimal(200000))>0){
            throw new BusinessException("借款金额超过20万元");

        }
        CustomerBorrowInfo borrowInfo = new CustomerBorrowInfo();
        borrowInfo.setCmId(cdtModel.getCmId());
        CustomerBorrowInfo customerBorrowInfo = baseMapper.selectOne(borrowInfo);
        if (customerBorrowInfo!=null){
            throw new BusinessException("该用户已经提交过借款采集意向！");
        }

        borrowInfo = new CustomerBorrowInfo();
        borrowInfo.setCmId(cdtModel.getCmId());
        borrowInfo.setCmNumber(mainInfo.getCmNumber());
        borrowInfo.setBorrowAmt(cdtModel.getBorrowAmt());
       // borrowInfo.setBorrowPurpose(URLDecoder.decode(cdtModel.getBorrowPurpose(),"UTF-8"));
        borrowInfo.setBorrowPurpose(cdtModel.getBorrowPurpose());
        borrowInfo.setBorrowPeriod(cdtModel.getBorrowPeriod());
        borrowInfo.setOverdueNum(0L);
        borrowInfo.setOverdueCreditStatus(0L);
        borrowInfo.setThdFlag(0L);
        borrowInfo.setCreateTime(new Date());

        getCustomerBorrowInfoMapper().insertSelective(borrowInfo);
    }
}