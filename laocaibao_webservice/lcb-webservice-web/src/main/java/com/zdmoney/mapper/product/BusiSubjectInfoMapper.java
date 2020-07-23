package com.zdmoney.mapper.product;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiSubjectInfo;

import java.util.List;
import java.util.Map;

public interface BusiSubjectInfoMapper extends JdMapper<BusiSubjectInfo, Long> {

    List<BusiSubjectInfo> selectSubjectInfoByIsPacked(Map<String, Object> map);

    int updatePackedForSubjectInfo(Map<String, Object> map);

}