package com.zdmoney.vo;/**
 * Created by pc05 on 2017/8/28.
 */

import com.zdmoney.gamecenter.api.dto.GainFragmentDto;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-08-28 10:55
 * @email : huangcy01@zendaimoney.com
 **/
public class GainFragmentVo extends GainFragmentDto{

    private String elephantParkUrl;//小象拼图URL

    public String getElephantParkUrl() {
        return elephantParkUrl;
    }

    public void setElephantParkUrl(String elephantParkUrl) {
        this.elephantParkUrl = elephantParkUrl;
    }
}
