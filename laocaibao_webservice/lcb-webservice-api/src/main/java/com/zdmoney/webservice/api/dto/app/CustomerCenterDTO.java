package com.zdmoney.webservice.api.dto.app;

import java.io.Serializable;

/**
 * Created by 00225181 on 2016/1/4.
 */
public class CustomerCenterDTO implements Serializable{
    private Long id;
    private String title="";
    private String subtitle="";
    private String imgUrl="";
    private String h5Url="";
    private String canShare="";
    private String mustLogin="";
    private Integer unReadTime=0;
    private String bubbleMark="";
    private String pageType="";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getCanShare() {
        return canShare;
    }

    public void setCanShare(String canShare) {
        this.canShare = canShare;
    }

    public String getMustLogin() {
        return mustLogin;
    }

    public void setMustLogin(String mustLogin) {
        this.mustLogin = mustLogin;
    }

    public Integer getUnReadTime() {
        return unReadTime;
    }

    public void setUnReadTime(Integer unReadTime) {
        this.unReadTime = unReadTime;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBubbleMark() {
        return bubbleMark;
    }

    public void setBubbleMark(String bubbleMark) {
        this.bubbleMark = bubbleMark;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }
}
