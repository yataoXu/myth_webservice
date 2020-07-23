package com.zdmoney.component.mq;

import com.zdmoney.mq.client.MqMessage;
import com.zdmoney.mq.client.consumer.listener.MqMessageListener;

import java.util.List;

/**
 * Created by user on 2017/11/1.
 * This is a listener for testing modified old sending message method
 * It works well and can consume message of LCB_GROUP topic
 */
public class ActivityMessageListener implements MqMessageListener {
    @Override
    public boolean execute(List<MqMessage> list) {
        for(MqMessage msg : list){
            System.out.println(msg.getKey()+"::"+msg.getData());
        }
        return true;
    }
}
