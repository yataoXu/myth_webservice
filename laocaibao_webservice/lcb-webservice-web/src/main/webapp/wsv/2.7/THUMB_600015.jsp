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
<title>拇指贷前置系统-系统消息接口</title>
</head>
<body>
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="600015">
	<table align="center">
		<tr>
			<td align="center"><font color="red">消息中心信息接口</font></td>
		</tr>
		<tr>
			<td align="right">消息id：</td>
			<td> <input name="msgId" id='msgId' type="text" value=''></td>
		</tr>
		<tr>
			<td align="right">用户id：</td>
			<td> <input name="userId" id='"userId"' type="text" value=''></td>
		</tr>
        <tr>
            <td align="right">消息类型：</td>
            <td> <input name="msgType" id="msgType" type="text" value=''></td>
        </tr>
		<tr>
			<td align="right"><input type="checkbox" name="isAllRead" value="0">是否全部已读</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="submit" value=" 提交  "/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>