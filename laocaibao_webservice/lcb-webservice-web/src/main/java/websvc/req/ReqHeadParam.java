/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package websvc.req;

import lombok.Data;

import java.io.Serializable;

/**
 * ReqHeadParam
 * <p/>
 * Author: Hao Chen
 * Date: 2015/6/29 15:10
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
@Data
public class ReqHeadParam implements Serializable {

    private String token;

    private String sessionToken;

    private String userAgent;

    private String version;
    
    //机构
    private String mechanism;
    //平台
    private String platform;
    //合作类型
    private String togatherType;
    //渠道
    private String openchannel;

    //系统-捞财宝lcb  信贷credit
    private String system;

    // 设备号
    private String deviceID;

    private String ip;
}