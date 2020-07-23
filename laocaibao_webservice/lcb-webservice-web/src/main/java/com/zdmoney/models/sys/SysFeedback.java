package com.zdmoney.models.sys;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_FEEDBACK")
public class SysFeedback extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select S_FEED_SEQ.nextval from dual")
    private Long id;

    private String content;

    private Long customerId;

    private String appType;

    private String system;

    private String appVersion;

    private String systemVersion;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operDate;

    private String feedbackType;

    private String feedbackSource;

    private SecurityString contactWay = SecurityString.valueof(null);//联系方式

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackSource() {
        return feedbackSource;
    }

    public void setFeedbackSource(String feedbackSource) {
        this.feedbackSource = feedbackSource;
    }

    public String getContactWay() {
        return contactWay.getValue();
    }

    public void setContactWay(String contactWay) {
        this.contactWay.setValue(contactWay);
    }
}