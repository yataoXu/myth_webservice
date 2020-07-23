/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package websvc.req;

import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * ReqMain
 * <p/>
 * Author: Hao Chen
 * Date: 2015/6/29 15:04
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
@Data
public class ReqMain implements Serializable {

    @Valid
    private ReqParam reqParam;

    private ReqHeadParam reqHeadParam;

    private String sign;

    private String sn;

    private String reqUrl;

    private String projectNo;

    private String reqTimestamp;

}