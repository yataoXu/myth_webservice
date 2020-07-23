package com.zdmoney.service;

import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.BankCardBindDto;
import com.zdmoney.integral.api.dto.lcbaccount.BankCardBindResultDto;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by 00232384 on 2017/3/24.
 */
@Service
@Log4j
public class HRService {

    @Autowired
    private BusiBankLimitService busiBankLimitService;

    @Autowired
    private IAccountFacadeService accountFacadeService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long autoBindCard(String bankCode, CustomerMainInfo mainInfo,String bankCard,String cellphone){
        Long bankId = null;
        //校验银行信息
        BusiBankLimit busiBankLimit = busiBankLimitService.validateBankLimit(AppConstants.PayChannelCodeContants.HUARUI_BANK, bankCode);
        //调用华瑞验卡绑卡接口
        BankCardBindDto bankCardBindDto = new BankCardBindDto();
        bankCardBindDto.setBankId(busiBankLimit.getBankCode());
        bankCardBindDto.setBankCode(busiBankLimit.getBankCode());
        bankCardBindDto.setBankNo(bankCard);
        bankCardBindDto.setBankName(busiBankLimit.getBankName());
        bankCardBindDto.setTelNo(cellphone);
        bankCardBindDto.setIDcard(mainInfo.getCmIdnum());
        bankCardBindDto.setUserName(mainInfo.getCmRealName());
        bankCardBindDto.setUserNo(mainInfo.getCmNumber());
        bankCardBindDto.setTransNo(SerialNoGeneratorService.generateBindSerialNo(mainInfo.getId()));
        ResultDto<BankCardBindResultDto> result = null;//accountFacadeService.bankCardBind(bankCardBindDto);
        if (!result.isSuccess()) {
            log.info("绑定银行卡失败，失败原因：" + result.getMsg());
        }
        else{
            Date date = new Date();
            if (StringUtils.isBlank(mainInfo.getBankAccountId())) {
                //新增绑卡流水
                CustomerBankAccount customerBankAccount = new CustomerBankAccount();
                customerBankAccount.setCbAccount(bankCard);
                customerBankAccount.setCbAccountName(mainInfo.getCmRealName());
                customerBankAccount.setCbBankCode(busiBankLimit.getBankCode());
                customerBankAccount.setCbBankName(busiBankLimit.getBankName());
                customerBankAccount.setCustomerId(mainInfo.getId());
                customerBankAccount.setCbInputDate(date);
                customerBankAccount.setCbModifyDate(date);
                customerBankAccount.setCbBindPhone(cellphone);
                int num = customerBankAccountMapper.insert(customerBankAccount);
                if (num != 1) {
                    throw new BusinessException("绑定银行卡失败");
                }
                //绑卡成功，更新用户绑卡ID,和绑卡时间
                int num2 = customerMainInfoMapper.updateCardInfo(new Date(), customerBankAccount.getId(), mainInfo.getId());
                if (num2 != 1) {
                    throw new BusinessException("绑定银行卡失败，请勿重复绑卡");
                }
                bankId = customerBankAccount.getId();
            }
        }
        return bankId;
    }


}
