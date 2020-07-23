package com.zdmoney.mapper;

import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateDto;

import java.util.List;
import java.util.Map;

/**
 * Created by qinzhuang on 2018/6/12.
 */
public interface BusiBorrowCertificateMapper {
    void insertRecord(BusiBorrowCertificateDto dto);

    List<BusiBorrowCertificateDto> selectListByMap(Map<String, Object> map);

    /**
     * 根据cmNumber查询最新的出借凭证记录
     * @param cmNumber
     * @return
     */
    BusiBorrowCertificateDto selectByCmNumber(String cmNumber);
}
