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
<title>账户管理</title>
</head>
<body >
<form action="${path}/account/customerAccount" method="post">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">客户账户管理</font></td>
			</tr>
			<tr>
				<td align="right">交易金额<font color="red">*</font>：</td>
				<td> <input name="amount" id='amount' type="text" size="18"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">交易类型<font color="red">*</font>：</td>
				<td> 
					<select id="type" name="type">
						<option value="">请选择</option>
						<option value="0">购买</option>
						<option value="1">提现</option>
						<option value="2">消费</option>
						<option value="3">还款</option>
						<option value="5">返利</option>
						<option value="6">冻结</option>
						<option value="7">解冻</option>
						<option value="14">退款</option>
						<option value="4">手续费</option>
						<option value="8">其它入</option>
						<option value="9">其它出</option>
						<option value="10">返利充值</option>
						<option value="11">产品收益</option>
						<option value="12">个人收益</option>
						<option value="13">产品收益提现</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">客户id<font color="red">*</font>：</td>
				<td> <input name="customerId" id='customerId' type="text" size="18"
					 value=''>
				</td>
			</tr>
			<tr>
				<td align="right">订单号<font color="red">*</font>：</td>
				<td> <input name="orderNum" id='orderNum' type="text" size="50"
					 value=''>
				</td>
			</tr>
			<tr>
				
			</tr>
			<tr>
				<td colspan="2" align="center">&nbsp;
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>