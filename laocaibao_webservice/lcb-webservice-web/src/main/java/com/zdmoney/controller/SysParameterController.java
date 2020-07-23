package com.zdmoney.controller;


import com.zdmoney.mapper.sys.SysParameterMapper;
import com.zdmoney.utils.MailUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/sysParameter")
public class SysParameterController {


    @RequestMapping("/reloadParam")
    @ResponseBody
    public String reloadParam(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache(SysParameterMapper.class.getCanonicalName());
        try {
            MailUtil.sendMail("test", "sendMail test");
        }catch (Exception e){

        }
        if (cache != null) {
            cacheManager.removeCache(SysParameterMapper.class.getCanonicalName());
            return "重载成功！";
        } else {
            return "cache 不存在！";
        }
    }


}
