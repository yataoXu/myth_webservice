package com.zdmoney.service.impl.payChannel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.BussErrorCode;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.payChannel.BusiPayBankLimitMapper;
import com.zdmoney.mapper.payChannel.BusiPayBankMapper;
import com.zdmoney.mapper.payChannel.BusiPayChannelMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payChannel.BusiPayBankLimit;
import com.zdmoney.models.payChannel.BusiPayChannel;
import com.zdmoney.service.BusiOrderTempService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.payChannel.BusiPayChannelService;
import com.zdmoney.utils.GsonUtils;
import com.zdmoney.utils.HttpUtils;
import com.zdmoney.utils.CommonHelper;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.JackJsonUtil;
import com.zdmoney.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import websvc.models.Model_530003;
import websvc.utils.JsonException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2015/12/21.
 */
@Service
@Slf4j
public class BusiPayChannelServiceImpl implements BusiPayChannelService {
    //    private static final log log = log.getlog(BusiPayChannelServiceImpl.class);
    @Autowired
    BusiPayChannelMapper payChannelMapper;
    @Autowired
    BusiPayBankMapper busiPayBankMapper;
    @Autowired
    BusiPayBankLimitMapper busiPayBankLimitMapper;
    @Autowired
    BusiPayChannelMapper busiPayChannelMapper;
    @Autowired
    BusiOrderTempService busiOrderTempService;
    @Autowired
    CustomerMainInfoService customerMainInfoService;
    @Autowired
    ConfigParamBean configParamBean;

