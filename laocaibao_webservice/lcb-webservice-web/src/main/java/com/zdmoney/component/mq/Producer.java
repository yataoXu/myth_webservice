package com.zdmoney.component.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.framework.annotation.SaveSendLog;
import com.zdmoney.mq.client.group.MqGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产者，初始化MQProducer
 * 活动消息队列
 */
@Service
@Slf4j
public class Producer {

    private List<DefaultMQProducer> producerList = new ArrayList<DefaultMQProducer>();

    @Autowired
    private ConfigParamBean configParamBean;

    /**
     * 启动RocketMq生产者
     */
    public DefaultMQProducer startProducer(String groupName,String topic,String tag) throws MQClientException {
        log.info("启动RocketMq生产者..{}" , groupName);

        //验证group，保证正确性
        MqGroup.validate(groupName,topic,tag);
        DefaultMQProducer producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(configParamBean.getNameSrvAddr());
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("启动RocketMq生产者异常", e);
            throw e;
        }
        log.info("启动RocketMq生产者成功..{}" , groupName);
        return producer;
    }


    /**
     * 发送消息
     *
     * @param groupName 消息组
     * @param key       业务id
     * @param body      消息内容
     * @return
     */
    @Async
    @SaveSendLog(sendName = "MarketingMq")
    public SendResult sendMsg(String groupName, String key, String body) throws Exception {
        if (null == groupName || groupName.isEmpty() || null == body || body.isEmpty()) {
            return null;
        }

        //验证group和topic，保证正确性
        MqGroup.validate(groupName,groupName, "push");

        Message msg = new Message(groupName,
                "push",
                key,
                body.getBytes());

        int isStart = 0;//标示消息组是否已经启动
        try {
            if (producerList.isEmpty()) {
                //消息组未启动
                DefaultMQProducer mqProducer = startProducer(groupName,groupName, "push");
                producerList.add(mqProducer);
                return send(mqProducer,msg);
            } else {
                for (DefaultMQProducer p : producerList) {
                    //如果找到匹配的组，则发消息
                    if (groupName.equals(p.getProducerGroup())) {
                        return send(p,msg);
                    } else {
                        isStart = 1;//消息组未启动
                    }
                }
            }
            if (isStart == 1) {
                //消息组未启动
                DefaultMQProducer mqProducer = startProducer(groupName,groupName, "push");
                producerList.add(mqProducer);
                return send(mqProducer,msg);
            }
        } catch (Exception e) {
            log.error("发送消息异常", e);
            throw e;
        }
        return null;
    }

    private SendResult send(DefaultMQProducer mqProducer,Message msg)throws Exception {
        for(int i=0;i<5;i++){
            try {
                SendResult result = mqProducer.send(msg);
                return result;
            }catch (Exception e){
                if(i == 4){
                    log.error("尝试{}次没有发送成功，发送失败！",i+1);
                    throw  e;
                }else{
                    log.warn("尝试发送{}次失败",i+1);
                    Thread.sleep(60*1000);
                    continue;
                }
            }
        }
        return null;
    }

}