package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.assets.api.dto.transfer.TransferPvResDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class TransferInitDTO {


    /*可转让本金*/
    private String principal;

    /*待收利息*/
    private String interest;

    /*剩余期限*/
    private Integer leftDays;

    /*协议列表*/
    private List<ProtocolDTO> protocolList = Lists.newArrayList();

    /*估值列表*/
    private List<EstimateDto> estimateList = Lists.newArrayList();

    /**
     * 初始化还款到账期前，弹窗提示状态 0：默认不弹窗 1：弹窗
     */
    private int alertType= 0;

    /**
     * 初始化还款到账期前，弹窗提示文字
     */
    private String alertInfo;

    /*v4.6估值列表*/
    private List<TransferPvResDto.DeliveryDateIrrPvDto> transferEstimateList = Lists.newArrayList();

    /*折扣*/
    private BigDecimal discount;


    /*转让手续费字符串*/
    private String transferFeeRateStr;

    private  int authFlag=0;//授权是否充足 0： 授权充足 1： 授权不足
}
