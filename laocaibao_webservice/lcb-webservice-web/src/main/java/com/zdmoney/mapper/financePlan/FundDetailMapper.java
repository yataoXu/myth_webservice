package com.zdmoney.mapper.financePlan;

import com.zdmoney.models.financePlan.BusiFundDetail;
import com.zdmoney.webservice.api.dto.plan.BusiFundDetailVO;
import com.zdmoney.webservice.api.dto.plan.FundDetailReqDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by gaol on 2017/6/7
 **/
public interface FundDetailMapper {

    /**
     * 查询资金详情
     * @param id
     * @return
     */
    BusiFundDetail selectByPrimaryKey(Long id);

    /**
     * 查询资金详情
     * @param fundDetailDTO
     * @return
     */
    List<BusiFundDetailVO> queryFundDetail(FundDetailReqDTO fundDetailDTO);

    /**
     * 查询资金详情 数量
     * @param fundDetailDTO
     * @return
     */
    long countFundDetail(FundDetailReqDTO fundDetailDTO);

    long saveFundDetail(BusiFundDetail fundDetail);

    int updateByMap(Map<String,Object> map);

    /**
     * 条件查询
     * @return
     */
    List<BusiFundDetail> selectByCondition(Map map);

}
