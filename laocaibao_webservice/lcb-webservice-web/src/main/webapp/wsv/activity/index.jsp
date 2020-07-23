<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%--
<%@ include file="/common/privi.jsp" %>
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>活动汇总接口11111</title>
</head>
<style type="text/css"> 
.tab{ margin:0; padding:0; /*合并边线*/border-collapse:collapse;} 
.tab td{ border:solid 1px #000} 
</style>
<body>
	<div>
		<table border="1" align="center" style="width: 300px;text-align: center;">
			<tr><td><a href="${sessionScope.path}/wsv/activity/905001.jsp" target="_blank">查某个时间段对应活动订单</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/activity/905002.jsp" target="_blank">用户活动期间订单总额列表</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/activity/905003.jsp" target="_blank">弹层活动初始化</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/activity/905004.jsp" target="_blank">更新活动开始时间</a></td></tr>

			<tr><td><a href="${sessionScope.path}/wsv/euroCup/index.jsp" target="_blank">欧洲杯接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/tencent/index.jsp" target="_blank">腾讯活动接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/activity/index.jsp" target="_blank">活动汇总接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/activity/908002.jsp" target="_blank">现金券接口</a></td></tr>
		</table>
	</div>
</body>
</html>
