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
<title>商户用户注册</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="700008">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">商户用户注册</font></td>
			</tr>
			<tr>
				<td>手机号码：<input type="text" name="cellPhone" value=""></td>
			</tr>
			<tr>
				<td>密码：<input type="text" name="password" value=""></td>
			</tr>
			<tr>
				<td>openId：<input type="text" name="openId" value=""></td>
			</tr>
			<tr>
				<td>验证码：<input type="text" name="validCode" value=""></td>
			</tr>
			<tr>
				<td>商户号：<input type="text" name="merchantCode" value=""></td>
			</tr>
			<tr>
				<td align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>