package websvc.servant.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.mapper.sys.SysIconMapper;
import com.zdmoney.models.InvestorEdu;
import com.zdmoney.models.sys.SmsLinkConfig;
import com.zdmoney.models.sys.SysInfoCenter;
import com.zdmoney.service.*;
import com.zdmoney.service.customer.CustomerCenterService;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.service.sys.*;
import com.zdmoney.session.CustomerSessionService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.ObjectUtils;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.SysMessageVo;
import com.zdmoney.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.SysFunctionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2016/3/26.
 */
@Service
@Slf4j
public class SysFunctionServiceImpl implements SysFunctionService {

    @Autowired
    private AppSysInitService appSysInitService;

    @Autowired
    private SysAdvertService sysAdvertService;

    @Autowired
    private SysHeadService sysHeadService;

    @Autowired
    private BusiBannerService busiBannerService;

    @Autowired
    private SysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysInfoCenterService sysInfoCenterService;

    @Autowired
    private CustomerCenterService customerCenterService;

    @Autowired
    private SysFeedbackService sysFeedbackService;

    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private BusiOrderService busiOrderService;

    public static Map<String,HistorySaleDTO> platformSaleMap = Maps.newTreeMap();

    @Autowired
    private RedisSessionManager redisSessionManager;

    private CustomerSessionService sessionService;

    @Autowired
    private TradeFunctionServiceImpl tradeFunctionService;

    @Autowired
    private InvestorEduService investorEduService;

    @Autowired
    private ConfigParamBean configParamBean;

	@Autowired
	private SysNoticeService sysNoticeService;

    @Autowired
    private SmsLinkConfigService smsLinkConfigService;

    @Autowired
	private SysIconMapper sysIconMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private IBusiBrandService busiBrandService;


    @FunctionId("400001")
    @Override
    public Result validateCode(ReqMain reqMain) throws Exception {
        Model_400001 cdtModel = (Model_400001) reqMain.getReqParam();
        Integer codeType = cdtModel.getCvType();
        String cvMobile = cdtModel.getCvMobile();
        customerValidateCodeService.sendValidateCode(0, cvMobile, "", codeType);
        return Result.success();
    }

    @FunctionId("400012")
    public Result unreadSysAnnouncement(ReqMain reqMain) throws Exception {
        Model_400012 cdtModel = (Model_400012) reqMain.getReqParam();
        String msgDate = cdtModel.getMsgDate();
        Map<String, Object> map = sysAnnouncementService.unreadSysAnnouncement(msgDate);
        return Result.success(map);
    }

    @FunctionId("400015")
    public Result invitationConsumeInfo(ReqMain reqMain) throws Exception {
        Model_400015 cdtModel = (Model_400015) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        Page<InviteFriendDto> page = customerCenterService.getInviteFriendLine(customerId, pageNo, pageSize);
        return Result.success(page);
    }

    @FunctionId("400017")
    @Override
    public Result shareUrlBySessionToken(ReqMain reqMain) throws Exception {
        Model_400017 model = (Model_400017) reqMain.getReqParam();
        Map<String, String> shareUrl = customerCenterService.getShareUrlBySessionToken(model.getSessionToken());
        return Result.success(shareUrl);
    }

    @FunctionId("400021")
    @Override
    public Result shareUrlByCustomerId(ReqMain reqMain) throws Exception {
        Model_400021 model = (Model_400021)reqMain.getReqParam();
        Map<String, String> shareUrl = customerCenterService.getShareUrlByCustomerId(model.getCustomerId());
        return Result.success(shareUrl);
    }

    @FunctionId("400019")
    public Result validateCode2(ReqMain reqMain) throws Exception {
        Model_400019 cdtModel = (Model_400019) reqMain.getReqParam();
        Integer type = cdtModel.getType();
        String cellPhone = cdtModel.getCellPhone();
        String busiMsg = cdtModel.getBusiMsg();
        Integer codeType = cdtModel.getCodeType();
        customerValidateCodeService.sendValidateCode(type, cellPhone, busiMsg, codeType);
        return Result.success();
    }

    @FunctionId("500014")
    public Result appIndex(ReqMain reqMain) throws Exception {
        Model_500014 cdtModel = (Model_500014) reqMain.getReqParam();
        InitDTO initDTOCommon = appSysInitService.getInitDTOCommon(cdtModel.getCustomerId(), false,null);
        InitDTO initDTOCustomer = appSysInitService.getInitDTOCustomer(cdtModel.getCustomerId(), false,null);
        ObjectUtils.copy(initDTOCustomer, initDTOCommon);

        Integer num = 0;

        String msgKey = "msg_customerId_" + cdtModel.getCustomerId();
        String msgDTO = redisSessionManager.get(msgKey);
        if (StringUtils.isNotBlank(msgDTO)) {
            num = Integer.valueOf(msgDTO);
        }
        // num = StringUtils.isNotBlank(cdtModel.getCustomerId()) ? sysMessageService.unreadMessageNumber(cdtModel.getCustomerId()) : 0;
        initDTOCommon.setUnreadNum(num);

        HistorySaleDTO hisDto = tradeFunctionService.getHistorySale();
        initDTOCommon.setHistorySaleDTO(hisDto);
        return Result.success(initDTOCommon);
    }

