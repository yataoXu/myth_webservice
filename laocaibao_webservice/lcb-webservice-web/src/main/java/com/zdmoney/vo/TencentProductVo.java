package com.zdmoney.vo;

import com.google.common.collect.Lists;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.web.dto.PacketDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TencentProductVo extends BusiProduct implements Serializable {

    private BigDecimal yearRate;
    private BigDecimal remaindAmt;
    private BigDecimal nowProportion;
    private int investPeriod;
    private String sellOut;
    private BigDecimal remaindTime;
    private BigDecimal remaindSaleStartTime;
    private BigDecimal addInterest;
    private String isTogether;
    private String agreementUrl;
    private String productDetailUrl;
    private String imgUrl;
    private String contractTypeDict;
    private String contractDesc;
    private String productUeditor;
    private String fundTypeName;
    private String isBuy;
    private String isNew;//是否新手
    private List<PacketDTO> packetDTOs = Lists.newArrayList();
}
