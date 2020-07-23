package com.zdmoney.common;


import com.zdmoney.trace.log.common.LogLevel;
import com.zdmoney.trace.log.provider.LogProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * 动态更改log日志级别
 * Author: silence.cheng
 * Date: 2017/10/31 15:24
 */
@Slf4j
public class DynamicChangeLogLevel implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //防止重复执行
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            log.info("=========================动态更改log日志级别初始化==================================");
            LogProvider logProvider = new LogProvider();
            logProvider.setRootLevel(LogLevel.INFO);   // 修改root 等级
            //修改某个包或类名（全路径）的等级
            //logProvider.setlogNameLevel("com.zdmoney.service",LogLevel.WARN);

        }
    }
}
