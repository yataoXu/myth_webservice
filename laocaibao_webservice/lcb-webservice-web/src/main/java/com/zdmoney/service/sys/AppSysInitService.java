package com.zdmoney.service.sys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.mapper.product.BusiProductLimitMapper;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.mapper.sys.SysIconMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.models.product.BusiProductInit;
import com.zdmoney.models.product.BusiProductLimit;
import com.zdmoney.models.sys.SysNotice;
import com.zdmoney.service.BusiBannerService;
import com.zdmoney.service.BusiProductService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.SysNoticeService;
import com.zdmoney.service.customer.CustomerCenterService;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiProductVO;
import com.zdmoney.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 00225181 on 2016/1/4.
 */
@Service
@Slf4j
public class AppSysInitService {

    @Autowired
    private BusiProductService busiProductService;

    @Autowired
    private BusiBannerService busiBannerService;

    @Autowired
    private SysHeadService sysHeadService;

    @Autowired
    private CustomerCenterService customerCenterService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private SysNoticeService sysNoticeService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiProductLimitMapper busiProductLimitMapper;

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductService productService;

    /**
     *  @Description: app pc 首页初始化
     *  @author huangcy
     *  @date 2017/9/11
    */
    public InitDTO getWsvDTOCommon(String customerId,boolean isPc,String version) {
        InitDTO dto = new InitDTO();
        int needNew = 0;//是否需要新手标
        List<BusiProductInit> res = Lists.newArrayList();
        String userlabel = null;
        if(StringUtils.isNotBlank(customerId)){
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCustomerId(Long.valueOf(customerId));
            if(0 == customerMainInfo.getIsConsumed()){
                needNew+=1;
            }
            userlabel = customerMainInfo.getUserLabel();
        }else {
            needNew+=1;
        }
        if(needNew == 0){//不需要新手标
            res = wsvInitProducts("0", "1", 4, userlabel);
        }else{
            res = wsvInitProducts("1", "1", 4, userlabel);
        }
        //获取首页精品推荐中在售和待售的数量
        int num1 = 0;
        if(res.size()>= 2){
            for (BusiProductInit re : res) {
                if (re.getSellOut() == 0 || re.getSellOut() == 2){
                    num1+=1;continue;
                }
            }
        }
        //当产品不足两个时 插入新手标
        if (num1 < 2){
            //取新手标
            res = wsvInitProducts("1", "1", 4, userlabel);
        }
        dto.setRecommendedProducts(res);
		List<SysNoticeSubDto> sysNotice = getSysNotice(customerId,dto);//系统公告
        dto.setSysNoticeDtoList(sysNotice);
        dto.setHeads(sysHeadService.findHeadDtoByNewsType(BusiConstants.SYS_HEAD_TYPE_HEADLINES));  //捞头条
        List<BannerDTO> popups = Lists.newArrayList();  //首页弹窗(目前获取一条)
        List<BannerDTO> dtos = busiBannerService.queryBannerDTOListByType("4");
        if (dtos.size() > 0) {
            popups.add(dtos.get(0));
        }
        dto.setPopups(popups);
        boolean hideBanner = sysSwitchService.getSwitchIsOnDiffEdition("hideBanner");
        if (!hideBanner) {
            List<BannerDTO> banners = busiBannerService.queryBannerDTOListByType(isPc ? "7" : "1"); //首页
            dto.setBanners(banners);
            //dto.setCustomerCenters(customerCenterService.getAllCustomerCenter());   //活动中心
        }
        dto.setTaskCenterUrl(configParamBean.getTouchWebappHomeUrl() + "/taskDetail?sessionToken=");
        dto.setInfoUrl(configParamBean.getTouchProductDetailsUrl()+"/infroDisclosure");
        return dto;
    }

