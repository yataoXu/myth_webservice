package com.zdmoney.service;

import cn.hutool.core.util.RandomUtil;
import com.zdmoney.enm.SerialNoType;
import com.zdmoney.utils.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 号码生成类
 * Created by 00232384 on 2017/2/9.
 */
public class SerialNoGeneratorService {

    /**
     * 生成转让编号
     */
    public static String generatorTransferNo(Long customerId) {
        return "T" + customerId+DateUtil.timeFormat(new Date(), DateUtil.TOTALFORMAT)+(int) (Math.random() * (100));
    }

    /**
     * 生成理财计划子债权转让编号
     */
    public static String generatorSubTransferNo(Long customerId) {
        return "TS" + customerId+DateUtil.timeFormat(new Date(), DateUtil.TOTALFORMAT)+(int) (Math.random() * (100));
    }

    /**
     * 华瑞开户请求流水号（实名认证）
     *
     * @return
     */
    public static String generateAuthSerialNo(Long customerId) {
        return "HRBAUTH" + DateUtil.timeFormat(new Date(), DateUtil.TOTALFORMAT) + customerId;
    }

    /**
     * 华瑞绑卡请求流水号
     *
     * @return
     */
    public static String generateBindSerialNo(Long customerId) {
        return "HRBBIND" + DateUtil.timeFormat(new Date(), DateUtil.TOTALFORMAT) + customerId;
    }

    /**
     * 华瑞绑卡请求流水号
     *
     * @return
     */
    public static String generateRechargeSerialNo(String orderId) {
        return "HRBRC" + orderId ;
    }


    /**
     * 华瑞投标请求流水号
     *
     * @return
     */
    public static String generateTenderSerialNo(String orderId) {
        return "HRBTB" + orderId ;
    }

    /**
     * 华瑞解绑请求流水号
     *
     * @return
     */
    public static String generateUnbindSerialNo(Long customerId) {
        return "HRBUB" + DateUtil.timeFormat(new Date(), DateUtil.TOTALFORMAT) + customerId;
    }

    /**
     * 解散已撮合订单流水号
     * @param customerId
     * @return
     */
    public static String generateDismissSerialNo(Long customerId) {
        return "DISMISS" + DateUtil.timeFormat(new Date(), DateUtil.TOTALFORMAT) + customerId;
    }

    public static String generateSendSerialNo(String orderId) {
        return "HRBSM" + orderId;
    }

    /**
     *
     * @param cmNumber
     * @return
     */
    public static String generateTransferNoByCmNum(String cmNumber) {
        return "TN_" + DateUtil.timeFormat(new Date(), DateUtil.dateFormat) + cmNumber;
    }



    /**
     * 根据订单id生成流水号
     */
    public static String generateTransNoByOrderId(String orderTmpId, String type){
        return type + "_" + orderTmpId;
    }

    /**
     * 根据订单id生成流水号
     */
    public static String generateTransNoRefundByOrderId(String orderTmpId){
        return "TNR_" + orderTmpId;
    }

    /**
     * 根据订单id生成流水号
     */
    public static String generateTransNoExChangeByOrderId(String orderTmpId){
        return "exchange_" + orderTmpId;
    }

    /**
     *签到获取奖励流水号
     * @return
     */
    public static String generateTransNo(String cmNumber){
        return "TN_" + DateUtil.timeFormat(new Date(), DateUtil.YYYYMMDD) + cmNumber;
    }

    /**
     *每日签到获取捞财币的流水号
     * @return
     */
    public static String generateTransNoForSign(String cmNumber){
        return "TN_SIGN" + DateUtil.timeFormat(new Date(), DateUtil.YYYYMMDD) + cmNumber;
    }

    /**
     * 请求流水号
     * @return
     */
    public static String generateTransNoForReq(){
        return "REQ_" + RandomUtil.randomUUID();
    }

    /**
     * 公共请求流水号
     * @return
     */
    public static String commonGenerateTransNo(SerialNoType serialNoPrefix, String cmNumber){
        return serialNoPrefix.getType() +"_"+ DateUtil.timeFormat(new Date(), DateUtil.dateFormat) + cmNumber;
    }

    public static String commonGenerateTransNo(SerialNoType serialNoPrefix, String cmNumber,String dateFormatStr){
        return serialNoPrefix.getType() +"_"+ DateUtil.timeFormat(new Date(), dateFormatStr)+"_" + cmNumber;
    }
}
