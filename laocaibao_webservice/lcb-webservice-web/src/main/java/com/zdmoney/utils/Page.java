package com.zdmoney.utils;

import com.google.common.collect.Lists;
import com.zdmoney.constant.ParamConstant;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Page<T> implements Serializable{

    private static final long serialVersionUID = -3385194333675039902L;

    private int pageNo = 1;//页码，默认是第一页
    private int pageSize = ParamConstant.PAGESIZE;//每页显示的记录数，默认是20  
    private int totalRecord;//总记录数  
    private int totalPage;//总页数  
    private List<T> results = Lists.newArrayList();//对应的当前页记录
//    private String pageStr;

//    public String getPageStr() {  
//        StringBuffer sb = new StringBuffer();  
//        if(totalRecord>0){  
//            sb.append(" <div id=\"DataTables_Table_0_paginate\" class=\"dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_full_numbers\">");
//            sb.append("共"+totalRecord+"条记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
//            sb.append("每页显示&nbsp;<select id=\"pageSizeSelect\" style=\"width: 80px\">");
//            sb.append("<option "+(pageSize==Contains.PAGESIZE?"selected":"")+" value=\""+Contains.PAGESIZE+"\">"+Contains.PAGESIZE+"</option>");
//            sb.append("<option "+(pageSize==50?"selected":"")+" value=\"50\">50</option>");
//            sb.append("<option "+(pageSize==100?"selected":"")+" value=\"100\">100</option>");
//            sb.append("</select>&nbsp;条&nbsp;&nbsp");
//            //首页、第一页
//            if(pageNo==1){  
//                sb.append("<a id=\"DataTables_Table_0_first\" class=\"first ui-corner-tl ui-corner-bl fg-button ui-button ui-state-default ui-state-disabled\" tabIndex=\"0\">First</a>");  
//                sb.append("<a id=\"DataTables_Table_0_previous\" class=\"previous fg-button ui-button ui-state-default ui-state-disabled\" tabIndex=\"0\">Previous</a>");  
//            }else{    
//            	sb.append("<a id=\"DataTables_Table_0_first\" class=\"first ui-corner-tl ui-corner-bl fg-button ui-button ui-state-default\" tabIndex=\"0\" href=\"#@\" onclick=\"nextPage(1,"+pageSize+")\">First</a>");
//                sb.append("<a id=\"DataTables_Table_0_previous\" class=\"previous fg-button ui-button ui-state-default\" tabIndex=\"0\" href=\"#@\" onclick=\"nextPage("+(pageNo-1)+","+pageSize+")\">Previous</a>");
//            }  
//            
//            //页码
//            sb.append("<span>");
//            int showTag = 10;    //分页标签显示数量  
//            int startTag = 1;  
//            int endTag = startTag+showTag-1;  
//            //分页显示5页，当前页居中
//            if(showTag <= totalPage){
//            	if(pageNo>5){  
//            		if(pageNo+5 <= totalPage){  
//            			startTag = pageNo-5;  
//            			endTag = pageNo+4;  
//            		}else{
//            			startTag = totalPage - showTag + 1;  
//            			endTag = totalPage;  
//            		}
//            	}  
//            }else{
//            	endTag = totalPage;
//            }
//            for(int i=startTag; i<=totalPage && i<=endTag; i++){  
//                if(pageNo==i)  
//                    sb.append("<a class=\"fg-button ui-button ui-state-default ui-state-disabled\" tabIndex=\"0\">"+i+"</a>");  
//                else  
//                    sb.append("<a class=\"fg-button ui-button ui-state-default\" tabIndex=\"0\" href=\"#@\" onclick=\"nextPage("+i+","+pageSize+")\">"+i+"</a>");  
//            }  
//            sb.append("</span>");
//            
//            //下一页、尾页
//            if(pageNo==totalPage){  
//                sb.append("<a id=\"DataTables_Table_0_next\" class=\"next fg-button ui-button ui-state-default ui-state-disabled\" tabIndex=\"0\">Next</a>");
//                sb.append("<a id=\"DataTables_Table_0_last\" class=\"last ui-corner-tr ui-corner-br fg-button ui-button ui-state-default ui-state-disabled\" tabIndex=\"0\">Last</a>");
//            }else{  
//            	sb.append("<a id=\"DataTables_Table_0_next\" class=\"next fg-button ui-button ui-state-default\" tabIndex=\"0\" href=\"#@\" onclick=\"nextPage("+(pageNo+1)+","+pageSize+")\">Next</a>");
//            	sb.append("<a id=\"DataTables_Table_0_last\" class=\"last ui-corner-tr ui-corner-br fg-button ui-button ui-state-default\" tabIndex=\"0\" href=\"#@\" onclick=\"nextPage("+totalPage+","+pageSize+")\">Last</a>");
//            }  
//            sb.append("</div>");  
//            
//            //分页查询函数
//            sb.append("<script type=\"text/javascript\">\n");  
//            sb.append("function nextPage(page,pageSize){");  
//            sb.append(" if(document.forms[0]){\n");  
//            sb.append("     var url = document.forms[0].getAttribute(\"action\");\n");  
//            sb.append("     url += \"?pageNo=\";\n");  
//            sb.append("     url += page;\n");  
//            sb.append("     url += \"&pageSize=\";\n");  
//            sb.append("     url += pageSize;\n");  
//            sb.append("     document.forms[0].action = url;\n");  
//            sb.append("     document.forms[0].submit();\n");  
//            sb.append(" }else{\n");  
//            sb.append("     var url = document.location;\n");  
//            sb.append("     url += \"?pageNo=\";\n");  
//            sb.append("     url += page;\n");  
//            sb.append("     url += \"&pageSize=\";\n");  
//            sb.append("     url += pageSize;\n");  
//            sb.append("     document.location = url;\n");  
//            sb.append(" }\n");  
//            sb.append("}\n");  
//            sb.append("</script>\n");  
//            
////            sb.append("<script type=\"text/javascript\">\n");  
////            sb.append("function nextPage(page){");  
////            sb.append(" if(true && document.forms[0]){\n");  
////            sb.append("     var url = document.forms[0].getAttribute(\"action\");\n");  
////            sb.append("     if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");  
////            sb.append("     else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");  
////            sb.append("     document.forms[0].action = url+page;\n");  
////            sb.append("     document.forms[0].submit();\n");  
////            sb.append(" }else{\n");  
////            sb.append("     var url = document.location+';\n");  
////            sb.append("     if(url.indexOf('?')>-1){\n");  
////            sb.append("         if(url.indexOf('currentPage')>-1){\n");  
////            sb.append("             var reg = /currentPage=\\d*/g;\n");  
////            sb.append("             url = url.replace(reg,'currentPage=');\n");  
////            sb.append("         }else{\n");  
////            sb.append("             url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");  
////            sb.append("         }\n");  
////            sb.append("     }else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");  
////            sb.append("     document.location = url + page;\n");  
////            sb.append(" }\n");  
////            sb.append("}\n");  
////            sb.append("</script>\n");  
//        }  
//        pageStr = sb.toString();  
//        return pageStr;  
//    }  
//    public void setPageStr(String pageStr) {  
//        this.pageStr = pageStr;  
//    }  

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
        int pageSize = StringUtil.parseInteger(request.getParameter("pageSize"), ParamConstant.PAGESIZE);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

    public void setBaseParamByJson(JSONObject json) {
        int pageNo = StringUtil.parseInteger(json.get("pageNo"), 1);
        int pageSize = StringUtil.parseInteger(json.get("pageSize"), ParamConstant.PAGESIZE);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

    public void setBaseParam(Map<String, Object> map) {
        int pageNo = StringUtil.parseInteger(map.get("pageNo"), 1);
        int pageSize = StringUtil.parseInteger(map.get("pageSize"), ParamConstant.PAGESIZE);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

}  