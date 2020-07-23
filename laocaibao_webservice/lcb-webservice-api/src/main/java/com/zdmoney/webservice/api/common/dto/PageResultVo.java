package com.zdmoney.webservice.api.common.dto;/**
 * Created by pc05 on 2017/11/24.
 */

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-24 9:56
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class PageResultVo<T> implements Serializable{
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

    public PageResultVo() {
    }

    public PageResultVo(List<T> data, Integer totalSize, Integer pageSize) {
        this.totalPage = (totalSize % pageSize == 0) ? totalSize / pageSize : totalSize / pageSize + 1;
        this.dataList = data;
        this.totalSize = totalSize;
    }

    public PageResultVo(List<T> data, Integer totalSize, Integer pageSize, int pageNo) {
        this(data, totalSize, pageSize);
        this.pageNo = pageNo;
    }

}
