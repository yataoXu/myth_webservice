package com.zdmoney.service.sys;

import com.zdmoney.base.BaseTest;
import com.zdmoney.mapper.sys.SysSwitchMapper;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * SysSwitchServiceTest
 * <p/>
 * Author: Hao Chen
 * Date: 2016-04-13 17:10
 * Mail: haoc@zendaimoney.com
 */
public class SysSwitchServiceTest extends BaseTest {

    @Autowired
    private SysSwitchService sysSwitchService;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testGetSwitchIsOn() throws Exception {
        Cache cache = CacheManager.getInstance().getCache(SysSwitchMapper.class.getCanonicalName());
        boolean isOverSold = sysSwitchService.getSwitchIsOn("isOverSold");
        boolean isUpAuthent = sysSwitchService.getSwitchIsOn("isUpAuthent");
        System.out.println(isOverSold);
        System.out.println(isUpAuthent);
        cache.flush();
        sysSwitchService.getSwitchIsOn("isOverSold");
        sysSwitchService.getSwitchIsOn("isUpAuthent");
    }
}