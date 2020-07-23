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
<title>用户验证码登录接口</title>
</head>
<body>
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="400022">
		<table>
			<tr>
				<td align="right">手机号<font color="red">*</font>：</td>
				<td> <input name="cmCellphone" type="text" size="100"
					maxlength="18">
				</td>
			</tr>
			<tr>
				<td align="right">验证码<font color="red">*</font>：</td>
				<td> <input name="validateCode" type="text" size="100"
					>
				</td>
			</tr>
			<tr>
				<td align="right">设备ID<font color="red">*</font>：</td>
				<td> <input name="deviceId" type="text" size="100"
				>
				</td>
			</tr>
			<tr>
				<td align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>