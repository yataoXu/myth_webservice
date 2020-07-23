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
<title>捞财宝订单接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="500005">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">保存支付信息接口</font></td>
			</tr>
			<tr>
				<td align="right">客户编号<font color="red">*</font>：</td>
				<td> <input name="customerId" id='customerId' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">积分金额<font color="red">*</font>：</td>
				<td> <input name="integralAmount" id='integralAmount' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">订单编号<font color="red">*</font>：</td>
				<td> <input name="orderId" id='orderId' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">银行卡号：</td>
				<td> <input name="cbAccount" id='cbAccount' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">持卡人姓名：</td>
				<td> <input name="cbAccountName" id='cbAccountName' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">银行代码：</td>
				<td> <input name="cbBankCode" id='cbBankCode' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">银行名称：</td>
				<td> <input name="cbBankName" id='cbBankName' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">支行名称：</td>
				<td> <input name="cbBranchName" id='cbBranchName' type="text" size="100"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">支行代码：</td>
				<td> <input name="cbSubBankCode" id='cbSubBankCode' type="text" size="100"
					 value=''>
				</td>
			</tr>
				<tr>
				<td align="right">银行描述：</td>
				<td> <input name="cbDesc" id='cbDesc' type="text" size="100"
					 value=''>
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