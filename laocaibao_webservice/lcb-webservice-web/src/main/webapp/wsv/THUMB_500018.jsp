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
<title>捞财宝app初始化</title>
</head>
<body >
<!--/wsv/1.10.0/500014-->
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="500014">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">查询债权协议</font></td>
			</tr>
			<tr>
				<td>订单编号：<input type="text" name="orderId"></td>
			</tr>
			<tr>
				<td align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>