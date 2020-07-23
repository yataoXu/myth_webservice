package com.zdmoney.annotation;

import java.lang.annotation.*;

/**
 * 是否需要校验签名
 * Created by pc05 on 2017/11/21.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignAuth {

}
