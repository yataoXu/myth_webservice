package com.zdmoney.webservice.api.common.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by xinqigu on 2015/8/24.
 */
public class ResultDto<T> implements Serializable {

    private static final long serialVersionUID = -4486931933403031940L;

    /** 正确代码 */
    public static final String SUCCESS_CODE = "0000";

    /** 错误代码 */
    public static final String ERROR_CODE = "1111";

    /** 用于绑卡返回原始信息   */
    public static final String ORIGINAL_ERROR_CODE = "3333";

    /** 用户已注册其他渠道非挖财渠道  */
    public static final String AUTH_CODE = "0001";

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

    public ResultDto() {
        this.code = SUCCESS_CODE;
    }

    /**
     * 成功
     * @param data
     */
    public ResultDto(T data) {
        this.code = SUCCESS_CODE;
        this.data = data;
    }

    /**
     * 成功
     * @param msg
     * @param data
     */
    public ResultDto(String msg, T data) {
        this.code = SUCCESS_CODE;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 成功or失败
     * @param msg
     * @param isSuccess
     */
    public ResultDto(String msg, boolean isSuccess) {
        if(isSuccess){
            this.code = SUCCESS_CODE;
        } else {
            this.code = ERROR_CODE;
        }
        this.msg = msg;
    }

    /**
     * 成功or失败
     * @param msg
     * @param isSuccess
     */
    public ResultDto(String msg,T data, boolean isSuccess) {
        if(isSuccess){
            this.code = SUCCESS_CODE;
        }else {
            this.code = ERROR_CODE;
        }
        this.msg = msg;
        this.data = data;
    }


    public ResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultDto(String code, String msg, T data) {
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
