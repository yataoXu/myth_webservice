package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.models.payChannel.BusiPayChannel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by 00231247 on 2016/5/17.
 */
@Getter
@Setter
public class RechargePCDTO {
    private String flowId;
    private String amount;
    private String payUrl;

    private String payPrivateKey;

    private String payMerchantNo;

    private String payVesion;

}
