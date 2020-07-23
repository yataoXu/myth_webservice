package com.zdmoney.models.flw;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 16:09
 * @Mail : xuyt@zendaimoney.com
 */
@Data
@XmlRootElement
@XmlAccessorType
public class Extension {
    private String reg_time;
    private String mobile;

    public void setMobile(String mobile) {
        String phoneNumber = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        this.mobile = phoneNumber;
    }
}
