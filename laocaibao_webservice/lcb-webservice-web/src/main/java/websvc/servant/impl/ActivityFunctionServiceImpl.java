package websvc.servant.impl;

import com.google.common.collect.Maps;
import com.zdmoney.common.CompanyAccounts;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.ActivityConstant;
import com.zdmoney.enm.SerialNoType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.coin.CoinOprDto;
import com.zdmoney.integral.api.dto.coin.CoinOprResultDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveResDto;
import com.zdmoney.integral.api.dto.lcbaccount.AccountOprResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.CustomerCashBackDto;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.integral.api.facade.ICoinFacadeService;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.integral.api.utils.DateUtils;
import com.zdmoney.mapper.BusiCashFlowMapper;
import com.zdmoney.mapper.CustInviteLineMapper;
import com.zdmoney.mapper.customer.CustomerAuthorizeMapper;
import com.zdmoney.mapper.lottey.LotteryCouponRuleMapper;
import com.zdmoney.mapper.lottey.LotteryInvitationRecordMapper;
import com.zdmoney.models.BusiCashFlow;
import com.zdmoney.models.InvestFriendsInfo;
import com.zdmoney.models.customer.CustomerAuthorizeInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.lottey.LotteryCouponRule;
import com.zdmoney.models.lottey.LotteryInvestAlert;
import com.zdmoney.models.lottey.LotteryInvitationRecord;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.secure.utils.ThreeDesUtil;
import com.zdmoney.service.*;
import com.zdmoney.service.lottery.LotteryInvestAlertService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.vo.ActivityOrderVo;
import com.zdmoney.vo.OrderProductExit;
import com.zendaimoney.lottery.api.dto.LotteryShareDto;
import com.zendaimoney.lottery.api.facade.ILotteryFacadeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.ActivityFunctionService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 活动接口
 * <p/>
 * Author: silence.cheng
 * Date: 2016-09-19 11:10
 */

@Service
public class ActivityFunctionServiceImpl implements ActivityFunctionService {

    private static Log log = LogFactory.getLog(ActivityFunctionServiceImpl.class);

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private LotteryInvestAlertService lotteryInvestAlertService;

    @Autowired
    private CustInviteLineMapper custInviteLineMapper;

    @Autowired
    private LotteryInvitationRecordMapper lotteryInvitationRecordMapper;

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    private LotteryCouponRuleMapper lotteryCouponRuleMapper;

    @Autowired
    private BusiMallService busiMallService;

    @Autowired
    private ICoinFacadeService coinFacadeService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ILotteryFacadeService lotteryFacadeService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private CustomerAuthorizeMapper customerAuthorizeMapper;

    @Autowired
    private CustomerMemberLevelService customerMemberLevelService;

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private IAccountFacadeService accountFacadeService;

    @Autowired
    private BusiCashFlowMapper busiCashFlowMapper;

    @FunctionId("905001")
    public Result getOrderPayRankList(ReqMain reqMain) throws Exception {
        Model_905001 model_905001 = (Model_905001)reqMain.getReqParam();
        String sessionToken=model_905001.getSessionToken();
        if (StringUtils.isBlank(sessionToken)){
            throw new BusinessException("sessionToken不能为空！");
        }
        CustomerMainInfo customerMainInfo=customerMainInfoService.findOneBySessionToken(sessionToken);
        if (customerMainInfo==null){
            throw new BusinessException("查询不到用户信息！");
        }

        Map queryMap = Maps.newTreeMap();
        queryMap.put("startDate", ActivityConstant.ACTIVITY_START_DATE);
        queryMap.put("endDate", ActivityConstant.ACTIVITY_END_DATE);
        queryMap.put("limitType", 0);
        //查询某一时间段订单记录
        List<BusiOrder> busiOrders= busiOrderService.selectOrderPayByDate(queryMap);

        queryMap.clear();
        queryMap.put("startDate", ActivityConstant.ACTIVITY_START_DATE);
        queryMap.put("endDate", ActivityConstant.ACTIVITY_END_DATE);
        queryMap.put("limitType", 0);
        queryMap.put("customerId",customerMainInfo.getId());

        //查询用户某一时间段订单记录
        List<BusiOrder> userOrders= busiOrderService.selectOrderPayByDate(queryMap);

        ActivityOrderVo activityOrderVo=new ActivityOrderVo();
        activityOrderVo.setIsBuy("N");
        activityOrderVo.setInRank("N");

        //活动订单数是否超过100
        if (CollectionUtils.isEmpty(busiOrders) || (CollectionUtils.isNotEmpty(busiOrders) && busiOrders.size()<=100)){
            activityOrderVo.setInRank("Y");
        }
        if (CollectionUtils.isNotEmpty(userOrders)){
            activityOrderVo.setIsBuy("Y"); //活动期间是否购买
          //  System.out.println("你已经投资获得过捞财币");
        }
        return Result.success(activityOrderVo);
    }

