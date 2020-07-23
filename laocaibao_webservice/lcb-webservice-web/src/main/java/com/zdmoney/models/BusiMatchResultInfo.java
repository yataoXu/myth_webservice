package com.zdmoney.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BusiMatchResultInfo {
    private Long id;

    private Long productId;

    private BigDecimal matctAmount;

    private String batchNo;

    private String status;

    private String serialNo;

    private int matchSucSize;

    private List<BusiMatchSucInfo> busiMatchSucInfos;

    private List<BusiAbnorMatchSucInfo> busiAbnorMatchSucInfos;
}