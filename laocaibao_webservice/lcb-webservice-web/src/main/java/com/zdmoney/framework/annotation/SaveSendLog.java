package com.zdmoney.framework.annotation;

/**
 * Created by 00225181 on 2016/3/2.
 */

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SaveSendLog {
    String sendName() default "";
    String busiName() default "";
}
