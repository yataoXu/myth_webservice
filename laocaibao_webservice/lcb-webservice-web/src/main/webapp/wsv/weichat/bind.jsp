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
<title>捞财宝测试微信绑定接口</title>
</head>
<body >
<form action="${path}/weichat/bind" method="post">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">测试微信绑定接口</font></td>
			</tr>
			<tr>
				<td align="center">
					手机号:<input type="text" name="cellPhone" value=""/>
				</td>
			</tr>

			<tr>
				<td align="center">
					登录密码:<input type="text" name="password" value=""/>
				</td>
			</tr>

			<tr>
				<td align="center">
					openId:<input type="text" name="openId" value=""/>
				</td>
			</tr>

			<tr>
				<td align="center">
					<input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>