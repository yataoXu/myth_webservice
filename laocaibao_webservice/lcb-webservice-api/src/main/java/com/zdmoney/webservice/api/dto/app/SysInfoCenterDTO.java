package com.zdmoney.webservice.api.dto.app;

import java.io.Serializable;

/**
 * Created by 00231247 on 2015/3/16.
 */
public class SysInfoCenterDTO implements Serializable {
    private Long id;

    private String title;

    private String imgUrl;

    private String infoSource;

    private String summary;

    private String publishDate;

    private String remark;

    private String topStatus;

    private String pcImgUrl;

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

    public String getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(String infoSource) {
        this.infoSource = infoSource;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(String topStatus) {
        this.topStatus = topStatus;
    }

    public String getPcImgUrl() {
        return pcImgUrl;
    }

    public void setPcImgUrl(String pcImgUrl) {
        this.pcImgUrl = pcImgUrl;
    }
}
