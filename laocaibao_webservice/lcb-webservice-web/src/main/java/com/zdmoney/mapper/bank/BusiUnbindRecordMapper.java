package com.zdmoney.mapper.bank;

import com.zdmoney.models.bank.BusiUnbindRecord;
import com.zdmoney.webservice.api.dto.customer.BusiUnbindRecordDto;
import com.zdmoney.webservice.api.dto.customer.BusiUnbindRecordVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BusiUnbindRecordMapper extends Mapper<BusiUnbindRecord> {
    int insertBusiUnbindRecord(BusiUnbindRecord busiUnbindRecord);

    List<BusiUnbindRecordVO> selectUnbindRecord(Map<String, Object> map);

    List<BusiUnbindRecordDto> selectBankUnbindByTime(Map<String, Object> map);

}