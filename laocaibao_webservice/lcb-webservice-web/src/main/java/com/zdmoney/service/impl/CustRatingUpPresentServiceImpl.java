package com.zdmoney.service.impl;

import com.zdmoney.common.BusinessOperation;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.account.IntegralGiveDto;
import com.zdmoney.integral.api.dto.account.IntegralResDto;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.mapper.customer.CustRatingUpPresentMapper;
import com.zdmoney.models.customer.CustRatingUpPresent;
import com.zdmoney.service.CustRatingUpPresentService;
import com.zdmoney.service.IOperStateRecordService;
import com.zdmoney.utils.JackJsonUtil;
import com.zdmoney.utils.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @date 2019-01-02 23:46:51
 */
@Service("custRatingUpPresentService")
public class CustRatingUpPresentServiceImpl implements CustRatingUpPresentService {
    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(CustRatingUpPresentServiceImpl.class);

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustRatingUpPresentMapper custRatingUpPresentMapper;

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;

    @Override
    public List<CustRatingUpPresent> queryCustRatingUpPresent(Map<String, Object> paramsMap){
        return custRatingUpPresentMapper.queryCustRatingUpPresent(paramsMap);
    }

    @Override
    public int saveCustRatingUpPresent(CustRatingUpPresent custRatingUpPresent){
        return custRatingUpPresentMapper.saveCustRatingUpPresent(custRatingUpPresent);
    }

    @Override
    public int savePresents(List<CustRatingUpPresent> presents) {
        return custRatingUpPresentMapper.savePresents(presents);
    }

    Date getBeginOfTheDay(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    Date getEndOfTheDay(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    @Autowired
    private IOperStateRecordService operStateRecordService;

    @Override
    public void sendRatingUpPresents() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        Date startTime = getBeginOfTheDay(calendar);
        Date endTime = getEndOfTheDay(calendar);
        String lockingThread = UUID.randomUUID().toString();
        Map<String,Object> params = new HashMap<>();
        params.put("lockingThread", lockingThread);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("status", CustRatingUpPresent.SENDING);
        params.put("initStatus", CustRatingUpPresent.NOT_SENT);
        custRatingUpPresentMapper.updateByMap(params);
        List<CustRatingUpPresent> presents = custRatingUpPresentMapper.queryCustRatingUpPresent(params);
        StringBuilder errors = new StringBuilder();
        for(CustRatingUpPresent present : presents){
            String s = sendPresent(present);
            if(StringUtils.isNotEmpty(s))
                errors.append(s);
        }
        params.put("initLock", lockingThread);
        params.put("status", CustRatingUpPresent.ALREADY_SENT);
        params.put("initStatus", CustRatingUpPresent.SENDING);
        custRatingUpPresentMapper.updateByMap(params);
        if(errors.length()>0){
            MailUtil.sendMail("升级送积分发生异常", errors.toString());
        }
    }
    
    String sendPresent(CustRatingUpPresent present){
        String error = "";
        IntegralGiveDto creditDto = new IntegralGiveDto();
        creditDto.setAccountNo(present.getCustomerNumber());//账户号
        creditDto.setNo(configParamBean.getRating_up_credit_code());//积分来源编号
        creditDto.setIntegral(present.getCreditNum());//赠送积分
        creditDto.setChannel(AppConstants.PARTNER_NO);//捞财宝
        creditDto.setTransNo(present.getCustomerNumber()+present.getNextRatingCode());
        try {
            operStateRecordService.saveOperFlow(BusinessOperation.SEND_RATING_UP_CREDIT.getOperType(),creditDto.getTransNo());
            //LOGGER.info(JackJsonUtil.objToStr(creditDto));
            ResultDto<IntegralResDto> integralResDtoResultDto = integralAccountFacadeService.giveGetIntegral(creditDto);
            if(integralResDtoResultDto == null || !integralResDtoResultDto.isSuccess()){
                throw new BusinessException("调用账户失败 "+(integralResDtoResultDto == null ? "" : integralResDtoResultDto.getMsg()));
            }
        }catch (Exception e){
            updateRecordWhenExceptionHappened(present);
            String s = String.format("用户%s升级送积分%d失败:%s\r\n", creditDto.getAccountNo(), creditDto.getIntegral(),e.getMessage());
            LOGGER.error(s, e);
            error = s;
        }
        return error;
    }

    void updateRecordWhenExceptionHappened(CustRatingUpPresent present){
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("status", CustRatingUpPresent.EXCEPTION_HAPPENED);
            params.put("customerId", present.getCustomerId());
            params.put("nextRatingNum", present.getNextRatingNum());
            params.put("initStatus", CustRatingUpPresent.SENDING);
            custRatingUpPresentMapper.updateByMap(params);
        }catch (Exception e){
            //ignore
        }
    }

}
