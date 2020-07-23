package com.zdmoney.vo.audit;/**
 * Created by pc05 on 2017/9/18.
 */

/**
 * 描述 : 业务审计vo
 *
 * @author : huangcy
 * @create : 2017-09-18 17:53
 * @email : huangcy01@zendaimoney.com
 **/
public class AuditVo {
    public Object params;
    public Object resultDto;

    public Object getResultDto() {
        return resultDto;
    }

    public void setResultDto(Object resultDto) {
        this.resultDto = resultDto;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