    // @Cacheable(value = "appIndex", key = "'appIndex_common_' + #version + '_' + #isPc")
    public InitDTO getInitDTOCommon(String customerId, boolean isPc,String version) {
        InitDTO dto = new InitDTO();
        if (isPc) {
            dto.setHandNewProducts(getProductVo("newHand", null)); //新手产品列表
            dto.setLimitProducts(getProductVo("limit", customerId)); //限购产品列表
        } else {
            if(StringUtils.isNotBlank(customerId)){
                CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCustomerId(Long.valueOf(customerId));
                if(customerMainInfo == null){
                    dto.setHandNewProducts(getProductVo("newHand", null)); //新手产品列表
                }else{
                    // 没有消费记录
                    if(0 == customerMainInfo.getIsConsumed()){
                        dto.setHandNewProducts(getProductVo("newHand", null)); //新手产品列表
                    }
                    /*else{
                        dto.setLimitProducts(getProductVo("limit", customerId)); //限购产品列表
                    }*/
                }
            }else{
                dto.setHandNewProducts(getProductVo("newHand", null)); //新手产品列表
            }
        }

        dto.setHotSellProducts(getProductVo("hotSell", null)); //热销产品列表
        dto.setHeads(sysHeadService.findHeadDtoByNewsType(BusiConstants.SYS_HEAD_TYPE_HEADLINES));  //捞头条
        List<BannerDTO> popups = Lists.newArrayList();  //首页弹窗(目前获取一条)
        List<BannerDTO> dtos = busiBannerService.queryBannerDTOListByType("4");
        if (dtos.size() > 0) {
            popups.add(dtos.get(0));
        }
        dto.setPopups(popups);
        boolean hideBanner = sysSwitchService.getSwitchIsOnDiffEdition("hideBanner");
        if (!hideBanner) {
            List<BannerDTO> banners = busiBannerService.queryBannerDTOListByType(isPc ? "7" : "1"); //首页
            dto.setBanners(banners);
            //dto.setCustomerCenters(customerCenterService.getAllCustomerCenter());   //活动中心
        }
        dto.setTaskCenterUrl(configParamBean.getTouchWebappHomeUrl() + "/taskDetail?sessionToken=");
        return dto;
    }

    @Cacheable(value = "appIndex", key = "'appIndex_' + #version + '_' + #customerId + '_' + #isPc")
    public InitDTO getInitDTOCustomer(String customerId, boolean isPc,String version) {
        InitDTO dto = new InitDTO();
		getSysNotice(customerId,dto);//系统公告
        dto.setLimitProducts(getProductVo("limit", customerId)); //限购产品列表
        if (isPc) {
            dto.setPcProducts(getProductVo("pc_top", customerId));
        } else {
            dto.setOrgProducts(getOrganProductVo(customerId));  //机构产品列表
        }
        return dto;
    }


