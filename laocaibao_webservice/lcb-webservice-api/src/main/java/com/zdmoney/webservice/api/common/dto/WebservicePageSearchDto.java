package com.zdmoney.webservice.api.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinqigu on 2015/8/25.
 */
public class WebservicePageSearchDto extends BaseDto {

    private static final int DEFAULT_PAGE_SIZE = 20;
    /** 页号 */
    protected int pageNo = 1;

    /** 每页数量 */
    protected int pageSize;

    /** 排序属性集合 */
    protected List<SortDto> sortDtoList = new ArrayList<SortDto>();

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<SortDto> getSortDtoList() {
        return sortDtoList;
    }

    public void setSortDtoList(List<SortDto> sortDtoList) {
        this.sortDtoList = sortDtoList;
    }

    public void addSorts(SortDto... sortDtos) {
        for (SortDto sortDto : sortDtos) {
            this.sortDtoList.add(sortDto);
        }
    }

    public int getStart() {
        if(this.pageSize ==0) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        return (this.pageNo-1) * this.pageSize;
    }

}
