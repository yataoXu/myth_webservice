package com.zdmoney.service.product;

import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.models.product.BusiProductListDTO;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.*;
import com.zdmoney.webservice.api.dto.ym.BusiProductDto;
import com.zdmoney.webservice.api.dto.ym.vo.BusiProductVo;

import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2016/3/16.
 */
public interface ProductService {

    Page<BusiProductVO> getProductDetail(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize,String showProductType) throws Exception;

    BusiProductVO getChannelProductDetail(String channel) throws Exception;

    /*
     * 转让产品列表
     */
    Page<BusiTransferProductVO> getTransferProductDetail(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize,String productId,String upLowFlag) throws Exception;

    /*
     * 转让产品列表（理财列表页）
     */
    Page<BusiProductVO> getIndexTransferProductDetail(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize,String productId,String upLowFlag) throws Exception;


    /*腾讯产品*/
    List<TencentProductVo> selectTencentProducts(String sessionToken,String channel,Long productId,String isNewHand) throws Exception;

    /**
     * 查询借款人信息
     * @param productId
     * @return
     */
    BusiBorrowerInfoVO queryBorrowerInfo(Long productId) throws Exception;

    /**
     * 查询投资记录
     * @param productId 产品ID
     * @return
     */
    Page<InvestRecordVO> queryInvestRecord(Long productId, int pageNo, int pageSize) throws Exception;

    /**
     * 查询个贷产品
     * @param id
     * @param customerId
     * @param topFlag
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<BusiProductVO> queryPersonalLoanDetail(Long id, Long customerId, String topFlag, int pageNo, int pageSize) throws Exception;

    /*
     * 理财计划产品列表
     */
    Page<BusiFinancePlanVO> financePlanDetail(int pageNo, int pageSize,String productId) throws Exception;

    /**
     * 产品列表[6周年]
     * @param map
     * @return
     * @throws Exception
     */

    BusiProductListVO queryProductList(Map<String, Object> map) throws Exception;

    /**
     * 产品列表: 更多
     * @param map
     * @return
     */
    Page<BusiProductInfo> queryProductListByType(Map<String, Object> map) throws Exception;

    /**
     * 产品列表[专区产品]
     * @param map
     * @return
     * @throws Exception
     */
    Page<BusiProductInfo> queryStaffProductList(Map<String, Object> map) throws Exception;



    List<BusiProductVo> getProductInfo(BusiProductDto busiProductDto);

    /**
     * 5.0 改版
     * @param paramsMap
     * @return
     */
    Page<BusiProductInfo> getProductListByType(Map<String, Object> paramsMap);

    /**
     * 产品介绍信息
     * @param productType
     * @return
     */
    BusiProductListDTO.Introduction getIntroductionInfo(int productType);

    /**
     * 查询产品信息
     * @param productType
     * @param paramsMap
     * @param flag
     * @return
     */
    List<BusiProductInfo> getProduct(int productType, Map<String, Object> paramsMap, boolean flag);
}

