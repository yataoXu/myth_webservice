/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.component.zookeeper;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;

/**
 * TimeOutZookeeperTransporter
 * <p/>
 * Author: Hao Chen
 * Date: 2015/12/23 14:49
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
public class TimeOutZookeeperTransporter implements ZookeeperTransporter {
    @Override
    public ZookeeperClient connect(URL url) {
        return new TimeOutZookeeperClient(url);
    }
}