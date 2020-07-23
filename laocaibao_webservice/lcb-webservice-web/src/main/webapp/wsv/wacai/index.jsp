<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%--
<%@ include file="/common/privi.jsp" %>
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户相关接口</title>
</head>
<style type="text/css"> 
.tab{ margin:0; padding:0; /*合并边线*/border-collapse:collapse;} 
.tab td{ border:solid 1px #000} 
</style>
<body>
	<div>
		<table border="1" align="center" style="width: 300px;text-align: center;">
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_410004.jsp" target="_blank">挖财信息确认界面查询信息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_410005.jsp" target="_blank">实名+认证接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/ApiTest/buidWacaiProduct" target="_blank">定时创建挖财任务</a></td></tr>
		</table>
	</div>
</body>
</html>