    @FunctionId("905002")
    public Result getOrderTotalAmtRankList(ReqMain reqMain) throws Exception {
        ActivityOrderVo activityOrderVo=new ActivityOrderVo();
        Model_905002 model_905002 = (Model_905002)reqMain.getReqParam();
        String sessionToken=model_905002.getSessionToken();
        if (StringUtils.isBlank(sessionToken)){
            return Result.fail("sessionToken不能为空！");
        }
        CustomerMainInfo customerMainInfo=customerMainInfoService.findOneBySessionToken(sessionToken);
        if (customerMainInfo==null){
            return Result.fail("用户信息不存在");
        }

        Map queryMap = Maps.newTreeMap();
        queryMap.put("startDate", ActivityConstant.ACTIVITY_START_DATE);
        queryMap.put("endDate", ActivityConstant.ACTIVITY_END_DATE);
        queryMap.put("limitType", 0);

        //查询用户某一时间段订单总额
        List orderPayList= busiOrderService.selectOrderPayTotalAmtByDate(queryMap);

        activityOrderVo.setOrderPayList(orderPayList);
        return Result.success(activityOrderVo);
    }



    /**
     * 到期弹层提示活动初始化
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("905003")
    public Result expireRemindInit(ReqMain reqMain) throws Exception {
        Map jsonMap = new HashMap();
        String customerId="";
        String alertDate = "";
        int isEmployees = 0;
        int layerStatus = 0;
        int windowStatus = 0;
        Date today = new Date();
        Date alertDay = new Date();
        BigDecimal totalInterest= new BigDecimal("0");
        String productLevelAlert="0",productLevelAds="0",signUrl="";
        Map authInfos;
        Model_905003 model_905002 = (Model_905003)reqMain.getReqParam();
        customerId=model_905002.getCustomerId();
        if (StringUtils.isBlank(customerId)){
            return Result.fail("customerId不能为空！");
        }
        CustomerMainInfo customerMainInfo=customerMainInfoService.findAuthCustomerById(Long.parseLong(customerId));
        if (customerMainInfo==null){
            return Result.fail("用户信息不存在");
        }
        boolean lock = false;
        String LOCK = "EXPIRE-REMIND-LOCK-" + customerMainInfo.getId();
        String KEY = "REMIND_LOCK-" + customerMainInfo.getId();
        try {
            // 获取锁
            lock = redisSessionManager.setNX(KEY, LOCK);
            System.out.println(Thread.currentThread().getName() + "当前线程是否获取到锁:" + lock);
            if(lock) {
                // 1分钟后,key值失效,自动释放锁
                redisSessionManager.expire(KEY, 1, TimeUnit.MINUTES);
                //查询是否是内部员工
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("realName", customerMainInfo.getCmRealName());
                paramMap.put("idNum", customerMainInfo.getCmIdnum());
                isEmployees = custInviteLineMapper.isInsideStaff(paramMap);
                //查询用户是否有弹窗记录 如有 需要展示浮窗
                Map alertParams = Maps.newTreeMap();
                String startDate = DateUtils.format(DateUtils.plusDays(today, -7), "yyyy-MM-dd hh:mm:ss");
                alertParams.put("cmNumber",customerMainInfo.getCmNumber());
                alertParams.put("startDate",startDate);
                List<LotteryInvestAlert> lotteryInvestAlerts = lotteryInvestAlertService.selectAlertBycmNumAndDate(alertParams);
                if(lotteryInvestAlerts != null && lotteryInvestAlerts.size() == 1){
                    windowStatus = 1 ;//展示浮窗
                    alertDay = lotteryInvestAlerts.get(0).getCreateDate();//用户第一次进入活动时间
                    Date shareStartDate = lotteryInvestAlerts.get(0).getShareStartDate();
                    if( shareStartDate != null){
                        HashMap<String, Object> checkParams = Maps.newHashMap();
                        checkParams.put("cmName",customerMainInfo.getCmRealName());
                        checkParams.put("cmNumber",customerMainInfo.getCmNumber());
                        checkParams.put("cellPhone",customerMainInfo.getCmCellphone());
                        checkParams.put("startDate",DateUtils.format(shareStartDate));
                        checkParams.put("endDate",DateUtils.format(DateUtils.plusDays(shareStartDate,7)));
                        checkParams.put("couponType",lotteryInvestAlerts.get(0).getId());
                        checkParams.put("channelCode","investAlert");
                        this.checkSendRed(checkParams);
                    }
                }

                //用户赚取利息查询时间区间
                Date startDay = DateUtils.plusDays(alertDay, -1);
                Date endDay = DateUtil.plusDay(today, -1);
                String startday = DateUtils.format(startDay, "yyyy-MM-dd");
                String endday = DateUtils.format(endDay, "yyyy-MM-dd");

                Map orderParams = Maps.newTreeMap();
                orderParams.put("customerId",customerMainInfo.getId());
                orderParams.put("startDate",startday+" 00:00:00");
                orderParams.put("endDate",endday+" 23:59:59");
                List<BusiOrder> busiOrders = busiOrderService.selectOrderByCustomerIdAndDate(orderParams);
                if(busiOrders != null && busiOrders.size() > 0){//用户昨日有投资到期
                    //查询之前是否已经弹框 如果未弹框 计算用户总收益
                    if(lotteryInvestAlerts == null ||  lotteryInvestAlerts.size() == 0 ){
                        LotteryInvestAlert lotteryInvestAlert = new LotteryInvestAlert();
                        lotteryInvestAlert.setCmNumber(customerMainInfo.getCmNumber());
                        lotteryInvestAlert.setCmName(customerMainInfo.getCmRealName());
                        lotteryInvestAlert.setCreateBy(customerMainInfo.getCmRealName());
                        lotteryInvestAlert.setCreateDate(DateUtils.parse(DateUtils.format(today,"yyyy-MM-dd")+" 00:00:00"));
                        if(lotteryInvestAlertService.insert(lotteryInvestAlert) == 1){
                            layerStatus = 1;//展示弹层
                            windowStatus = 1 ;//展示浮窗
                            alertDate = DateUtils.format   (today,"MM月dd日")+"-"+DateUtils.format(DateUtil.plusDay(today, 7),"MM月dd日");
                        }
                    }

                    for (BusiOrder busiOrder : busiOrders) {//计算用户总收益
                        BigDecimal principalinterest = busiOrder.getPrincipalinterest();//本息
                        BigDecimal orderAmt = busiOrder.getOrderAmt();//本金
                        totalInterest = totalInterest.add(principalinterest.subtract(orderAmt));
                    }
                }
                //授权是否充足
               // authInfos = busiOrderService.userGrantFlag(Long.parseLong(customerId),0,null);// 0:充足 1：不足

            }else{
                return Result.fail("没有获取到锁，不执行弹层初始化!");
            }
        }finally{
            if(lock){// 如果获取了锁，则释放锁
                redisSessionManager.remove(KEY);
                System.out.println(Thread.currentThread().getName() + "请求结束，释放锁!");
            }
        }
        jsonMap.put("alertDate",alertDate);
        jsonMap.put("totalInterest",totalInterest.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()+"");//总共赚取的金额
        jsonMap.put("isEmployees",isEmployees);//是否内部用户 0:非内部用户 1:内部用户
        jsonMap.put("layerStatus",layerStatus);//前端是否弹弹层 0:不弹 1:弹
        jsonMap.put("windowStatus",windowStatus);//前端是否展示浮窗 0:不展示  1:展示
        jsonMap.put("shareUrl",configParamBean.getShareUrl());//活动分享Url
        //jsonMap.put("authInfos",authInfos);//0： 授权充足 1： 授权不足

        return Result.success(jsonMap);
    }

    /**
     * 到期弹层提示发红包
     * @param params
     */
    @Async
    private void checkSendRed(Map<String,Object> params){
        int authNum = 0;
        int flag = 3;
        //获取用户邀请好友完成实名认证数量
        List<InvestFriendsInfo> investFriendsInfos = custInviteLineMapper.queryHelpFriends(params);
        if(investFriendsInfos != null || investFriendsInfos.size() > 0){
            authNum = investFriendsInfos.size();
        }
        if(authNum == 0){
            return;
        }
        //查询用户的发红包纪录
        params.put("typeNo",configParamBean.getInvestAlertTypeNo());
        int redCount = lotteryInvitationRecordMapper.countByCmNumAndTypeNo(params);
        if(redCount < 6 && redCount < authNum) {
            for (int i = redCount; i < authNum && i<6; i++) {
                LotteryInvitationRecord lotteryInvitationRecord = new LotteryInvitationRecord();
                lotteryInvitationRecord.setCmNumber(params.get("cmNumber").toString());
                lotteryInvitationRecord.setTypeNo(configParamBean.getInvestAlertTypeNo());//活动编号
                lotteryInvitationRecord.setCmName(params.get("cmName").toString());
                lotteryInvitationRecord.setCmCellPhone(params.get("cellPhone").toString());
                lotteryInvitationRecord.setInvestName(investFriendsInfos.get(i).getInvestName());
                lotteryInvitationRecord.setInvestPhone(investFriendsInfos.get(i).getInvitedCellPhone());
                lotteryInvitationRecord.setInvestId(investFriendsInfos.get(i).getInvestId());
                lotteryInvitationRecord.setCouponType(params.get("couponType").toString());
                lotteryInvitationRecord.setCreateDate(new Date());
                //查询红包规则
                Long id = 0L;
                int i1 = (int)Math.ceil((i+1) / 2D);
                if (i1 == 1) {
                    id = Long.parseLong(configParamBean.getCoupon5());
                } else if (i1 == 2) {
                    id = Long.parseLong(configParamBean.getCoupon10());
                } else {
                    id = Long.parseLong(configParamBean.getCoupon50());
                }
                //查询红包规则
                LotteryCouponRule lotteryCouponRule = lotteryCouponRuleMapper.selectByPrimaryKey(id);
                if (lotteryCouponRule == null) {
                    return;
                }
                lotteryInvitationRecord.setAmount(new BigDecimal(lotteryCouponRule.getAmount()));
                int insert = lotteryInvitationRecordMapper.insert(lotteryInvitationRecord);
                if (insert == 1) { //发红包
                    CouponGiveDto couponDto = new CouponGiveDto();
                    couponDto.setAccountNo(params.get("cmNumber").toString());
                    couponDto.setSource("INVESTCOUPON");
                    couponDto.setAmount(lotteryCouponRule.getAmount());
                    couponDto.setInvestAmount(lotteryCouponRule.getInvestAmount());
                    couponDto.setPeriod(lotteryCouponRule.getPeriod());
                    couponDto.setInvestPeriod(lotteryCouponRule.getInvestPeriod());
                    couponDto.setTransNo(SerialNoGeneratorService.generateTransferNoByCmNum(params.get("cmNumber").toString()));
                    ResultDto<CouponGiveResDto> couponGiveResDtoResultDto = welfareService.gainCoupon(couponDto);
                    if (!couponGiveResDtoResultDto.isSuccess()) {//发红包失败
                        log.info("弹窗发红包活动发送红包失败:couponDto{}" + couponDto+" | msg:"+couponGiveResDtoResultDto.getMsg());return;
                    }
                }else{
                    log.error("用户弹层活动插入记录表失败:"+lotteryInvitationRecord);
                }
            }
        }
    }

