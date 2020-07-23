package com.zdmoney.component.mq;

import com.zdmoney.marketing.common.RandomCharUtils;
import com.zdmoney.mq.client.MqMessage;
import com.zdmoney.mq.client.group.MqGroup;
import com.zdmoney.mq.client.producer.MqProducer;
import com.zdmoney.mq.client.producer.handler.MqSendFailHandler;
import com.zdmoney.webservice.api.common.MsgType;
import com.zdmoney.webservice.api.common.WelfareType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/11/2.
 * A util class for sending all kinds of message using different MqProducer to generate different messages of topics
        */
@Service
@Slf4j
public class ProducerService implements ApplicationContextAware{


    private static ApplicationContext ctx;

    /*@Autowired()
    @Qualifier("welfareMsgProducer")
    private MqProducer welfareMsgProducer;*/

    private static  Map<String,MqProducer> producerMap;

    SimpleDateFormat timeStrFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    protected MqProducer getProducer(String topic){
        return InitializtionHelper.getProducer(topic);
    }

    private static class InitializtionHelper{
        static{
            producerMap = new HashMap<>();
            String[] beanNames = ctx.getBeanNamesForType(MqProducer.class);
            for(String beanName : beanNames){
                MqProducer mqProducer = ctx.getBean(beanName, MqProducer.class);
                producerMap.put(mqProducer.getTopic(),mqProducer);
            }
            ctx = null;
        }
        static MqProducer getProducer(String topic){
            return producerMap.get(topic);
        }
    }

    /**
     * 注册送红包
     * @param data
     */
    public final void sendRegistrationMsg(String data){
        /*String key = getKey(WelfareType.REGISTRATION.getPrefix(),WelfareType.splitor);
        final MqMessage msg = new MqMessage(key,data);*/
        sendMsg(data,MqGroup.WEBSERVICE_WELFARE_4_2,WelfareType.REGISTRATION.getPrefix(),WelfareType.splitor);
    }

    /**
     * 发送数据采集消息
     * @param data 消息体
     * @param msgType 消息类型,用于区分同一topic下的不同消息，不可为空
     */
    @Async
    public final void sendCollectingDataMsg(String data,MsgType msgType){
        if(msgType == null){
            log.error("MsgType must not be null",new IllegalArgumentException("MsgType must not be null"));
            return;
        }
        sendMsg(data,MqGroup.WEBSERVICE_COLLECTING_DATA,msgType.getPrefix(),MsgType.splitor);
    }

    /**
     * 挖财信息注册确认通知标的
     * @param data
     * @param msgType
     */
    @Async
    public final void sendWacaiInfoConfirmDataMsg(String data,MsgType msgType){
        if(msgType == null){
            log.error("MsgType must not be null",new IllegalArgumentException("MsgType must not be null"));
            return;
        }
        sendMsg(data,MqGroup.WEBSERVICE_WACAI_INFOCONFIRM_NOTICE,msgType.getPrefix(),MsgType.splitor);
    }

    /**
     * 通用的发送消息方法
     * @param data 消息体
     * @param group 消息topic
     * @param keyPrefix key的前缀，用于区分同一topic下的不同消息，可为空
     */
    public void sendMsg(String data, MqGroup group, String keyPrefix,String splitor){
        if(group == null){
            log.error("Group must not be null",new IllegalArgumentException("Group must not be null"));
            return;
        }
        String key = getKey(keyPrefix,splitor);
        final MqMessage msg = new MqMessage(key,data);
        MqProducer producer =  getProducer(group.getTopic());
        if(producer == null){
            log.error("Can not find proper producer for topic:"+group.getTopic(),
                    new RuntimeException("Can not find proper producer for topic:"+group.getTopic()));
            return;
        }
        doSendMsg(producer,msg);
    }
    /**
     * 发送消息
     * @param mqProducer
     * @param msg
     */
    void doSendMsg(final MqProducer mqProducer,final MqMessage msg ){
        try {
            mqProducer.send(msg, new MqSendFailHandler() {//retry at most four times if sending fails
                int n = 0;

                @Override
                public void execute() {
                    if (n++ < 4) {
                        try {
                            mqProducer.send(msg, this);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    /**
     * 使用自定义的splitor
     * @param busiType
     * @param splitor
     * @return
     */
    public  String getKey(String busiType,String splitor) {
        String key = "";
        key = busiType + splitor;
        key = key + timeStrFormat.format(new Date()) + "_";
        key = key + RandomCharUtils.createRandomCharData(16);
        return key;
    }

    /**
     * 默认使用“_” 作为分割符
     * @param busiType
     * @return
     */
    public  String getKey(String busiType) {
        return getKey(busiType,"_");
    }
}
