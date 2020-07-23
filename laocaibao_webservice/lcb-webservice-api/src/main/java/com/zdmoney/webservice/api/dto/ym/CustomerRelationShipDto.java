package com.zdmoney.webservice.api.dto.ym;/**
 * Created by pc05 on 2017/12/7.
 */

import com.zdmoney.webservice.api.common.dto.SerialDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-12-07 16:12
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class CustomerRelationShipDto extends SerialDto {

    private Long id;

    private String custNumber;

    private Integer custType;

    private String inviteCode;

    private Date createDate;

    private Date modifyDate;

    private int count;
}
