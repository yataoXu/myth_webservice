package com.zdmoney.mapper;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.BorrowValidateCode;

import java.util.Map;

/**
 * Created by 46186 on 2018/6/21.
 */
public interface BorrowValidateCodeMapper extends JdMapper<BorrowValidateCode, Long> {
    BorrowValidateCode selectLastOneByEmailAndType(Map<String,Object> map);
}
