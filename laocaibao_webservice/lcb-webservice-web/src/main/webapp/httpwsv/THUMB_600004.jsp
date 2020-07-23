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
<title>拇指贷前置系统-busiBanner</title>
</head> 
<body>
  	<form action="${path}/ApiTest/transmitRequest" method="post" name="model_600004">
		<input type="hidden" name="method" value="600004">
		<table align="center">
			<tr>
				<td>
					banner类型：<input type="text" name="type" value="2"/>2:app,3:H5
				</td>
			</tr>
			<tr>
				<td>
					<input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>