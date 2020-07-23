package com.zdmoney.service.customer;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.exception.HessianRpcException;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsDto;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsSearchDto;
import com.zdmoney.integral.api.dto.IntegralAccountDto;
import com.zdmoney.integral.api.dto.cash.CashSearchDto;
import com.zdmoney.integral.api.dto.cash.CashSearchResDto;
import com.zdmoney.integral.api.dto.coupon.*;
import com.zdmoney.integral.api.dto.lottery.LotteryQualificationForType;
import com.zdmoney.integral.api.facade.ICashFacadeService;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.integral.api.facade.ILotteryQualificationFacadeService;
import com.zdmoney.mapper.CustInviteLineMapper;
import com.zdmoney.mapper.customer.CustomerCenterMapper;
import com.zdmoney.marketing.entity.ShareMessage;
import com.zdmoney.models.CashSearchResInfo;
import com.zdmoney.models.CustInviteLine;
import com.zdmoney.models.customer.CustomerCenter;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.CustomerSignService;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.CouponShareDto;
import com.zdmoney.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 00225181 on 2016/1/4.
 */
@Service
@Slf4j
public class CustomerCenterService extends BaseBusinessService<CustomerCenter, Long> {

    private CustomerCenterMapper getCustomerCenterMapper() {
        return (CustomerCenterMapper) baseMapper;
    }

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private ILotteryQualificationFacadeService lotteryQualificationFacadeService;

    @Autowired
    private CustomerSignService customerSignService;

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    private CustInviteLineMapper custInviteLineMapper;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private ICashFacadeService cashFacadeService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    public List<CustomerCenterDTO> getAllCustomerCenter(int type) {
        List<CustomerCenterDTO> dtos = Lists.newArrayList();
        String key = "customer:center:" + type;
        String value = redisSessionManager.get(key);
        if (StringUtils.isNotEmpty(value)) {
            dtos = JSON.parseObject(value, List.class);
        } else {
            Example example = new Example(CustomerCenter.class);
            example.createCriteria().andEqualTo("status", "1").andEqualTo("auditStatus", "1").andEqualTo("recordType", type);
            example.setOrderByClause("sort desc,create_date desc");
            List<CustomerCenter> customerCenters = getCustomerCenterMapper().selectByExample(example);
            List<CustomerCenter> customerCenterList = customerCenters.subList(0, customerCenters.size()>4?4:customerCenters.size());
            for (CustomerCenter customerCenter : customerCenterList) {
                CustomerCenterDTO dto = new CustomerCenterDTO();
                dto.setId(customerCenter.getId());
                dto.setTitle(customerCenter.getTitle());
                dto.setCanShare(customerCenter.getCanShare());
                dto.setH5Url(customerCenter.getH5Url());
                dto.setImgUrl(customerCenter.getImgUrl() == null ? "" : configParamBean.getImgPath() + "/" + customerCenter.getImgUrl());
                dto.setMustLogin(customerCenter.getMustLogin());
                dto.setSubtitle(customerCenter.getSubtitle());
                String isRead = customerCenter.getIsRead();
                dtos.add(dto);
            }
            if (CollectionUtils.isNotEmpty(dtos)) {
                redisSessionManager.put(key, JSON.toJSONString(dtos), 5, TimeUnit.MINUTES);
            }
        }
        return dtos;
    }

    public List<CustomerCenter> geDiscoveryCustomerCenter() {
        Example example = new Example(CustomerCenter.class);
        example.createCriteria().andEqualTo("status", "1").andEqualTo("auditStatus", "1").andEqualTo("recordType","2");
        example.setOrderByClause("sort desc,create_date desc");
        List<CustomerCenter> customerCenters = getCustomerCenterMapper().selectByExample(example);
        return customerCenters;
    }