    @Override
    @Deprecated
    public String getPayChannel(Model_530003 model_530003) throws Exception {
        if (model_530003.getOrderId() < 0) {
            throw new BusinessException("订单号不正确！");
        }
        BusiOrderTemp orderTemp = busiOrderTempService.selectByPrimaryKey(model_530003.getOrderId());
        CustomerMainInfo mainInfo = null;
        if (orderTemp != null) {
            Long customerId = orderTemp.getCustomerId();
            mainInfo = customerMainInfoService.findOne(customerId);
            if (StringUtils.isEmpty(mainInfo.getBankAccountId())) {
                if (StringUtils.isEmpty(model_530003.getBankAccount())) {
                    throw new BusinessException("银行卡号不能为空！");
                }
                String bankAccount = model_530003.getBankAccount();
                if (StringUtils.isEmpty(bankAccount)) {
                    throw new BusinessException("银行卡不能为空！");
                }
                BusiPayBankLimit limit = new BusiPayBankLimit();
                limit.setCode(model_530003.getBankCode());
                limit.setBankStatus("1");
                limit.setPayChannel(AppConstants.PayChannelCodeContants.LIANLIAN);
                List<BusiPayBankLimit> limits = busiPayBankLimitMapper.select(limit);
                if (limits.isEmpty()) {
                    log.error("查询银行信息失败，没有找到[" + model_530003.getBankCode() + "]所对应的连连银行代码！");
                    throw new BusinessException("系统异常，请联系管理员！错误代码：0003");
                } else {
                    String lianlianBankCode = limits.get(0).getBankCode();
                    boolean isCard = checkCardBin(bankAccount, lianlianBankCode);
                    if (isCard) {

                    } else {
                        throw new BusinessException("校验银行卡不通过！");
                    }
                }
            }
        } else {
            throw new BusinessException("没有查询到订单！");
        }
        if (StringUtils.isEmpty(model_530003.getBankCode())) {
            throw new BusinessException("银行代码不能为空！");
        }

        if (model_530003.getPayCash() == null) {
            throw new BusinessException("划扣金额不能为空！");
        }
        if (!StringUtils.isEmpty(mainInfo.getBankAccountId())) {
            List<BusiPayChannel> channels = getPayChannel(model_530003.getBankCode(), model_530003.getPayCash());
            for (BusiPayChannel channel : channels) {
                channel.setPayUrl(configParamBean.getCashierAddr() + channel.getPayUrl());
            }
            return JsonException.toJsonStr(BussErrorCode.ERROR_CODE_0000.getErrorcode(), "0", "发送成功!", channels, null);
        } else {
            throw new BusinessException("已超出该卡最大支付限额，建议更换修改金额后，进行购买。银行卡支付限额由银行设定，捞财宝无法提升限额。");
        }
//        Map<String,BusiPayChannel> channelMap = getChannel();
//        BusiPayBankLimit payBankLimit = new BusiPayBankLimit();
//        payBankLimit.setCode(model_530003.getBankCode());
//        payBankLimit.setBankStatus("1");
//        List<BusiPayBankLimit> limits = busiPayBankLimitMapper.select(payBankLimit);
//        if(limits.isEmpty()){
//           throw new BusinessException("查询银行失败！");
//        }
//
//        BusiPayChannel llChannel = null;
//        BusiPayChannel ldChannel = null;
//        BusiPayChannel tppChannel = null;
//        List<BusiPayChannel> channels = Lists.newArrayList();
//        for(BusiPayBankLimit limit : limits){
//            BigDecimal singleAmt = limit.getSingleAmt();
//            if(singleAmt == null){
//                log.error("数据异常,查询银行限额失败，错误代码：0002，"+ JackJsonUtil.objToStr(limit)+"");
//                throw new BusinessException("系统异常，如有疑问，请联系管理员！错误代码：0002");
//            }
//            if(singleAmt.compareTo(new BigDecimal("0")) == 0
//                    || singleAmt.compareTo(model_530003.getPayCash()) == 1
//                    || singleAmt.compareTo(model_530003.getPayCash()) == 0
//                    ){
//                if(AppConstants.PayChannelCodeContants.LIANLIAN.equals(limit.getPayChannel())){
//                    llChannel = new BusiPayChannel();
//                    llChannel.setPayPlatformCode(limit.getPayChannel());
//                    getPayChannel(llChannel);
//                    if(llChannel != null && !StringUtils.isEmpty(llChannel.getChannelName())){
//                        channels.add(llChannel);
//                    }
//                }
//                if(AppConstants.PayChannelCodeContants.LIANDONG.equals(limit.getPayChannel())){
//                    ldChannel = new BusiPayChannel();
//                    ldChannel.setPayPlatformCode(limit.getPayChannel());
//                    getPayChannel(ldChannel);
//                    if(ldChannel != null && !StringUtils.isEmpty(ldChannel.getChannelName())){
//                        channels.add(ldChannel);
//                    }
//                }
//                if(AppConstants.PayChannelCodeContants.TPP.equals(limit.getPayChannel())){
//                    if(llChannel == null && ldChannel == null){
//                        if(!StringUtils.isEmpty(mainInfo.getBankAccountId())){
//                            tppChannel = new BusiPayChannel();
//                            tppChannel.setPayPlatformCode(limit.getPayChannel());
//                            if(channelMap.get(limit.getPayChannel()) == null){
//                                throw new BusinessException("已超出该卡最大支付限额，建议更换修改金额后，进行投资。银行卡支付限额由银行设定，捞财宝无法提升限额。");
//                            }
//                            tppChannel.setChannelName(channelMap.get(limit.getPayChannel()).getChannelName());
//                            channels.add(tppChannel);
//                        }else{
//                            throw new BusinessException("已超出该卡最大支付限额，建议更换修改金额后，进行投资。银行卡支付限额由银行设定，捞财宝无法提升限额。");
//                        }
//                    }
//                }
//            }
//        }
//        if (!channels.isEmpty()) {
//            return JsonException.toJsonStr(BussErrorCode.ERROR_CODE_0000.getErrorcode(), "0", "发送成功!", channels, null);
//        } else {
//            throw new BusinessException("已超出该卡最大支付限额，建议更换修改金额后，进行投资。银行卡支付限额由银行设定，捞财宝无法提升限额。");
//        }
    }

