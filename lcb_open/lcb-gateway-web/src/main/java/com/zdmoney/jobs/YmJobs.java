//package com.zdmoney.jobs;/**
// * Created by pc05 on 2017/12/1.
// */
//
//import com.ctrip.framework.apollo.model.ConfigChange;
//import com.ctrip.framework.apollo.model.ConfigChangeEvent;
//import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
//import com.google.common.collect.Maps;
//import com.xiaoleilu.hutool.util.StrUtil;
//import com.zdmoney.conf.OpenConfig;
//import com.zdmoney.service.WdzjService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.config.TriggerTask;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ScheduledFuture;
//
///**
// * 描述 :
// *
// * @author : huangcy
// * @create : 2017-12-01 16:28
// * @email : huangcy01@zendaimoney.com
// **/
//@Component
//@Slf4j
//public class YmJobs implements SchedulingConfigurer{
//
//    @Autowired
//    private WdzjService wdzjService;
//
//    @Autowired
//    private OpenConfig openConfig;
//
//    private ScheduledTaskRegistrar taskRegistrar;
//
//    private Map<String,TriggerTask> tasks = Maps.newHashMap();
//
//
//    @ApolloConfigChangeListener
//    public void onPropChange(ConfigChangeEvent changeEvent){
//        for (String key : changeEvent.changedKeys()) {
//            ConfigChange change = changeEvent.getChange(key);
//            log.info(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
//            String proper = "";
//            if (StrUtil.equalsIgnoreCase("cron.check.wdzj.data",change.getPropertyName())){
//                proper = "checkWdzjData";
//            }else if(StrUtil.equalsIgnoreCase("cron.update.member.belong",change.getPropertyName())){
//                proper = "updateMemberBelong";
//            }
//            addCheckData(proper);
//        }
//    }
//
//    /**
//     * 动态添加定时任务
//     * author:chenyun
//     * @param cronName
//     */
//    private void addCheckData(final String cronName){
//        if(StrUtil.isBlank(cronName)){
//            return;
//        }
//        TriggerTask triggerTask1 = tasks.get(cronName);
//        if (triggerTask1 != null){
//            TaskScheduler scheduler = this.taskRegistrar.getScheduler();
//            ScheduledFuture<?> future = scheduler.schedule(triggerTask1.getRunnable(), triggerTask1.getTrigger());
//            future.cancel(true);
//        }
//
//        TriggerTask triggerTask = new TriggerTask(new Runnable() {
//            public void run() {
//                // 任务逻辑
//                log.info(cronName+"已启动,启动参数isOpen:" + System.getProperty("isOpen"));
//                if (StrUtil.equalsIgnoreCase(System.getProperty("isOpen"),"true")) {
//                    if (StrUtil.equalsIgnoreCase(cronName,"checkWdzjData")){
//                        wdzjService.checkWdzjData();
//                    }else if(StrUtil.equalsIgnoreCase(cronName,"updateMemberBelong")){
//                        wdzjService.updateWdzjData();
//                    }
//
//                }
//            }
//        }, new Trigger() {
//            public Date nextExecutionTime(TriggerContext triggerContext) {
//                String cronTime = "";
//                if (StrUtil.equalsIgnoreCase(cronName,"checkWdzjData")){
//                    cronTime = openConfig.getCheckWdzjData();
//                }else if(StrUtil.equalsIgnoreCase(cronName,"updateMemberBelong")){
//                    cronTime = openConfig.getUpdateMemberBelong();
//                }
//                // 任务触发，可修改任务的执行周期
//                CronTrigger trigger = new CronTrigger(cronTime);
//                Date nextExec = trigger.nextExecutionTime(triggerContext);
//                return nextExec;
//            }
//        });
//        this.taskRegistrar.addTriggerTask(triggerTask);
//        this.tasks.put(cronName,triggerTask);
//    }
//
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        this.taskRegistrar=taskRegistrar;
//        addCheckData("checkWdzjData");
//        addCheckData("updateMemberBelong");
//    }
//}
