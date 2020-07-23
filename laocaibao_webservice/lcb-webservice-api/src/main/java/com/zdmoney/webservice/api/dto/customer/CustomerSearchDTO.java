package com.zdmoney.webservice.api.dto.customer;

import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import lombok.Data;

/**
 * Created by user on 2017/9/5.
 */
@Data
public class CustomerSearchDTO extends PageSearchDto{
    private String cmNumber;

    private String cmCellphone;

    private String memberType;

    private String plannerName;

    private String cmRealName;

    private String cmIdnum;

    private String cmEmployee;

    private String cbAccount;

    private String cbValid;

    private String beginTime;

    private String endTime;

    private String ownerCode;//归属人邀请码

    private String introducerCode;//邀请人邀请码

}
