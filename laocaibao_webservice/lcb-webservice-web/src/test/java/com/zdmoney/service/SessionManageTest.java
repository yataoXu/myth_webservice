package com.zdmoney.service;

import com.zdmoney.base.BaseTest;
import com.zdmoney.session.RedisSessionManager;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by 00225181 on 2016/1/5.
 */
public class SessionManageTest extends BaseTest{
    @Autowired
    RedisSessionManager sessionManager;

    @Test
    public void test(){
        sessionManager.remove("lcb_session_app:15821329195:2f0648f9b61b077b5a6c6c663ac42464");
        System.out.println("del...");
        Set<String> keys = sessionManager.getKeys("*15821329195*");
        for(String key:keys){
            System.out.println(key);
            String value = sessionManager.get(key);
            JSONObject json = JSONObject.fromObject(value);
            System.out.println(json.get("cmInviteCode"));
        }
    }
}
