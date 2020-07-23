package com.zdmoney.service.impl;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.customer.CustRatingChangingRecordMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.sm.SendSmReqDto;
import com.zdmoney.message.api.facade.ISmFacadeService;
import com.zdmoney.models.CustomerRatingConfig;
import com.zdmoney.models.customer.CustRatingChangingRecord;
import com.zdmoney.models.customer.CustRatingUpPresent;
import com.zdmoney.models.customer.CustomerInvestingInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.CustRatingChangingRecordService;
import com.zdmoney.service.CustRatingUpPresentService;
import com.zdmoney.service.CustomerRatingConfigService;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.trace.utils.LcbTraceCallable;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.JackJsonUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.CustRatingChangingVo;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import websvc.utils.SpringContextHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * @date 2019-01-02 23:47:00
 */
@Service("custRatingChangingRecordService")
public class CustRatingChangingRecordServiceImpl implements CustRatingChangingRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustRatingChangingRecordServiceImpl.class);

    @Autowired
    private CustRatingChangingRecordMapper custRatingChangingRecordMapper;

    @Autowired
    private CustomerRatingConfigService customerRatingConfigService;

    @Autowired
    private CustRatingUpPresentService custRatingUpPresentService;

    @Autowired
    private CustomerMainInfoMapper mainInfoMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ISmFacadeService iSmFacadeService;

    @Autowired
    private SysParameterService parameterService;

    @Override
    public List<CustRatingChangingRecord> queryCustRatingChangingRecord(Map<String, Object> paramsMap){
        return custRatingChangingRecordMapper.queryCustRatingChangingRecord(paramsMap);
    }

    @Override
    public int saveCustRatingChangingRecord(CustRatingChangingRecord custRatingChangingRecord){
        return custRatingChangingRecordMapper.saveCustRatingChangingRecord(custRatingChangingRecord);
    }

    @Override
    public void checkAndUpdateCustomerRating(String ids) {
        int coreThreadNum = Runtime.getRuntime().availableProcessors() * 2;
        final int pageSize = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(coreThreadNum);
        List<Long> exclusiveCustomers = findExclusiveCustomers();
        Map<String,Object> params = new HashMap<>();
        params.put("exclusiveCustomers", exclusiveCustomers);
        params.put("accountType", CustomerAccountType.LENDER.getValue());
        List<String> specificCustomers = null;
        if(StringUtils.isNotBlank(ids)){
            specificCustomers =  Arrays.asList(ids.split(","));
            params.put("ids", specificCustomers);
        }
        int total = mainInfoMapper.getCountBySearchParamNew(params);
        int times = (total + pageSize - 1)/pageSize;
        List<Future<String>> taskResults = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(times);
        long startTime = System.nanoTime();
        for(int i=0; i<times;i++){
            taskResults.add(executorService.submit(new Task(i+1, pageSize, specificCustomers, exclusiveCustomers, latch)));
        }
        try {
            latch.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        LOGGER.info("更新用户会员等级耗时:"+(System.nanoTime()-startTime)/1000_000_000);
        processTaskResults(taskResults);//发送错误邮件
        custRatingUpPresentService.sendRatingUpPresents();//赠送积分
    }

    List<Long> findExclusiveCustomers(){
        List<SysParameter> parameters = parameterService.findByPrType(AppConstants.SPECIAL_FINANCE_PEOPLE);
        List<String> customers = new ArrayList<>(parameters.size()+1);
        for(SysParameter parameter : parameters){
            customers.add(parameter.getPrValue());
        }
        customers.add(configParamBean.getGuarantee_cm_number());
        customers.add(configParamBean.getWacaiProductRemainingPartBuyer());
        Map<String,Object> params = new HashMap<>();
        params.put("cmNumbers", customers);
        List<CustomerMainInfo> customerMainInfos = mainInfoMapper.getCustomerBySearchParam(params);
        List<Long> ids = new ArrayList<>(customers.size());
        for(CustomerMainInfo customerInfo: customerMainInfos){
            ids.add(customerInfo.getId());
        }
        return ids;
    }

    void processTaskResults(List<Future<String>> taskResults){
        StringBuilder error = new StringBuilder();
        try {
            for (Future<String> future : taskResults) {
                if (future.isDone() && StringUtils.isNotEmpty(future.get())) {
                    error.append(future.get());
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        if(error.length() > 0){
            System.out.println(error.toString());
            MailUtil.sendMail("更新会员等级发生异常", error.toString());
        }
    }

    enum RatingResult{
        DO_NOTHING,TO_UPDATE_CHANGING_DATE,TO_UPGRADE,TO_DOWNGRADE
    }

    class Task extends LcbTraceCallable<String> {
        private int pageNo;
        private int pageSize;
        private CountDownLatch latch;
        private List<String> statusList;
        private List<String> specificCustomers;
        private List<Long> exclusiveCustomers;
        private Map<Integer,List<CustomerInvestingInfo>> customersToUpgrade;
        private Map<Integer,List<CustomerInvestingInfo>> customersToDowngrade;
        private List<Long> customersToUpdateChangingDate;
        private Calendar now;
        private Calendar beginOfTheDay;
        StringBuilder errorMsg;
        public Task(int pageNo, int pageSize, List<String> specificCustomers, List<Long> exclusiveCustomers, CountDownLatch latch) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.specificCustomers = specificCustomers;
            this.exclusiveCustomers = exclusiveCustomers;
            this.latch = latch;
            now = new GregorianCalendar();
            now.setTime(new Date());

            beginOfTheDay = new GregorianCalendar();
            beginOfTheDay.setTime(now.getTime());
            beginOfTheDay.set(Calendar.HOUR_OF_DAY, 0);
            beginOfTheDay.set(Calendar.MINUTE, 0);
            beginOfTheDay.set(Calendar.SECOND, 0);
            beginOfTheDay.set(Calendar.MILLISECOND, 0);

            errorMsg = new StringBuilder();
            statusList = Arrays.asList(
                    AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0,
                    AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14,
                    AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17,
                    AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18);
        }

        @Override
        public String concreteCall() throws Exception {
            try{
                List<CustomerMainInfo> customers = query();
                updateCustomersRating(customers);
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
                errorMsg.append("用户等级处理发生异常："+e.getMessage()).append("\r\n");
            }finally {
                latch.countDown();
            }
            return errorMsg.toString();
        }

        List<CustomerMainInfo> query(){
            Page<List<CustomerMainInfo>> page = new Page<>();
            page.setPageNo(pageNo);
            page.setPageSize(pageSize);
            Map<String,Object> params = new HashMap<>();
            params.put("cmValid", "0");
            params.put("accountType", CustomerAccountType.LENDER.getValue());
            params.put("ids", specificCustomers);
            params.put("exclusiveCustomers", exclusiveCustomers);
            params.put("page", page);
            return mainInfoMapper.getCustomerBySearchParam(params);
        }

        void updateCustomersRating(List<CustomerMainInfo> customers){
            List<CustomerInvestingInfo> customerInvestingInfos = queryCustomerInvestingInfo(customers);
            groupCustomers(customerInvestingInfos);
            doUpdate();
        }

        List<CustomerInvestingInfo> queryCustomerInvestingInfo(List<CustomerMainInfo> customers){
             List<Long> customerIds = new ArrayList<>(pageSize);
             for(CustomerMainInfo customer : customers){
                 customerIds.add(customer.getId());
             }

             Map<String,Object> params = new HashMap<>();
             params.put("customerIds", customerIds);
             params.put("accountType", CustomerAccountType.LENDER.getValue());
             params.put("statusList", statusList);
             params.put("orderBeforeDate", beginOfTheDay.getTime());
             return mainInfoMapper.selectCustomerInvestingInfo(params);
        }

        void groupCustomers(List<CustomerInvestingInfo> customerInvestingInfos){
             customersToUpgrade = new HashMap<>();
             customersToDowngrade = new HashMap<>();
             customersToUpdateChangingDate = new ArrayList<>();
             putCustomersInRightGroup(customerInvestingInfos);
        }

        void putCustomersInRightGroup(List<CustomerInvestingInfo> customerInvestingInfos){
             Calendar ratingChangineDate = new GregorianCalendar();
             CustomerRatingConfig ratingConfig;
             for(CustomerInvestingInfo info : customerInvestingInfos){
                 RatingResult ratingResult = detemineNewRating(info, ratingChangineDate);
                 ratingConfig = customerRatingConfigService.findRatingByInvestingAmt(info.getAmt());
                 if(ratingResult == RatingResult.TO_DOWNGRADE){
                     List<CustomerInvestingInfo> list = customersToDowngrade.get(ratingConfig.getRatingNum());
                     if(list == null){
                         list = new ArrayList<>();
                         customersToDowngrade.put(ratingConfig.getRatingNum(), list);
                     }
                     list.add(info);
                 }else if(ratingResult == RatingResult.TO_UPGRADE){
                     List<CustomerInvestingInfo> list = customersToUpgrade.get(ratingConfig.getRatingNum());
                     if(list == null){
                         list = new ArrayList<>();
                         customersToUpgrade.put(ratingConfig.getRatingNum(), list);
                     }
                     list.add(info);
                 }else if(ratingResult == RatingResult.TO_UPDATE_CHANGING_DATE){
                     customersToUpdateChangingDate.add(info.getId());
                 }
             }
        }

        RatingResult detemineNewRating(CustomerInvestingInfo info, Calendar ratingChangineDate){
             CustomerRatingConfig ratingConfig = customerRatingConfigService.findByRatingNum(info.getMemberLevel());
             RatingResult result = RatingResult.DO_NOTHING;
             ratingChangineDate.setTime(info.getRatingChangingDate());
             int days = getDaysBetween(ratingChangineDate,now);
             if(ratingConfig.getMinInvestingAmt().compareTo(info.getAmt()) >= 0){//投资减少或一直未投资
                 if(days >= configParamBean.getRating_last_days()){
                     if(ratingConfig.getMaxInvestingAmt()!=null && ratingConfig.getMaxInvestingAmt().compareTo(BigDecimal.ZERO) == 0){//一直未投资为铁象，更新有效期
                         result = RatingResult.TO_UPDATE_CHANGING_DATE;
                     }else {//以前打标的不是铁象，有投资，投资减少，降级
                         result = RatingResult.TO_DOWNGRADE;
                     }
                 }
             }else if(ratingConfig.getMaxInvestingAmt() != null && ratingConfig.getMaxInvestingAmt().compareTo(info.getAmt()) < 0){
                 result = RatingResult.TO_UPGRADE;
             }else{
                 if(days >= configParamBean.getRating_last_days()){
                     result = RatingResult.TO_UPDATE_CHANGING_DATE;
                 }
             }
             return result;
        }

        int getDaysBetween(Calendar startCal, Calendar endCal){
            int y1 = startCal.get(Calendar.YEAR);
            int y2 = endCal.get(Calendar.YEAR);
            int days = (y2-y1)*365 + endCal.get(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR);
            return Math.abs(days);
        }

        void doUpdate(){
            upgradeCustomers();
            downgradeCustomers();
            updateCustomersExpireDate();
        }

        void upgradeCustomers(){
            if(!CollectionUtils.isEmpty(customersToUpgrade)){
                for(Map.Entry<Integer,List<CustomerInvestingInfo>> entry : customersToUpgrade.entrySet()){
                    if(doUpdateCustomersAndSaveChangingRecords(entry.getKey(), entry.getValue()))
                         buildAndSavePresentRecords(entry.getKey(), entry.getValue());
                }
            }
        }

        void downgradeCustomers(){
            if(!CollectionUtils.isEmpty(customersToDowngrade)){
                for(Map.Entry<Integer,List<CustomerInvestingInfo>> entry : customersToDowngrade.entrySet()){
                    doUpdateCustomersAndSaveChangingRecords(entry.getKey(), entry.getValue());
                }
            }
        }

        void updateCustomersExpireDate(){
            if(!CollectionUtils.isEmpty(customersToUpdateChangingDate)){
                Map<String,Object> params = new HashMap<>();
                params.put("customerIds", customersToUpdateChangingDate);
                params.put("ratingChangingDate", new Date());
                mainInfoMapper.updateByMap(params);
            }
        }

        boolean doUpdateCustomersAndSaveChangingRecords(Integer newRating, List<CustomerInvestingInfo> list){
            boolean done = false;
            try {
                List<CustRatingChangingRecord> changingRecords = new ArrayList<>();
                List<Long> customerIds = new ArrayList<>();
                Date now = new Date();
                for (CustomerInvestingInfo customerInvestingInfo : list) {
                    customerIds.add(customerInvestingInfo.getId());
                    changingRecords.add(buildChangingRecord(newRating, customerInvestingInfo, now));
                }
                CustRatingChangingRecordService service = SpringContextHelper.getBean(CustRatingChangingRecordService.class);
                service.updateCustomersAndSaveChangingRecords(customerIds, newRating, changingRecords);
                done = true;
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
                try {
                    errorMsg.append(String.format("用户【%s】变更会员等级到【%d】失败:%s", JackJsonUtil.objToStr(list), newRating.intValue(), e.getMessage())).append("\r\n");
                } catch (IOException e1) {
                    //ignore
                }
            }
            return done;
        }

        CustRatingChangingRecord buildChangingRecord(Integer newRating, CustomerInvestingInfo info, Date createTime){
            String direction = CustRatingChangingRecord.DIRECTION_UP;
            if(newRating.intValue() < info.getMemberLevel().intValue()){
                direction = CustRatingChangingRecord.DIRECTION_DOWN;
            }
            CustomerRatingConfig curRating = customerRatingConfigService.findByRatingNum(info.getMemberLevel());
            CustomerRatingConfig nextRating = customerRatingConfigService.findByRatingNum(newRating);
            CustRatingChangingRecord record = new CustRatingChangingRecord();
            record.setCreateTime(createTime);
            record.setCurrentAssets(info.getAmt());
            record.setCustomerId(info.getId());
            record.setCustomerNumber(info.getCmNumber());
            record.setChangingDirection(direction);
            record.setCurrentRatingCode(curRating.getRatingCode());
            record.setCurrentRatingNum(curRating.getRatingNum());
            record.setNextRatingCode(nextRating.getRatingCode());
            record.setNextRatingNum(nextRating.getRatingNum());
            return record;
        }

        void buildAndSavePresentRecords(Integer newRating, List<CustomerInvestingInfo> list){
            try {
                List<Long> customerIds = new ArrayList<>();
                List<CustRatingUpPresent> presents = new ArrayList<>();
                for (CustomerInvestingInfo info : list) {
                    customerIds.add(info.getId());
                    presents.addAll(buildUgradingPresent(newRating, info));
                }
                Map<String, Object> params = new HashMap<>();
                params.put("customerIds", customerIds);
                List<CustRatingUpPresent> presentsAlreadySent = custRatingUpPresentService.queryCustRatingUpPresent(params);
                List<CustRatingUpPresent> presentsToSend = new ArrayList<>();
                for (CustRatingUpPresent record : presents) {
                    boolean toSend = true;
                    for (CustRatingUpPresent sentOne : presentsAlreadySent) {
                        if (record.getCustomerId().equals(sentOne.getCustomerId())
                                && record.getNextRatingCode().equals(sentOne.getNextRatingCode())) {
                            toSend = false;
                        }
                    }
                    if (toSend) presentsToSend.add(record);
                }
                if(!presentsToSend.isEmpty()) custRatingUpPresentService.savePresents(presentsToSend);
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
                try {
                    errorMsg.append(String.format("保存用户升级积分记录【%s】失败:%s", JackJsonUtil.objToStr(list), e.getMessage())).append("\r\n");
                } catch (IOException e1) {
                    //ignore
                }
            }
        }

        List<CustRatingUpPresent> buildUgradingPresent(Integer newRating, CustomerInvestingInfo info){
             List<CustomerRatingConfig> ratingConfigs = customerRatingConfigService.findInclusiveConfigsBetweenTwoRatings(info.getMemberLevel(), newRating);
             List<CustRatingUpPresent> presents = new ArrayList<>();
             if(!ratingConfigs.isEmpty() && ratingConfigs.size()>1){
                 Date now = new Date();
                 for(int i = 1; i<ratingConfigs.size();i++){
                     CustomerRatingConfig nextRating = ratingConfigs.get(i);
                     CustRatingUpPresent present = new CustRatingUpPresent();
                     present.setCustomerId(info.getId());
                     present.setCustomerNumber(info.getCmNumber());
                     present.setNextRatingCode(nextRating.getRatingCode());
                     present.setNextRatingNum(nextRating.getRatingNum());
                     present.setCreditNum(nextRating.getUpgradingCredit());
                     present.setCreateTime(now);
                     presents.add(present);
                 }
             }
             return presents;
        }
    }

    @Transactional
    public void updateCustomersAndSaveChangingRecords(List<Long> customerIds, Integer newRating, List<CustRatingChangingRecord> changingRecords){
        custRatingChangingRecordMapper.saveRecords(changingRecords);
        Map<String,Object> params = new HashMap<>();
        params.put("customerIds", customerIds);
        params.put("newRating", newRating);
        params.put("ratingChangingDate", new Date());
        mainInfoMapper.updateByMap(params);
    }

    @Override
    public void sendCustomerRatingChangingMsg() {
        String lockingThread = UUID.randomUUID().toString();
        lockRecords(lockingThread);
        sendMsg(lockingThread);
        postProcessAfterMsgSent(lockingThread);
    }

    void lockRecords(String lockingThread){
        Date now = new Date();
        Date startTime = DateUtil.getDateBeginTime(now);
        Date endTime = DateUtil.getDateEndTime(now);
        Map<String,Object> params = new HashMap<>();
        params.put("lockingThread", lockingThread);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("status", CustRatingChangingRecord.Status.SENDING);
        params.put("initStatus", CustRatingChangingRecord.Status.NOT_SENT);
        custRatingChangingRecordMapper.updateByMap(params);
    }

    void sendMsg(String lockingThread){
        Map<String,Object> params = new HashMap<>();
        params.put("lockingThread", lockingThread);
        params.put("status", CustRatingChangingRecord.Status.SENDING);
        sendRatingUpMsg(params);
        sendRatingDownMsg(params);
    }

    void postProcessAfterMsgSent(String lockingThread){
        Map<String,Object> params = new HashMap<>();
        params.put("initLock", lockingThread);
        params.put("status", CustRatingChangingRecord.Status.ALREADY_SENT);
        params.put("initStatus", CustRatingChangingRecord.Status.SENDING);
        custRatingChangingRecordMapper.updateByMap(params);
    }

    void sendRatingUpMsg(Map<String,Object> params){
        sendMsgByDirectionAndRatingNum(params, CustRatingChangingRecord.DIRECTION_UP);
    }

    void sendRatingDownMsg(Map<String,Object> params){
        sendMsgByDirectionAndRatingNum(params, CustRatingChangingRecord.DIRECTION_DOWN);
    }

    void sendMsgByDirectionAndRatingNum(Map<String,Object> params, String direction){
        Map<String,Object> localParams = new HashMap<>(params);
        localParams.put("direction", direction);
        List<CustomerRatingConfig> configs = customerRatingConfigService.findAll();
        StringBuilder errors = new StringBuilder();
        for(CustomerRatingConfig ratingConfig : configs){
            localParams.put("nextRatingNum", ratingConfig.getRatingNum());
            List<CustRatingChangingVo> records = custRatingChangingRecordMapper.queryCustRatingChangingVo(localParams);
            if(!records.isEmpty()) {
                String error = sendMsgByRatingNum(ratingConfig, records, direction);
                if(StringUtils.isNotEmpty(error)) errors.append(error);
            }
        }
        if(errors.length()>0)
            MailUtil.sendMail("发送会员等级消息存在问题", errors.toString());
    }

    String sendMsgByRatingNum(CustomerRatingConfig ratingConfig, List<CustRatingChangingVo> records, String direction){
        StringBuilder sb = new StringBuilder();
        int batchSize = 1000;
        int total = records.size();
        int times = (total + batchSize - 1)/batchSize;
        int fromIndex = 0 ,toIndex = 0;
        for(int i=0;i < times;i++){
            fromIndex = i*batchSize;
            toIndex = Math.min((i+1)*batchSize, total);
            String error = doSendMsg(ratingConfig, records.subList(fromIndex, toIndex), direction);
            if(StringUtils.isNotEmpty(error)){
                sb.append(error);
            }
        }
        return sb.toString();
    }

    String doSendMsg(CustomerRatingConfig ratingConfig, List<CustRatingChangingVo> records, String direction){
        String error = null;
        String batchNo = DateUtil.getDateFormatString(new Date(),DateUtil.TOTALFORMAT) + ratingConfig.getRatingNum() + direction;
        String msgTemplate = direction.equals(CustRatingChangingRecord.DIRECTION_UP)? configParamBean.getRatingUpMsg():configParamBean.getRatingDownMsg();
        SendSmReqDto sendDto = new SendSmReqDto();
        sendDto.setBatchNo(batchNo);
        sendDto.setSendMsg(String.format(msgTemplate,ratingConfig.getRatingDescr()));
        sendDto.setInstant(true);
        List<String> mobiles = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for(CustRatingChangingVo record : records){
            mobiles.add(record.getCellphone());
            ids.add(record.getId());
        }
        sendDto.setMobiles(mobiles);
        try {
            //LOGGER.info(sendDto.toString());
            MessageResultDto resultDto = iSmFacadeService.sendMarketMsg(sendDto);
            if(resultDto == null || !resultDto.isSuccess()) {
                error = String.format("发送短信失败:%s,失败原因:%s\r\n",JackJsonUtil.objToStr(sendDto),resultDto==null?"调用message失败":resultDto.getMsg());
                LOGGER.error(error);
                handleMsgSendingFailure(ids);
            }
        } catch (Exception e) {
            error = String.format("发送短信:%s,发生异常:%s\r\n",sendDto.toString(),e.getMessage());
            LOGGER.error(error);
            handleMsgSendingFailure(ids);
        }
        return error;
    }

    void handleMsgSendingFailure(List<Long> ids){
        Map<String,Object> params = new HashMap<>();
        params.put("ids",ids);
        params.put("status", CustRatingChangingRecord.Status.EXCEPTION_HAPPENED);
        params.put("initStatus", CustRatingChangingRecord.Status.SENDING);
        try{
            custRatingChangingRecordMapper.updateByMap(params);
        }catch (Exception e){
            LOGGER.error("更新会员等级变更记录【%s】为 4 失败：%s",Arrays.toString(ids.toArray()),e.getMessage());
        }
    }
}
