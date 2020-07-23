package com.zdmoney.conf;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
public class OpenConfig implements InitializingBean {

    private Map<String,Field> fieldAutowiredKeyMap;

    private DefaultConversionService conversionService = new DefaultConversionService();

    @Value("${public.key}")
	private String publicKey;

    @Value("${webservice.remote.url}")
    private String webserviceRemoteUrl;

    @Value("${credit.req.key}")
    private String creditReqKey;

    @Value("${message.remote.url}")
    private String msgEmailRemoteUrl;

    @Value("${ym.job.isOpen}")
    private Boolean isOpen;

    @Value("${ym.customers.url}")
    private String ymUrl;

    @Value("${white.ip.list}")
    private String whiteIpList;

    @Value("${email.users}")
    private String emailUsers;

//    @Value("${cron.check.wdzj.data}")
//    private String checkWdzjData;
//
//    @Value("${cron.update.member.belong}")
//    private String updateMemberBelong;

    @Override
    public void afterPropertiesSet(){
        synchronized (this){
            fieldAutowiredKeyMap = new HashMap<>();
            Field[] fields = getClass().getDeclaredFields();
            for(Field field : fields){
                Value fieldAnnotation = field.getAnnotation(Value.class);
                if(fieldAnnotation != null){
                    String autowiredKey = fieldAnnotation.value();
                    String propKey = autowiredKey;
                    if(autowiredKey.startsWith("$")){
                        propKey = autowiredKey.substring(2,autowiredKey.length()-1);
                    }
                    fieldAutowiredKeyMap.put(propKey,field);
                }
            }
        }
    }


    @ApolloConfigChangeListener
    public void onPropChange(ConfigChangeEvent changeEvent){
        for (String key : changeEvent.changedKeys()) {
            ConfigChange change = changeEvent.getChange(key);
            log.info(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
            processChangeEvent(change);
        }
    }

    private void processChangeEvent(ConfigChange change){
        switch (change.getChangeType()){
            case MODIFIED: onModified(change);break;
            case ADDED: break;
            case DELETED: break;
            default:
        }
    }

    private void onModified(ConfigChange change){
        Field field = fieldAutowiredKeyMap.get(change.getPropertyName());
        if (field == null ){
            log.error(String.format("set class com.zdmoney.conf.OpenConfig, field %s failed",change.getPropertyName()));
            return;
        }
        try{
            Class<?> type = field.getType();
            Object newValue = change.getNewValue();
            Object convertedValue = newValue;
            if (type.isPrimitive()){
                convertedValue = conversionService.convert(newValue,type);
            }
            field.set(this,convertedValue);
        }catch (Exception e){
            log.error(String.format("set class com.zdmoney.conf.OpenConfig, field %s failed",change.getPropertyName()),e);
        }
    }
}
