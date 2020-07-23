package com.zdmoney.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2015/12/8.
 */
@Setter
@Getter
public class AccountRecordDTO {

    /*金额*/
    private BigDecimal amount = new BigDecimal(0);

    /*记录类型*/
    private String recordType = "";

    /*流水号*/
    private String recordNum = "";

    /*创建日期*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /*流水类型描述*/
    private String recordTypeDesc = "";

    private Integer direction;

}
