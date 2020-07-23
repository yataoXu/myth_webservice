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
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800008.jsp" target="_blank">加息券查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800005.jsp" target="_blank">用户余额查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800001.jsp" target="_blank">捞财币商品查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800002.jsp" target="_blank">捞财币兑换接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800003.jsp" target="_blank">捞财币领取接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800004.jsp" target="_blank">捞财币兑换记录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800006.jsp" target="_blank">商城中立即兑换接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800017.jsp" target="_blank">上期捞财币商城中立即兑换接口</a></td></tr>
			<%--<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800007.jsp" target="_blank">任务中心领取奖励接口</a></td></tr>--%>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800009.jsp" target="_blank">捞财币明细接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB800010.jsp" target="_blank">userToken转customerId接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB810001.jsp" target="_blank">任务中心--任务列表</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB810002.jsp" target="_blank">任务中心--领取奖励</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.0/THUMB810003.jsp" target="_blank">任务中心--多捞多得列表接口</a></td></tr>
		</table>
	</div>
</body>
</html>