    /**
     * 用户弹层活动 更新首次分享时间接口
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("905004")
    public Result updateFirstShareTime(ReqMain reqMain) throws Exception{
        String customerId="";
        Date today = new Date();
        Model_905004 model_905004 = (Model_905004)reqMain.getReqParam();
        customerId=model_905004.getCustomerId();
        if (StringUtils.isBlank(customerId)){
            return Result.fail("customerId不能为空！");
        }
        CustomerMainInfo customerMainInfo=customerMainInfoService.findAuthCustomerById(Long.parseLong(customerId));
        if (customerMainInfo==null){
            return Result.fail("用户信息不存在");
        }
        //查询用户是否有弹窗记录 如有 需要展示浮窗
        Map alertParams = Maps.newTreeMap();
        String startDate = DateUtils.format(DateUtils.plusDays(today, -7), "yyyy-MM-dd hh:mm:ss");
        //String endDate = DateUtils.format(DateUtils.plusDays(today, 7), "yyyy-MM-dd hh:mm:ss");
        alertParams.put("cmNumber",customerMainInfo.getCmNumber());
        alertParams.put("startDate",startDate);
        //alertParams.put("endDate",endDate);
        List<LotteryInvestAlert> lotteryInvestAlerts = lotteryInvestAlertService.selectAlertBycmNumAndDate(alertParams);
        if(lotteryInvestAlerts != null && lotteryInvestAlerts.size() == 1){
            LotteryInvestAlert lotteryInvestAlert = lotteryInvestAlerts.get(0);
            if(lotteryInvestAlert.getShareStartDate() == null){
                //更新
                lotteryInvestAlert.setShareStartDate(DateUtils.parse(DateUtils.format(today,"yyyy-MM-dd")+" 00:00:00"));
                int i = lotteryInvestAlertService.updateShareDateByPrimaryKey(lotteryInvestAlert);
                if(i == 1){
                    log.info("更新首次分享时间成功!{}"+lotteryInvestAlert);
                    return Result.success();
                }
            }
        }
        return  Result.fail();
    }

    @FunctionId("906001")
    @Override
    public Result craneMachineInit(ReqMain reqMain) throws Exception {
        Model_906001 model = (Model_906001)reqMain.getReqParam();
        Map<String, Object> jsonMap = new HashMap<>();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(model.getCmNumber());
        if (customerMainInfo==null){
            return Result.fail("用户信息不存在");
        }
        // 查询捞财币数量
        Long coinBalance = busiMallService.getCoinBalance(model.getCmNumber());
        jsonMap.put("coinBalance", coinBalance);
        // 兑换捞财币金额
        long exchangeCoin = lotteryInvestAlertService.queryExchangeCoinCount();
        jsonMap.put("exchangeCoin", exchangeCoin);
        return Result.success(jsonMap);
    }

    @FunctionId("906002")
    @Override
    public Result getCraneMachineChance(ReqMain reqMain) throws Exception {
        Model_906002 model = (Model_906002)reqMain.getReqParam();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(model.getCmNumber());
        if (customerMainInfo == null){
            return Result.fail("用户信息不存在");
        }
        long exchangeCoin = lotteryInvestAlertService.queryExchangeCoinCount();
        Long coinBalance = busiMallService.getCoinBalance(model.getCmNumber());
        if (coinBalance < exchangeCoin){
            return Result.fail("捞财币不足", coinBalance);
        }
        CoinOprDto oprDto = new CoinOprDto();
        oprDto.setAccountNo(customerMainInfo.getCmNumber());
        oprDto.setTips("兑换:抓公仔");
        oprDto.setCoin(exchangeCoin);
        oprDto.setOrderNo("DH"+System.currentTimeMillis());
        oprDto.setTransNo(SerialNoGeneratorService.generateTransferNoByCmNum(customerMainInfo.getCmNumber()));
        ResultDto<CoinOprResultDto> resultDto = coinFacadeService.consumeCoin(oprDto);
        return resultDto.isSuccess() ? Result.success(busiMallService.getCoinBalance(model.getCmNumber())) : Result.fail("捞财币兑换抓公仔机会失败!");
    }

    @FunctionId("906003")
    @Override
    public Result activityTransfer(ReqMain reqMain) throws Exception {
        Model_906003 model = (Model_906003)reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(model.getSessionToken());
        if (mainInfo == null){
            return Result.fail("用户信息不存在");
        }
        LotteryShareDto lotteryShareDto = new LotteryShareDto();
        lotteryShareDto.setCmNumber(mainInfo.getCmNumber());
        lotteryShareDto.setTypeNo(model.getTypeNo());
        lotteryShareDto.setShareType(model.getShareType());
        lotteryFacadeService.addShareLotteryQualification(lotteryShareDto);
        return Result.success();
    }

    @FunctionId("906004")
    @Override
    public Result scratchCardInit(ReqMain reqMain) throws Exception {
        Model_906004 model = (Model_906004)reqMain.getReqParam();
        Map<String, Object> jsonMap = new HashMap<>();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(model.getCmNumber());
        if (customerMainInfo==null){
            return Result.fail("用户信息不存在");
        }
        // 查询捞财币数量
        Long coinBalance = busiMallService.getCoinBalance(model.getCmNumber());
        jsonMap.put("coinBalance", coinBalance);
        // 兑换捞财币金额
        long exchangeCoin = lotteryInvestAlertService.queryCoinCount();
        jsonMap.put("exchangeCoin", exchangeCoin);
        return Result.success(jsonMap);
    }

    @FunctionId("906005")
    @Override
    public Result scratchCardChance(ReqMain reqMain) throws Exception {
        Model_906005 model = (Model_906005)reqMain.getReqParam();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(model.getCmNumber());
        if (customerMainInfo == null){
            return Result.fail("用户信息不存在");
        }
        long exchangeCoin = lotteryInvestAlertService.queryCoinCount();
        Long coinBalance = busiMallService.getCoinBalance(model.getCmNumber());
        if (coinBalance < exchangeCoin){
            return Result.fail("捞财币不足", coinBalance);
        }
        CoinOprDto oprDto = new CoinOprDto();
        oprDto.setAccountNo(customerMainInfo.getCmNumber());
        oprDto.setTips("兑换:黄金刮刮卡");
        oprDto.setCoin(exchangeCoin);
        oprDto.setOrderNo("DH"+System.currentTimeMillis());
        oprDto.setTransNo(SerialNoGeneratorService.generateTransferNoByCmNum(customerMainInfo.getCmNumber()));
        ResultDto<CoinOprResultDto> resultDto = coinFacadeService.consumeCoin(oprDto);
        return resultDto.isSuccess() ? Result.success(busiMallService.getCoinBalance(model.getCmNumber())) : Result.fail("捞财币兑换刮刮卡失败!");
    }

    @FunctionId("907001")
    @Override
    public Result getUserLevel(ReqMain reqMain) throws Exception {
        Model_907001 model = (Model_907001)reqMain.getReqParam();
        String sessionToken = model.getSessionToken();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(sessionToken)) {
            String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            if (mainInfo == null) {
                return Result.fail("用户不存在");
            }
            Map resultMap = customerMemberLevelService.queryMemberLevel(mainInfo);
            return Result.success(resultMap);
        }else {
            return Result.fail("sessionToken不能为空");
        }
    }


    @FunctionId("908002")
    @Override
    public Result gainCash(ReqMain reqMain) throws Exception {
        Model_908002 model = (Model_908002)reqMain.getReqParam();
        CustomerMainInfo customerMainInfo = customerMainInfoService.validateCustomerInfo(model.getCustomerId());
        if (customerMainInfo == null){
            return Result.fail("用户信息不存在");
        }
        String cmNumber=customerMainInfo.getCmNumber();
        //String flowId = busiOrderService.buildCeNumber("CASH", "8888", model.getCustomerId());
        String flowId = "CASH_" + model.getCashNo() + "_" + cmNumber;
        CustomerCashBackDto cashBackDto = new CustomerCashBackDto();
        cashBackDto.setAccountNo(cmNumber);
        cashBackDto.setCouponAccountNo(CompanyAccounts.getCompanyAccounts().getGshbAccount());
        cashBackDto.setCouponLoginId(CompanyAccounts.getCompanyAccounts().getGshbAccountFuiouId());
        cashBackDto.setLoginId(customerMainInfo.getFuiouLoginId());
        cashBackDto.setCashNo(model.getCashNo());
        cashBackDto.setOrderNo(flowId);
        cashBackDto.setTransNo(flowId);

        ResultDto<AccountOprResultDto> resultDto;
        try{
            resultDto = accountFacadeService.customerCashBack(cashBackDto);
            if (!resultDto.isSuccess()) {
                log.error("领取现金券失败! 用户编号为:"+ cmNumber+ "  ,错误原因:"+resultDto.getMsg());
                return  Result.fail("领取现金券失败");
            }
        }catch (Exception e) {
            log.error("调用账户系统领取现金券异常"+ "cmNumber:" + cmNumber + ",现金券编号："+model.getCashNo() +"，异常信息:" + e);
            MailUtil.sendMail("调用账户系统领取现金券异常", "cmNumber:" + cmNumber + ",现金券编号："+model.getCashNo() +"，异常信息:" + e);
            return  Result.fail("领取现金券异常");
        }

        saveOpenCash(customerMainInfo,flowId,model,resultDto.getData());
        return  Result.success();
    }

    @Transactional
    private void saveOpenCash(CustomerMainInfo customerMainInfo, String flowId, Model_908002 model, AccountOprResultDto data){
        BusiCashFlow busiCashFlow = new BusiCashFlow();
        busiCashFlow.setCmName(customerMainInfo.getCmRealName());
        busiCashFlow.setCmNumber(customerMainInfo.getCmNumber());
        busiCashFlow.setCmPhone(ThreeDesUtil.encryptMode(customerMainInfo.getCmCellphone()));
        busiCashFlow.setTrdDate(new Date());
        busiCashFlow.setAccountSeriNo(data.getRecordNum());
        busiCashFlow.setStatus("0");
        busiCashFlow.setCashAmt(data.getAmount().longValue());
        busiCashFlow.setCashSource(model.getPublishSource());
        busiCashFlow.setCashNo(model.getCashNo());
        busiCashFlow.setFlowNum(flowId);

        busiCashFlowMapper.saveBusiCashFlow(busiCashFlow);
    }
}