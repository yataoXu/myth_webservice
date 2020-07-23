/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.utils;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;

/**
 * ObjectUtils
 * <p/>
 * Author: Hao Chen
 * Date: 2015/6/9 15:49
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
public class ObjectUtils {

    private static DozerBeanMapper mapper = new DozerBeanMapper(Lists.newArrayList("conf/dozerBeanMapping.xml"));

    public static void copy(Object source, Object dest) {
        mapper.map(source, dest);
    }

    public static <T> T dump(Object source, Class<T> clazz){
        return mapper.map(source, clazz);
    }
}