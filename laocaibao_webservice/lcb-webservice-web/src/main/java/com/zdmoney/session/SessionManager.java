/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.session;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * SessionManager
 * <p/>
 * Author: Hao Chen
 * Date: 2015/6/24 10:27
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
public interface SessionManager {

    String get(String key);

    void remove(String key);

    Set<String> getKeys(String key);

    void put(String key, String value, long timeout, TimeUnit timeUnit);

    String getLike(String key);

    void updateTimeout(String key, Integer timeout, TimeUnit timeUnit);

    void addEdgeInfo(String key);

    String getEdgeInfo(String key);

    void removeEdgeInfo(String key);

    boolean setNX(String key, String lock);

    boolean expire(String key, long timeout, TimeUnit timeUnit);

    void putHash(String key, String hashKey, String value);

    void removeHash(String key, String hashKey);

    String getHash(String key, String hashKey);

}