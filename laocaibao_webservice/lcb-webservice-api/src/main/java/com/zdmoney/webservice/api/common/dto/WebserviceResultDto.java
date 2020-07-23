package com.zdmoney.webservice.api.common.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by xinqigu on 2015/8/24.
 */
public class WebserviceResultDto<T> implements Serializable {

    private static final long serialVersionUID = -4486931933403031940L;

    /** 正确代码 */
    public static final String SUCCESS_CODE = "0000";

    /** 错误代码 */
    public static final String ERROR_CODE = "1111";

    /** 用于绑卡返回原始信息   */
    public static final String ORIGINAL_ERROR_CODE = "3333";

    /**
     * 返回（正确/错误）代码
     */
    protected String code;

    /**
     * 返回信息描述
     */
    protected String msg;

    /**
     * 返回结果集
     */
    protected T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WebserviceResultDto() {
        this.code = SUCCESS_CODE;
    }

    /**
     * 成功
     * @param data
     */
    public WebserviceResultDto(T data) {
        this.code = SUCCESS_CODE;
        this.data = data;
    }

    /**
     * 成功
     * @param msg
     * @param data
     */
    public WebserviceResultDto(String msg, T data) {
        this.code = SUCCESS_CODE;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 成功or失败
     * @param msg
     * @param isSuccess
     */
    public WebserviceResultDto(String msg, boolean isSuccess) {
        if(!isSuccess){
            this.code = ERROR_CODE;
        }
        this.msg = msg;
    }

    /**
     * 成功or失败
     * @param msg
     * @param isSuccess
     */
    public WebserviceResultDto(String msg, T data, boolean isSuccess) {
        if(!isSuccess){
            this.code = ERROR_CODE;
        }
        this.msg = msg;
        this.data = data;
    }


    public WebserviceResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WebserviceResultDto(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return StringUtils.equalsIgnoreCase(SUCCESS_CODE, code);
    }

    public static ResultDto FAIL(String message) {
        return new ResultDto(message, false);
    }

    public static ResultDto SUCCESS() {
        return new ResultDto();
    }
}
