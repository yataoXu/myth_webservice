package com.zdmoney.mapper.financePlan;

import com.zdmoney.models.financePlan.BusiDebtQueue;
import com.zdmoney.vo.BusiDebtDetailVo;
import com.zdmoney.webservice.api.dto.plan.BusiDebtDetailVO;
import com.zdmoney.webservice.api.dto.plan.DebtDetailReqDTO;
import com.zdmoney.webservice.api.dto.plan.DebtQueueDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by gaol on 2017/6/6
 **/
public interface DebtDetailMapper {

    /**
     * 查询债权详情
     * @param deptProductDetailDTO
     * @return
     */
    List<BusiDebtDetailVO> queryDeptProductDetail(DebtDetailReqDTO deptProductDetailDTO);

    /**
     * 查询债权详情 数量
     * @param deptProductDetailDTO
     * @return
     */
    long countDeptProductDetail(DebtDetailReqDTO deptProductDetailDTO);

    /**
     * 插入债权详情
     * @param busiDebtDetail
     * @return
     */
    int insertDebtDetail(BusiDebtDetailVO busiDebtDetail);

    /**
     * 查询产品详情债权列表（app）
     * @param map
     * @return
     */
    List<BusiDebtDetailVo> selectFinancePlanDetails(Map<String, Object> map);

    /**
     * 查询用户订单详情债权列表（app）
     * @param map
     * @return
     */
    List<BusiDebtDetailVo> selectOrderBusiDebtDetails(Map<String, Object> map);

    /**
     * 查询用户订单详情债权列表（app）-特殊订单
     * @param map
     * @return
     */
    List<BusiDebtDetailVo> selectSpecialOrder(Map<String, Object> map);

    /**
     * 查询一笔特殊订单
     * @return
     */
    String selectOneSpecialOrder(Long orderId);


    /**
     * 更新债权详情
     * @param map
     * @return
     */
    int updateDebtDetail(Map<String, Object> map);

    /**
     * 根据id查询债权信息
     * @param id
     * @return
     */
    BusiDebtDetailVO selectDebtDetailByPrimaryKey(Long id);

    /**
     * 推送债权信息
     * @param debtQueueList
     * @return
     */
    int saveDebtInfo(BusiDebtQueue debtQueueList);

    /**
     * 根据债权编号查询债权信息
     * @param subjectNo
     * @return
     */
    List<BusiDebtDetailVO> queryDebtDetailBySubjectNo(String subjectNo);

    /**
     * 根据标的编号查询债权信息
     * @param subjectNo
     * @return
     */
    BusiDebtDetailVO selectDebtDetailBySubjectNo(String subjectNo);

    /**
     * 根据理财计划id获取匹配信数量
     * @param productId
     * @return
     */
    long debtMatchNums(Long productId);

}
