package com.zdmoney.webservice.api.dto.ym.vo;/**
 * Created by pc05 on 2017/11/22.
 */

import com.zdmoney.webservice.api.common.dto.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 9:38
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class CustomerMainInfoVo extends BaseDto{
    private String mobile;

    private String name;

    private String referralsCode;

    private Date registerDate;

    private String cardNo;

    private String inviteCode;

    private String customerCode;

    private Date firstConsumeDate;

    private Date lastUpdate;

    private Date realNameTime;

    private String channelCode;

    private String registerSource;

    private Short cmValid;

    private String bk;

    private String customerType;
}
