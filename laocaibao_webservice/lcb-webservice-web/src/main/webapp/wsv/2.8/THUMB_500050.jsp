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
<title>拇指贷前置系统-查询协议接口</title>
</head>
<body>
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="500050">
	<table align="center">
		<tr>
			<td align="center"><font color="red">查询协议接口</font></td>
		</tr>
		<tr>
			<td align="right">产品类型</td>
			<td> <input name="productType" type="text"/></td>
		</tr>
		<tr>
			<td align="right">协议编号：</td>
			<td> <input name="agreementNo" id='agreementNo' type="text" /></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="submit" value=" 提交 "/></td>
		</tr>
	</table>
</form>
</body>
</html>