package com.zdmoney.models.customer;/**
 * Created by pc05 on 2018/2/5.
 */

import lombok.Data;

import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2018-02-05 14:19
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class CustomerLevelChangeLog {

    private Long id;

    private Long custId;

    private String beforeLevel;

    private String afterLevel;

    private Date createDate;
}
