package com.zdmoney.service;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateDto;

import java.util.Map;

/**
 * Created by 46186 on 2018/6/19.
 */
public interface ResultBorrowCertificateService {
    PageResultDto<BusiBorrowCertificateDto> selectListByMap(Map<String, Object> map);

    void insertBorrowCertificate(BusiBorrowCertificateDto model);

}