    public List<BusiProductVO> getOrganProductVo(String customerId) {
        if (!StringUtils.isEmpty(customerId)) {
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Long.parseLong(customerId));
            if (customerMainInfo != null) {
                String inviteCode = customerMainInfo.getCmIntroduceCode();
                if (!StringUtils.isEmpty(inviteCode)) {
                    boolean isOrgan = customerMainInfoService.isOrganCustomer(inviteCode);
                    if (isOrgan) {//合作商户的邀请码
                        Map<String, Object> map = Maps.newTreeMap();
                        map.put("contract", "no");
                        map.put("orgInviteCode", inviteCode);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        map.put("sDate", sdf.format(new Date()));
                        List<BusiProductVO> vos = busiProductService.getOrgProducts(map);
                        for (BusiProductVO vo : vos) {
                            String url = configParamBean.getProductDetailUrl() + "/showPicInfo?productId=" + vo.getId();
                            vo.setImgUrl(url);
//                            String detailUrl = configParamBean.getProductDetailUrl() + "/showItemDetail?productId=" + vo.getId();
                            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500016&resultPage=front/app/itemDetails&productId="+ vo.getId();
                            vo.setProductDetailUrl(detailUrl);
                            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());
                        }
                        return vos;
                    }
                }
            }
        }
        return Lists.newArrayList();
    }

    /**
     *  @Description: app / pc 首页产品
     *  @author huangcy
     *  @date 2017/9/11
    */
    private List<BusiProductInit> wsvInitProducts(String isNew, String isRecommend,int pageSize, String userlabel){
        Page<BusiProduct> page = new Page<>();
        int pageNo = 1;
        Map<String, Object> map = Maps.newTreeMap();
        //map.put("isRecommend",isRecommend); //是否推荐产品 0:否 1:是
        map.put("newHand",isNew);//是否新手标
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("sDate", sdf.format(new Date()));
        map.put("userlabel", userlabel);
        List<BusiProductInit> vos = busiProductService.selectProductsByIsrecommend(map);
        if (vos.size() > 0){
            for (BusiProductInit vo : vos) {
                 vo.setProductDetailUrl(configParamBean.getTouchProductDetailsUrl()+"productDetails?productId="+vo.getId());
            }
        }
        return vos;
    }

    //获取产品
    private List<BusiProductVO> getProductVo(String flag, String customerId) {
        //只查询一个产品
        Page<BusiProduct> page = new Page<>();
        int pageNo = 1;
        int pageSize = 1;
        Map<String, Object> map = Maps.newTreeMap();
        map.put("contract", "no");
        //map.put("limit", "2");//默认不限购
        if ("hotSell".equals(flag)) {
            map.put("newHand", "4");
        } else if ("newHand".equals(flag)) {
            map.put("newHand", "3");
        } else if ("limit".equals(flag)) {
            map.put("limit", "3");//默认不限购
            map.put("newHand", "2");
            if (!StringUtils.isEmpty(customerId)) {
                CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Long.parseLong(customerId));
                if (customerMainInfo != null) {
                    if (customerMainInfo.getLimitType() != null) {
                        BusiProductLimit productLimit = busiProductLimitMapper.selectByPrimaryKey(customerMainInfo.getLimitType());
                        Date limitLastDate = customerMainInfo.getLimitLastDate();
                        Date nowDate = new Date();
                        if (productLimit.getId().equals(customerMainInfo.getLimitType()) && limitLastDate != null && limitLastDate.after(nowDate)) {
                            //在限购期内，不能再次购买
                            map.put("limit", "4");
                        }
                    }
                }
            }
        } else if ("pc_top".equals(flag)) {
            map.put("pc_top", "true");
            //pc推荐产品最多查两条
            pageSize = 2;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("sDate", sdf.format(new Date()));
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        List<BusiProductVO> vos = busiProductService.selectProductDetail(map);

        StringBuilder ids = new StringBuilder();
        List<BusiProductVO> productDynamicData = new ArrayList<>();
        for (BusiProductVO pro : vos) {
            ids.append(pro.getId()).append(",");
        }
        if (StringUtils.isNotBlank(ids.toString())) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ids", ids.toString().substring(0, ids.length() - 1).split(","));
            paramMap.put("sDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            productDynamicData = busiProductService.getProductDynamicData(paramMap);
        }

        for (BusiProductVO vo : vos) {
            String url = configParamBean.getProductDetailUrl() + "/showPicInfo?productId=" + vo.getId();
            vo.setImgUrl(url);
            String detailUrl = configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500016&resultPage=front/app/itemDetails&productId=" + vo.getId();
            vo.setProductDetailUrl(detailUrl);
            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl());

            if (CollectionUtils.isNotEmpty(productDynamicData)) {
                for (BusiProductVO dynamicVO : productDynamicData){
                    if(vo.getId().equals(dynamicVO.getId())){
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
        return vos;
    }

	public List<SysNoticeSubDto> getSysNotice(String customerId,InitDTO initDTO) {
        Map<String, Object> requestMap = Maps.newTreeMap();
        //APP端的公告展现:1. 审核要通过的  没有逻辑删除的 状态是启用的
		Page<SysNotice> page = new Page<>();
		page.setPageNo(1);
		page.setPageSize(10);//限制10条系统公告
		requestMap.put("page", page);
        requestMap.put("status", 1);
        requestMap.put("auditStatus", 1);
        requestMap.put("noticeType",1); //公告类型 1:首页公告
		List<SysNotice> sysNoticeList = sysNoticeService.getSysNoticeList(requestMap);//查询紧急公告列表
		List<SysNoticeSubDto> sysNoticeDtoList = Lists.newArrayList();
		if (CollUtil.isNotEmpty(sysNoticeList)) {
			String shareRedPackUrl = ""; //登录分享红包url
			if (!StringUtils.isEmpty(customerId)) {
				CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Long.parseLong(customerId));
				if (customerMainInfo != null) {
					shareRedPackUrl = getShareRedPackUrl(customerMainInfo);
				}
			}
			for (SysNotice sysNotice : sysNoticeList) {
				SysNoticeSubDto dto = new SysNoticeSubDto();
				dto.setId(sysNotice.getId());
				dto.setContentType(sysNotice.getNoticeLabel());
				dto.setTitle(sysNotice.getTitle());
				dto.setUrl(StrUtil.builder(configParamBean.getMessageAndNoticeDetailUrl(),"?msgType=2&userId=",customerId,"&msgId=",sysNotice.getId().toString()).toString());
				sysNoticeDtoList.add(dto);
			}
			if (sysNoticeDtoList.size()>0){
				SysNoticeSubDto sysNoticeSubDto = sysNoticeDtoList.get(0);
				SysNoticeDTO sysNoticeDTO = new SysNoticeDTO();
				sysNoticeDTO.setId(sysNoticeSubDto.getId());
				sysNoticeDTO.setUrl(sysNoticeSubDto.getUrl());
				sysNoticeDTO.setTitle(sysNoticeSubDto.getTitle());
				sysNoticeDTO.setContentType(sysNoticeSubDto.getContentType());
				sysNoticeDTO.setShareRedPackUrl(shareRedPackUrl);
				initDTO.setSysNoticeDTO(sysNoticeDTO);
			}
		}
		return sysNoticeDtoList;
    }

    /*获取登录分享红包url*/
    public String getShareRedPackUrl(CustomerMainInfo mainInfo) {
        boolean hideBanner = sysSwitchService.getSwitchIsOnDiffEdition("hideBanner");
        if (!hideBanner) {
            String grantTime = configParamBean.getLoginGrantRedPackTime();
            boolean isLogonShareRed = sysSwitchService.getSwitchIsOn("isLogonShareRed");
            Date grantDate = DateTime.parse(grantTime, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
            if (isLogonShareRed && mainInfo.getCmInputDate().after(grantDate)) {
                ResultDto<String> result = couponFacadeService.getInviteUrl(mainInfo.getCmNumber(), mainInfo.getCmInviteCode());
                if (result.isSuccess()) {
                    return result.getData();
                }
            }
        }
        return null;
    }

    /**
     * 此为运维测试redis集群专用方法，测试完毕即可删除
     * @return
     */
    @Cacheable(value = "testRedisCluster", key = "'redistCluster_'+#i")
    public Object test(int i){
        System.out.println("不从redis获取");
        return "A request!" +i;
    }


    /**
     * 5.0首页 精品推荐
     *
     * @param customerMainInfo
     * @return
     */
    public List<BusiProductInfo> getRecommendProductList(CustomerMainInfo customerMainInfo){
        List<BusiProductInfo> productInfoList = new ArrayList<>();
        Page<BusiProductInfo> page = new Page<>();
        page.setPageNo(1);
        page.setPageSize(2);
        Map<String, Object> map = new HashMap<>();
        String userLabel = "";
        boolean newcomer = false;
        // 未登录或未消费, 推荐新手标
        if (customerMainInfo == null || customerMainInfo.getIsConsumed() == 0) {
            newcomer = true;
        }
        boolean flag = (newcomer == false) ? true : false;
        map.put("sDate", DateUtil.getDateFormatString(new Date(), DateUtil.fullFormat));
        map.put("page", page);
        // 查询新手标产品
        if (newcomer) {
            map.put("productType", 6);
            productInfoList = productService.getProduct(6, map, flag);
        }
        // 非新手或新手标产品为空时, 依次查询散标或智投宝产品
        if (!newcomer || CollectionUtils.isEmpty(productInfoList)) {
            map.put("newcomer", 1); // 过滤新手标
            map.put("productType", 3); // 智投宝
            productInfoList = productService.getProduct(3, map, flag);
            if (CollectionUtils.isEmpty(productInfoList)){
                map.put("productType", 2); // 散标
                productInfoList = productService.getProduct(2, map, flag);
            }
        }
        return productInfoList;
    }
}
