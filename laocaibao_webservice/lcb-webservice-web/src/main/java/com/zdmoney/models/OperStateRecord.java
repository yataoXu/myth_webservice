package com.zdmoney.models;

import lombok.Data;

import java.util.Date;

/**
 * Created by user on 2018/8/21.
 */
@Data
public class OperStateRecord {

    private Long id;

    private String operType;

    private String keyword;

    private String  currentState;

    private Date createTime;

    private String  extraInfo;

    private String spareField;

    @Override
    public String toString() {
        return "OperStateRecord{" +
                "operType='" + operType + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
