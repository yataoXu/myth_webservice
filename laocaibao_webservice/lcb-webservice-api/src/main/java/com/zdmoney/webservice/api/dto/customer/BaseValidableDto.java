package com.zdmoney.webservice.api.dto.customer;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by user on 2017/11/21.
 */
public class BaseValidableDto implements Serializable{

    public transient static Validator validator;

    private String errrMsg;

    private transient volatile boolean validated;//flag shows if the bean has been validated

    public static Validator getValidator(){
       return Helper.getValidator();
    }

    public String getErrrMsg(){
        return errrMsg;
    }

    /**
     * check if the bean breaks constraints
     */
    public void validate(){
        Validator v = this.getValidator();
        synchronized (BaseValidableDto.class){
            if(validated) return;
            Set<ConstraintViolation<BaseValidableDto>> constraintViolations = v.validate(this);
            if(!constraintViolations.isEmpty()){
                String errrMsg = "";
                for (ConstraintViolation<BaseValidableDto> constraintViolation : constraintViolations) {
                    errrMsg += constraintViolation.getMessage() + ",";
                }
                this.errrMsg = errrMsg;
            }
            validated = true;
        }
    }

    /**
     * return {#code true} if the bean is valid,or {#code false} not
     * @return
     */
    public boolean isValid(){
        if(validated) return StringUtils.isBlank(errrMsg);
        this.validate();
        return StringUtils.isBlank(errrMsg);
    }

    /**
     * A class for helpping initialize the validator
     */
    static class Helper{
        static{
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        public static Validator getValidator(){ return validator; }
    }
}
