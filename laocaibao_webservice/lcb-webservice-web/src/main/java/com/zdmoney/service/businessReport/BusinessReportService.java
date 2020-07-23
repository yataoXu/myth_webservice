package com.zdmoney.service.businessReport;/**
 * Created by pc05 on 2017/9/28.
 */


import com.zdmoney.data.agent.api.dto.bc.BusinessReportDto;
import com.zdmoney.service.AsyncService;
import com.zdmoney.trace.utils.TraceGenerator;
import com.zdmoney.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 描述 : 业务审计service
 *
 * @author : huangcy
 * @create : 2017-09-28 10:36
 * @email : huangcy01@zendaimoney.com
 **/
@Service
public class BusinessReportService {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public void sendReport(String featureNo,Object content){
        //审计
        BusinessReportDto brDto = new BusinessReportDto();
        brDto.setTraceId(TraceGenerator.generatorId());
        brDto.setSystemNo("S00001");
        brDto.setFeatureNo(featureNo);
        brDto.setNotifyNo("WEB-SERVICE");
        brDto.setNotifyContent(JSONUtils.toJSON(content));
        asyncService.commonAuditInfo(brDto);
    }

}
