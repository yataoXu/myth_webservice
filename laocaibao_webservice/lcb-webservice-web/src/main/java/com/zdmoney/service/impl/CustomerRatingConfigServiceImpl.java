package com.zdmoney.service.impl;

import com.google.common.collect.Maps;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.CustomerRatingConfigMapper;
import com.zdmoney.models.CustomerRatingConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zdmoney.service.CustomerRatingConfigService;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * @date 2019-01-02 23:46:36
 */
@Service("customerRatingConfigService")
public class CustomerRatingConfigServiceImpl implements CustomerRatingConfigService,InitializingBean {

    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRatingConfigServiceImpl.class);

    @Autowired
    private CustomerRatingConfigMapper customerRatingConfigMapper;

    @Autowired
    private CuratorFramework curatorFramework;

    private volatile Map<Integer,CustomerRatingConfig> configMap;

    private volatile List<CustomerRatingConfig> ratingConfigList;

    private static final String NODE_PATH = "/customerRatingConfigs";

    private static final String NODE_DATA = "1";

    @Override
    public void afterPropertiesSet() throws Exception {
        initConfigs();
        createNodeAndListenTo();
    }

    void initConfigs(){
        synchronized (this){
            List<CustomerRatingConfig> customerRatingConfigs =
                    queryCustomerRatingConfig(new HashMap<String, Object>());
            Collections.sort(customerRatingConfigs, new Comparator<CustomerRatingConfig>() {
                @Override
                public int compare(CustomerRatingConfig o1, CustomerRatingConfig o2) {
                    return Integer.compare(o1.getRatingNum(), o2.getRatingNum());
                }
            });
            for(CustomerRatingConfig ratingConfig : customerRatingConfigs){
                ratingConfig.setMaxInvestingAmt(ratingConfig.getMaxInvestingAmt() != null?
                        ratingConfig.getMaxInvestingAmt().multiply(BigDecimal.valueOf(10000)):null);
                ratingConfig.setMinInvestingAmt(ratingConfig.getMinInvestingAmt().multiply(BigDecimal.valueOf(10000)));
            }
            ratingConfigList = customerRatingConfigs;
            Map<Integer,CustomerRatingConfig> map = new HashMap<>();
            for(CustomerRatingConfig ratingConfig:ratingConfigList){
                map.put(ratingConfig.getRatingNum(),ratingConfig);
            }
            configMap = map;
        }
    }

    void createNodeAndListenTo(){
        try{
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(NODE_PATH);
        }catch (Exception e){
            //ignore
        }
        try{
            final NodeCache nodeCache = new NodeCache(curatorFramework, NODE_PATH, false);
            nodeCache.start(true);
            nodeCache.getListenable().addListener(
                    new NodeCacheListener() {
                        @Override
                        public void nodeChanged() throws Exception {
                            /*System.out.println("Node data is changed, new data: " +
                                    new String(nodeCache.getCurrentData().getData()));*/
                            reset();
                        }
                    }
            );
        }catch (Exception e){
            LOGGER.error("监听zk失败会员等级配置节点失败",e);
        }
    }

    void reset(){
        LOGGER.info("重新读取会员等级配置");
        initConfigs();
    }

    void notifyZk(){
        try {
            curatorFramework.setData().forPath(NODE_PATH, NODE_DATA.getBytes());
        } catch (Exception e) {
            LOGGER.error("通知zk失败会员等级配置变更失败",e);
        }
    }

    @Override
    public List<CustomerRatingConfig> queryCustomerRatingConfig(Map<String, Object> paramsMap){
        return customerRatingConfigMapper.queryCustomerRatingConfig(paramsMap);
    }

    @Override
    public int updateCustomerRatingConfig(CustomerRatingConfig customerRatingConfig){
        int n= customerRatingConfigMapper.updateCustomerRatingConfig(customerRatingConfig);
        notifyZk();
        return n;
    }

    @Override
    public CustomerRatingConfig findByRatingNum(Integer ratingNum) {
        return configMap.get(ratingNum);
    }

    @Override
    public CustomerRatingConfig findRatingByInvestingAmt(BigDecimal investingAmt) {
        Assert.notNull(investingAmt, "在投金额不能为空");
        if(investingAmt.compareTo(BigDecimal.ZERO) == 0) return ratingConfigList.get(0);
        for(CustomerRatingConfig ratingConfig:ratingConfigList){
            if(investingAmt.compareTo(ratingConfig.getMinInvestingAmt())>0 &&
                    (ratingConfig.getMaxInvestingAmt() == null || investingAmt.compareTo(ratingConfig.getMaxInvestingAmt())<=0)){
                return ratingConfig;
            }
        }
        throw new BusinessException("找不到在投金额对应会员等级");
    }

    @Override
    public List<CustomerRatingConfig> findConfigsBetweenTwoRatings(Integer minRatingNum, Integer maxRatingNum) {
        return findConfigsBetweenTwoRatings(minRatingNum, maxRatingNum, false);
    }

    @Override
    public List<CustomerRatingConfig> findInclusiveConfigsBetweenTwoRatings(Integer minRatingNum, Integer maxRatingNum) {
        return findConfigsBetweenTwoRatings(minRatingNum, maxRatingNum, true);
    }

    List<CustomerRatingConfig> findConfigsBetweenTwoRatings(Integer minRatingNum, Integer maxRatingNum, boolean inclusive){
        List<CustomerRatingConfig> list = new ArrayList<>();
        for(CustomerRatingConfig ratingConfig : ratingConfigList){
            int ratingNum = ratingConfig.getRatingNum();
            if(ratingNum > minRatingNum && ratingNum < maxRatingNum
                    || (inclusive && (ratingNum >= minRatingNum && ratingNum <= maxRatingNum))){
                list.add(ratingConfig);
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<CustomerRatingConfig> findAll() {
        return Collections.unmodifiableList(ratingConfigList) ;
    }


    @Override
    public List<CustomerRatingConfig> findAllNoCache() {
        Map<String, Object> objectObjectHashMap = Maps.newHashMap();
        return customerRatingConfigMapper.queryCustomerRatingConfig(objectObjectHashMap) ;
    }
}
