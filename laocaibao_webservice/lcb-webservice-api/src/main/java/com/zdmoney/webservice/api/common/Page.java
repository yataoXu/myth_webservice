package com.zdmoney.webservice.api.common;

import com.google.common.collect.Lists;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Page<T> implements Serializable{

    private static final long serialVersionUID = -3385194333675039902L;

    private int pageNo = 1;//页码，默认是第一页
    private int pageSize = 10;//每页显示的记录数，默认是20
    private int totalRecord;//总记录数  
    private int totalPage;//总页数  
    private List<T> results = Lists.newArrayList();//对应的当前页记录

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

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        //在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.setTotalPage(totalPage);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Page [pageNo=").append(pageNo).append(", pageSize=")
                .append(pageSize).append(", results=").append(results).append(
                ", totalPage=").append(totalPage).append(
                ", totalRecord=").append(totalRecord).append("]");
        return builder.toString();
    }

    public void setBaseParam(HttpServletRequest request) {
        int pageNo = StringUtil.parseInteger(request.getParameter("pageNo"), 1);
        int pageSize = StringUtil.parseInteger(request.getParameter("pageSize"), 10);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

    public void setBaseParamByJson(JSONObject json) {
        int pageNo = StringUtil.parseInteger(json.get("pageNo"), 1);
        int pageSize = StringUtil.parseInteger(json.get("pageSize"), 10);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

    public void setBaseParam(Map<String, Object> map) {
        int pageNo = StringUtil.parseInteger(map.get("pageNo"), 1);
        int pageSize = StringUtil.parseInteger(map.get("pageSize"), 10);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

}  