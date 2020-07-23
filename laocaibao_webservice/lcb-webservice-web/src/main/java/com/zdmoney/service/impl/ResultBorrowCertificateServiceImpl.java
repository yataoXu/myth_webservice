package com.zdmoney.service.impl;

import com.zdmoney.mapper.BusiBorrowCertificateMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.ResultBorrowCertificateService;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.zdmoney.service.impl.CustomerInfoServiceImpl.doSearch;

/**
 * Created by 46186 on 2018/6/19.
 */
@Service
@Slf4j
public class ResultBorrowCertificateServiceImpl implements ResultBorrowCertificateService {
    @Autowired
    private BusiBorrowCertificateMapper busiBorrowCertificateMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Override
    public PageResultDto<BusiBorrowCertificateDto> selectListByMap(Map<String, Object> map) {
        return doSearch(map,new CustomerInfoServiceImpl.Mapper<BusiBorrowCertificateDto>(){

            @Override
            public List<BusiBorrowCertificateDto> query(Map<String, Object> map) {
                return busiBorrowCertificateMapper.selectListByMap(map);
            }
        });
    }

    @Override
    public void insertBorrowCertificate(BusiBorrowCertificateDto model) {
        busiBorrowCertificateMapper.insertRecord(model);
    }
}
