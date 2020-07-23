package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by silence.cheng on 2017/6/5.
 */

@Data
public class BusiDebtDetailVo implements Serializable{

    private  Long   id;
    private  String productId;
    private  String borrowerNumber;
    private  String borrowerName;
    @JSONField(format = "yyyy-MM-dd")
    private  Date borrowerDate;
    private  String debtType;//债权类型
    /*协议模板列表*/
    private List<AgreementNameDto> agreementTempletes = Lists.newArrayList();
}