    @FunctionId("501014")
    public Result pcIndex(ReqMain reqMain) throws Exception {
        Model_501014 cdtModel = (Model_501014) reqMain.getReqParam();
        InitDTO initDTOCommon = appSysInitService.getInitDTOCommon(cdtModel.getCustomerId(), true,null);
        InitDTO initDTOCustomer = appSysInitService.getInitDTOCustomer(cdtModel.getCustomerId(), true,null);
        ObjectUtils.copy(initDTOCustomer, initDTOCommon);
        return Result.success(initDTOCommon);
    }

    @FunctionId("501015")
    @Deprecated
    public Result wsvInit(ReqMain reqMain) throws Exception {
        Model_501015 cdtModel = (Model_501015) reqMain.getReqParam();
        boolean ispc = StringUtils.equals(cdtModel.getFromStatus(), "1");
        InitDTO initDTOCommon = appSysInitService.getWsvDTOCommon(cdtModel.getCustomerId(), ispc,null);
        if(StringUtils.equals(cdtModel.getFromStatus(),"0")) {// app 初始化
            Integer num = 0;
            String msgKey = "msg_customerId_" + cdtModel.getCustomerId();
            String msgDTO = redisSessionManager.get(msgKey);
            if (StringUtils.isNotBlank(msgDTO)) {
                num = Integer.valueOf(msgDTO);
            }
            initDTOCommon.setUnreadNum(num);

            HistorySaleDTO hisDto = tradeFunctionService.getHistorySale();
            initDTOCommon.setHistorySaleDTO(hisDto);
        }
        return Result.success(initDTOCommon);
    }

	@FunctionId("501017")
	public Result appIcons(ReqMain reqMain) throws Exception {
		return Result.success(sysIconMapper.querySysIcon());
	}

    @FunctionId("600001")
    public Result feedback(ReqMain reqMain) throws Exception {
        Model_600001 cdtModel = (Model_600001) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        String contactWay = cdtModel.getContactWay();
        String content = cdtModel.getContent();
        String feedbackType = cdtModel.getFeedbackType();
        sysFeedbackService.feedback(customerId, contactWay, content,feedbackType);
        return Result.success();
    }

