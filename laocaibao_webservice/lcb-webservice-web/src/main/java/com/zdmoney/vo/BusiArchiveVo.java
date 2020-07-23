package com.zdmoney.vo;

import java.io.Serializable;

/**
 * Created by user on 2018/1/18.
 */
public class BusiArchiveVo implements Serializable {

    private String fullName;

    private String descr;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
