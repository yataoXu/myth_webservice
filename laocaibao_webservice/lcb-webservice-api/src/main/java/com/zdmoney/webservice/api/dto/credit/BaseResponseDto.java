package com.zdmoney.webservice.api.dto.credit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/10/19.
 */
public class BaseResponseDto implements Serializable{
    private String code;

    private String msg;

    public BaseResponseDto(CreditResponse response) {
        this.code = response.getCode();
        this.msg = response.getName();
    }

    public BaseResponseDto(CreditResponse response, String msg) {
        this.code = response.getCode();
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess(){
        return  CreditResponse.SUCCESS.getCode().equals(this.code);
    }

    public enum CreditResponse{
        SUCCESS("000000","处理成功",null),
        INVALID_PARAMS("100001","参数验证失败","参数缺失或不合法"),
        INVALID_SIGNATURE("100002","签名验证失败","MD5签名失败"),
        SERVICE_EXCEPTION("100003","调用接口服务异常",null),
        SERVICE_UNACCESSABLE("100004","调用接口不可用","服务禁止调用"),
        INVALID_RESPONSE("100005","被调用接口响应不合法","调用的系统响应内容为空"),
        BID_ALREADY_EXISTS("200001","借款标的已存在","重复推送借款标的"),
        TRADE_FLOW_ALREADY_EXISTS("200002","交易编号重复","如划扣等，带有交易编号的必须唯一"),
        IP_ON_BLACKLIST("300001","调用者IP不在白名单范围内",null),
        SYSTEM_EXCEPTION("999999","系统处理异常","系统异常，联系管理员");

        private static final Map<String,CreditResponse> codeToInstanceMap = new HashMap<String,CreditResponse>();

        private  volatile static boolean inited = false;
        private String code;

        private String name;

        private String descr;

        CreditResponse(String code, String name, String descr) {
            this.code = code;
            this.name = name;
            this.descr = descr;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescr() {
            return descr;
        }

        public static CreditResponse getInstance(String code){
            if(!inited){
                synchronized(CreditResponse.class){
                    if(!inited) {
                        CreditResponse[] responses = CreditResponse.values();
                        for (CreditResponse creditResponse : responses) {
                            codeToInstanceMap.put(creditResponse.getCode(), creditResponse);
                        }
                        inited = true;
                    }
                }
            }
            return codeToInstanceMap.get(code);
        }
    }

}
