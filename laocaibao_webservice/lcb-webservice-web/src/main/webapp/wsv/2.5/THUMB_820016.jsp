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
<title>邀请好友投资明细</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="820016">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">邀请好友投资明细</font></td>
			</tr>
			<tr>
				<td>cmNumber：<input type="text" name="cmNumber"></td>
			</tr>
			<tr>
				<td>startDate：<input type="text" name="startDate"></td>
			</tr>
			<tr>
				<td>endDate：<input type="text" name="endDate"></td>
			</tr>
            <tr>
                <td>Type(0:没有投资金额  1:有投资总额  2:区分性别  3:大转盘活动)：<input type="text" name="type"></td>
            </tr>
			<tr>
				<td align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>