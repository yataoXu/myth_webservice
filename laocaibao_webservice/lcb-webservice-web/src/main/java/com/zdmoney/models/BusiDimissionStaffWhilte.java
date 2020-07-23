package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @date 2019-02-28 09:25:53
 */
@Data
public class BusiDimissionStaffWhilte implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private String cmNumber;

    /**
     * 有效天数
     */
    private Long expiryDays;

    /**
     * 失效日期
     */
    private Date expiryDate;

    /**
     * 离职时间
     */
    private Date quitTime;
    /**
     * 插入日期
     */
    private Date insertDate;

}
