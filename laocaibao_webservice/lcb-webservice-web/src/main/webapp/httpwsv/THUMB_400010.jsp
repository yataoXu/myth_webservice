<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/privi.jsp"%>
<html>
<%
String path = request.getContextPath();
session.setAttribute("path", path);
%>
<head>
<link href="${sessionScope.path}/js/img/zdico.png" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>捞财宝app注销</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="400010">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">积分详细接口</font></td>
			</tr>
			<tr>
				<td align="right">用户ID<font color="red">*</font>：</td>
				<td> <input name="accountNo" id='accountNo' type="text" size="100"
					maxlength="50">
				</td>
			</tr>
			<tr>
			<tr>
				<td align="right">页码<font color="red">*</font>：</td>
				<td> <input name="pageNo" id='pageNo' type="text" size="100"
					maxlength="50">
				</td>
			</tr>
			<tr>
			<tr>
				<td align="right">页显示数<font color="red">*</font>：</td>
				<td> <input name="pageSize" id='pageSize' type="text" size="100"
					maxlength="50">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">&nbsp;
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>