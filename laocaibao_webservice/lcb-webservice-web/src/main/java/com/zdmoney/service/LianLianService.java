package com.zdmoney.service;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.impl.BankCardServiceImpl;
import com.zdmoney.utils.CommonHelper;
import com.zdmoney.utils.HttpConnectionUtil;
import com.zdmoney.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import websvc.utils.SysLogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 00232384 on 2017/3/21.
 */
@Slf4j
@Service
public class LianLianService {

    @Autowired
    private ConfigParamBean configParamBean;


    @Async
    public void unbindLlCard(CustomerMainInfo mainInfo, CustomerBankAccount bankAccount) {
        /**调用收银台解绑*/
        String merchantCode = configParamBean.getPayMerchantNo();
        String privateKey = configParamBean.getPayPrivateKey();
        String payUrl = configParamBean.getCashierAddrLan() + "/query/cardNewUnbind";
        Map<String, String> map = new HashMap<String, String>();
        String certNo = mainInfo.getCmIdnum();
        String bankCardNo = bankAccount.getCbAccount();
        String sign = MD5.MD5Encode(merchantCode + "|0|" + certNo + "|" + bankCardNo + "|" + privateKey);
        map.put("merchantCode", merchantCode);
        map.put("certType", "0");
        map.put("certNo", certNo);
        map.put("bankCardNo", bankCardNo);
        map.put("sign", sign);
        String strResult = HttpConnectionUtil.send2(payUrl, map);
        log.info("调用收银台解绑strResult=" + strResult);
        Map<String, Object> jsonMap = CommonHelper.jsonToMapObj(strResult);
        Object responseDesc = jsonMap.get("responseDesc");
        log.info("调用收银台解绑responseDesc=" + responseDesc);
        if (AppConstants.INTEGRAL_STATUS_SUCCESS.equals(jsonMap.get("responseCode")) || "0038".equals(jsonMap.get("responseCode"))) {
            log.info("调用收银台解绑成功");
        } else {
            log.error("调用收银台解绑失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId());
            SysLogUtil.save("调用收银台解绑失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId(), "Unbind-bank-card-fail", BankCardServiceImpl.class.getName());
            throw new BusinessException("调用收银台解绑失败，请致电客服！");
        }
    }
}
