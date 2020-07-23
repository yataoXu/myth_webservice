<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%--
<%@ include file="/common/privi.jsp" %>
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>捞财宝前置系统</title>
</head>
<style type="text/css"> 
.tab{ margin:0; padding:0; /*合并边线*/border-collapse:collapse;} 
.tab td{ border:solid 1px #000} 
</style>
<body>
	<div>
		<table border="1" align="center" style="width: 300px;text-align: center;">
			<tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540001.jsp" target="_blank">借款首页初始化</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540002.jsp" target="_blank">借款详情</a></td></tr>
            <tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540003.jsp" target="_blank">还款详情</a></td></tr>
            <tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540004.jsp" target="_blank">立即签约</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540005.jsp" target="_blank">查询借款协议</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540006.jsp" target="_blank">查询委托协议</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_540007.jsp" target="_blank">查询模板协议</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/personalLoan/THUMB_541000.jsp" target="_blank">查询理财计划模板协议</a></td></tr>
		</table>
	</div>
</body>
</html>
