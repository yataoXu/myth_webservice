package com.zdmoney.models;

import java.util.Date;

public class TSysPermission {
    private Long id;

    private String permName;

    private String permUrl;

    private String permType;

    private Short permLevel;

    private Short parentId;

    private String creator;

    private String updator;

    private Date createTime;

    private Date updateTime;

    private String resvFld1;

    private String resvFld2;

    private String resvFld3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName == null ? null : permName.trim();
    }

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl == null ? null : permUrl.trim();
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType == null ? null : permType.trim();
    }

    public Short getPermLevel() {
        return permLevel;
    }

    public void setPermLevel(Short permLevel) {
        this.permLevel = permLevel;
    }

    public Short getParentId() {
        return parentId;
    }

    public void setParentId(Short parentId) {
        this.parentId = parentId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getResvFld1() {
        return resvFld1;
    }

    public void setResvFld1(String resvFld1) {
        this.resvFld1 = resvFld1 == null ? null : resvFld1.trim();
    }

    public String getResvFld2() {
        return resvFld2;
    }

    public void setResvFld2(String resvFld2) {
        this.resvFld2 = resvFld2 == null ? null : resvFld2.trim();
    }

    public String getResvFld3() {
        return resvFld3;
    }

    public void setResvFld3(String resvFld3) {
        this.resvFld3 = resvFld3 == null ? null : resvFld3.trim();
    }
}