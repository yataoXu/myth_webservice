package com.zdmoney.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yp on 2017/6/27.
 */
public class CopyUtil {

    private static final Log log = LogFactory.getLog(CopyUtil.class);

    public static Object copyProperties(Object target,Object source){
        BeanUtilsBean.getInstance().getConvertUtils()
                .register(new SqlDateConverter(null), Date.class);
        BeanUtilsBean.getInstance().getConvertUtils().register(new BigDecimalConverter(null), BigDecimal.class);
        BeanUtilsBean.getInstance().getConvertUtils().register(new StringConverter(null), String.class);
        BeanUtilsBean.getInstance().getConvertUtils().register(new IntegerConverter(null), Integer.class);
        try {
            BeanUtils.copyProperties(target, source);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return target;
    }
}
