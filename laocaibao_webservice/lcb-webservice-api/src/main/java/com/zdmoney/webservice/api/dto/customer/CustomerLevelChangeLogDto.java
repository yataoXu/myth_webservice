package com.zdmoney.webservice.api.dto.customer;/**
 * Created by pc05 on 2018/2/5.
 */

import com.zdmoney.webservice.api.base.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2018-02-05 14:32
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class CustomerLevelChangeLogDto extends BaseDto {

    private Long custId;

    private String beforeLevel;

    private String afterLevel;

    private Date createDate;

    private Integer isQuit;

    private String changeTime;
}
