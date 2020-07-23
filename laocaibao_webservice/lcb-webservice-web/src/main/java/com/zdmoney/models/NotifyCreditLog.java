package com.zdmoney.models;

import lombok.Data;

import java.util.Date;

/**
 * 回调前前记录表
 * Created by qinz on 2019/2/23.
 */
@Data
public class NotifyCreditLog {

    /**申请流水号*/
    private String applyNo;

    private String reqParams;

    private String rspResult;

    /**1、申请提现失败2、申请提现成功3、到账失败*/
    private Integer notifyType;

    private Long customerId;

    private Date createTime;

    private Date modifyTime;

}