    public Page<InviteFriendDto> getInviteFriendLine(Long customerId, Integer pageNo, Integer pageSize) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        String cellPhone = mainInfo.getCmCellphone();
        Map<String, Object> map = Maps.newHashMap();
        map.put("cellphone", cellPhone);
        PageHelper.startPage(pageNo, pageSize);
        List<CustInviteLine> inviteLines = custInviteLineMapper.selectByCondition(map);
        com.github.pagehelper.Page dtoPage = (com.github.pagehelper.Page) inviteLines;
        Page<InviteFriendDto> page = new Page<InviteFriendDto>();
        if (!inviteLines.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            List<InviteFriendDto> dtoList = Lists.newArrayList();
            for (CustInviteLine inviteLine : inviteLines) {
                InviteFriendDto dto = new InviteFriendDto();
                dto.setActionTimeVal(format.format(inviteLine.getActionTime()));
                String sourceType = "";
                if ("0".equals(inviteLine.getStatus())) {
                    sourceType = "注册";
                } else if ("1".equals(inviteLine.getStatus())) {
                    sourceType = "实名认证";
                } else if ("2".equals(inviteLine.getStatus())) {
                    sourceType = "出借成功";
                }
                dto.setSourceType(sourceType);
                dto.setSourceName(sourceType);
                String phone = inviteLine.getInvitedCellphone();
                dto.setMobile(phone.substring(0, 3) + "****" + phone.subSequence(7, phone.length()));
                dtoList.add(dto);
            }
            page = LaocaiUtil.convertPage(dtoPage, dtoList);
        }
        return page;
    }

    public List<IntegralAccountDetailsDto> integralInfo(Long customerId, Integer pageNo, Integer pageSize) {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        IntegralAccountDetailsSearchDto searchDto = new IntegralAccountDetailsSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        searchDto.setPageNo(pageNo);
        searchDto.setPageSize(pageSize);
        PageResultDto<IntegralAccountDetailsDto> resultDto = integralAccountFacadeService.searchDetails(searchDto);
        if (resultDto.isSuccess()) {
            return resultDto.getDataList();
        } else {
            throw new HessianRpcException("integral.record",resultDto.getMsg());
        }
    }

    public Map<String, Object> couponInfo(Long customerId, Integer pageSize, Integer pageNo) {
        Map<String, Object> rtnMap = Maps.newTreeMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //查询用户是否存在
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);

        //查询红包
        List<PacketDTO> packetList = new ArrayList<>();
        CouponSearchDto searchDto = new CouponSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        searchDto.setPageSize(pageSize);
        searchDto.setPageNo(pageNo);
        PageResultDto<CouponDto> result = couponFacadeService.getPageCouponsByAccountNo(searchDto);

        if (result.isSuccess()) {
            rtnMap.put("pageSize",searchDto.getPageSize());
            rtnMap.put("pageNo",result.getPageNo());
            rtnMap.put("totalPage",result.getTotalPage());
            rtnMap.put("totalSize",result.getTotalSize());
            rtnMap.put("couponNum", "0");
            if(result.getDataList()!=null){
                //查询待开启红包数量
                CouponShareSearchDto searchDto2 = new CouponShareSearchDto();
                searchDto2.setAccountNo(mainInfo.getCmNumber());
                ResultDto<List<CouponShareResDto>> result2 = couponFacadeService.getCouponShareList(searchDto2);
                if(result2.isSuccess()){
                    rtnMap.put("couponNum", result2.getData().size());
                }
                for (CouponDto couponDto : result.getDataList()) {
                    PacketDTO dto = new PacketDTO();
                    dto.setAmount(couponDto.getAmount().toString());
                    dto.setPacketId(couponDto.getNo());
                    dto.setInvestAmount(couponDto.getInvestAmount().toString());
                    dto.setEndTime(sdf.format(couponDto.getEndTime()));
                    dto.setInvestPrice(couponDto.getInvestPeriod().toString());
                    dto.setStartTime(sdf.format(couponDto.getStartTime()));
                    dto.setStatus(couponDto.getStatus());
                    dto.setCondition(couponDto.getConditionString());
                    dto.setPacketTime(couponDto.getDateString());
                    packetList.add(dto);
                }
            }

        } else {
            throw new HessianRpcException("coupon.record",result.getMsg());
        }

        PacketAmoutDTO amoutDTO = new PacketAmoutDTO();
        ResultDto<CouponAccountDto> couponResult = couponFacadeService.getCouponAmountByAccountNo(mainInfo.getCmNumber());
        if (couponResult.isSuccess()) {
            if (couponResult.getData() == null) {
                amoutDTO.setUsedAmount("0");
                amoutDTO.setValidAmount("0");
            } else {
                amoutDTO.setUsedAmount(couponResult.getData().getUsedAmount().toString());
                amoutDTO.setValidAmount(couponResult.getData().getAvailAmount().toString());
            }
        } else {
            throw new HessianRpcException("coupon.record",result.getMsg());
        }
        rtnMap.put("validAmount", amoutDTO.getValidAmount());
        rtnMap.put("usedAmount", amoutDTO.getUsedAmount());
        rtnMap.put("result", packetList);
        SysParameter couponeMemo = sysParameterService.findOneByPrType("couponeMemo");
        if (couponeMemo == null) {
            rtnMap.put("memo", "");
        } else {
            rtnMap.put("memo", couponeMemo.getPrValue());
        }
        return rtnMap;
    }

    public List<CouponShareDto> invitationCouponInfo(Long customerId) {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        CouponShareSearchDto searchDto = new CouponShareSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        ResultDto<List<CouponShareResDto>> result = couponFacadeService.getCouponShareList(searchDto);
        if (result.isSuccess()) {
            List<CouponShareDto> shares = Lists.newArrayList();
            for (CouponShareResDto resDto : result.getData()) {
                CouponShareDto shareDto = new CouponShareDto();
                shareDto.setNo(resDto.getNo());
                shareDto.setConditionString(resDto.getConditionString());
                shareDto.setInvestAmount(resDto.getInvestAmount());
                shareDto.setInvestPeriod(resDto.getInvestPeriod());
                shareDto.setInviteUrl(resDto.getInviteUrl());
                shareDto.setAmount(resDto.getAmount());
                shareDto.setPeriod(resDto.getPeriod());
                shares.add(shareDto);
            }
            return shares;
        } else {
            throw new HessianRpcException("coupon.invitation.record",result.getMsg());
        }
    }


    public IntegralDTO myIntegral(Long customerId, Integer pageNo, Integer pageSize) {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        IntegralDTO integral = new IntegralDTO();
        //积分明细
        IntegralAccountDetailsSearchDto searchDto = new IntegralAccountDetailsSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        searchDto.setPageNo(pageNo);
        searchDto.setPageSize(pageSize);
        PageResultDto<IntegralAccountDetailsDto> resultDto = integralAccountFacadeService.searchDetails(searchDto);
        if (resultDto.isSuccess()) {
            integral.setTotalPage(resultDto.getTotalPage().toString());
            integral.setTotalSize(resultDto.getTotalSize().toString());
            integral.setPageNo(resultDto.getPageNo().toString());
            resultDto.getPageNo();
            List<Map<String, String>> details = Lists.newArrayList();
            for (IntegralAccountDetailsDto detail : resultDto.getDataList()) {
                Map<String, String> detailMap = Maps.newHashMap();
                String sourceName = detail.getSourceName();//积分类别
                Integer integralValue = detail.getIntegral() == null ? 0 : detail.getIntegral();//积分
                detailMap.put("sourceName", sourceName);
                detailMap.put("value", integralValue.toString());
                detailMap.put("action", detail.getAction());
                Date date = detail.getCreateTime();
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                detailMap.put("time", date == null ? "" : f.format(date));
                details.add(detailMap);
            }
            integral.setDetails(details);
        }
        //可用积分
        ResultDto<IntegralAccountDto> accountBalanceDto = integralAccountFacadeService.getIntegralAccount(mainInfo.getCmNumber());
        if (accountBalanceDto.isSuccess()) {
            IntegralAccountDto accountDto = accountBalanceDto.getData();
            if (accountDto != null) {
                Integer balance = accountDto.getAvailableIntegral();
                integral.setIntegral(balance == null ? "0" : balance.toString());
            }
        } else {
            throw new HessianRpcException("integral.balance",accountBalanceDto.getMsg());
        }
        /*CustomerSign customerSign = new CustomerSign();
        customerSign.setCmNumber(mainInfo.getCmNumber());
        customerSign.setSignDate(new Date());
        boolean signed = customerSignService.isSign(customerSign);
        if (signed) {//已签到
            integral.setIsSign("1");
        } else {//未签到
            integral.setIsSign("0");
        }
        //添加签到积分和签到提示语两个字段
        CustomerSign sign = new CustomerSign();
        sign.setCmNumber(mainInfo.getCmNumber());
        //获取上一次签到记录
        CustomerSign lastSign = customerSignService.findOneByCmNumberOrderBySignDate(mainInfo.getCmNumber());
        if (lastSign == null) {
            sign.setSignTime(0);
        } else {
            sign.setSignTime(lastSign.getSignTime());
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        int signUpperLimit = CustomerSignService.SIGN_UPPER_LIMIT;
        integral.setSignPercent(df.format((double) sign.getSignTime() / (double) signUpperLimit));
        integral.setSignStr("已连续签到" + sign.getSignTime() + "天，连续签到满" + signUpperLimit + "天送一张刮刮卡。");*/
        return integral;
    }

    public IntegralAccountBalanceDTO integralBalance(Long customerId) {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        //可用积分
        ResultDto<IntegralAccountDto> resultDto = integralAccountFacadeService.getIntegralAccount(mainInfo.getCmNumber());
        if (resultDto.isSuccess()) {
            IntegralAccountBalanceDTO integral = new IntegralAccountBalanceDTO();
            IntegralAccountDto accountDto = resultDto.getData();
            Integer balance = accountDto.getAvailableIntegral();
            integral.setIntegral(balance == null ? 0 : balance);
            return integral;
        } else {
            throw new HessianRpcException("integral.balance",resultDto.getMsg());
        }
    }


    public Map<String, String> getShareUrlBySessionToken(String sessionToken) {
        if (StringUtils.isEmpty(sessionToken)) {
            throw new BusinessException("customer.not.login");
        }
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
        CustomerMainInfo main = customerMainInfoService.findOneByCmNumber(cmNumber);
        return getShareUrl(main);
    }

    public Map<String, String> getShareUrlByCustomerId(Long customerId) {
        CustomerMainInfo main = customerMainInfoService.findOne(customerId);
        return getShareUrl(main);
    }

    public Map<String, String> getShareUrl(CustomerMainInfo main){
        Map<String, String> map = Maps.newHashMap();
        if (main != null) {
            if (!"3".equals(main.getCmStatus().toString())) {
                map.put("isAuth", "0");
            } else {
                map.put("isAuth", "1");
                map.put("url", main.getCmInviteCode() == null ? "" : configParamBean.getQrCodeurl() + main.getCmInviteCode());
                map.put("inviteCode", main.getCmInviteCode() == null ? "" : main.getCmInviteCode());
                map.put("customerId", main.getId().toString());
            }
        } else {
            map.put("isAuth", "2");
        }
        return map;
    }

    /**
     * 分享送刮刮卡
     *
     * @param cmToken
     * @return
     */
    public void share(String cmToken) {
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(cmToken, configParamBean.getUserTokenKey());
        CustomerMainInfo mainInfo = checkExistByCmNumber(cmNumber);
        SysParameter reLotteryCode = sysParameterService.findOneByPrType("reLotteryCode");
        if (reLotteryCode == null) {
            log.error("数据字典没有配置刮刮卡代码！本次分享送刮刮卡请求不发送，customerId=" + mainInfo.getId());
        } else {
            //送刮刮卡
            ShareMessage shareMessage = new ShareMessage();
            shareMessage.setCmNumber(mainInfo.getCmNumber());
            shareMessage.setLotteryCode(Integer.parseInt(reLotteryCode.getPrValue()));
            shareMessage.setCustomerId(mainInfo.getId());
            sendRocketMqMsg(BusiTypeEnum.SHARE, shareMessage);
        }
    }

    public GuaguakaDTO getGuaguaka(String cmToken) {
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(cmToken, configParamBean.getUserTokenKey());
        CustomerMainInfo mainInfo = checkExistByCmNumber(cmNumber);
        GuaguakaDTO dto = new GuaguakaDTO();
        SysParameter reLotteryCode = sysParameterService.findOneByPrType("reLotteryCode");
        if (reLotteryCode != null) {
            int lotteryType = Integer.parseInt(reLotteryCode.getPrValue());
            ResultDto<LotteryQualificationForType> resultDto = lotteryQualificationFacadeService.getLotteryQualification(mainInfo.getCmNumber(), lotteryType);
            if (resultDto.isSuccess()) {
                dto.setGuaguaka(resultDto.getData().getLotteryQuantity());
                return dto;
            } else {
                throw new HessianRpcException("lotteryCode.record", resultDto.getMsg());
            }
        } else {
            throw new BusinessException("lotteryCode.not.config");
        }
    }

    public Map<String, Object> cashInfo(Long customerId, Integer pageSize, Integer pageNo) {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        Map<String, Object> resultMap = Maps.newHashMap();
        CashSearchDto searchDto = new CashSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        searchDto.setPageNo(pageNo);
        searchDto.setPageSize(pageSize);

        PageResultDto<CashSearchResDto> resultDto = cashFacadeService.getPageCashByAccountNo(searchDto);
        resultMap.put("pageSize",pageSize);
        resultMap.put("pageNo",resultDto.getPageNo());
        resultMap.put("totalPage",resultDto.getTotalPage());
        resultMap.put("totalSize",resultDto.getTotalSize());
        if (resultDto.isSuccess()) {
            List<CashSearchResDto> resList = resultDto.getDataList();
            List<CashSearchResInfo> list = new ArrayList<>();
            for (CashSearchResDto dto : resList){
                CashSearchResInfo info = new CashSearchResInfo(dto);
                list.add(info);
            }
            resultMap.put("cashInfoList", list);
        } else {
            throw new HessianRpcException("cash.record",resultDto.getMsg());
        }
        return resultMap;
    }

}
