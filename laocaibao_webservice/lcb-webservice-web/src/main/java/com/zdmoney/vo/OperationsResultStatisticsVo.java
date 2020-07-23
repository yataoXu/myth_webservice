package com.zdmoney.vo;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2018/1/18.
 */
public class OperationsResultStatisticsVo implements Serializable {

    @Transient
    private Date analysisTime;

    private String id;

    private String month;

    public Date getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(Date analysisTime) {
        this.analysisTime = analysisTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
