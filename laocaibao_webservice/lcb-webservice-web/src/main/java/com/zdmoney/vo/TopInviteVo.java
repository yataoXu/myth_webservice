/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * TopInviteVo
 * <p/>
 * Author: Hao Chen
 * Date: 2016-07-22 10:43
 * Mail: haoc@zendaimoney.com
 */
@Setter
@Getter
public class TopInviteVo {

    private int inviteCount;

    private String cellphone;

    private Date mTime;

}