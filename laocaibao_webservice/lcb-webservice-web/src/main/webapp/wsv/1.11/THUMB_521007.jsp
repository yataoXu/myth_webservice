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
<title>捞财宝2.0--充值绑卡接口</title>
</head>
<body>
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="521007">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">充值绑卡接口</font></td>
			</tr>
			<tr>
				<td align="right">用户编号：</td>
				<td> <input name="customerId" id="customerId" type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">充值金额：</td>
				<td> <input name="amount" id="amount" type="text" value='1'></td>
			</tr>
			<tr>
				<td align="right">银行代码：</td>
				<td> <input name="bankCode" id="bankCode" type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">银行名称：</td>
				<td> <input name="bankName" id="bankName" type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">银行卡号：</td>
				<td> <input name="bankCard" id="bankCard" type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">充值类型：</td>
				<td> <input name="rechargeType" id="rechargeType" type="text" value='1'></td>
				1.快捷支付 2.网银支付
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value=" 提交"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>