package com.zdmoney.utils;

import com.zdmoney.exception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * validator校验工具类
 *
 * @author gosling
 * @date 2018年8月30日 11:26:39
 */
public class ValidatorUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws BusinessException  校验不通过，则报BusinessException异常
     */
    public static void validate(Object object, Class<?>... groups) throws BusinessException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new BusinessException(constraint.getMessage());
        }
    }

}
