package com.zdmoney.facade;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.facade.IMsgMessageFacadeService;
import com.zdmoney.models.customer.CustomerCenter;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysInfoCenter;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.BusiBannerService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.IBusiBrandService;
import com.zdmoney.service.customer.CustomerCenterService;
import com.zdmoney.service.sys.AppSysInitService;
import com.zdmoney.service.sys.SysAdvertService;
import com.zdmoney.service.sys.SysInfoCenterService;
import com.zdmoney.service.sys.SysSwitchService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.DateUtil;

import com.zdmoney.utils.ValidatorUtils;
import com.zdmoney.web.dto.AdvertDTO;
import com.zdmoney.web.dto.IndexDTO;
import com.zdmoney.web.dto.InitDTO;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.app.*;
import com.zdmoney.webservice.api.dto.busi.BusiBrandDto;
import com.zdmoney.webservice.api.facade.IAppFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.servant.impl.TradeFunctionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gaol
 * @create 2019-03-20
 */
@Slf4j
@Service("appService")
public class AppFacadeService implements IAppFacadeService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private AppSysInitService appSysInitService;

    @Autowired
    private CustomerCenterService customerCenterService;

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private BusiBannerService busiBannerService;

    @Autowired
    private TradeFunctionServiceImpl tradeFunctionService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private SysAdvertService advertService;

    @Autowired
    private IBusiBrandService busiBrandService;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @Autowired
    private SysInfoCenterService sysInfoCenterService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private IMsgMessageFacadeService iMsgMessageFacadeService;

    @Override
    public ResultDto<com.zdmoney.webservice.api.dto.app.IndexDTO> getIndexInfo(IndexVO indexVO) {
        ResultDto<com.zdmoney.webservice.api.dto.app.IndexDTO> resultDto = new ResultDto<>();
        IndexDTO indexDTO = new IndexDTO();
        try {
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Convert.toLong(indexVO.getCustomerId()));
            boolean isPC = StringUtils.equals(indexVO.getSource(), "1");
            String key = "lcb:index:info";
            String value = redisSessionManager.get(key);
            if (StringUtils.isNotEmpty(value)) {
                indexDTO = JSONObject.parseObject(value, com.zdmoney.web.dto.IndexDTO.class);
            } else {
                // 了解捞财宝url
                String understandUrl = configParamBean.getUnderstandUrl();
                if (StringUtils.isNotEmpty(understandUrl)) {
                    indexDTO.setUnderstandUrl(Arrays.asList(understandUrl.split("\\|")));
                }
                // 新手运营位url
                String newcomerUrl = configParamBean.getNewcomerUrl();
                if (StringUtils.isNotEmpty(newcomerUrl)) {
                    indexDTO.setNewcomerUrl(Arrays.asList(newcomerUrl.split("\\|")));
                }
                indexDTO.setSysNoticeDTO(appSysInitService.getSysNotice(indexVO.getCustomerId(), new InitDTO()));
                boolean hideBanner = sysSwitchService.getSwitchIsOnDiffEdition("hideBanner");
                if (!hideBanner) {
                    indexDTO.setBannerList(busiBannerService.queryBannerDTOListByType(isPC ? "7" : "1"));
                }
                redisSessionManager.put(key, JSON.toJSONString(indexDTO), 5, TimeUnit.MINUTES);
            }
            if (customerMainInfo != null) {
                indexDTO.setConsume(customerMainInfo.getIsConsumed() == 1 ? true : false);
                indexDTO.setOpenAccount(StringUtils.isNotEmpty(customerMainInfo.getFuiouLoginId()) ? true : false);
                // 非新手不显示新手运营位
                if (customerMainInfo.getIsConsumed() == 1 || StringUtils.isNotEmpty(customerMainInfo.getFuiouLoginId())) {
                    indexDTO.setNewcomerUrl(null);
                }
                // 开户认证引导弹窗, 一天只弹一次
                String guideKey = "guide:" + DateUtil.getDateFormatString(new Date(), DateUtil.YYYYMMDD) + indexVO.getCustomerId();
                String guideValue = redisSessionManager.get(guideKey);
                if (StringUtils.isNotEmpty(guideValue)) {
                    indexDTO.setShowGuide(false);
                } else {
                    if (StringUtils.isEmpty(customerMainInfo.getFuiouLoginId()) || customerMainInfo.getCmStatus() != 3) {
                        indexDTO.setShowGuide(true);
                        redisSessionManager.put(guideKey, "show:guide:true", 1, TimeUnit.DAYS);
                    }
                }
                if (3 == customerMainInfo.getCmStatus()) {
                    indexDTO.setRealNameAuth(true);
                }
            }
            // 1非新手  0：新手
            int newcomer = 1;
            if (customerMainInfo == null || customerMainInfo.getIsConsumed() == 0) {
                newcomer = 0;
            }
            indexDTO.setCustomerCenters(customerCenterService.getAllCustomerCenter(newcomer));
            indexDTO.setProductInfoList(appSysInitService.getRecommendProductList(customerMainInfo));
            indexDTO.setHistorySaleDTO(tradeFunctionService.getHistorySale());
            List<com.zdmoney.web.dto.BannerDTO> dtos = busiBannerService.queryBannerDTOListByType("4");
            if (dtos.size() > 0) {
                indexDTO.setPopups(dtos.get(0));
            }
            com.zdmoney.webservice.api.dto.app.IndexDTO indexInfo = JSON.parseObject(JSON.toJSONString(indexDTO), com.zdmoney.webservice.api.dto.app.IndexDTO.class);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(indexInfo);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = (ResultDto<com.zdmoney.webservice.api.dto.app.IndexDTO>) baseService.resultError(e);
        }
        return resultDto;
    }

    @Override
    public ResultDto<PublicizeDTO> getPublicizeInfo() {
        ResultDto<PublicizeDTO> resultDto = new ResultDto<>();
        PublicizeDTO publicizeDTO = new PublicizeDTO();
        Advert advert = new Advert();
        BusiBrandDto busiBrandDto = new BusiBrandDto();
        try {
            AdvertDTO advertDTO = advertService.getAdvertDTO();
            String nowDate = DateUtil.getDateFormatString(new Date(), DateUtil.YMDSTR);
            String key = "PPXC" + nowDate;
            String value = redisSessionManager.get(key);
            if (StringUtils.isNotEmpty(value)) {
                busiBrandDto = JSONObject.parseObject(value, BusiBrandDto.class);
            } else {
                busiBrandDto = busiBrandService.getBusiBrand(nowDate);
                if (busiBrandDto != null) {
                    busiBrandDto.setWeek(cn.hutool.core.date.DateUtil.dayOfWeekEnum(busiBrandDto.getDisplayDate()).toChinese());
                    redisSessionManager.put(key, JSON.toJSONString(busiBrandDto), 5, TimeUnit.MINUTES);
                }
            }
            BeanUtils.copyProperties(advertDTO, advert);
            publicizeDTO.setAdvert(advert);
            publicizeDTO.setBrand(busiBrandDto == null ? new BusiBrandDto() : busiBrandDto);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(publicizeDTO);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = (ResultDto<PublicizeDTO>) baseService.resultError(e);
        }
        return resultDto;
    }

    @Override
    public ResultDto<AccountInfoDTO> getAccountInfo(AccountVO accountVO) {
        ResultDto<AccountInfoDTO> resultDto = new ResultDto<>();
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        try {
            ValidatorUtils.validate(accountVO);
            com.zdmoney.web.dto.AccountInfoDTO accountInfo = accountOverview520003Service.getAccountInfo(accountVO.getCustomerId());
            BeanUtils.copyProperties(accountInfo, accountInfoDTO);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(accountInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = (ResultDto<AccountInfoDTO>) baseService.resultError(e);
        }
        return resultDto;
    }

    @Override
    public ResultDto<DiscoveryInfoDTO> discovery() {
        ResultDto<DiscoveryInfoDTO> resultDto = new ResultDto<>();
        DiscoveryInfoDTO discoveryDTO = new DiscoveryInfoDTO();

        try {
            // 运行位
            List<CustomerCenterDTO> dtos = Lists.newArrayList();
            String key = "lcb:discovery:customerCenters";
            String value = redisSessionManager.get(key);
            if (StringUtils.isNotEmpty(value)) {
                dtos = JSONObject.parseObject(value, List.class);
            } else {
                List<CustomerCenter> customerCenters = customerCenterService.geDiscoveryCustomerCenter();
                int customerCenterSize = customerCenters.size() > 8 ? 8 : customerCenters.size();
                if (customerCenterSize > 0) {
                    for (int i = 0; i < customerCenterSize; i++) {
                        CustomerCenterDTO dto = new CustomerCenterDTO();
                        dto.setId(customerCenters.get(i).getId());
                        dto.setTitle(customerCenters.get(i).getTitle());
                        dto.setCanShare(customerCenters.get(i).getCanShare());
                        dto.setH5Url(customerCenters.get(i).getH5Url());
                        dto.setImgUrl(customerCenters.get(i).getImgUrl() == null ? "" : configParamBean.getImgPath() + "/" + customerCenters.get(i).getImgUrl());
                        dto.setMustLogin(customerCenters.get(i).getMustLogin());
                        dto.setSubtitle(customerCenters.get(i).getSubtitle());
                        dto.setBubbleMark(customerCenters.get(i).getBubbleMark());
                        dto.setPageType(customerCenters.get(i).getPageType());
                        dtos.add(dto);
                    }
                    redisSessionManager.put(key, JSON.toJSONString(dtos), 5, TimeUnit.MINUTES);
                }
            }
            discoveryDTO.setCustomerCenters(dtos);

            // banner显示
            List<BannerDTO> banners = new ArrayList<>();
            String key1 = "lcb:discovery:banners";
            String value1 = redisSessionManager.get(key1);
            if (StringUtils.isNotEmpty(value1)) {
                banners = JSONObject.parseObject(value1, List.class);
            } else {
                List<com.zdmoney.web.dto.BannerDTO> bannerList = busiBannerService.getBannerDTOList("11");
                int size = bannerList.size() > 3 ? 3 : bannerList.size();
                if (bannerList.size() > 0) {
                    for (int i = 0; i < size; i++) {
                        BannerDTO bannerDTO = new BannerDTO();
                        bannerDTO.setId(bannerList.get(i).getId());
                        bannerDTO.setImgName(bannerList.get(i).getImgName());
                        bannerDTO.setTitle(bannerList.get(i).getTitle());
                        bannerDTO.setUrl(bannerList.get(i).getUrl());
                        banners.add(bannerDTO);
                    }
                    redisSessionManager.put(key1, JSON.toJSONString(banners), 5, TimeUnit.MINUTES);
                }
            }
            discoveryDTO.setBanners(banners);

            //资讯列表
            List<SysInfoCenterDTO> dtoList = new ArrayList<SysInfoCenterDTO>();
            String key2 = "lcb:discovery:sysInfoCenter";
            String value2 = redisSessionManager.get(key2);
            if (StringUtils.isNotEmpty(value2)) {
                dtoList = JSONObject.parseObject(value2, List.class);
            } else {
                List<SysInfoCenter> sysInfoCenters = sysInfoCenterService.getSysInfoCenter();
                int sysInfoCenterSize = sysInfoCenters.size() > 3 ? 3 : sysInfoCenters.size();
                if (sysInfoCenters.size() > 0) {
                    for (int i = 0; i < sysInfoCenterSize; i++) {
                        SysInfoCenterDTO sysInfoCenterDTO = new SysInfoCenterDTO();
                        sysInfoCenterDTO.setId(sysInfoCenters.get(i).getId());
                        sysInfoCenterDTO.setImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenters.get(i).getImgUrl());
                        sysInfoCenterDTO.setPcImgUrl(configParamBean.getImgPath() + "/" + sysInfoCenters.get(i).getPcImgUrl());
                        sysInfoCenterDTO.setInfoSource(sysInfoCenters.get(i).getInfoSource());
                        sysInfoCenterDTO.setPublishDate(DateUtil.getDateFormatString(sysInfoCenters.get(i).getPublishDate(), DateUtil.YMDSTR));
                        sysInfoCenterDTO.setRemark(sysInfoCenters.get(i).getRemark());
                        sysInfoCenterDTO.setSummary(sysInfoCenters.get(i).getSummary());
                        sysInfoCenterDTO.setTitle(sysInfoCenters.get(i).getTitle());
                        sysInfoCenterDTO.setTopStatus(sysInfoCenters.get(i).getTopStatus());
                        dtoList.add(sysInfoCenterDTO);
                    }
                    redisSessionManager.put(key2, JSON.toJSONString(dtoList), 5, TimeUnit.MINUTES);
                }
            }
            discoveryDTO.setSysInfoCenters(dtoList);


            //积分商城推荐
            List<GoodsDTO> goodsList = Lists.newArrayList();
            String key3 = "lcb:discovery:goods";
            String value3 = redisSessionManager.get(key3);
            if (StringUtils.isNotEmpty(value3)) {
                goodsList = JSONObject.parseObject(value3, List.class);
            } else {
                try {
                    String body = HttpUtil.createGet(configParamBean.getGoodsListUrl()).execute().body();
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    log.info("小象生活数据：[{}]", body);
                    if (jsonObject != null) {
                        String jsonstr = jsonObject.getJSONArray("data").getJSONObject(3).getJSONArray("pdlist").toString();
                        List<GoodsDTO> list = JSONObject.parseArray(jsonstr, GoodsDTO.class);
                        int goodsSize = list.size() > 3 ? 3 : list.size();
                        if (list.size() > 0) {
                            for (int i = 0; i < goodsSize; i++) {
                                GoodsDTO goodsDTO = list.get(i);
                                goodsDTO.setDetailUrl(configParamBean.getGoodsDetailUrl() + "?" + goodsDTO.getId());
                                goodsList.add(goodsDTO);
                            }
                            redisSessionManager.put(key3, JSON.toJSONString(goodsList), 5, TimeUnit.MINUTES);
                        }
                    }
                } catch (Exception e) {
                    log.info("未获取到小象商城数据！");
                }
            }
            discoveryDTO.setGoodsList(goodsList);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(discoveryDTO);

        } catch (Exception e) {
            resultDto = (ResultDto<DiscoveryInfoDTO>) baseService.resultError(e);
            e.printStackTrace();
        }

        return resultDto;
    }


    @Override
    public ResultDto<ShowMsgDTO> showMsg(IndexVO indexVO) {
        ResultDto<ShowMsgDTO> resultDto = new ResultDto<>();
        ShowMsgDTO showMsgDTO = new ShowMsgDTO();

        try {
            if (indexVO.getCustomerId() != null) {
                // 消息小红点
                MessageResultDto<Integer> msgResult = iMsgMessageFacadeService.unReadCount(indexVO.getCustomerId());
                if (msgResult.isSuccess()) {
                    showMsgDTO.setShowMsgDot(msgResult.getData() > 0 ? true : false);
                }
                resultDto.setCode(ResultDto.SUCCESS_CODE);
                resultDto.setData(showMsgDTO);
            } else {
                showMsgDTO.setShowMsgDot(false);
                resultDto.setData(showMsgDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = (ResultDto<ShowMsgDTO>) baseService.resultError(e);
        }
        return resultDto;
    }
}