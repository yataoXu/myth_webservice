<%@ page language="java" import="java.util.*,websvc.utils.SpringContextHelper" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ include file="/common/privi.jsp"%>
<%
	//设置页面全局变量
	String path = request.getContextPath();
	session.setAttribute("path", path);

 	/* TSysProjectMapper tSysProjectMapper=(TSysProjectMapper)SpringContextHelper.getBean("TSysProjectMapper"); 
	List<com.zdmoney.models.TSysProject> projectList = tSysProjectMapper.getProjectList(null);
	if(projectList.size()>0){
		session.setAttribute("projectList", projectList);
	}  */
%>
