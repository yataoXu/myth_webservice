package com.zdmoney.mapper.financePlan;

import com.zdmoney.webservice.api.dto.plan.BusiMatchExceptionVO;

import java.util.List;
import java.util.Map;

/**
 * Created by yangp on 2017/10/17
 **/
public interface BusiMatchExceptionMapper {

    /**
     * 查询撮合异常流水
     * @param map
     * @return
     */
    List<BusiMatchExceptionVO> queryMatchException(Map<String, Object> map);

    /**
     * 保存撮合异常流水
     * @param map
     * @return
     */
    long saveMatchException(Map<String, Object> map);

    /**
     * 更新撮合异常流水
     * @param map
     * @return
     */
    int updateByMap(Map<String, Object> map);

}
