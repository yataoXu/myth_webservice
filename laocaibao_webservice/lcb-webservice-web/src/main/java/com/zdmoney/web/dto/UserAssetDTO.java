package com.zdmoney.web.dto;

import com.zdmoney.utils.Page;
import com.zdmoney.vo.UserAssetVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 00225181 on 2015/12/2.
 * 2.0用户持有资产接口
 */
@Getter
@Setter
public class UserAssetDTO {
    /*用户持有资产列表*/
    private Page<UserAssetVo> userAssetVos = new Page<UserAssetVo>();

    /*持有总资产*/
    private String holdAsset = "0";

    /*待收总利息*/
    private String unCollectInterest = "0";

    /*已收总利息*/
    private String collectedInterest = "0";

    // 待收加息利息 v4.1
    private String noRecieveAddInterest = "0";

    // 待收利息 v4.1
    private String noRecieveInterest = "0";

    /*已收加息利息 v4.1*/
    private String recieveAddInterest = "0";

    /**已收加息 v4.1*/
    private String recieveInterest = "0";


}
