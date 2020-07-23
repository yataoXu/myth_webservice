package com.zdmoney.models.zdpay;

import lombok.Data;

/**
 * @author lwf
 * @date 2018/8/2
 * use:
 */
@Data
public class ChangeMobileRespBO extends BaseBO {

    private static final long serialVersionUID = -5105894130260577475L;

    /**
     * 新手机号
     */
    private String newMobile;
    private String reserved;

}
