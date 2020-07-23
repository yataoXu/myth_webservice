package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2017/9/5.
 */
@Data
public class  BusiUnbindRecordVO implements Serializable{


    private Long id;

    private String ubBankCode;

    private String ubTelephone;

    private String ubRealName;

    private String ubIdnum;

    private Date operTime;

    private String operMan;

    private String remark;

    private String unType;
}
