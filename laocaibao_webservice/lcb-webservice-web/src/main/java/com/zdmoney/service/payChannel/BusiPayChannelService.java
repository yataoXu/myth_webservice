package com.zdmoney.service.payChannel;

import com.zdmoney.models.payChannel.BusiPayChannel;
import websvc.models.Model_530003;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 00225181 on 2015/12/21.
 */
public interface BusiPayChannelService {
    /**
     * 获取支付渠道，连收银台 （No Use）
     * @param model_530003
     * @return
     * @throws Exception
     */
    String getPayChannel(Model_530003 model_530003) throws  Exception;

    /**
     * 根据银行编码和支付金额获取支付列表
     * @param bankCode
     * @param payCash
     * @return
     */
    List<BusiPayChannel> getPayChannel(String bankCode,BigDecimal payCash);

    boolean checkCardBin(String bankCardNo,String bankCode);

    BusiPayChannel  validatePayChannel(String channelCode);

    BusiPayChannel  getCurrentPayChannel();

    BusiPayChannel getLianLianPayChannel();
}
