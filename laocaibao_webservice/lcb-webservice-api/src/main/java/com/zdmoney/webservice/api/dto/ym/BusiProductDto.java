package com.zdmoney.webservice.api.dto.ym;/**
 * Created by pc05 on 2017/11/21.
 */

import com.zdmoney.webservice.api.common.dto.PageSearchVo;
import lombok.Data;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-21 15:57
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class BusiProductDto extends PageSearchVo {

    private String startTime;

    private String endTime;
}
