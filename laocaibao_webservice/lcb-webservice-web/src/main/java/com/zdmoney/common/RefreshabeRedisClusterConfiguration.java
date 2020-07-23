package com.zdmoney.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.util.StringUtils;

import java.util.Set;

public class RefreshabeRedisClusterConfiguration extends RedisClusterConfiguration implements InitializingBean {

    @Autowired
    private ConfigParamBean configParamBean;

    private void init(){
        Set<String> hostAndPortSet = StringUtils.commaDelimitedListToSet(configParamBean.getSpring_redis_cluster_nodes());
        for(String hostAndPort:hostAndPortSet){
            String[] args = StringUtils.split(hostAndPort, ":");
            clusterNode(args[0], Integer.valueOf(args[1]).intValue());
        }
        setMaxRedirects(Integer.valueOf(configParamBean.getSpring_redis_cluster_max_redirects()));
    }

    public void refresh(){
        this.init();
        afterRefreshed();
    }

    public void afterRefreshed(){

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
}
