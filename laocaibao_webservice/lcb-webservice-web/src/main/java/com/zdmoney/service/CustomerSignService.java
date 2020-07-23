package com.zdmoney.service;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.account.IntegralResDto;
import com.zdmoney.integral.api.dto.account.IntegralSignDto;
import com.zdmoney.integral.api.dto.coin.CoinOprDto;
import com.zdmoney.integral.api.dto.coin.CoinOprResultDto;
import com.zdmoney.integral.api.facade.ICoinFacadeService;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.mapper.customer.CustomerSignMapper;
import com.zdmoney.marketing.api.facade.IMarketingFacadeService;
import com.zdmoney.marketing.entity.SignMessage;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.customer.CustomerSign;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.utils.LaocaiUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CustomerSignService extends BaseBusinessService<CustomerSign, Long> {

    public static final int SIGN_UPPER_LIMIT = 7;

    private CustomerSignMapper getCustomerSignMapper() {
        return (CustomerSignMapper) baseMapper;
    }

    @Autowired
    private IMarketingFacadeService marketingFacadeService;
    @Autowired
    private ConfigParamBean configParamBean;

    public boolean isSign(CustomerSign sign) {
        return getCustomerSignMapper().countSign(sign) > 0;
    }

    public CustomerSign findOneByCmNumberOrderBySignDate(String cmNumber) {
        return getCustomerSignMapper().findOneByCmNumberOrderBySignDate(cmNumber);
    }

    @Transactional
    public CustomerSign sign(String sessionToken) {
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
        CustomerMainInfo mainInfo = checkExistByCmNumber(cmNumber);
        CustomerSign sign = new CustomerSign();
        sign.setCmNumber(mainInfo.getCmNumber());
        sign.setSignDate(new Date());
        if (isSign(sign)) {
            throw new BusinessException("customer.today.signed");
        }

//        //签到送捞财币
//        List<SysParameter> sysParameterList =  sysParameterService.findByPrType("signCoinValue");
//        if (sysParameterList.size() == 0)
//            throw new BusinessException("字典表无签到捞财币配置");
//
//        Long signCoinValue = 0L;
//        try {
//            signCoinValue =  Long.valueOf(sysParameterList.get(0).getPrValue());
//        }
//        catch (Exception e){
//            throw new BusinessException("签到积分配置错误，无法转换成一个有效数字");
//        }

//        CoinOprDto oprDto = new CoinOprDto();
//        oprDto.setAccountNo(mainInfo.getCmNumber());
//        oprDto.setTips("每日签到送积分");
//        oprDto.setCoin(signCoinValue);
//        ResultDto<CoinOprResultDto> result = coinFacadeService.gainCoin(oprDto);
//        if (result.isSuccess()) {
//            if (result.getData() != null) {
//                sign.setCoin(result.getData().getCoin());
//            }
        com.zdmoney.marketing.api.dto.ResultDto<Long> resultDto = marketingFacadeService.sign(mainInfo.getCmNumber());
        if(resultDto.isSuccess()) {
            int times;
            CustomerSign lastSign = getCustomerSignMapper().findOneByCmNumberOrderBySignDate(mainInfo.getCmNumber());
            if (lastSign != null && isYesterday(lastSign.getSignDate()) && lastSign.getSignTime() < SIGN_UPPER_LIMIT) {
                times = lastSign.getSignTime() + 1;
            } else {
                times = 1;
            }
            sign.setSignTime(times);
            sign.setCoin(resultDto.getData());
            CustomerSign savedSign = save(sign);
            if (savedSign == null)
                throw new BusinessException("签到记录保存失败");
            return savedSign;
        }else {
            log.error("签到失败，marketing返回：{}",resultDto.getMsg());
            throw  new BusinessException("customer.sign.fail");
        }
//        }

        /*
        //签到不送积分，并且取消签到7天送刮刮卡活动 modify by spark 2016-04-28 基于V2.3需求
        IntegralSignDto signDto = new IntegralSignDto();
        signDto.setAccountNo(mainInfo.getCmNumber());
        signDto.setPlatform(platform);
        signDto.setChannel("LCB");
        ResultDto<IntegralResDto> result = integralAccountFacadeService.signGetIntegral(signDto);
        if (result.isSuccess()) {
            if (result.getData() != null) {
                sign.setIntegral(result.getData().getIntegral());
            }
            int times;
            CustomerSign lastSign = getCustomerSignMapper().findOneByCmNumberOrderBySignDate(mainInfo.getCmNumber());
            if (lastSign != null && isYesterday(lastSign.getSignDate()) && lastSign.getSignTime() < SIGN_UPPER_LIMIT) {
                times = lastSign.getSignTime() + 1;
            } else {
                times = 1;
            }
            sign.setSignTime(times);
            CustomerSign savedSign = save(sign);
            if (savedSign != null) {
                // 活动处理
                SignMessage signMessage = new SignMessage();
                signMessage.setCmNumber(savedSign.getCmNumber());
                signMessage.setSuccessionDays(savedSign.getSignTime());

                SysParameter reLotteryCode = sysParameterService.findOneByPrType("reLotteryCode");
                if (reLotteryCode == null) {
                    log.error("数据字典没有配置刮刮卡代码！本次分享送刮刮卡请求不发送，cmNumber=" + savedSign.getCmNumber());
                } else {
                    signMessage.setLotteryCode(Integer.parseInt(reLotteryCode.getPrValue()));
                    signMessage.setCustomerId(mainInfo.getId());
                    sendRocketMqMsg(BusiTypeEnum.SIGN, signMessage);
                }
                return savedSign;
//            JSONObject json = (JSONObject) JSONObject.toJSON(result.getData());
//            json.put("integral", sign.getIntegral());
//            json.put("signTime", sign.getSignTime());
//            json.put("sourceNo", result.getData().getSourceNo());
//            json.put("name", result.getData().getName());
//            json.put("serialNo", result.getData().getSerialNo());
            }
        }
         */
//        return null;
    }

    private static boolean isYesterday(Date date) {
        DateTime day = new DateTime(date).millisOfDay().withMinimumValue();
        DateTime today = DateTime.now().millisOfDay().withMinimumValue();
        return Days.daysBetween(today, day).getDays() == -1;
    }

}
