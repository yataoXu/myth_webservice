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
<title>富友存管接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="430009">
		<table align="center">
			<tr>
				<td align="center"><font color="red">提现</font></td>
			</tr>
			<tr>
				<td>用户ID：<input type="text" name="customerId"></td>
			</tr>
			<tr>
				<td>提现金额：<input type="text" name="payAmt"></td>
			</tr>
			<tr>
				<td>提现类型(1:普通提现 2:快速提现)：<input type="text" name="type"></td>
			</tr>
			<tr>
				<td>返回url：<input type="text" name="pageUrl"></td>
			</tr>
			<tr>
				<td align="center"><input type="submit" value="提交"/></td>
			</tr>
		</table>
	</form>
</body>
</html>