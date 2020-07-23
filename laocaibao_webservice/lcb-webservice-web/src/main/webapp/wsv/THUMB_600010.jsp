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
  	<form action="${path}/wsv/thumb600010" method="post">
		<input type="hidden" name="method" value="600010">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">提现接口</font></td>
			</tr>
			<tr>
				<td align="right">客户编号<font color="red">*</font>：</td>
				<td> <input name="customerId" id='customerId' type="text" size="100"
					maxlength="11" value='15'>
				</td>
			</tr>
			<tr>
				<td align="right">订单金额<font color="red">*</font>：</td>
				<td> <input name="orderAmt" id='orderAmt' type="text" size="100"
					maxlength="11" value='1000'>
				</td>
			</tr>
			<tr>
				<td align="right">银行编号<font color="red">*</font>：</td>
				<td> <input name="bankCode" id='bankCode' type="text" size="100"
					maxlength="11" value='101'>
				</td>
			</tr>
			<tr>
				<td align="right">银行卡号<font color="red">*</font>：</td>
				<td> <input name="bankCardCode" id='bankCardCode' type="text" size="100"
					maxlength="30" value='62148351302822000'>
				</td>
			</tr>
			<tr>
				<td align="right">客户姓名<font color="red">*</font>：</td>
				<td> <input name="acctUserName" id='acctUserName' type="text" size="100"
					maxlength="11" value='王朝曦'>
				</td>
			</tr>
			<tr>
				<td align="right">开户行编号<font color="red">*</font>：</td>
				<td> <input name="openBankCode" id='openBankCode' type="text" size="100"
					maxlength="30" value='1110'>
				</td>
			</tr>
			<tr>
				<td align="right">开户行名<font color="red">*</font>：</td>
				<td> <input name="openBankName" id='openBankName' type="text" size="100"
					maxlength="30" value='天使支行'>
				</td>
			</tr>
			<tr>
				<td align="right">手续费<font color="red">*</font>：</td>
				<td> <input name="counterFee" id='counterFee' type="text" size="100"
					maxlength="11" value='1.00'>
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