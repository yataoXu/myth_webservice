package com.zdmoney.service.impl.product;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.component.redis.KeyGenerator;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.facade.InnerEmployeeService;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.product.BusiProductLimitMapper;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.models.product.BusiProductLimit;
import com.zdmoney.models.product.BusiProductListDTO;
import com.zdmoney.models.sys.SysNotice;
import com.zdmoney.service.*;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.*;
import com.zdmoney.web.dto.BannerDTO;
import com.zdmoney.webservice.api.dto.ym.BusiProductDto;
import com.zdmoney.webservice.api.dto.ym.vo.BusiProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by 00225181 on 2016/3/16.
 * 产品列表接口
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {


    @Autowired
    BusiProductService busiProductService;
    @Autowired
    ConfigParamBean configParamBean;
    @Autowired
    CustomerMainInfoService customerMainInfoService;
    @Autowired
    BusiProductLimitMapper busiProductLimitMapper;

    @Autowired
    BusiOrderService busiOrderService;

    @Autowired
    BusiProductMapper busiProductMapper;

    @Autowired
    RedisSessionManager redisSessionManager;

    @Autowired
    private SysNoticeService sysNoticeService;

    @Autowired
    private InnerEmployeeService innerEmployeeService;

    @Autowired
    CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private BusiBannerService busiBannerService;

    public Page<BusiProductVO> getProductDetail(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize,String showProductType) throws Exception {
        Page<BusiProductVO> page = new Page<>();
        List<BusiProductVO> busiProductVOList = Lists.newArrayList();

        String productDTO = redisSessionManager.get(KeyGenerator.PRODUCT_LIST.getKey());

        Gson gson = new GsonBuilder().setDateFormat(DateUtil.fullFormat).create();
        // 查询固收产品首页列表
        if ("0".equals(isLoan) || "LCB_Manager_Job".equals(isLoan)) {
            boolean flag = true;
            if (StringUtils.isNotBlank(productDTO)) {
                net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(productDTO);
                try {
                    List<BusiProductVO> proList = gson.fromJson(json.get("results").toString(), new TypeToken<ArrayList<BusiProductVO>>() {}.getType());
                    page.setPageNo((int) json.get("pageNo"));
                    page.setPageSize((int) json.get("pageSize"));
                    page.setResults(proList);
                    page.setTotalPage((int) json.get("totalPage"));
                    flag = false;
                } catch (Exception e) {
                    log.error("500001 parse json exception :", e);
                }
            }
            if (flag) {
                pageNo = 1;
                pageSize = 10;
                //推荐
                Page<BusiProductVO> resultRecommend = products(id, customerId, topFlag, "3", pageNo, pageSize);
                busiProductVOList.addAll(resultRecommend.getResults());
                //优选
                Page<BusiProductVO> resultCommon = products(id, customerId, topFlag, "1", pageNo, pageSize);
                busiProductVOList.addAll(resultCommon.getResults());
                //智选
                Page<BusiProductVO> resultContract = products(id, customerId, topFlag, "2", pageNo, pageSize);
                busiProductVOList.addAll(resultContract.getResults());
                //转让(优选+智选转让产品)
                Page<BusiProductVO> resultTransfer = getIndexTransferProductDetail(id, customerId, topFlag, null, pageNo, pageSize, null, null);
                busiProductVOList.addAll(resultTransfer.getResults());
                page.setPageNo(pageNo);
                page.setPageSize(busiProductVOList.size());
                if (CollectionUtils.isNotEmpty(busiProductVOList)) {
                    page.setTotalRecord(busiProductVOList.size());
                } else {
                    page.setTotalPage(0);
                }
                page.setResults(busiProductVOList);
                try {
                    String productJson = gson.toJson(page);//JSON.toJSONString(page, true);
                    log.info("put productsJson to cache start:" + productJson);
                    redisSessionManager.put(KeyGenerator.PRODUCT_LIST.getKey(), productJson, KeyGenerator.PRODUCT_LIST.getTime(), KeyGenerator.PRODUCT_LIST.getTimeUnit());
                    log.info("put productsJson to cache end:" + productJson);
                } catch (Exception e) {
                    log.error("put productsJson to cache error", e);
                }
            }
        } else {
            // 获取多产品
            if ("4".equals(isLoan)){
                //转让
                page = getIndexTransferProductDetail(id, customerId, topFlag, isLoan, pageNo, pageSize, null, null);
            }else if (StringUtils.isNotBlank(showProductType)){
                //pc端优选,智选,排除转让产品
                if ( "1".equals(showProductType) || "2".equals(showProductType)){
                    page = products(id, customerId, topFlag, isLoan, pageNo, pageSize);
                }
            }else if ("1".equals(isLoan)){//app优选 +转让产品
                //优选
                Page<BusiProductVO> resultCommon = products(id, customerId, topFlag, "1", pageNo, pageSize);
                busiProductVOList.addAll(resultCommon.getResults());
                //优选转让
                Page<BusiProductVO> resultTransfer = getIndexTransferProductDetail(id, customerId, topFlag, "1", pageNo, pageSize, null, null);
                busiProductVOList.addAll(resultTransfer.getResults());

                page.setPageNo(pageNo);
                page.setPageSize(busiProductVOList.size());
                page.setResults(busiProductVOList);

                if (CollectionUtils.isNotEmpty(busiProductVOList)){
                    page.setTotalRecord(busiProductVOList.size());
                    page.setTotalPage(page.getTotalPage());
                }
            }else if ("2".equals(isLoan)){//app智选列表 +转让产品
                //智选
                Page<BusiProductVO> resultContract = products(id, customerId, topFlag, "2", pageNo, pageSize);
                busiProductVOList.addAll(resultContract.getResults());
                //智选转让
                Page<BusiProductVO> resultTransfer = getIndexTransferProductDetail(id, customerId, topFlag, "2", pageNo, pageSize, null, null);
                busiProductVOList.addAll(resultTransfer.getResults());
                page.setPageNo(pageNo);
                page.setPageSize(busiProductVOList.size());
                page.setResults(busiProductVOList);

                if (CollectionUtils.isNotEmpty(busiProductVOList)){
                    page.setTotalRecord(busiProductVOList.size());
                    page.setTotalPage(page.getTotalPage());
                }
            }else {
                page = products(id, customerId, topFlag, isLoan, pageNo, pageSize);
            }

        }
        List<BusiProductVO> redisProductVO = page.getResults();

        StringBuilder ids = new StringBuilder();
        List<BusiProductVO> productDynamicData = new ArrayList<>();
        for (BusiProductVO redisVO : redisProductVO) {
            ids.append(redisVO.getId()).append(",");
        }
        if (StringUtils.isNotBlank(ids.toString())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ids", ids.toString().substring(0, ids.length() - 1).split(","));
            paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            productDynamicData = busiProductService.getProductDynamicData(paramMap);
        }

        for (BusiProductVO redisVO : redisProductVO) {
            for (BusiProductVO dynamicVO : productDynamicData) {
                if (redisVO.getId().equals(dynamicVO.getId())) {
                    redisVO.setRemaindAmt(dynamicVO.getRemaindAmt());
                    redisVO.setNowProportion(dynamicVO.getNowProportion());
                    redisVO.setRemaindTime(dynamicVO.getRemaindTime());
                    redisVO.setRemaindSaleStartTime(dynamicVO.getRemaindSaleStartTime());
                    redisVO.setSellOut(dynamicVO.getSellOut());
                    redisVO.setHotSellFlag(dynamicVO.getHotSellFlag());
                    redisVO.setLeftReservatTime(dynamicVO.getLeftReservatTime());
                }
            }
        }
        return page;
    }

    public Page<BusiTransferProductVO> getTransferProductDetail(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize, String productId, String upLowFlag) throws Exception {
        Page<BusiTransferProductVO> page = new Page<>();
        List<BusiTransferProductVO> vos = Lists.newArrayList();
        page.setPageNo(pageNo);
        page.setPageSize(vos.size());
        page = transferProducts(id, customerId, topFlag, isLoan, pageNo, pageSize, productId, upLowFlag);
        return page;
    }

    /*
     * 转让产品列表（理财列表页）
     */
    public Page<BusiProductVO> getIndexTransferProductDetail(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize, String productId, String upLowFlag) throws Exception {
        Page<BusiProductVO> page = new Page<>();
        List<BusiProductVO> vos = Lists.newArrayList();
        page.setPageNo(pageNo);
        page.setPageSize(vos.size());
        page = indexTransferProducts(id, customerId, topFlag, isLoan, pageNo, pageSize, productId, upLowFlag);
        return page;
    }

    @Override
    public BusiProductVO getChannelProductDetail(String channel) throws Exception {
        Page<BusiProductVO> productDetail = products(null, null, null, "1", channel, 1, 1);
        if (productDetail.getTotalRecord() > 0) {
            return productDetail.getResults().get(0);
        } else {
            return null;
        }
    }

    private Page<BusiProductVO> products(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize) throws Exception {
        return products(id, customerId, topFlag, isLoan, null, pageNo, pageSize);
    }

    private Page<BusiTransferProductVO> transferProducts(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize, String productId, String upLowFlag) throws Exception {
        return transferProducts(topFlag, isLoan, pageNo, pageSize, productId, upLowFlag);
    }

    private Page<BusiProductVO> indexTransferProducts(Long id, Long customerId, String topFlag, String isLoan, int pageNo, int pageSize, String productId, String upLowFlag) throws Exception {
        return indexTransferProducts(topFlag, isLoan, pageNo, pageSize, productId, upLowFlag);
    }

    private Page<BusiProductVO> products(Long id, Long customerId, String topFlag, String isLoan, String productChannel, int pageNo, int pageSize) throws Exception {
        Page<BusiProductVO> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        //优选
        if ("1".equals(isLoan)) {
//            map.put("contract", "no");
            map.put("recommend", "no");
            map.put("subjectType", "1");
        }
        //智选
        if ("2".equals(isLoan)) {
//            map.put("contract", "yes");
            map.put("recommend", "no");
//            map.put("personalLoan", 0);
            map.put("subjectType", "2");
        }
        //推荐
        if ("3".equals(isLoan)) {
            map.put("recommend", "yes");
        }
        if (id != null) {
            map.put("id", id);
        }
        if (StringUtils.isNotEmpty(topFlag)) {
            map.put("topFlag", topFlag);
        }
        if (StringUtils.isNotEmpty(productChannel)) {
            map.put("productChannel", productChannel);
            map.put("limit", "5");//只取渠道产品
        } else {
            map.put("limit", "0");//默认不限购
        }
        List<BusiProductVO> busis = Lists.newArrayList();
        if ("1".equals(topFlag)) {//置顶的产品不查新手标
            map.put("newHand", "0");//置顶，只查非新手标
        } else {
            map.put("newHand", "2");//新手标
        }
        if (customerId != null) {//用户邀请码是否为合作商户
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(customerId);
            if (customerMainInfo != null) {
                String inviteCode = customerMainInfo.getCmIntroduceCode();
                if (!StringUtils.isEmpty(inviteCode)) {
                    boolean isOrgan = customerMainInfoService.isOrganCustomer(inviteCode);
                    if (isOrgan) {//合作商户的邀请码
                        map.put("orgInviteCode", inviteCode);
                    }
                }
                if (customerMainInfo.getLimitType() != null) {
                    BusiProductLimit productLimit = busiProductLimitMapper.selectByPrimaryKey(customerMainInfo.getLimitType());
                    Date limitLastDate = customerMainInfo.getLimitLastDate();
                    Date nowDate = new Date();
                    if (productLimit.getId().equals(customerMainInfo.getLimitType()) && limitLastDate != null && limitLastDate.after(nowDate)) {
                        //在限购期内，不能再次购买，限购
                        map.put("limit", "1");
                    }
                }
                if (!StringUtils.isEmpty(customerMainInfo.getOpenId())) {//绑定过微信
                    if (AppConstants.BuyWechatStatus.BUY.equals(customerMainInfo.getBuyWechat())) {
                        map.put("buyWeichat", "yes");
                    }
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        map.put("sDate", sdf.format(new Date()));
        busis.addAll(busiProductService.selectProductDetail(map));
        for (BusiProductVO vo : busis) {
            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500016&resultPage=front/app/itemDetails&productId=" + vo.getId();
            vo.setProductDetailUrl(detailUrl);
            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
        }
        page.setResults(busis);
        return page;
    }

    private Page<BusiTransferProductVO> transferProducts(String topFlag, String isLoan, int pageNo, int pageSize, String productId, String upLowFlag) throws Exception {
        Page<BusiTransferProductVO> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        if (StringUtils.isNotEmpty(topFlag)) {
            map.put("topFlag", topFlag);
        }

        if (StringUtils.isNotEmpty(productId)) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
            paramsMap.put("productId", productId);
            BusiProduct bp = busiProductService.findProductById(paramsMap);
            if (bp == null) {
                throw new BusinessException("该产品Id不存在！");
            }
            map.put("productId", productId);
            map.put("upLowFlag", 1);
        }
        if (StringUtils.isNotEmpty(upLowFlag)) {
            map.put("upLowFlag", 1);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        map.put("sDate", sdf.format(new Date()));

        List<BusiTransferProductVO> busis = busiProductService.selectTransferProductDetail(map);
        for (BusiTransferProductVO vo : busis) {
            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500036&resultPage=front/app/transferDetails&productId=" + vo.getId();
            vo.setProductDetailUrl(detailUrl);
            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
            vo.setWelfare(AppConstants.PRODUCT_NO_WELFARE);
        }
        page.setResults(busis);
        return page;
    }

    private Page<BusiProductVO> indexTransferProducts(String topFlag, String isLoan, int pageNo, int pageSize, String productId, String upLowFlag) throws Exception {
        Page<BusiProductVO> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        if (StringUtils.isNotEmpty(topFlag)) {
            map.put("topFlag", topFlag);
        }

        if (StringUtils.isNotEmpty(productId)) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
            paramsMap.put("productId", productId);
            BusiProduct bp = busiProductService.findProductById(paramsMap);
            if (bp == null) {
                throw new BusinessException("该产品Id不存在！");
            }
            map.put("productId", productId);
            map.put("upLowFlag", 1);
        }
        if (StringUtils.isNotEmpty(upLowFlag)) {
            map.put("upLowFlag", 1);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        map.put("sDate", sdf.format(new Date()));
        List<BusiProductVO> busis = busiProductService.selectIndexTransferProductDetail(map);
        List<BusiProductVO> busiProductVOList=Lists.newArrayList();
        for (BusiProductVO vo : busis) {
            if ("1".equals(isLoan) && "1".equals(vo.getContractTypeDict()) && vo.getPersonLoan().intValue()!=1){//优选转让
                String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500036&resultPage=front/app/transferDetails&productId=" + vo.getId();
                vo.setProductDetailUrl(detailUrl);
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
                busiProductVOList.add(vo);
            }
            if ("2".equals(isLoan) && "2".equals(vo.getContractTypeDict()) &&  vo.getPersonLoan().intValue()!=1){//智选转让
                String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500036&resultPage=front/app/transferDetails&productId=" + vo.getId();
                vo.setProductDetailUrl(detailUrl);
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
                busiProductVOList.add(vo);
            }
            if (StringUtils.isBlank(isLoan)){  //固收产品不显示个贷转让产品
                if (vo.getPersonLoan().intValue()!=1){
                    String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500036&resultPage=front/app/transferDetails&productId=" + vo.getId();
                    vo.setProductDetailUrl(detailUrl);
                    vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
                    busiProductVOList.add(vo);
                }
            }
            if ("personLoan".equals(isLoan) && vo.getPersonLoan().intValue()==1){//个贷转让产品
                String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500036&resultPage=front/app/transferDetails&productId=" + vo.getId();
                vo.setProductDetailUrl(detailUrl);
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
                busiProductVOList.add(vo);
            }
        }
        page.setResults(busiProductVOList);
        return page;
    }

    @Override
    public List<TencentProductVo> selectTencentProducts(String sessionToken, String channel, Long productId, String isNewHand) throws Exception {
        return queryTencentProducts(sessionToken, channel, productId, isNewHand);
    }

    private List<TencentProductVo> queryTencentProducts(String sessionToken, String productChannel, Long productId, String isNewHand) throws Exception {
        Map<String, Object> map = Maps.newTreeMap();
        map.put("productId", productId);
        map.put("isNewHand", isNewHand);
        map.put("productChannel", productChannel);
        List<TencentProductVo> busis = Lists.newArrayList();
        CustomerMainInfo customerMainInfo = null;
        if (StringUtils.isNotBlank(sessionToken)) {//用户邀请码是否为合作商户
            String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
            customerMainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            if (customerMainInfo != null) {
                String inviteCode = customerMainInfo.getCmIntroduceCode();
                if (!StringUtils.isEmpty(inviteCode)) {
                    boolean isOrgan = customerMainInfoService.isOrganCustomer(inviteCode);
                    if (isOrgan) {//合作商户的邀请码
                        map.put("orgInviteCode", inviteCode);
                    }
                }
                if (customerMainInfo.getLimitType() != null) {
                    BusiProductLimit productLimit = busiProductLimitMapper.selectByPrimaryKey(customerMainInfo.getLimitType());
                    Date limitLastDate = customerMainInfo.getLimitLastDate();
                    Date nowDate = new Date();
                    if (productLimit.getId().equals(customerMainInfo.getLimitType()) && limitLastDate != null && limitLastDate.after(nowDate)) {
                        //在限购期内，不能再次购买，限购
                        map.put("limit", "1");
                    }
                }
                if (!StringUtils.isEmpty(customerMainInfo.getOpenId())) {//绑定过微信
                    if (AppConstants.BuyWechatStatus.BUY.equals(customerMainInfo.getBuyWechat())) {
                        map.put("buyWeichat", "yes");
                    }
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("sDate", sdf.format(new Date()));
        busis = busiProductService.selectTencentProducts(map);
        for (TencentProductVo vo : busis) {
            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500016&resultPage=front/app/itemDetails&productId=" + vo.getId();
            vo.setProductDetailUrl(detailUrl);
            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
            vo.setIsBuy("N");
            if (customerMainInfo != null) {
                Map queryMap = Maps.newTreeMap();
                queryMap.put("customerId", customerMainInfo.getId());
                queryMap.put("productId", vo.getId());
                List<BusiOrder> orderList = busiOrderService.selectProductOrders(queryMap);
                if (!orderList.isEmpty()) {
                    vo.setIsBuy("Y");
                }
                queryMap = Maps.newTreeMap();
                queryMap.put("customerId", customerMainInfo.getId());
                //判断是否投资过产品
                List<BusiOrder> orderInfo = busiOrderService.selectOrderViewByProperty(queryMap);
                if (CollectionUtils.isNotEmpty(orderInfo)) {
                    vo.setIsNew("N");
                } else {
                    vo.setIsNew("Y");
                }
//                CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(customerMainInfo.getId());
//                if (mainInfo != null && vo.getLimitType()==4) {
//                    if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(vo.getIsTransfer())) {
//                        Long limitTpye = vo.getLimitType();
//                        BusiProductLimit busiProductLimit = busiProductLimitMapper.selectByPrimaryKey(limitTpye);
//                        if (busiProductLimit != null) {
//                            //查询红包
//                             List<PacketDTO> dtos = getRedPacket(mainInfo.getCmNumber());
//                             vo.setPacketDTOs(dtos);
//                        }
//                    }
//
//                }
            }
        }
        return busis;
    }

    @Override
    public BusiBorrowerInfoVO queryBorrowerInfo(Long productId) throws Exception {
        return busiProductMapper.queryBorrowerInfo(productId);
    }

    @Override
    public Page<InvestRecordVO> queryInvestRecord(Long productId, int pageNo, int pageSize) throws Exception {
        Page<InvestRecordVO> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("productId", productId);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);

        List<InvestRecordVO> investList = busiProductMapper.queryInvestRecord(map);
        page.setResults(investList);
        return page;
    }

    @Override
    public Page<BusiProductVO> queryPersonalLoanDetail(Long id, Long customerId, String topFlag, int pageNo, int pageSize) throws Exception {
        Page<BusiProductVO> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        // 个贷
        map.put("personalLoan", 1);
        map.put("contract", "yes");
        map.put("recommend", "no");

        if (id != null) {
            map.put("id", id);
        }
        if (StringUtils.isNotEmpty(topFlag)) {
            map.put("topFlag", topFlag);
        }
        map.put("limit", "0");//默认不限购
        List<BusiProductVO> busis = Lists.newArrayList();
        if ("1".equals(topFlag)) {//置顶的产品不查新手标
            map.put("newHand", "0");//置顶，只查非新手标
        } else {
            map.put("newHand", "2");//新手标
        }
        if (customerId != null) {//用户邀请码是否为合作商户
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(customerId);
            if (customerMainInfo != null) {
                String inviteCode = customerMainInfo.getCmIntroduceCode();
                if (!StringUtils.isEmpty(inviteCode)) {
                    boolean isOrgan = customerMainInfoService.isOrganCustomer(inviteCode);
                    if (isOrgan) {//合作商户的邀请码
                        map.put("orgInviteCode", inviteCode);
                    }
                }
                if (customerMainInfo.getLimitType() != null) {
                    BusiProductLimit productLimit = busiProductLimitMapper.selectByPrimaryKey(customerMainInfo.getLimitType());
                    Date limitLastDate = customerMainInfo.getLimitLastDate();
                    Date nowDate = new Date();
                    if (productLimit.getId().equals(customerMainInfo.getLimitType()) && limitLastDate != null && limitLastDate.after(nowDate)) {
                        //在限购期内，不能再次购买，限购
                        map.put("limit", "1");
                    }
                }
                if (!StringUtils.isEmpty(customerMainInfo.getOpenId())) {//绑定过微信
                    if (AppConstants.BuyWechatStatus.BUY.equals(customerMainInfo.getBuyWechat())) {
                        map.put("buyWeichat", "yes");
                    }
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        map.put("sDate", sdf.format(new Date()));
        busis.addAll(busiProductService.selectProductDetail(map));
        //个贷转让产品
        Page<BusiProductVO> resultTransfer = getIndexTransferProductDetail(id, customerId, topFlag, "personLoan", pageNo, pageSize, null, null);
        busis.addAll(resultTransfer.getResults());

        StringBuilder ids = new StringBuilder();
        List<BusiProductVO> productDynamicData = new ArrayList<>();
        for (BusiProductVO pro : busis) {
            ids.append(pro.getId()).append(",");
        }
        if (StringUtils.isNotBlank(ids.toString())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ids", ids.toString().substring(0, ids.length() - 1).split(","));
            paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            productDynamicData = busiProductService.getProductDynamicData(paramMap);
        }

        for (BusiProductVO vo : busis) {
            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500016&resultPage=front/app/itemDetails&productId=" + vo.getId();
            vo.setProductDetailUrl(detailUrl);
            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());

            if (CollectionUtils.isNotEmpty(productDynamicData)) {
                for (BusiProductVO dynamicVO : productDynamicData) {
                    if (vo.getId().equals(dynamicVO.getId())) {
                        vo.setRemaindAmt(dynamicVO.getRemaindAmt());
                        vo.setNowProportion(dynamicVO.getNowProportion());
                        vo.setRemaindTime(dynamicVO.getRemaindTime());
                        vo.setRemaindSaleStartTime(dynamicVO.getRemaindSaleStartTime());
                        vo.setSellOut(dynamicVO.getSellOut());
                        vo.setHotSellFlag(dynamicVO.getHotSellFlag());
                        vo.setLeftReservatTime(dynamicVO.getLeftReservatTime());
                    }
                }
            }
        }
        page.setResults(busis);
        return page;
    }

    public Page<BusiFinancePlanVO> financePlanDetail(int pageNo, int pageSize, String productId) throws Exception {
        Page<BusiFinancePlanVO> page = new Page<>();
        List<BusiFinancePlanVO> vos = Lists.newArrayList();
        page.setPageNo(pageNo);
        page.setPageSize(vos.size());
        page = getFinancePlanDetails(pageNo, pageSize, productId);
        return page;
    }

    private Page<BusiFinancePlanVO> getFinancePlanDetails(int pageNo, int pageSize, String productId) throws Exception {
        Page<BusiFinancePlanVO> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        if (StringUtils.isNotEmpty(productId)) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
            paramsMap.put("productId", productId);
            BusiProduct bp = busiProductService.findProductById(paramsMap);
            if (bp == null) {
                throw new BusinessException("该产品Id不存在！");
            }
            map.put("productId", productId);
            map.put("upLowFlag", 1);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        map.put("sDate", sdf.format(new Date()));

        List<BusiFinancePlanVO> busis = busiProductService.selectFinancePlanDetail(map);
        for (BusiFinancePlanVO vo : busis) {
            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500016&resultPage=front/app/itemDetails&productId=" + vo.getId();
            vo.setProductDetailUrl(detailUrl);
            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
            vo.setInvestPeriod(vo.getCloseDay());
        }
        page.setResults(busis);
        return page;
    }

    @Override
    public Page<BusiProductInfo> queryStaffProductList(Map<String, Object> map) throws Exception {
        List<BusiProductInfo> staffList = Lists.newArrayList();// 专区产品
        map.put("sDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Page<BusiProductInfo> page = new Page<>();
        List<BusiProductInfo> tempList = new ArrayList<>();
        int pageNo = Convert.toInt(map.get("pageNo"));
        int pageSize = 10;
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        staffList= busiProductMapper.queryStaffProductList(map);

        for(BusiProductInfo pro:staffList) {
            pro.setInvestPeriod(pro.getCloseDay());// 理财计划的投资期限为封闭期
            // 详情页URL
            StringBuilder detailUrl = new StringBuilder(configParamBean.getTouchProductDetailsUrl());
            detailUrl.append("/productDetails?productId=").append(pro.getId());
            pro.setProductDetailUrl(detailUrl.toString());
            pro.setCountdown(computeSecond(pro));
            pro.setRemaindAmt(new BigDecimal(pro.getRemaindAmt()).toString());
        }

        int size = staffList.size();
        int fromIndex = pageSize * (pageNo - 1);
        int toIndex = fromIndex + pageSize;
        if (toIndex >= size) {
            toIndex = size;
        }
        if (fromIndex < size) {
            tempList = staffList.subList(fromIndex, toIndex);
        }
        page.setTotalRecord(staffList.size());
        page.setResults(tempList);
        return page;
    }


    @Override
    public BusiProductListVO queryProductList(Map<String, Object> map) throws Exception {
        BusiProductListVO busiProductVO = new BusiProductListVO();
        BusiProductInfo hotProduct = null;// 热销推荐
        List<BusiProductInfo> productInfoList = null;
        List<BusiProductInfo> timeDepositList = new ArrayList<>();// 定期产品
        List<BusiProductInfo> personalLoanList = new ArrayList<>();// 个贷产品(散标)
        List<BusiProductInfo> financialPlanList = new ArrayList<>();// 理财计划(智投保)
        List<BusiProductInfo> transferList = new ArrayList<>();// 转让产品
        List<BusiProductInfo> newHandList = new ArrayList<>();// 新手专享产品


        //获取用户级别
        String customerId = map.get("customerId")==null?"":map.get("customerId").toString();
        String userLabel="";
        CustomerMainInfo customerMainInfo = null;
        if (StringUtils.isNotBlank(customerId)){//已登录用户
            customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
            userLabel = customerMainInfo.getUserLabel();
        }
        map.put("userLabel", userLabel);

        String productType = map.get("productType").toString();
        map.put("sDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 查询所有审核上架产品
        if ("0".equals(productType) || "5".equals(productType)) {
            // 为了不占用过多的数据库连接, 这里一次性查出最近一个月内所有定期, 个贷, 理财计划产品
            productInfoList = busiProductMapper.queryProductList(map);
            // 查询转让产品
            productInfoList.addAll(busiProductMapper.queryTransferProductList(map));
        } else if ("4".equals(productType)) {
            productInfoList = busiProductMapper.queryTransferProductList(map);
        } else {
            productInfoList = busiProductMapper.queryProductList(map);
        }
        // 获取公告, 缓存1小时
        String notice = redisSessionManager.get("PRO_NOTICE");
        if (StringUtils.isBlank(notice)) {
            Map<String, Object> requestMap = Maps.newTreeMap();
            requestMap.put("status", 1); // 启用
            requestMap.put("auditStatus", 1); // 审核要通过的
            requestMap.put("noticeType", 2); // 理财页公告
            List<SysNotice> noticeList = sysNoticeService.getSysNoticeList(requestMap);
            if (CollectionUtils.isNotEmpty(noticeList)) {
                SysNotice sysNotice = noticeList.get(0);
                busiProductVO.setNotice(sysNotice.getContent());
                redisSessionManager.put(KeyGenerator.PRODUCT_NOTICE.getKey(), sysNotice.getContent(),
                        KeyGenerator.PRODUCT_NOTICE.getTime(), KeyGenerator.PRODUCT_NOTICE.getTimeUnit());
            }
        } else {
            busiProductVO.setNotice(notice);
        }
        // 产品分类
        for (BusiProductInfo pro : productInfoList) {
            // 判断是否是新手标产品
            if ("1".equals(pro.getNewHand()) && 2 == pro.getProductTag()) {
                if (4 == pro.getSubjectType()){
                    pro.setInvestPeriod(pro.getCloseDay());// 理财计划的投资期限为封闭期
                }
                newHandList.add(pro);
            } else if (pro.getIsArea() == 1){
                hotProduct = pro;
            } else if (1 == pro.getIsTransfer() && 6 == pro.getProductTag()){
                transferList.add(pro);
            } else if (1 == pro.getSubjectType() || 2 == pro.getSubjectType()){
                timeDepositList.add(pro);
            } else if (3 == pro.getSubjectType()){
                personalLoanList.add(pro);
            } else if (4 == pro.getSubjectType()){
                pro.setInvestPeriod(pro.getCloseDay());// 理财计划的投资期限为封闭期
                financialPlanList.add(pro);
            }
            // 详情页URL
            StringBuilder detailUrl = new StringBuilder(configParamBean.getTouchProductDetailsUrl());
            if (6 == pro.getProductTag() && 1 == pro.getIsTransfer()) {
                detailUrl.append("/transferDetails?productId=").append(pro.getId());
            } else {
                detailUrl.append("/productDetails?productId=").append(pro.getId());
            }
            pro.setProductDetailUrl(detailUrl.toString());
            pro.setCountdown(computeSecond(pro));
            pro.setRemaindAmt(new BigDecimal(pro.getRemaindAmt()).toString());
        }
        // 如果购买过新手标产品, 则清空不显示
        if (customerMainInfo != null && customerMainInfo.getIsConsumed() == 1) {
            newHandList.clear();
        }
        // 各类型商品上架总数
        busiProductVO.setTimeDepositCount(timeDepositList.size());
        busiProductVO.setPersonalLoanCount(personalLoanList.size());
        busiProductVO.setFinancialPlanCount(financialPlanList.size());
        busiProductVO.setTransferCount(transferList.size());
        busiProductVO.setNewHandCount(newHandList.size());
        busiProductVO.setHotProduct(hotProduct);

        busiProductVO.setTimeDeposit(productfilter(timeDepositList, productType));
        busiProductVO.setPersonalLoan(productfilter(personalLoanList, productType));
        busiProductVO.setFinancialPlan(productfilter(financialPlanList, productType));
        busiProductVO.setTransfer(productfilter(transferList, productType));
        busiProductVO.setNewHand(productfilter(newHandList, productType));


        return busiProductVO;
    }

    /**
     * 数据过滤
     * @param productList
     * @param productType
     * @return
     */
    private List<BusiProductInfo> productfilter(List<BusiProductInfo> productList, String productType){
        if (CollectionUtils.isEmpty(productList)) return null;
        List<BusiProductInfo> proList = new ArrayList<>();
        List<BusiProductInfo> tempList = new ArrayList<>();
        // 列表的每种类型产品最多只展示5条数据
        if (productList.size() > 4 && "0".equals(productType)) proList = productList.subList(0, 5);
        else proList = productList;
        if ("0".equals(productType)) {
            for (BusiProductInfo pro : proList) {
                // 不显示结售和售罄的产品
                int sell = pro.getSellOut();
                if (sell == 1 || sell == 3) {
                    tempList.add(pro);
                }
            }
            proList.removeAll(tempList);
        }
        return proList;
    }

    /**
     * 计算预约产品倒计时
     * @param productInfo
     * @return
     */
    private Long computeSecond(BusiProductInfo productInfo){
        Date reservatTime = productInfo.getReservatTime();
        Date saleStartDate = productInfo.getSaleStartDate();
        Date currentDate = new Date();
        Date saleEndDate = productInfo.getSaleEndDate();
        BigDecimal principal = productInfo.getProductPrincipal();// 项目本金
        BigDecimal investAmt = productInfo.getTotalInvestAmt();// 投资总金额
        Long res = 0L;
        if (reservatTime != null && saleStartDate != null) {
            // 预约产品，起售时间>=当前时间>=预约购买时间 >=结售时间，投资总金额=项目本金， 不显示倒计时
            if (saleStartDate.getTime() >= currentDate.getTime() || currentDate.getTime() >= reservatTime.getTime()
                    || reservatTime.getTime() >= saleEndDate.getTime() || principal.compareTo(investAmt) == 0) {
                res = 0L;
            } else {
                Long surplus = (reservatTime.getTime() - currentDate.getTime()) / 1000;
                res = (surplus <= 0) ? 0 : surplus;
            }
        }
        return res;
    }

    @Override
    public Page<BusiProductInfo> queryProductListByType(Map<String, Object> paramsMap) throws Exception {
        int type = Convert.toInt(paramsMap.get("productType"));
        int pageNo = Convert.toInt(paramsMap.get("pageNo"));
        Long customerId = Convert.toLong(paramsMap.get("customerId"));
        Page<BusiProductInfo> page = new Page<>();
        List<BusiProductInfo> productInfoList = new ArrayList<>();
        int pageSize = 10;
        Map<String, Object> map = new HashMap<>();
        map.put("productType", type);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        BusiProductListVO productVo = null;
        String userLabel="";
        List<BusiProductInfo> tempList = new ArrayList<>();
        if (customerId!=null){
            CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(customerId);
            userLabel = customerMainInfo.getUserLabel();
        }
        map.put("userLabel", userLabel);
        map.put("customerId", customerId);
        map.put("rateSort", paramsMap.get("rateSort"));
        map.put("termSort", paramsMap.get("termSort"));

        if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_5 == type) {
            productVo = this.queryProductList(map);
            productInfoList.addAll(CollectionUtils.isNotEmpty(productVo.getFinancialPlan()) ? productVo.getFinancialPlan() : tempList);
            productInfoList.addAll(CollectionUtils.isNotEmpty(productVo.getTimeDeposit()) ? productVo.getTimeDeposit() : tempList);
            productInfoList.addAll(CollectionUtils.isNotEmpty(productVo.getPersonalLoan()) ? productVo.getPersonalLoan() : tempList);
            productInfoList.addAll(CollectionUtils.isNotEmpty(productVo.getTransfer()) ? productVo.getTransfer() : tempList);
            productInfoList.addAll(CollectionUtils.isNotEmpty(productVo.getNewHand()) ? productVo.getNewHand() : tempList);

            List<BusiProductInfo> closeProductList = new ArrayList<>();
            List<BusiProductInfo> opendProductList = new ArrayList<>();
            if (productInfoList != null && CollectionUtils.isNotEmpty(productInfoList)) {
                for (int i = 0; i < productInfoList.size(); i++) {
                    BusiProductInfo busiProductInfo = productInfoList.get(i);
                    if (busiProductInfo != null&&(busiProductInfo.getSellOut() == 0 || busiProductInfo.getSellOut() == 2)) {
                        opendProductList.add(busiProductInfo);
                    } else {
                        closeProductList.add(busiProductInfo);
                    }
                }
                productInfoList.clear();
            }
            productInfoList.addAll(opendProductList);
            productInfoList.addAll(closeProductList);
//            Collections.sort(productInfoList, new Comparator<BusiProductInfo>() {
//                @Override
//                public int compare(BusiProductInfo o1, BusiProductInfo o2) {
//                    return o1.getSellOut() - o2.getSellOut();
//                }
//            });

            int size = productInfoList.size();
            int fromIndex = pageSize * (pageNo - 1);
            int toIndex = fromIndex + pageSize;
            if (toIndex >= size) {
                toIndex = size;
            }
            if (productVo.getHotProduct() != null) {
                productInfoList.add(0, productVo.getHotProduct());
            }
            if (fromIndex < size) {
                tempList = productInfoList.subList(fromIndex, toIndex);
            }
            page.setTotalRecord(productInfoList.size());
            page.setResults(tempList);
        } else {
            map.put("page", page);
            productVo = this.queryProductList(map);
            if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_1 == type) productInfoList = productVo.getTimeDeposit();
            if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_2 == type) productInfoList = productVo.getPersonalLoan();
            if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_3 == type) productInfoList = productVo.getFinancialPlan();
            if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_4 == type) productInfoList = productVo.getTransfer();
            if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_6 == type) productInfoList = productVo.getNewHand();
            page.setResults(productInfoList);
        }
        return page;
    }

    @Override
    public List<BusiProductVo> getProductInfo(BusiProductDto busiProductDto) {
        PageHelper.startPage(busiProductDto.getPageNo(),busiProductDto.getPageSize());
        return busiProductMapper.getProductInfo(busiProductDto);
    }

    /**
     * 针对产品类型, 用户标签, 第一页默认排序 缓存1分钟. 产品是否购买, 剩余可投金额为实时查询
     *
     * 默认排序规则
     *     散标&智投宝
     *          1, 新手标(如果有)无论是否可购买永远展示在最顶端
     *          2, 是否可买优先级[未售罄, 未起售, 已售罄]
     *          3, 产品优先级[挖财, 其他]
     *          4, 利率(包含加息利率)
     *     转让产品
     *          1, 是否可买优先级[未售罄, 未起售, 已售罄]
     *          2, 转让后年利率
     *          3, 发布日期
     * 年利率&期限排序规则
     *     1, 是否可买优先级[未售罄, 未起售, 已售罄]
     *     2, 具体年利率&期限升序或降序
     *
     * @param
     *      productType 2: 个贷(散标) 3: 理财计划(智投保) 4: 转让产品
     *      pageSize 首页个性推荐为2
     * @return Page<BusiProductInfo>
     */
    @Override
    public Page<BusiProductInfo> getProductListByType(Map<String, Object> paramsMap) {
        Page<BusiProductInfo> page = new Page<>();
        List<BusiProductInfo> productInfoList = new ArrayList<>();
        int productType = Convert.toInt(paramsMap.get("productType"));
        Long customerId = Convert.toLong(paramsMap.get("customerId"));
        int pageNo = Convert.toInt(paramsMap.get("pageNo"));
        String pageSize = Convert.toStr(paramsMap.get("pageSize"));

        Map<String, Object> map = new HashMap<>();
        map.put("productType", productType);
        page.setPageNo(pageNo);
        page.setPageSize(StringUtils.isEmpty(pageSize) ? ParamConstant.PAGE_SIZE : Convert.toInt(pageSize));
        String userLabel = "";
        CustomerMainInfo customerMainInfo = null;
        boolean flag = false;
        if (customerId != null){
            customerMainInfo = customerMainInfoService.checkCustomerId(customerId);
            userLabel = customerMainInfo.getUserLabel();
            if (customerMainInfo.getIsConsumed() == 1 && !"1".equals(customerMainInfo.getUserLabel())) flag = true;
        }
        map.put("userLabel", userLabel);
        map.put("rateSort", paramsMap.get("rateSort"));
        map.put("termSort", paramsMap.get("termSort"));
        map.put("sDate", DateUtil.getDateFormatString(new Date(), DateUtil.fullFormat));
        map.put("page", page);

        if (pageNo == 1 && paramsMap.get("rateSort") == null && paramsMap.get("termSort") == null) {
            String key = "pro:" + productType + ":label:" + userLabel + ":no:" + pageNo;
            String value = redisSessionManager.get(key);
            if (StringUtils.isNotEmpty(value)) {
                page = JSON.parseObject(value, new TypeReference<Page<BusiProductInfo>>(){});
                List<Long> proIds = page.getResults().stream().map(BusiProductInfo :: getId).collect(Collectors.toList());
                List<BusiProductInfo> realTimeProductList = busiProductMapper.queryRealTimeProductData(proIds);
                page.getResults().stream().forEach(pro -> realTimeProductList.stream().forEach(rtb -> {
                    if (pro.getId().equals(rtb.getId())) {
                        pro.setSellOut(rtb.getSellOut());
                        pro.setRemaindAmt(rtb.getRemaindAmt());
                    }
                }));
            } else {
                productInfoList = getProduct(productType, map, flag);
                page.setResults(productInfoList);
                if (CollectionUtils.isNotEmpty(productInfoList)) {
                    redisSessionManager.put(key, JSON.toJSONString(page), 1, TimeUnit.MINUTES);
                }
            }
        } else {
            productInfoList = getProduct(productType, map, flag);
            page.setResults(productInfoList);
        }
        return page;
    }

    @Override
    public List<BusiProductInfo> getProduct(int productType, Map<String, Object> paramsMap, boolean flag){
        List<BusiProductInfo> productInfoList = new ArrayList<>();
        if (AppConstants.PRODUCT_TYPE.PRODUCT_TYPE_4 == productType) {
            productInfoList = busiProductMapper.queryTransferProductList(paramsMap);
        } else {
            productInfoList = busiProductMapper.queryProductList(paramsMap);
        }

        productInfoList.stream().forEach(pro -> {
            pro.setProductDetailUrl(new StringBuilder(configParamBean.getTouchProductDetailsUrl()).append((6 == pro.getProductTag() && 1 == pro.getIsTransfer()) ? "/transferDetails" : "/productDetails")
                    .append("?productId=").append(pro.getId()).toString());
        });
        if (flag) {
            productInfoList.removeAll(productInfoList.stream()
                    .filter(pro -> ("1".equals(pro.getNewHand()) && 2 == pro.getProductTag()))
                    .collect(Collectors.toList()));
        }
        return productInfoList;
    }

    /**
     * 获取产品介绍信息
     * 缓存1小时
     *
     * @param productType
     * @return
     */
    @Override
    public BusiProductListDTO.Introduction getIntroductionInfo(int productType){
        BusiProductListDTO busiProductList = new BusiProductListDTO();
        BusiProductListDTO.Introduction introduction = new BusiProductListDTO.Introduction();
        List<BannerDTO> bannerList = new ArrayList<>();
        // product : introduction : productType
        String key = "p:i:" + productType;
        String value = redisSessionManager.get(key);
        if (StringUtils.isNotEmpty(value)) {
            introduction = JSON.parseObject(value, BusiProductListDTO.Introduction.class);
        } else {
            if (productType == 2) {
                bannerList = busiBannerService.queryBannerDTOListByType(AppConstants.BANNER_TYPE.BANNER_TYPE_9);
            } else if (productType == 3) {
                bannerList = busiBannerService.queryBannerDTOListByType(AppConstants.BANNER_TYPE.BANNER_TYPE_8);
            } else if (productType == 4) {
                bannerList = busiBannerService.queryBannerDTOListByType(AppConstants.BANNER_TYPE.BANNER_TYPE_10);
            }
            if (CollectionUtils.isNotEmpty(bannerList)) {
                BannerDTO banner = bannerList.get(0);
                introduction.setId(banner.getId());
                introduction.setImg(banner.getImgName());
                introduction.setUrl(banner.getUrl());
                redisSessionManager.put(key, JSON.toJSONString(introduction), 5, TimeUnit.MINUTES);
            }
        }
        return introduction;
    }

}
