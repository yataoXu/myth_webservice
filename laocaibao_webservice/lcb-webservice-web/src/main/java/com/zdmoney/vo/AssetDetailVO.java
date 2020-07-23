package com.zdmoney.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 00250968 on 2017/11/29
 **/
@Data
public class AssetDetailVO implements Serializable {

    private BigDecimal totalAsset;

    private List<AssetInfo> assetList;

    private BigDecimal accountBalance;

}
