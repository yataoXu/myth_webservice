package com.zdmoney.constant;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import lombok.Getter;

/**
 * 挖财返回码
 * Created by qinz on 2019/3/12.
 */
@Getter
public enum CreditConstant {

    SUCCESS("0000", "接口调用成功"),
    BUSSINESS_SUCCESS("000001", "业务处理成功"),

    PARAM_EMPTY("200004", "缺少业务请求参数"),

    ILLEGAL_USERS("410001", "非法客户（其他渠道客户）"),
    INFO_INCONSISTENT("410002", "三要素不一致"),
    USER_NOTEXIST("410004", "客户不存在"),
    UNBIND("410006", "客户未绑卡"),
    NO_LOAN("410007", "未放款不可解绑"),
    NO_BALANCE("430001", "账户余额不足"),
    TIMES_LIMIT("430002", "提现次数超限"),


    BUSSINESS_ERROR("499999", "业务处理异常"),
    ERROR("900000", "系统异常"),

    WACAI_CHANNEL("1002","挖财"),

    INDEX_STR("?","截取标识");

    private String code;

    private String msg;

    CreditConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultDto getResult(CreditConstant param) {
        return new ResultDto(param.getCode(), param.getMsg());
    }

}
