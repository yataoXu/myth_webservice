package com.zdmoney.webservice.api.dto.ym;/**
 * Created by pc05 on 2017/11/22.
 */


import com.zdmoney.webservice.api.common.dto.PageSearchVo;
import lombok.Data;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 9:30
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class BusiRebateDto extends PageSearchVo {
    private String startTime;

    private String endTime;
}