    /**
     * 2017-03-11 存管版本去除TPP渠道
     *
     * @param bankCode
     * @param payCash
     * @return
     */
    public List<BusiPayChannel> getPayChannel(String bankCode, BigDecimal payCash) {
//        Map<String,BusiPayChannel> channelMap = getChannel();
        BusiPayBankLimit payBankLimit = new BusiPayBankLimit();
        payBankLimit.setCode(bankCode);
        payBankLimit.setBankStatus("1");
        List<BusiPayBankLimit> limits = busiPayBankLimitMapper.select(payBankLimit);
        if (limits.isEmpty()) {
            throw new BusinessException("查询银行失败！");
        }
        BusiPayChannel llChannel = null;
//        BusiPayChannel ldChannel = null;
//        BusiPayChannel tppChannel = null;
        //新增华瑞支付渠道
        BusiPayChannel hrChannel = null;
        List<BusiPayChannel> channels = Lists.newArrayList();
        for (BusiPayBankLimit limit : limits) {
            BigDecimal singleAmt = limit.getSingleAmt();
            if (singleAmt == null) {
                try {
                    log.error("数据异常,查询银行限额失败，错误代码：0002，" + JackJsonUtil.objToStr(limit) + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw new BusinessException("系统异常，如有疑问，请联系管理员！错误代码：0002");
            }
            if (singleAmt.compareTo(new BigDecimal("0")) == 0
                    || singleAmt.compareTo(payCash) == 1
                    || singleAmt.compareTo(payCash) == 0
                    ) {
                if (AppConstants.PayChannelCodeContants.LIANLIAN.equals(limit.getPayChannel())) {
                    llChannel = new BusiPayChannel();
                    llChannel.setPayPlatformCode(limit.getPayChannel());
                    getPayChannel(llChannel);
                    if (llChannel != null && !StringUtils.isEmpty(llChannel.getChannelName())) {
                        channels.add(llChannel);
                    }
                }
//                if (AppConstants.PayChannelCodeContants.LIANDONG.equals(limit.getPayChannel())) {
//                    ldChannel = new BusiPayChannel();
//                    ldChannel.setPayPlatformCode(limit.getPayChannel());
//                    getPayChannel(ldChannel);
//                    if (ldChannel != null && !StringUtils.isEmpty(ldChannel.getChannelName())) {
//                        channels.add(ldChannel);
//                    }
//                }
                //增加华瑞渠道
                if (AppConstants.PayChannelCodeContants.HUARUI_BANK.equals(limit.getPayChannel())) {
                    hrChannel = new BusiPayChannel();
                    hrChannel.setPayPlatformCode(limit.getPayChannel());
                    getPayChannel(hrChannel);
                    if (hrChannel != null && !StringUtils.isEmpty(hrChannel.getChannelName())) {
                        channels.add(hrChannel);
                    }
                }
//                if(AppConstants.PayChannelCodeContants.TPP.equals(limit.getPayChannel())){
//                    if(llChannel == null && ldChannel == null && hrChannel==null){
////                        if(!StringUtils.isEmpty(mainInfo.getBankAccountId())){
//                            tppChannel = new BusiPayChannel();
//                            tppChannel.setPayPlatformCode(limit.getPayChannel());
//                            if(channelMap.get(limit.getPayChannel()) == null){
//                                throw new BusinessException(AppConstants.OUT_BANK_LIMIT_MSG);
//                            }
//                            tppChannel.setChannelName(channelMap.get(limit.getPayChannel()).getChannelName());
//                            channels.add(tppChannel);
////                        }else{
////                            throw new BusinessException("已超出该卡最大支付限额，建议更换修改金额后，进行投资。银行卡支付限额由银行设定，捞财宝无法提升限额。");
////                        }
//                    }
//                }
            }
        }
        if (!channels.isEmpty()) {
            return channels;
        } else {
            throw new BusinessException("已超出该卡最大支付限额，建议更换修改金额后，进行出借。银行卡支付限额由银行设定，捞财宝无法提升限额。");
        }
    }

    public Map<String, BusiPayChannel> getChannel() {
        Example example = new Example(BusiPayChannel.class);
        example.createCriteria().andEqualTo("isEnable", "1");
        List<BusiPayChannel> channels = busiPayChannelMapper.selectByExample(example);
        if (channels.isEmpty()) {
            log.error("系统没有配置有效的支付渠道！错误代码：0001");
            throw new BusinessException("系统异常，如有疑问请联系管理员！错误代码：0001");
        }
        Map<String, BusiPayChannel> channelMap = Maps.newTreeMap();
        for (BusiPayChannel channel : channels) {
            channelMap.put(channel.getPayPlatformCode(), channel);
        }
        return channelMap;
    }

    public boolean checkCardBin(String bankCardNo, String bankCode) {
        try {
            BusiPayChannel payChannel = new BusiPayChannel();
            payChannel.setPayPlatformCode(AppConstants.PayChannelCodeContants.LIANLIAN);
            List<BusiPayChannel> channels = busiPayChannelMapper.select(payChannel);
            if (channels.isEmpty()) {
                log.error("系统没有配置连连的支付渠道信息！");
                throw new BusinessException("检验卡Bin失败，如有疑问，请联系管理员！");
            }
            payChannel = channels.get(0);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("merchantCode", configParamBean.getPayMerchantNo());
            map.put("reqSernum", String.valueOf(new Date().getTime()));
            map.put("reqTime", DateUtil.timeFormat(new Date(), DateUtil.dateFormat));
            map.put("productType", payChannel.getPayPlatformCode());
            map.put("bankCardNo", bankCardNo);
            String sign = getSign(map, configParamBean.getPayPrivateKey());
            map.put("sign", sign);
            log.info("卡bin校验请求：" + JackJsonUtil.objToStr(map));
            StringBuffer res = HttpUtils.URLPost(configParamBean.getCashCardBinUrl(), "data=" + GsonUtils.toJson(map));
            log.info("卡bin校验返回：" + res);
            Map paraMap = CommonHelper.jsonToMapObj(res.toString());
            if ("0000".equals(paraMap.get("responseCode"))) {
                String cardType = paraMap.get("cardType").toString();
                if (!"2".equals(cardType)) {
                    log.info("只能绑定类型为储蓄卡的银行卡");
                    throw new BusinessException("只能绑定类型为储蓄卡的银行卡，如有疑问，请联系管理员！");
                }

                String cashBankCode = paraMap.get("bankCode").toString();
                if (bankCode.equals(cashBankCode)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                String responseDesc = paraMap.get("responseDesc").toString();
                log.error("卡bin校验返回失败，失败信息：" + responseDesc);
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String getSign(Map<String, Object> map, String privateKey) {

        StringBuffer sb = new StringBuffer();
        sb.append(map.get("merchantCode")).append("|")
                .append(map.get("reqSernum")).append("|")
                .append(map.get("reqTime")).append("|")
                .append(map.get("productType")).append("|")
                .append(map.get("bankCardNo")).append("|")
                .append(privateKey);
        try {// 根据验签规则生成验签信息
            String md5SignStr = MD5.MD5Encode(sb.toString());
            return md5SignStr;
        } catch (Exception e) {
            return "";
        }
    }

    public void getPayChannel(BusiPayChannel payChannel) {
        BusiPayChannel channel = new BusiPayChannel();
        channel.setPayPlatformCode(payChannel.getPayPlatformCode());
        channel.setChannelName(null);
        channel.setPayMerchantNo(null);
        channel.setPayPrivateKey(null);
        channel.setPayUrl(null);
        channel.setPayVesion(null);
        channel.setIsEnable((short) 1);
//        6222021001059748589
        List<BusiPayChannel> channels = payChannelMapper.select(channel);
        if (channels.isEmpty()) {
        } else {
            channel = channels.get(0);
            payChannel.setPayVesion(configParamBean.getPayVersion3());
            payChannel.setPayUrl(channel.getPayUrl());
            payChannel.setPayPrivateKey(configParamBean.getPayPrivateKey());
            payChannel.setPayMerchantNo(configParamBean.getPayMerchantNo());
            payChannel.setChannelName(channel.getChannelName());
            payChannel.setUseCity(channel.getUseCity());
        }
    }

    /**
     * 获取支付渠道
     *
     * @param channelCode
     * @return
     */
    public BusiPayChannel validatePayChannel(String channelCode) {
        BusiPayChannel busiPayChannel = new BusiPayChannel();
        busiPayChannel.setIsEnable((short) 1);
        busiPayChannel.setUseCity("1");
        busiPayChannel.setPayPlatformCode(channelCode);
        List<BusiPayChannel> payChannels = busiPayChannelMapper.select(busiPayChannel);
        int resCount = payChannels.size();
        if (resCount == 0) {
            throw new BusinessException("获取支付渠道失败，原因：后台没有设置可使用的支付渠道！");
        }
        if (resCount > 1) {
            throw new BusinessException("获取支付渠道失败，原因：后台设置可使用的支付渠道为多个！");
        }
        return payChannels.get(0);
    }

    /**
     * 获取当前支付渠道
     *
     * @return
     */
    public BusiPayChannel getCurrentPayChannel() {
        BusiPayChannel busiPayChannel = new BusiPayChannel();
        busiPayChannel.setIsEnable((short) 1);
        busiPayChannel.setUseCity("1");
        List<BusiPayChannel> payChannels = busiPayChannelMapper.select(busiPayChannel);
        int resCount = payChannels.size();
        if (resCount == 0) {
            throw new BusinessException("获取支付渠道失败，原因：后台没有设置可使用的支付渠道！");
        }
        if (resCount > 1) {
            throw new BusinessException("获取支付渠道失败，原因：后台设置可使用的支付渠道为多个！");
        }
        return payChannels.get(0);
    }

    /**
     * 获取当前支付渠道
     *
     * @return
     */
    public BusiPayChannel getLianLianPayChannel() {
        BusiPayChannel busiPayChannel = null;
        try {
            busiPayChannel = this.validatePayChannel(AppConstants.PayChannelCodeContants.LIANLIAN);
        } catch (Exception e) {
            log.error("获取支付渠道异常");
        }
        return busiPayChannel;
    }

}
