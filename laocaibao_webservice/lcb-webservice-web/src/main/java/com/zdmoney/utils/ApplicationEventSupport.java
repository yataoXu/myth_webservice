package com.zdmoney.utils;

import org.springframework.context.ApplicationEvent;
import websvc.utils.SpringContextHelper;

/**
 * Created by user on 2019/2/26.
 */
public class ApplicationEventSupport {

    public static class NewOrderEvent extends ApplicationEvent{
        public NewOrderEvent(String orderNo) {
            super(orderNo);
        }

        @Override
        public String getSource() {
            return super.getSource().toString();
        }
    }

    public static class TenderSucceededEvent extends ApplicationEvent {
        public TenderSucceededEvent(String orderNo) {
            super(orderNo);
        }

        @Override
        public String getSource() {
            return super.getSource().toString();
        }
    }

    public static void publishApplicationEvent(ApplicationEvent event){
        SpringContextHelper.context.publishEvent(event);
    }
}
