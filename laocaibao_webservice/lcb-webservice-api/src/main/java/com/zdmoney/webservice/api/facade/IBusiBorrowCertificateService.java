package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateSearchDto;

import java.math.BigDecimal;
import java.util.Map;


/**
 * Created by 46186 on 2018/6/19.
 */
public interface IBusiBorrowCertificateService {
    //查询出借凭管理列表
    PageResultDto<BusiBorrowCertificateDto> searchList(BusiBorrowCertificateSearchDto searchDto);
    //插入一条新纪录
    ResultDto create(BusiBorrowCertificateDto model);
    //获取在投本金
    ResultDto<String> getHoldAsset(String customerId);
}
