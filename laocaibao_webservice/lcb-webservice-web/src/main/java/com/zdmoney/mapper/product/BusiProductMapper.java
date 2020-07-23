package com.zdmoney.mapper.product;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.models.product.BusiProductInit;
import com.zdmoney.vo.*;
import com.zdmoney.web.dto.CustomerAuthorizeDTO;
import com.zdmoney.webservice.api.dto.ym.BusiProductDto;
import com.zdmoney.webservice.api.dto.ym.vo.BusiProductVo;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BusiProductMapper extends JdMapper<BusiProduct, Long> {

    List<BusiProduct> selectProductsByAuditFlag(Map<String, Object> map);

    List<BusiProductVO> selectProductDetail(Map<String, Object> map);

    List<BusiProductInit> selectProductsByIsrecommend(Map<String, Object> map);

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
    BusiProduct findProductById (Map<String, Object> map);

    /**
     * 查询理财计划详情
     * @param map
     * @return
     */
    List<BusiFinancePlanVO> selectFinancePlanDetail(Map<String, Object> map);

    /**
     * 产品列表[6周年]
     * @param map
     * @return
     */
    List<BusiProductInfo> queryProductList(Map<String, Object> map);


    /**
     * 产品列表[员工专享]
     * @param map
     * @return
     */
    List<BusiProductInfo> queryStaffProductList(Map<String, Object> map);

    /**
     * 查询转让列表
     * @param map
     * @return
     */
    List<BusiProductInfo> queryTransferProductList(Map<String, Object> map);

    /**已过结售期未卖光未重置的产品*/
    List<BusiProduct> queryExpiredProduct();

    List<BusiProductInit> getNewProducts();

    /**
     * 获取产品列表
     * @param
     * @return
     */
    List<BusiProductVo> getProductInfo(BusiProductDto busiProductDto);

    CustomerAuthorizeDTO queryProductSettleDate(long productId);

    /**
     * 查询产品实时数据
     * 是否售罄, 剩余可投金额
     * @param proIds
     * @return
     */
    List<BusiProductInfo> queryRealTimeProductData(List<Long> proIds);

    List<BusiProduct> queryProductInfo(Map<String, Object> params);


    String queryProductNameForWacai();

}