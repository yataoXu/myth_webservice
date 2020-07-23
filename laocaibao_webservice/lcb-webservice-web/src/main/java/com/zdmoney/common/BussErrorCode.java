package com.zdmoney.common;

import com.google.common.base.Objects;

/**
 * 错误码
 * @author 黄基霖
 *
 */
public enum BussErrorCode {
	/**
	 * 前两位:系统or模块                  例:推送任务模块11
	 * 后两位:具体详细错误 例:校验码验证失败01
	 * */
    /**
     * 成功
     */
    ERROR_CODE_0000("0000","成功"),
    /**
     * 系统JSON转换异常
     */
    ERROR_CODE_0101("0101","系统JSON转换异常"),
    
    /**
     * 请求APP的版本过低
     */
    ERROR_CODE_2100("2100","请求APP的版本过低"),
    
    /**
     * 匹配不到请求APP的类型
     */
    ERROR_CODE_2101("2101","匹配不到请求APP的类型"),
    
    /**
     * 系统异常,请联系管理员
     */
    ERROR_CODE_0102("0102","系统异常,请联系管理员"),
    
    /**
     * 请求参数不正确
     */
    ERROR_CODE_0103("0103","请求参数不正确"),
    
    /**
     * 请求参数长度超过最大允许长度
     */
    ERROR_CODE_0104("0104","请求参数长度超过最大允许长度"),
    
    /**
     * 日期格式不正确
     */
    ERROR_CODE_0105("0105","日期格式不正确"),
    
    /**
     * 参数类型不正确
     */
    ERROR_CODE_0106("0106","参数类型不正确"),
    
    /**
     * 校验码验证失败
     */
    ERROR_CODE_0107("0107","校验码验证失败"),
    
    /**
     * URL请求过期
     */
    ERROR_CODE_0108("0108","URL请求过期"),
    
    /**
     * 流水号已存在
     */
    ERROR_CODE_0109("0109","流水号已存在"),
    
    /**
     * 验签失败
     */
    ERROR_CODE_0110("0110","验签失败"),

    /**
     * 未登录
     */
    ERROR_CODE_0111("0111","未登录"),

    /**
     * 未登录
     */
    ERROR_CODE_0112("0112","已在另一设备登录"),

    /**
     * 会话失效
     */
    ERROR_CODE_0113("0113","会话失效"),

    /**
     * 本次请求超时
     */
    ERROR_CODE_1102("1102","本次请求超时"),

    /**
     * 无法通过项目编号找到密钥
     */
    ERROR_CODE_1103("1103","无法通过项目编号找到密钥"),
    
    /**
     * 新增设备失败,用户设备已存在
     */
    ERROR_CODE_1104("1104","新增设备失败,用户设备已存在"),
    
    /**
     * 注销设备失败,用户不存在
     */
    ERROR_CODE_1105("1105","注销设备失败,用户不存在"),
    
    /**
     * 注销设备失败,设备不存在
     */
    ERROR_CODE_1106("1106","注销设备失败,设备不存在"),
    /**
     * 项目编号不能为空
     */
    ERROR_CODE_1107("1107","项目编号不能为空"),
    
    /**
     * 项目私有密钥没配置
     */
    ERROR_CODE_1108("1108","项目私有密钥没配置"),
    /**
     * 网络连接超时
     */
    ERROR_CODE_1109("1109","连接第三方系统异常"),

    ERROR_CODE_1110("1110","邮件发送失败"),

    ERROR_CODE_1111("1111","校验JwtToken失败");

    private String errorcode;

    public String getErrorcode() {
        return errorcode;
    }

    public String getErrordesc() {
        return errordesc;
    }

    private String errordesc;

    BussErrorCode(String errorcode, String errordesc) {
        this.errorcode = errorcode;
        this.errordesc = errordesc;
    }

    public static String explain(String errorCode) {
        for (BussErrorCode bussErrorCode : BussErrorCode.values()) {
            if (Objects.equal(errorCode, bussErrorCode.errorcode)) {
                return bussErrorCode.errordesc;
            }
        }
        return errorCode;
    }


}
