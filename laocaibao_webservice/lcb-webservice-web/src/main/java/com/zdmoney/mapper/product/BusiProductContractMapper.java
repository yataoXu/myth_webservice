package com.zdmoney.mapper.product;

import com.zdmoney.models.product.BusiProductContract;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BusiProductContractMapper extends Mapper<BusiProductContract> {

    int updateFinishStatus(String subjectNo);

    int updateByMap(Map<String,Object> map);

    List<BusiProductContract> selectBusiProductContract(Map map);

    List<BusiProductContract> selectBusiProductContractByPackedStatus(Map map);

}