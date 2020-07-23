package com.zdmoney.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.zdmoney.service.TyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/12/25 0025.
 */
@Slf4j
@Controller
@RequestMapping("/lycj")
public class LycjController {

    @Autowired
    private TyService tyService;

    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping("/sendData")
    @ResponseBody
    public String sendLycjData(String dateTime){
        if (StrUtil.isBlank(setKey())){
            return "不在请求范围!";
        }
        if(StrUtil.isBlank(dateTime) || !ReUtil.isMatch("\\d{4}-\\d{2}-\\d{2}",dateTime)){
            log.info("日期格式不合法->dateTime:[{}]",dateTime);return "日期格式不合法!";
        }
        Future<String> stringFuture = tyService.sendLycjLoanInfo(dateTime, 1, 500);
        Future<String> stringFuture1 = tyService.sendLycjLoanUser(dateTime, 1, 500);
        Future<String> prepayment = tyService.sendLycjPrepayment(dateTime, 1, 500);
        StrBuilder strBuilder = StrBuilder.create();
        while (true){
            if (stringFuture.isDone() && stringFuture1.isDone() && prepayment.isDone()){
                try {
                    strBuilder.append(stringFuture.get()).append(stringFuture1.get()).append(prepayment.get());break;
                } catch (InterruptedException e) {

                    e.printStackTrace();
                    break;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        return strBuilder.toString();
    }

    /**
     * 防止重复请求
     * @return
     */
    public String setKey(){
        StrBuilder strBuilder = StrBuilder.create();
        String key = strBuilder.append("LYCJ_").append(DateUtil.today()).toString();
        return jedisCluster.set(key, "存在", "NX", "PX", 5*1000);
    }
}
