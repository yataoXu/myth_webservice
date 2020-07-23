package com.zdmoney.service;

import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.bank.BusiBankLimitMapper;
import com.zdmoney.models.bank.BusiBankLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by 00232384 on 2017/3/10.
 */
@Service
public class BusiBankLimitService {

    @Autowired
    private BusiBankLimitMapper busiBankLimitMapper;

    /**
     * 获取银行限额信息
     * @param channelCode
     * @return
     */
    public BusiBankLimit validateBankLimit(String channelCode, String bankCode){
        BusiBankLimit query = new BusiBankLimit();
        query.setBankCode(null);
        query.setCode(bankCode);
        query.setPayChannel(channelCode);
        List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(query);
        if (CollectionUtils.isEmpty(bankLimits)) {
            throw new BusinessException("银行代码转换错误");
        }
        if (bankLimits.size() != 1) {
            throw new BusinessException("银行代码转换错误");
        }
        return bankLimits.get(0);
    }

    /**
     * 获取银行限额信息(存量用户激活)
     * @param bankCode 连连行号
     * @return
     */
    public BusiBankLimit queryInternalCode(String bankCode){
        BusiBankLimit query = new BusiBankLimit();
        query.setBankCode(null);
        query.setLlBankCode(bankCode);
        query.setPayChannel(AppConstants.PayChannelCodeContants.HUARUI_BANK);
        List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(query);
        if (CollectionUtils.isEmpty(bankLimits)) {
            throw new BusinessException("银行代码转换错误");
        }
        if (bankLimits.size() != 1) {
            throw new BusinessException("银行代码转换错误");
        }
        return bankLimits.get(0);
    }
}
