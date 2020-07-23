/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.mapper.sys.SysSwitchMapper;
import com.zdmoney.models.sys.SysSwitch;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * SysSwitchService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-16 17:28
 * Mail: haoc@zendaimoney.com
 */
@Service
public class SysSwitchService extends BaseService<SysSwitch, Long> implements ServletContextAware {

    private SysSwitchMapper getSysSwitchMapper() {
        return (SysSwitchMapper) baseMapper;
    }

    private String webAppRootKey;

    public boolean getSwitchIsOn(String type) {
        List<SysSwitch> sysSwitches = getSysSwitchMapper().selectByType(type);
        return !sysSwitches.isEmpty() && BusiConstants.SYS_SWITCH_ON.equals(sysSwitches.get(0).getSwitchValue());
    }

    public boolean getSwitchIsOnDiffEdition(String prType){
        if(webAppRootKey == null){
            return getSwitchIsOn(prType);
        }else{
            return getSwitchIsOn(webAppRootKey + "." + prType);
        }
    }


    @Override
    public void setServletContext(ServletContext servletContext) {
        webAppRootKey = servletContext.getInitParameter("webAppRootKey");
    }

    public String getWebAppRootKey(){
        return webAppRootKey;
    }
}