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
			<tr><td><a href="${sessionScope.path}/wsv/fuiou/THUMB_900101.jsp" target="_blank">竞猜申请展示</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/THUMB_900102.jsp" target="_blank">竞猜申请提交</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/THUMB_900103.jsp" target="_blank">获取获奖名单接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/THUMB_901001.jsp" target="_blank">英雄榜列表接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/THUMB_901002.jsp" target="_blank">判断投票者今天是否已投竞猜者接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/THUMB_901003.jsp" target="_blank">竞猜申请提交</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/THUMB_902001.jsp" target="_blank">邀请榜单接口</a></td></tr>
		</table>
	</div>
</body>
</html>
