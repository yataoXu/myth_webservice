package com.zdmoney.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@Getter
@Setter
public class TransferDTO {

    private String protocolName;

    private String protocolNo;

    private BigDecimal transferFeeRate;

    @JSONField(format = "MM-dd HH:mm")
    private Date applyDate;

    @JSONField(format = "yyyy-MM-dd")
    private Date transferDate;

    private String protocolUrl;
}
