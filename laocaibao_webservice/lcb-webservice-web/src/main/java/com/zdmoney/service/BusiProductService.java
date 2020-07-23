package com.zdmoney.service;

import com.google.common.collect.Maps;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductInit;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.service.sys.SysSwitchService;
import com.zdmoney.utils.CopyUtil;
import com.zdmoney.vo.BusiFinancePlanVO;
import com.zdmoney.vo.BusiProductVO;
import com.zdmoney.vo.BusiTransferProductVO;
import com.zdmoney.vo.TencentProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BusiProductService extends BaseService<BusiProduct, Long> {

    @Autowired
    private SysSwitchService sysSwitchService;
    @Autowired
    private BusiProductSubMapper busiProductSubMapper;

    private BusiProductMapper getBusiProductMapper() {
        return (BusiProductMapper) baseMapper;
    }

    public List<BusiProduct> selectProductsByAuditFlag(Map<String, Object> map) {
        return getBusiProductMapper().selectProductsByAuditFlag(map);
    }

    public List<BusiProductVO> selectProductDetail(Map<String, Object> map) {
        return getBusiProductMapper().selectProductDetail(map);
    }

    public List<BusiProductInit> selectProductsByIsrecommend(Map<String, Object> map) {
        return getBusiProductMapper().selectProductsByIsrecommend(map);
    }

    public List<BusiProductVO> getOrgProducts(Map<String, Object> map) {
        return getBusiProductMapper().getOrgProducts(map);
    }


    public String queryCustomerStatus(Map<String, Object> map) {
        return getBusiProductMapper().queryCustomerStatus(map);
    }

    public int updateInvestAmt(Map<String, Object> map) {
        return getBusiProductMapper().updateInvestAmt(map);
    }

    public List<BusiProductVO> getPCProducts(Map<String, Object> map) {
        return getBusiProductMapper().getPCProducts(map);
    }

    public BusiProductVO getProductDetailPC(Map<String, Object> map) {
        return getBusiProductMapper().getProductDetailPC(map);
    }

    /**
     * 查询转让产品详情
     *
     * @param map
     * @return
     */
    public List<BusiTransferProductVO> selectTransferProductDetail(Map<String, Object> map) {
        return getBusiProductMapper().selectTransferProductDetail(map);
    }

    /**
     * 查询转让产品详情(理财列表页面)
     *
     * @param map
     * @return
     */
    public List<BusiProductVO> selectIndexTransferProductDetail(Map<String, Object> map) {
        return getBusiProductMapper().selectIndexTransferProductDetail(map);
    }

    /**
     * 查询腾讯产品
     *
     * @param map
     * @return
     */
    public List<TencentProductVo> selectTencentProducts(Map<String, Object> map) {
        return getBusiProductMapper().selectTencentProducts(map);
    }

    /**
     * 查询产品相关动态数据
     *
     * @param map
     * @return
     */
    public List<BusiProductVO> getProductDynamicData(Map<String, Object> map) {
        return getBusiProductMapper().getProductDynamicData(map);
    }

    public BusiProduct findProductById (Map<String, Object> map) {
        return getBusiProductMapper().findProductById(map);
    }

    /*
     *捞财宝消费
     *修改产品余额，订单状态
     */
    @Transactional
    public void consumeProduct(BusiOrderSub order) {
        log.info(">>>>>>>>>>捞财宝更新产品库存开始，订单号：【{}】", order.getId());
        BigDecimal orderAmt = order.getOrderAmt();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("amt", orderAmt);
        map.put("productId", order.getProductId());
        boolean isOverSold = sysSwitchService.getSwitchIsOn("isOverSold");
        if(!OrderConstants.OrderHoldType.HOLD_SPECIAL.equals(order.getHolderType())){
            if (!isOverSold ) {//不可超卖
                map.put("isOverSold", "false");
            }
        }
        //更新主产品
        if(AppConstants.OrderProductType.FINANCE_PLAN_SUB != order.getProductType()){
            int num = getBusiProductMapper().updateInvestAmt(map);
            if (num == 1 ) {
                log.info(">>>>>>>>>>捞财宝更新主产品库存结束，订单号：【{}】", order.getId());
            } else {
                log.info(">>>>>>>>>>捞财宝更新子产品库存失败，订单号：【{}】" + order.getId());
                throw new BusinessException(AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.SELL_OUT));
            }
        }
        //更新子产品
        if(order.getProductId() != 0 ){
            int n = busiProductSubMapper.updateInvestAmt(map);
            if (n == 1) {
                log.info(">>>>>>>>>>捞财宝更新子产品库存结束，订单号：【{}】", order.getId());
            } else {
                log.info(">>>>>>>>>>捞财宝更新子产品库存失败，订单号：【{}】" + order.getId());
                throw new BusinessException(AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.SELL_OUT));
            }
        }

    }

    /**
     * 回退产品库存
     * @param order
     */
    @Transactional
    public void refundProduct(BusiOrderSub order) {
        log.info(">>>>>>>>>>回退产品库存开始，订单号：【{}】，产品ID：【{}】，回退订单金额：【{}】", order.getId(), order.getProductId(), order.getOrderAmt());
        BigDecimal orderAmt = order.getOrderAmt();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("amt", orderAmt);
        map.put("productId", order.getProductId());
        if(AppConstants.OrderProductType.FINANCE_PLAN_SUB !=  order.getProductType()){
            int num = getBusiProductMapper().refundInvestAmt(map);
            if (num == 1) {
                log.info(">>>>>>>>>>回退产品库存结束，订单号：【{}】，产品ID：【{}】，回退订单金额：【{}】", order.getId(), order.getProductId(), order.getOrderAmt());
            } else {
                log.info(">>>>>>>>>>回退产品库存失败，订单号：【{}】，产品ID：【{}】，回退订单金额：【{}】", order.getId(), order.getProductId(), order.getOrderAmt());
                throw new BusinessException("回退产品库存更新操作失败");
            }
        }
        if(order.getProductId() != 0 ){
            int num = busiProductSubMapper.refundInvestAmt(map);
            if (num == 1) {
                log.info(">>>>>>>>>>回退子产品库存结束，订单号：【{}】，产品ID：【{}】，回退订单金额：【{}】", order.getId(), order.getProductId(), order.getOrderAmt());
            } else {
                log.info(">>>>>>>>>>回退子产品库存失败，订单号：【{}】，产品ID：【{}】，回退订单金额：【{}】", order.getId(), order.getProductId(), order.getOrderAmt());
                throw new BusinessException("回退产品库存更新操作失败");
            }
        }
    }

    /**
     * 回退转让产品
     * @param order
     */
    @Transactional
    public void dealWithTransferProduct(BusiOrderSub order) {
        log.info(">>>>>>>>>>回退转让产品开始，订单号：【{}】，产品ID：【{}】", order.getId(), order.getProductId());
        BusiProduct updProduct = new BusiProduct();
        updProduct.setId(order.getProductId());
        updProduct.setShowEndDate(null);
        updProduct.setTotalInvestPerson(0);
        updProduct.setTotalInvestAmt(BigDecimal.ZERO);
        updProduct.setUpLowFlag(AppConstants.ProductUpLowStatus.PRODUCT_UP);
        int num = getBusiProductMapper().updateByPrimaryKeySelective(updProduct);
        if (num == 1) {
            log.info(">>>>>>>>>>回退转让产品结束，订单号：【{}】，产品ID：【{}】", order.getId(), order.getProductId());
        }
        else {
            log.info(">>>>>>>>>>回退转让产品失败，订单号：【{}】，产品ID：【{}】", order.getId(), order.getProductId());
            throw new BusinessException("更新转让产品状态失败");
        }
        BusiProductSub productSub = new BusiProductSub();
        CopyUtil.copyProperties(productSub,updProduct);
        int num2 = busiProductSubMapper.updateByPrimaryKeySelective(productSub);
        if (num2 == 1) {
            log.info(">>>>>>>>>>回退转让子产品结束，订单号：【{}】，产品ID：【{}】", order.getId(), order.getProductId());
        }
        else {
            log.info(">>>>>>>>>>回退转让子产品失败，订单号：【{}】，产品ID：【{}】", order.getId(), order.getProductId());
            throw new BusinessException("更新转让子产品状态失败");
        }
    }


    /**
     * 查询理财计划详情
     *
     * @param map
     * @return
     */
    public List<BusiFinancePlanVO> selectFinancePlanDetail(Map<String, Object> map) {
        return getBusiProductMapper().selectFinancePlanDetail(map);
    }

    public int updateMainSub(Map<String, Object> map){
        getBusiProductMapper().updateMainSub(map);
        return busiProductSubMapper.updateMainSub(map);
    }

    public int insertSubProduct(BusiProductSub busiProductSub){
        return busiProductSubMapper.insertSelective(busiProductSub);
    }

    public int updateSubProduct(BusiProductSub busiProductSub){
        return busiProductSubMapper.updateByPrimaryKeySelective(busiProductSub);
    }

    public List<BusiProductSub> selectProductSub(Map<String, Object> map){
        return busiProductSubMapper.selectProductSub(map);
    }

    public BusiProductSub getBusiProductSubById(Long productId){
        return busiProductSubMapper.selectByPrimaryKey(productId);
    }


    public List<BusiProduct> queryExpiredProduct(){
        return getBusiProductMapper().queryExpiredProduct();
    }

    public List<BusiProductInit> getNewProducts(){return getBusiProductMapper().getNewProducts();}


    /**
     * 根据标的编号查询子产品信息
     */
    public BusiProductSub findProductSubBySubjectNo(String subjectNo){
        return busiProductSubMapper.findProductSubBySubjectNo(subjectNo);
    }
}
