package com.zdmoney.service.customer;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zdmoney.component.redis.KeyGenerator;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.gamecenter.api.dto.FragmentRecordDto;
import com.zdmoney.gamecenter.api.facade.IFragmentFacadeService;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.account.IntegralGiveDto;
import com.zdmoney.integral.api.dto.account.IntegralResDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentPublishDto;
import com.zdmoney.integral.api.dto.coin.CoinOprDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveDto;
import com.zdmoney.integral.api.dto.voucher.VoucherPublishDto;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.mapper.customer.CustomerSignInfoMapper;
import com.zdmoney.mapper.gameCenter.DailyTaskRecordMapper;
import com.zdmoney.mapper.payment.PaymentCalendarMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.customer.CustomerSignInfo;
import com.zdmoney.models.customer.CustomerSignLog;
import com.zdmoney.models.customer.CustomerSignRule;
import com.zdmoney.models.lottey.LotterySign;
import com.zdmoney.service.BusiMallService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.SerialNoGeneratorService;
import com.zdmoney.service.gameCenter.DailyTaskRecordService;
import com.zdmoney.service.lottery.LotterySignService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.vo.PaymentCalendar;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_550007;
import websvc.models.Model_550008;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by gosling on 2016/12/7.
 */
@Service
@Slf4j
public class CustomerSignInfoService{

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private CustomerSignInfoMapper customerSignInfoMapper;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private PaymentCalendarMapper paymentCalendarMapper;

    @Autowired
    private DailyTaskRecordMapper dailyTaskRecordMapper;

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private BusiMallService busiMallService;

    @Autowired
    private DailyTaskRecordService dailyTaskRecordService;

    @Autowired
    private IFragmentFacadeService fragmentFacadeService;

    @Autowired
    private LotterySignService lotterySignService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;

    /**
     * 用户签到
     * @param reqMain
     * @return
     * @throws Exception
     */
    public Result customerSign(ReqMain reqMain) throws Exception{
        Model_550007 model = (Model_550007) reqMain.getReqParam();
        Map<String, Object> resMap = new HashMap<>();
        try {
            // 校验用户是否存在
            CustomerMainInfo customerInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());
            // 查询用户当天是否已签到
            Long isSign = customerSignInfoMapper.queryNowadaysSignCount(customerInfo.getId());
            String key = "SIGN-" + DateTime.now().toString("yyyyMMdd") + model.getCustomerId();
            String val = redisSessionManager.get(key);
            if (isSign >= 1 && StringUtils.isNotEmpty(val)) {
                return Result.fail("用户ID:" + customerInfo.getId() + ",在" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "已完成签到,不能重复签到");
            } else {
                Integer integral = 1;
                IntegralGiveDto giveDto = new IntegralGiveDto();
                giveDto.setTransNo(key);
                giveDto.setIntegral(integral);
                giveDto.setNo(AppConstants.GIVE_INTEGRAL.SIGN);
                giveDto.setAccountNo(customerInfo.getCmNumber());
                giveDto.setPlatform(reqMain.getReqHeadParam().getPlatform());
                giveDto.setChannel(AppConstants.APPLICATION_CONTEXT_NAME);
                ResultDto<IntegralResDto> result = integralAccountFacadeService.giveGetIntegral(giveDto);
                if (result.isSuccess()) {
                    CustomerSignInfo signInfo = new CustomerSignInfo();
                    signInfo.setChestStatus(0);
                    signInfo.setCustomerId(customerInfo.getId());
                    signInfo.setSignDate(new Date());
                    signInfo.setStatus(1L);
                    customerSignInfoMapper.insertSignInfo(signInfo);

                    CustomerSignLog signLog = new CustomerSignLog();
                    signLog.setIntegral(integral);
                    signLog.setCustomerId(customerInfo.getId());
                    customerSignInfoMapper.insertSignLog(signLog);
                    redisSessionManager.put(key, model.getCustomerId() + ", already signed", 1, TimeUnit.DAYS);
                    resMap.put("msg", "恭喜您获得" + integral + "个积分");
                } else {
                    log.info("账户系统签到送积分失败:" + result.getMsg());
                }
            }
        } catch (Exception e) {
            log.info("签到失败, 原因:" + e);
            throw e;
        }
        return Result.success(resMap);

