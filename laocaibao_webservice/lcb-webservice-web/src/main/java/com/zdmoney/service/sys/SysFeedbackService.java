/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.google.common.base.Charsets;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.sys.SysFeedbackMapper;
import com.zdmoney.models.sys.SysFeedback;
import com.zdmoney.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * SysFeedbackService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-31 11:48
 * Mail: haoc@zendaimoney.com
 */
@Service
public class SysFeedbackService extends BaseService<SysFeedback, Long> {

    private SysFeedbackMapper getSysFeedbackMapper() {
        return (SysFeedbackMapper) baseMapper;
    }

    public void feedback(Long customerId, String contactWay, String content,String feedbackType) {
        SysFeedback sysFeedback = new SysFeedback();
        sysFeedback.setOperDate(new Date());
        byte[] cnttByte = Base64.decode(content);
        String cntt = new String(cnttByte, Charsets.UTF_8);
        sysFeedback.setContent(cntt);
        sysFeedback.setContactWay(contactWay);
        //反馈来源  1 表示APP
        sysFeedback.setFeedbackSource(AppConstants.FeedbackSource.FEEDBACKSOURCE_APP);
        sysFeedback.setCustomerId(customerId);
        sysFeedback.setFeedbackType(feedbackType);
        getSysFeedbackMapper().insert(sysFeedback);
    }

}