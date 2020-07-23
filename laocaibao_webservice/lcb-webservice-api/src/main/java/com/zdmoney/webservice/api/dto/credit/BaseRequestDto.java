package com.zdmoney.webservice.api.dto.credit;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by user on 2017/10/19.
 */
public abstract class BaseRequestDto implements Serializable {

    private transient volatile static  Validator validator;//

    private transient volatile boolean validated = false;

    private transient boolean isValid = false;

    private transient String validationMsg;

    @NotBlank(message = "签名不能为空")
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getValidationMsg() {
        return validationMsg;
    }

    public void setValidationMsg(String validationMsg) {
        this.validationMsg = validationMsg;
    }

    public BaseRequestDto(String sign) {
        this.sign = sign;
    }

    public abstract boolean checkSignature();

    public static String generateSignature(String...args){
        throw new UnsupportedOperationException();
    }

    public boolean isValid(){
        if(validated) return isValid;
        if(validator == null)
            getValidator();
        synchronized (this){
            validateFields();
        }
        return isValid;
    }

    private void validateFields(){
        if(validated) return;
        validated = true;
        Set<ConstraintViolation<BaseRequestDto>> constraintViolations = validator.validate(this);
        if (constraintViolations.size() > 0) {
            String errrMsg = "";
            for (ConstraintViolation<BaseRequestDto> constraintViolation : constraintViolations) {
                errrMsg += constraintViolation.getMessage() + ",";
            }
            validationMsg = errrMsg;
            isValid = false;
        }else{
            isValid = true;
        }
    }

    private Validator getValidator(){
        if (validator == null) {
            synchronized (BaseRequestDto.class){
                if(validator == null) {
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    validator = factory.getValidator();
                }
            }
        }
        return validator;
    }
    //getInstance 使用静态内部类的静态域获得
    //枚举类（作为内部类，避免暴露枚举类）中 构造方法中创建，只需定义一个枚举实例
}
