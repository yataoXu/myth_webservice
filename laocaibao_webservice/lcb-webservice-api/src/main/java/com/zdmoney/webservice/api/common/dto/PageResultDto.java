package com.zdmoney.webservice.api.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinqigu on 2015/8/25.
 */
@Data
public class PageResultDto<T> extends ResultDto<T> {

    /**
     * 总记录数
     */
    protected Integer totalSize = 0;

    /**
     * 总页数
     */
    protected Integer totalPage = 0;

    /**
     * 当前页数
     */
    protected Integer pageNo = 1;

    /**
     * 列表
     */
    protected List<T> dataList = new ArrayList();

    public PageResultDto() {
    }

    public PageResultDto(List<T> data, Integer totalSize, Integer pageSize) {
        this.code = SUCCESS_CODE;
        this.totalPage = (totalSize % pageSize == 0) ? totalSize / pageSize : totalSize / pageSize + 1;
        this.dataList = data;
        this.totalSize = totalSize;
    }

    public PageResultDto(List<T> data, Integer totalSize, Integer pageSize, int pageNo) {
        this(data, totalSize, pageSize);
        this.pageNo = pageNo;
    }

    public PageResultDto(String code, String msg, List<T> data) {
        super(code, msg);
        this.dataList = data;
    }
}
