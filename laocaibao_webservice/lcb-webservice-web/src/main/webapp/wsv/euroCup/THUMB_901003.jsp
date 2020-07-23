<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String path = request.getContextPath();
session.setAttribute("path", path);
%>
<head>
<link href="${sessionScope.path}/js/img/zdico.png" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>欧洲杯接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="901003">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">欧洲杯投票接口</font></td>
			</tr>
			<tr>
				<td>竞选人sessionToken：<input type="text" name="heroSessionToken"></td>
			</tr>
			<tr>
				<td>投票人sessionToken：<input type="text" name="voterSessionToken">如果传投票人sessionToken，则从投票人sessionToken取其信息，否则使用手机号和验证码校验用户</td>
			</tr>
			<tr>
				<td>投票人手机号：<input type="text" name="voterCellPhone"></td>
			</tr>
			<tr>
				<td>投票人手机验证码：<input type="text" name="voterValidateCode"></td>
			</tr>
			<tr>
				<td align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>