    @FunctionId("600002")
    public Result sysAnnouncementList(ReqMain reqMain) throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
        Model_600002 cdtModel = (Model_600002) reqMain.getReqParam();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        //APP端的公告展现:1. 审核要通过的  没有逻辑删除的 状态是启用的
        paramMap.put("status", 1);
        paramMap.put("auditStatus", 1);
        //根据参数获取系统消息列表
        Page page = sysAnnouncementService.getSysAnnouncementPage(pageNo, pageSize, paramMap);
        return Result.success(page);
    }

    @FunctionId("600004")
    public Result getBannerList(ReqMain reqMain) throws Exception {
        Model_600004 model_600004 = (Model_600004) reqMain.getReqParam();
        String type = model_600004.getType();
        List<BannerDTO> dtoList = busiBannerService.getBannerDTOList(type);
        return Result.success(dtoList);
    }

    @FunctionId("600013")
    @Deprecated
    public Result getAdvert(ReqMain reqMain) throws Exception {
        AdvertDTO advertDTO = sysAdvertService.getAdvertDTO();
        return Result.success(advertDTO);
    }

    @FunctionId("600014")
    public Result getMessageOrNoticeList(ReqMain reqMain) throws Exception {
        Model_600014 cdtModel = (Model_600014) reqMain.getReqParam();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        String userId = cdtModel.getUserId();
        Integer type = cdtModel.getType();
		cn.hutool.json.JSONObject messageOrNoticeList = sysMessageService
				.getMessageOrNoticeList(type, pageNo, pageSize, userId);
		return Result.success(messageOrNoticeList);
    }

    @FunctionId("600015")
    public Result getMessageInfo(ReqMain reqMain) throws Exception {
        Model_600015 cdtModel = (Model_600015) reqMain.getReqParam();
        Integer msgId = cdtModel.getMsgId();
        String userId = cdtModel.getUserId();
        Integer isAllRead = cdtModel.getIsAllRead() == null ? 0 : cdtModel.getIsAllRead();
        Integer msgType = cdtModel.getMsgType();
        SysMessageVo messageVo = sysMessageService.getMessageInfo(msgId, userId, isAllRead,msgType);
        return Result.success(messageVo);
    }

    @FunctionId("800011")
    public Result sysNoticeList(ReqMain reqMain) throws Exception {
        Model_800011 cdtModel = (Model_800011) reqMain.getReqParam();
        int pageNo = cdtModel.getPageNo();
        int pageSize = cdtModel.getPageSize();
        //资讯列表
        Page<SysInfoCenterDTO> sysInfoCenterPage = sysInfoCenterService.getSysInfoCenterDTOPage(pageNo, pageSize);
        Map result = Maps.newHashMap();
        result.put("banner",sysInfoCenterService.getRecommendedBanner());
        result.put("results",sysInfoCenterPage);
        return Result.success(result);
    }

    @FunctionId("800012")
    public Result sysNoticeDetail(ReqMain reqMain) throws Exception {
        Model_800012 cdtModel = (Model_800012) reqMain.getReqParam();
        Long sid = cdtModel.getSid();
        SysInfoCenterDTO sysInfoCenterDTO = sysInfoCenterService.getSysInfoCenterDTO(sid);
        return Result.success(sysInfoCenterDTO);
    }

    @FunctionId("800013")
    public Result sysHeadList(ReqMain reqMain) throws Exception {
        List<HeadDTO> dtoList = sysHeadService.findHeadDtoByNewsType(BusiConstants.SYS_HEAD_TYPE_SMALL_HORN);
        HeadDTO headDTO = new HeadDTO();
        if (dtoList.size() > 0) {
            headDTO = dtoList.get(0);
        }
        return Result.success(headDTO);
    }

    @FunctionId("800015")
    public Result statistics(ReqMain reqMain) throws Exception {
        Model_800015 model_800015 = (Model_800015)reqMain.getReqParam();
        Long id = model_800015.getId();
        SysInfoCenter sysInfoCenter = sysInfoCenterService.findOne(id);
        sysInfoCenter.setViewNum(sysInfoCenter.getViewNum()+1);
        if(sysInfoCenter != null) {
            sysInfoCenterService.updateNotNull(sysInfoCenter);
        }else{
            log.error("资讯不存在：id={}",id);
        }
        return Result.success();
    }

    @FunctionId("900011")
    @Override
    public Result validateCode3(ReqMain reqMain) throws Exception {
        Model_900011 cdtModel = (Model_900011) reqMain.getReqParam();
        Integer codeType = cdtModel.getCodeType();
        String cvMobile = cdtModel.getCvMobile();
        Integer cvType = cdtModel.getCvType();
        String busiMsg = "";
        if (1 == cvType) {
            busiMsg = "您提交了找回密码的请求";
        } else if (2 == cvType) {
            busiMsg = "您提交了交易申请";
        }
        customerValidateCodeService.sendValidateCode(cvType, cvMobile, busiMsg, codeType);
        return Result.success();
    }

    @FunctionId("800021")
    @Override
    public Result investEduInit(ReqMain reqMain) throws Exception {
        Model_800021 model = (Model_800021) reqMain.getReqParam();
        // 资讯列表
        Page<SysInfoCenterDTO> sysInfoCenterPage = sysInfoCenterService.getSysInfoCenterDTOPageForEdu(1, 3);
        List<InvestorEdu> investorList = investorEduService.queryInvestorEdu(null);
        Map result = new HashMap<>();
        result.put("informationList",sysInfoCenterPage.getResults());

        if (CollectionUtils.isNotEmpty(investorList)) {
            for (InvestorEdu edu : investorList) {
                if (StringUtils.isNotEmpty(edu.getImgUrl())) {
                    edu.setImgUrl(configParamBean.getImgPath() + "/" + edu.getImgUrl());
                }
            }
            if (investorList.size() > 3) {
                investorList = investorList.subList(0, 4);
            }
        }
        result.put("educateList", investorList);
        return Result.success(result);
    }

    @FunctionId("800022")
    @Override
    public Result investEduList(ReqMain reqMain) throws Exception {
        Model_800022 model = (Model_800022) reqMain.getReqParam();
        Page<InvestorEdu> page = new Page<>();
        Map<String, Object> map = new HashMap<>();
        page.setPageNo(model.getPageNo());
        page.setPageSize(10);
        map.put("page", page);
        List<InvestorEdu> investorList = investorEduService.queryInvestorEdu(map);
        int count = investorEduService.queryInvestorEduCount(null);
        if (CollectionUtils.isNotEmpty(investorList)) {
            for (InvestorEdu edu : investorList) {
                if (StringUtils.isNotEmpty(edu.getImgUrl())) {
                    edu.setImgUrl(configParamBean.getImgPath() + "/" + edu.getImgUrl());
                }
            }
        }
        page.setResults(investorList);
        page.setTotalRecord(count);
        return Result.success(page);
    }

    @FunctionId("800023")
    @Override
    public Result investEduDetail(ReqMain reqMain) throws Exception {
        Model_800023 model = (Model_800023) reqMain.getReqParam();
        Map<String, Object> map = new HashMap<>();
        map.put("id", model.getId());
        List<InvestorEdu> investorList = investorEduService.queryInvestorEdu(map);
        if (CollectionUtils.isNotEmpty(investorList)) {
            for (InvestorEdu edu : investorList) {
                if (StringUtils.isNotEmpty(edu.getImgUrl())) {
                    edu.setImgUrl(configParamBean.getImgPath() + "/" + edu.getImgUrl());
                }
            }
        }
        return Result.success(investorList);
    }

    @FunctionId("600016")
    public Result getSmsUrl(ReqMain reqMain) throws Exception {
        SmsLinkConfig linkConfig = smsLinkConfigService.getSmsLink();
        return Result.success(linkConfig);
    }
}
