package com.zdmoney.webservice.api.common.dto;/**
 * Created by pc05 on 2017/11/24.
 */

import lombok.Data;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-24 11:27
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class PageSearchVo extends BaseVo{
    /** 页号 */
    protected int pageNo = 1;
    /** 每页数量 */
    protected int pageSize = 20;
}