        /*// 校验用户是否存在
        CustomerMainInfo customerInfo = customerMainInfoService.findOne(model.getCustomerId());
        if(customerInfo == null) throw new BusinessException("customer.not.exist");

        boolean lock = false;
        String LOCK = "CUSTOMER-SIGN-LOCK-" + customerInfo.getId();
        String KEY = KeyGenerator.USER_SIGN.getKey() + customerInfo.getId();
        Map<String, Object> resMap = new HashMap<>();
        try {
            // 获取锁
            lock = redisSessionManager.setNX(KEY, LOCK);
            System.out.println(Thread.currentThread().getName() + "当前线程是否获取到锁:" + lock);
            if(lock){
                // 1分钟后,key值失效,自动释放锁
                redisSessionManager.expire(KEY, KeyGenerator.USER_SIGN.getTime(), KeyGenerator.USER_SIGN.getTimeUnit());

                // 如果当前用户当天已经签到,则不能再次签到
                Long isSign = customerSignInfoMapper.queryNowadaysSignCount(customerInfo.getId());
                if(isSign >= 1){
                    return Result.fail("用户ID:" + customerInfo.getId() + ",在" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "已完成签到,不能重复签到");
                }
                // 查询用户是否漏签
                Long yesterday = customerSignInfoMapper.queryYesterdaySignInfo(customerInfo.getId());
                if(yesterday == 0 || yesterday == null){
                    customerSignInfoMapper.updateSignStatus(customerInfo.getId());
                }
                // 保存用户签到记录
                int res = saveSignInfo(customerInfo.getId(),customerInfo.getCmNumber(),resMap);
                if (res == 0){
                    return Result.fail("用户ID:" + customerInfo.getId() + ",在" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "已完成签到,不能重复签到");
                }
                Long signCount = customerSignInfoMapper.querySignCount(customerInfo.getId());
                // 记录连续签到天数,30天一个周期. 超过30天时重新记录
                Long days = (signCount <= 30) ? signCount : 1;
                if(signCount > 30){
                    customerSignInfoMapper.updateSignStatus(customerInfo.getId());
                }
                // 记录用户通过签到获取的奖品日志和连续签到天数
                Map<String, Object> map = sendGiftMsg(customerInfo.getCmNumber(), days.intValue());

                if(map != null){
                    CustomerSignLog signLog = (CustomerSignLog) map.get("signLog");
                    signLog.setDays(days);
                    signLog.setCustomerId(customerInfo.getId());
                    customerSignInfoMapper.insertSignLog(signLog);
                    resMap.put("msg", map.get("msg"));
                    if(StringUtils.isBlank(resMap.get("awardType")+"") || !StringUtils.equals(resMap.get("awardType")+"","4")){
                        resMap.put("awardType", map.get("awardType"));
                    }
                }
            }else{
                return Result.fail("没有获取到锁，不执行签到!");
            }
        } finally {
            if(lock){// 如果获取了锁，则释放锁
                redisSessionManager.remove(KEY);
                System.out.println(Thread.currentThread().getName() + "请求结束，释放锁!");
            }
        }
        return Result.success(resMap);*/
    }

    /**
     * 查询用户签到详情
     * @param reqMain
     * @return
     * @throws Exception
     */
    public Result queryCustomerSignCount(ReqMain reqMain) throws Exception{
        Model_550008 model = (Model_550008) reqMain.getReqParam();

        CustomerMainInfo customerInfo = customerMainInfoService.checkCustomerId(model.getCustomerId());

        CustomerSignLog signLog = customerSignInfoMapper.querySignDetail(customerInfo.getId());
        List<CustomerSignInfo> signDateList = customerSignInfoMapper.queryCustomerSignDate(customerInfo.getId());
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("signDateList", signDateList);
        msgMap.put("integral", signLog.getIntegral());
        return Result.success(msgMap);

        /*CustomerMainInfo customerInfo = customerMainInfoService.findOne(model.getCustomerId());
        if(customerInfo == null) throw new BusinessException("customer.not.exist");

        CustomerSignLog signLog = customerSignInfoMapper.querySignDetail(customerInfo.getId());
        List<CustomerSignInfo> signDateList = customerSignInfoMapper.queryCustomerSignDate(customerInfo.getId());
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("fragmentStatus",0); //送碎片状态 0:未送 1:已送
        Result result = null;
        if(signLog != null && signDateList != null){
            StringBuilder predict = new StringBuilder();

            String signInfo = "+" + signLog.getCoin() + "捞财币";
            predict.append("已连签" + signLog.getDays() + "天");
            int awardType = 0;

            if(signLog.getDays() == 7){
                awardType = 1;
                predict.append(",送1张加息券");
            }else if(signLog.getDays() == 15){
                awardType = 2;
                predict.append(",送" + signLog.getRedPacketMoney() + "元红包");
            }else if(signLog.getDays() == 30){
                awardType = 3;
                predict.append(",送1张预约券");
            }else if(signLog.getDays() < 7){
                predict.append(",再连续签到" + (7 - signLog.getDays()) + "天送1张加息券");
            }else if(signLog.getDays() > 7 && signLog.getDays() < 15){
                predict.append(",再连续签到" + (15 - signLog.getDays()) + "天送1个红包");
            }else if(signLog.getDays() > 15 && signLog.getDays() < 30){
                predict.append(",再连续签到" + (30 - signLog.getDays()) + "天送1张预约券");
            }
            msgMap.put("elephantParkUrl", configParamBean.getElephantParkUrl());//小象乐园Url
            msgMap.put("awardType", awardType);
            msgMap.put("signInfo", signInfo);
            msgMap.put("predict", predict.toString());
            msgMap.put("signDays", signLog.getDays());
            msgMap.put("signDesc",cumulativeSignDesc(customerInfo.getCmNumber(),msgMap));

            signLog.setSignDateList(signDateList);

            List<CustomerSignInfo> tempList = new ArrayList<>();
            for(CustomerSignInfo sign : signDateList){
                if(sign.getStatus() == 1){
                    Date first = sign.getSignDate();
                    tempList.add(addCustomerSignInfo(first, 7));
                    tempList.add(addCustomerSignInfo(first, 15));
                    tempList.add(addCustomerSignInfo(first, 30));
                    break;
                }
            }

            for(CustomerSignInfo sign1 : signDateList){
                for(CustomerSignInfo sign2 : tempList){
                    if(sign1.getSignDate().equals(sign2.getSignDate()) & sign1.getStatus() == 1){
                        sign2.setStatus(3L);
                        sign1.setStatus(3L);
                    }
                }
            }
            List<CustomerSignInfo> customerSignInfos = foreseeAwards(customerInfo.getCmNumber(), tempList);
            signDateList.addAll(customerSignInfos);

            Set set = new  HashSet();
            List newList = new ArrayList<>();
            for(Iterator iter = signDateList.iterator(); iter.hasNext();) {
                CustomerSignInfo sign = (CustomerSignInfo) iter.next();
                if(set.add(sign)) newList.add(sign);
                if(sign.getStatus() == 0) sign.setStatus(1L);
            }

            // 查询捞财币数量
            Long coinBalance = busiMallService.getCoinBalance(customerInfo.getCmNumber());
            msgMap.put("coinBalance", coinBalance);
            msgMap.put("signDateList", newList);
            // msgMap.putAll(cashTransferPlan(model.getCustomerId()));
            result = Result.success(msgMap);
        }else{
            result = Result.fail("数据异常");
        }
        return result;*/
    }

    /**
     * 回款计划
     * @param customerId
     * @return
     */
    private Map<String, Object> cashTransferPlan(Long customerId) {
        Map<String, Object> map = new HashMap<>();
        List<PaymentCalendar> paymentReceives = paymentCalendarMapper.selectPaymentCalendar(customerId);

        double lastMonthReceive = 0d;
        double currentMonthReceive = 0d;
        double currentMonthUnReceive = 0d;
        double nextMonthUnReceive = 0d;
        double lastUnReceiveMoney = 0d;

        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 上个月
        String lastMonth = sdf.format(getMonth(date, -1));
        // 当月
        String currentMonth = sdf.format(getMonth(date, 0));
        // 下个月
        String nextMonth = sdf.format(getMonth(date, 1));

        for (PaymentCalendar pc : paymentReceives) {
            // 已回款
            if ("receive".equals(pc.getReceiveStatus())) {
                if (pc.getReceiveMonth().equals(lastMonth)){ // 上个月已回款总额
                    lastMonthReceive += pc.getTotalPrincipalInterest().doubleValue();
                } else if (pc.getReceiveMonth().equals(currentMonth)) { // 当月已回款总额
                    currentMonthReceive += pc.getTotalPrincipalInterest().doubleValue();
                }
            } else if ("unReceive".equals(pc.getReceiveStatus())){ // 待回款
                if (pc.getReceiveMonth().equals(currentMonth)) { // 当月待回款总额
                    currentMonthUnReceive += pc.getTotalPrincipalInterest().doubleValue();
                } else if (pc.getReceiveMonth().equals(nextMonth)) { // 下个月待回款总额
                    nextMonthUnReceive += pc.getTotalPrincipalInterest().doubleValue();
                } else if (pc.getReceiveMonth().equals(lastMonth)) {
                    lastUnReceiveMoney += pc.getTotalPrincipalInterest().doubleValue();
                }
            }
        }
        Map<String, Object> paymentMap = new HashMap<>();
        // 上个月
        paymentMap.put("last_receiveMoney", lastMonthReceive);
        paymentMap.put("last_unReceiveMoney", lastUnReceiveMoney);
        // 当月
        paymentMap.put("current_receiveMoney", currentMonthReceive);
        paymentMap.put("current_unReceiveMoney", currentMonthUnReceive);
        // 下个月
        paymentMap.put("next_receiveMoney", 0d);
        paymentMap.put("next_unReceiveMoney", nextMonthUnReceive);

        map.put("returnedMoney", paymentMap);
        map.put("paymentReceives", paymentReceives);
        return map;
    }

    /**
     *  @Description: 预期获奖日期集合
     *  @author huangcy
     *  @date 2017/8/28
    */
    private List<CustomerSignInfo> foreseeAwards(String cmNumber,List<CustomerSignInfo> tempList){
        //查询用户累计签到天数
        Map<String, Object> params = new HashMap<>();
        Date nowDate = new Date();
        params.put("cmNumber",cmNumber);
        params.put("startDate",new Date(DateUtil.beginOfMonth(nowDate).getTime()));
        params.put("endDate",new Date(DateUtil.endOfMonth(nowDate).getTime()));
        int signNum = dailyTaskRecordMapper.countSameMonthSigns(params);
        //获取累计签到多少天能送宝箱
        LotterySign byDate = lotterySignService.findByDate();
        if (byDate == null){
            log.error("查询T_LOT_LOTTERY_SIGN失败 !method=[{}], params=[{}]","cumulativeSign","");return tempList;
        }
        int signDays = byDate.getSignDays();
        //再签到多少天送宝箱
        int i = signDays - (signNum % signDays)+1;
        int ii = 0;
        long thisMonthTime = DateUtil.endOfMonth(DateUtil.endOfMonth(nowDate)).getTime();
        long nextMonthTime = DateUtil.endOfMonth(DateUtil.nextMonth()).getTime();
        Date firstDay = DateUtil.beginOfMonth(DateUtil.nextMonth());
        List<CustomerSignInfo> chestList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while(addDate(nowDate,(i+signDays*ii)).getTime()<=thisMonthTime){//本月
            if (i == 0){
                i+=1;continue;
            }
            CustomerSignInfo customerSignInfo = addChestInfo(nowDate, (i + signDays * ii));
            chestList.add(customerSignInfo);
            ii+=1;
        }
        int b = 1;
        while (addDate(firstDay,(signDays*b)).getTime()<=nextMonthTime){//下月
            CustomerSignInfo customerSignInfo = addChestInfo(firstDay, (signDays*b));
            chestList.add(customerSignInfo);
            b+=1;
        }
        //排序+去重
        for (CustomerSignInfo customerSignInfo : tempList) {
            int a = 0;
            for (CustomerSignInfo signInfo : chestList) {
                if (sdf.format(customerSignInfo.getSignDate()).compareTo(sdf.format(signInfo.getSignDate()))<0){
                    chestList.add(a,customerSignInfo);break;
                }
                a+=1;
                if (a>=chestList.size()){
                    chestList.add(customerSignInfo);break;
                }
            }
        }
        return chestList;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.endOfMonth(new Date()).toString());
        System.out.println((Date)DateUtil.beginOfMonth(DateUtil.nextMonth()));
        //System.out.println(DateUtil.end);
    }

    /**
     *  @Description: 获取签到红包碎片描述
     *  @author huangcy
     *  @date 2017/8/4
    */
    private String cumulativeSignDesc(String cmNumber,Map<String, Object> msgMap){
        //查询用户当月累计签到次数
        Map<String, Object> params = new HashMap<>();
        Date nowDate = new Date();
        params.put("cmNumber",cmNumber);
        params.put("startDate",new Date(DateUtil.beginOfMonth(nowDate).getTime()));
        params.put("endDate",new Date(DateUtil.endOfMonth(nowDate).getTime()));
        int signNum = dailyTaskRecordMapper.countSameMonthSigns(params);
        //获取累计签到多少天能送宝箱
        LotterySign byDate = lotterySignService.findByDate();
        if (byDate == null){
            log.error("查询T_LOT_LOTTERY_SIGN失败 !method=[{}], params=[{}]","cumulativeSignDesc","");return "";
        }
        int signDays = byDate.getSignDays();
        StringBuilder sb = new StringBuilder();
        com.zdmoney.gamecenter.api.common.dto.ResultDto<List<FragmentRecordDto>> listBySourceWithMonth = fragmentFacadeService.getListBySourceWithMonth(cmNumber, "3");
        if(!listBySourceWithMonth.isSuccess()){
            log.error("调用fragmentFacadeService获取签到送宝箱数量失败 !method=[{}], params=[{}],data=[{}]","cumulativeSignDesc",cmNumber,listBySourceWithMonth.getData());return "";
        }
        List<FragmentRecordDto> data = listBySourceWithMonth.getData();
        if(data != null && data.size() > 0){
            msgMap.put("fragmentStatus",1);
        }
        sb.append("本月累计签到"+signNum+"天");
        if(signNum%signDays == 0){
            if (data != null && data.size() > 0){
                sb.append(",送"+data.get(0).getAwardName()+"碎片");
                return sb.toString();
            }
        }else {
            sb.append(",再累计签到"+(signDays - (signNum%signDays))+"天送碎片奖励");
            return sb.toString();
        }
        return "";
    }

    /**
     * 根据签到天数发放对应奖品
     * @param cmNumber
     * @param signCount
     * @return
     * @throws Exception
     */
    private Map<String, Object> sendGiftMsg(String cmNumber, int signCount) throws Exception{
        StringBuilder msg = new StringBuilder();
        CustomerSignLog signLog = new CustomerSignLog();
        Map<String, Object> map = new HashMap<>();
        int awardType = 0;

        if (signCount == 7) awardType = 1;
        else if (signCount == 15) awardType = 2;
        else if (signCount == 30) awardType = 3;
        map.put("awardType", awardType);

        int coinNum = sendCoin(cmNumber);
        signLog.setCoin(coinNum);
        msg.append("恭喜您已获得" + coinNum + "捞财币");

        // 查询发放礼品规则
        CustomerSignRule signRule = customerSignInfoMapper.querySignRule(signCount);
        if(signRule != null){
            switch (signCount){
                case 7:
                    BigDecimal rateCoupon = sendCoupon(cmNumber, signRule);
                    signLog.setRateCoupon(rateCoupon);
                    msg.append("和1张加息券");
                    break;
                case 15:
                    int money = sendRedEnvelopes(cmNumber, signRule);
                    signLog.setRedPacketMoney(money);
                    msg.append("和" + money + "元红包");
                    break;
                case 30:
                    int validDays = sendAppointmentCoupon(cmNumber, signRule);
                    signLog.setAppointCoupon(validDays);
                    msg.append("和1张预约券");
                    break;
            }
        }
        map.put("msg", msg.toString());
        map.put("signLog", signLog);
        return map;
    }

    /**
     * 保存用户签到信息
     * @param customerId
     * @throws Exception
     */
    private int saveSignInfo(Long customerId,String cmNumber,Map<String,Object> resMap) throws Exception{
        String dateKey = customerId + KeyGenerator.USER_SIGN_DAY.getKey();
        String insertRes = redisSessionManager.get(dateKey);

        Long isSign = customerSignInfoMapper.queryNowadaysSignCount(customerId);

        if(StringUtils.isBlank(insertRes) || isSign == 0){
            CustomerSignInfo signInfo = new CustomerSignInfo();
            signInfo.setChestStatus(0);
            dailyTaskRecordService.cumulativeSign(signInfo,cmNumber,resMap);
            signInfo.setCustomerId(customerId);
            signInfo.setSignDate(new Date());
            signInfo.setStatus(1L);// 0:过期  1:正常
            Long res = customerSignInfoMapper.insertSignInfo(signInfo);
            if(res <= 0) throw new BusinessException("Insert.Sign in info.failed");
            int second = getTodaySurplusSecond();
            redisSessionManager.put(dateKey, "用户ID:" + customerId + ",在" + new SimpleDateFormat("yyyy年MM月dd日").format(new Date()) + "已完成签到", second, TimeUnit.SECONDS);
        }else{
            return 0;
        }
        return 1;
    }

    /**
     * 发捞财币
     * @param cmNumber
     * @return
     * @throws Exception
     */
    private Integer sendCoin(String cmNumber) throws Exception{
        // 每日签到随机赠送1-1000捞财币
        Integer coinNum = getRandomInteger(1, 1000);
        // 如果是周末,则额外奖励1000捞财币
        if(isWeekend()) coinNum += 1000;

        CoinOprDto cod = new CoinOprDto();
        cod.setAccountNo(cmNumber);
        cod.setCoin(Long.valueOf(coinNum));
        cod.setTips("签到");
        cod.setTransNo(SerialNoGeneratorService.generateTransNo(cmNumber));
        welfareService.gainCoin(cod);
        return coinNum;
    }

    /**
     * 发加息券
     * @param cmNumber
     * @param signRule
     * @return
     * @throws Exception
     */
    private BigDecimal sendCoupon(String cmNumber, CustomerSignRule signRule) throws Exception{
        BigDecimal rate = getRandomFolat(signRule.getMinRate(), signRule.getMaxRate());
        VoucherPublishDto publishDto = new VoucherPublishDto();
        publishDto.setAccountNo(cmNumber);
        publishDto.setInvestPeriod(signRule.getInvestPeriod());
        publishDto.setPeriod(signRule.getPeriod());
        publishDto.setDays(signRule.getDays());
        publishDto.setInvestAmount(signRule.getInvestAmount());
        publishDto.setRate(rate);
        publishDto.setSource("SIGN");
        publishDto.setTransNo(SerialNoGeneratorService.generateTransNo(cmNumber));
        welfareService.publishVoucher(publishDto);
        return rate;
    }

    /**
     * 发红包
     * @param cmNumber
     * @param signRule
     * @return
     * @throws Exception
     */
    private Integer sendRedEnvelopes(String cmNumber, CustomerSignRule signRule) throws Exception{
        int money = getRandomInteger(signRule.getMinMoney(), signRule.getMaxMoney());
        CouponGiveDto giveDto = new CouponGiveDto();
        giveDto.setAccountNo(cmNumber);
        giveDto.setSource("SIGN");
        giveDto.setInvestPeriod(signRule.getInvestPeriod());
        giveDto.setInvestAmount(signRule.getInvestAmount());
        giveDto.setAmount(money);
        giveDto.setPeriod(signRule.getPeriod());
        giveDto.setTransNo(SerialNoGeneratorService.generateTransNo(cmNumber));
        welfareService.gainCoupon(giveDto);
        return money;
    }

    /**
     * 发预约券
     * @param cmNumber
     * @param signRule
     * @return
     * @throws Exception
     */
    private Integer sendAppointmentCoupon(String cmNumber, CustomerSignRule signRule) throws Exception{
        AppointmentPublishDto publishDto = new AppointmentPublishDto();
        publishDto.setAccountNo(cmNumber);
        publishDto.setSource("SIGN");
        publishDto.setPeriod(signRule.getValidDays());
        publishDto.setInvestPeriod(signRule.getInvestPeriod());
        publishDto.setInvestAmount(new BigDecimal(Integer.parseInt(signRule.getInvestAmount().toString())));
        publishDto.setTransNo(SerialNoGeneratorService.generateTransNo(cmNumber));
        welfareService.publishAppointment(publishDto);
        return signRule.getValidDays();
    }

    /**
     *  @Description: 预期发碎片时间
     *  @author huangcy
     *  @date 2017/8/28
    */
    private CustomerSignInfo addChestInfo(Date date, int days){
        CustomerSignInfo signInfo = new CustomerSignInfo();
        signInfo.setSignDate(addDate(date, days));
        signInfo.setStatus(2L);
        signInfo.setChestStatus(1);
        return signInfo;
    }

    /**
     * 计算预计发放奖品日期
     * @param date
     * @param days
     * @return
     */
    private CustomerSignInfo addCustomerSignInfo(Date date, int days){
        CustomerSignInfo signInfo = new CustomerSignInfo();
        signInfo.setSignDate(addDate(date, days));
        signInfo.setStatus(2L);
        signInfo.setChestStatus(0);
        return signInfo;
    }

    /**
     * 判断当前日期是否是周末
     * @return
     */
    private boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // week等于0时,代表星期天,为6则代表星期六
        return week == 6 || week == 0 ? true : false;
    }

    /**
     * 生成随机数-整数
     * 规则:从scopeMin开始,包含scopeMax
     * @param scopeMin
     * @param scopeMax
     * @return
     */
    private Integer getRandomInteger(int scopeMin, int scopeMax) {
        Random random = new Random();
        return (random.nextInt(scopeMax) % (scopeMax - scopeMin + 1) + scopeMin);
    }

    /**
     * 生成随机数-3位小数
     * 规则:从scopeMin开始,包含scopeMax
     * @param scopeMin
     * @param scopeMax
     * @return
     */
    private BigDecimal getRandomFolat(float scopeMin, float scopeMax){
        BigDecimal bd = new BigDecimal(Math.random() * (scopeMax - scopeMin) + scopeMin);
        return bd.setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 指定日期加上指定天数得到新日期
     * @param date
     * @param day
     * @return
     */
    private static Date addDate(Date date ,long day){
        long time = date.getTime();
        day = (day - 1) * 24 * 60 * 60 * 1000;
        time += day;
        return new Date(time);
    }

    /**
     * 根据当前日期获取月
     * @param date
     * @param n
     * @return
     */
    private static Date getMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    public static int getTodaySurplusSecond(){
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate
                .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
                .get(Calendar.DATE) + 1, 0, 0, 0);
        return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
    }
}