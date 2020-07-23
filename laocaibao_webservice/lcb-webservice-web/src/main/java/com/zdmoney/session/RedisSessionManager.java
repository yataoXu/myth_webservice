/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.session;

import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisSessionManager
 * <p/>
 * Author: Hao Chen
 * Date: 2015/6/24 10:38
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
@Slf4j
public class RedisSessionManager implements SessionManager {

    private final String LOCK_SUCCESS = "OK";
    private final String SET_IF_NOT_EXIST = "NX";
    private final String SET_WITH_EXPIRE_TIME = "PX";
    private final Long RELEASE_SUCCESS = 1L;
    RedisConnection redisConnection;

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    /* session 前缀 */
    private static final String EDGE_INFO_KEY = "lcb_edge_info";

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String get(String key) {
        if (key == null)
            return null;
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Set<String> getKeys(String key) {
        return stringRedisTemplate.keys(key);
    }

    @Override
    public void put(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public String getLike(String key) {
        Set<String> keys = stringRedisTemplate.keys(key);
        return get(Iterators.getNext(keys.iterator(), null));
    }

    @Override
    public void updateTimeout(String key, Integer timeout, TimeUnit timeUnit) {
        log.info("updateTimeout begin,key【{}】",key);
        if (StringUtils.contains(key, "*")) {
            Set<String> keys = stringRedisTemplate.keys(key);
            for (String s : keys) {
                stringRedisTemplate.expire(s, timeout, timeUnit);
            }
            log.info("expire with star symbol,perhaps one or more key");
        } else {
            stringRedisTemplate.expire(key, timeout, timeUnit);
            log.info("expire without star symbol,only one key");
        }
        log.info("updateTimeout end,key【{}】",key);
    }

    @Override
    public void addEdgeInfo(String key) {
        log.info("addEdgeInfo begin,key【{}】",key);
        stringRedisTemplate.opsForHash().put(EDGE_INFO_KEY, key, String.valueOf(new Date().getTime()));
        log.info("addEdgeInfo end,key【{}】",key);
    }

    @Override
    public String getEdgeInfo(String key) {
        return (String) stringRedisTemplate.opsForHash().get(EDGE_INFO_KEY, key);
    }

    public void removeEdgeInfo(String key) {
        log.info("removeEdgeInfo begin,key 【{}】",key);
        stringRedisTemplate.opsForHash().delete(EDGE_INFO_KEY, key);
        log.info("removeEdgeInfo end,key 【{}】",key);
    }

    @Override
    public boolean setNX(String key, String lock) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, lock);
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.expire(key, timeout, timeUnit);
    }


    @Override
    public void putHash(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key,hashKey,value);
    }

    @Override
    public void removeHash(String key, String hashKey) {
        stringRedisTemplate.opsForHash().delete(key,hashKey);
    }

    @Override
    public String getHash(String key, String hashKey) {
        return (String)stringRedisTemplate.opsForHash().get(key,hashKey);
    }



    /**
     * @Author: weiNian
     * @param
     * @Description: 获取一个jedis 客户端
     * @Date: 2018/7/20 17:25
     */
    private JedisCluster getJedis(){

            redisConnection = jedisConnectionFactory.getConnection();
            return  (JedisCluster)redisConnection.getNativeConnection();
    }

    /**
     * @Author: weiNian
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @param repeat 重试次数
     * @Description: 尝试获取分布式锁,间隔1秒重试
     * @Date: 2018/7/20 17:25
     */
    public  boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime ,int repeat) throws  Exception{
        JedisCluster jedisCluster = getJedis();
        String result = "";
        int curRepeat = 0;
        while (!LOCK_SUCCESS.equals(result) && curRepeat<= repeat){
            result = jedisCluster.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (!LOCK_SUCCESS.equals(result)) {
                log.info("{}第{}次尝试获取资源锁",Thread.currentThread().getName(),curRepeat+1);
                Thread.sleep(1000);
                curRepeat++;
            }
        }
        redisConnection.close();
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }


    /**
     * @Author: weiNian
     * @param lockKey 锁
     * @param requestId 请求标识
     * @Description: 释放分布式锁
     * @Date: 2018/7/20 17:32
     */
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        JedisCluster jedisCluster = getJedis();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedisCluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        redisConnection.close();
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key){
        JedisCluster jedisCluster = getJedis();
        return jedisCluster.exists(key);
    }

}