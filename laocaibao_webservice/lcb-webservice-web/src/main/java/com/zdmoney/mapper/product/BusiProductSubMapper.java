package com.zdmoney.mapper.product;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.vo.*;

import java.util.List;
import java.util.Map;

public interface BusiProductSubMapper extends JdMapper<BusiProductSub, Long> {

    List<BusiProduct> selectProductsByAuditFlag(Map<String, Object> map);

    List<BusiProductVO> selectProductDetail(Map<String, Object> map);

    List<BusiProductVO> getOrgProducts(Map<String, Object> map);

    String queryCustomerStatus(Map<String, Object> map);

    int updateInvestAmt(Map<String, Object> map);

    int updateMainSub(Map<String, Object> map);

    int refundInvestAmt(Map<String, Object> map);

    List<BusiProductVO> getPCProducts(Map<String, Object> map);

    BusiProductVO getProductDetailPC(Map<String, Object> map);

    /**
     * 查询转让产品详情
     * @param map
     * @return
     */
    List<BusiTransferProductVO> selectTransferProductDetail(Map<String, Object> map);

    /**
     * 查询转让产品详情(理财列表页)
     * @param map
     * @return
     */
    List<BusiProductVO> selectIndexTransferProductDetail(Map<String, Object> map);

    /**
     * 查询腾讯产品
     * @param map
     * @return
     */
    List<TencentProductVo> selectTencentProducts(Map<String, Object> map);

    BusiProductRuleVo selectProductWithRule(Long productId);

    /**
     * 查询借款人信息
     * @param productId
     * @return
     */
    BusiBorrowerInfoVO queryBorrowerInfo(Long productId);

    /**
     * 查询投资记录
     * @param map
     * @return
     */
    List<InvestRecordVO> queryInvestRecord(Map<String, Object> map);

    /**
     * 查询产品相关动态数据
     * @param map
     * @return
     */
    List<BusiProductVO> getProductDynamicData(Map<String, Object> map);

    /**
     * 根据产品ID查询产品详情
     * @param map
     * @return
     */
    BusiProductSub findProductById(Map<String, Object> map);


    /**
     * 查询理财计划详情
     * @param map
     * @return
     */
    List<BusiFinancePlanVO> selectFinancePlanDetail(Map<String, Object> map);

    List<BusiProductSub> selectProductSub(Map<String, Object> map);

    int saveProductSub(BusiProductSub busiProductSub);

    int saveProductSubForWacai(BusiProductSub busiProductSub);

    /**
     * 根据标的编号查询子产品信息
     */
    BusiProductSub findProductSubBySubjectNo(String subjectNo);
}