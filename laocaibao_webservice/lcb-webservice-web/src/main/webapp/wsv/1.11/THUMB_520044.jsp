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
<title>捞财宝2.0--用户资产接口v4.1</title>
</head>
<body>
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="520044">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">用户资产接口</font></td>
			</tr>
			<tr>
				<td align="right">用户编号：</td>
				<td> <input name="customerId" id="customerId" type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">是否查持有资产，0:持有资产，1:历史资产：, 2:即将到期</td>
				<td> <input name="isHold" id="isHold" type="text" value='0'></td>
			</tr>
			<tr>
				<td align="right">订单编号：</td>
				<td> <input name="orderId" id="orderId" type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">订单类型：(订单类型 0：全部，1：智投计划，2：散标，3:定期，4:转让)</td>
				<td> <input name="orderType" id="orderType" type="text" value='0'></td>
			</tr>
			<tr>
				<td align="right">页码：</td>
				<td> <input name="pageNo" id="pageNo" type="text" value='1'></td>
			</tr>
			<tr>
				<td align="right">每页条数：</td>
				<td> <input name="pageSize" id="pageSize" type="text" value='20'></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>