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
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>拇指贷前置系统-系统消息接口</title>
	<script type="text/javascript">
		$(function(){
			$('#usePayPassword').click(function(){
				$('#payPassword').show();
				$('#validateCode').hide();
				$('#validateType').val(2);
			});
			$('#useValidateCode').click(function(){
				$('#payPassword').hide();
				$('#validateCode').show();
				$('#validateType').val(1);
			});
		});
	</script>
</head>
<body>
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="800019">
	<input type="hidden" name="validateType" id="validateType" value="1">
	<table align="center">
		<tr>
			<td align="center" style="color:red;">修改手机号前校验信息接口</td>
		</tr>
		<tr>
			<td align="right">用户Id</td>
			<td> <input name="customerId"  type="text"/></td>
		</tr>
		<tr id='validateCode'>
			<td align="right">手机验证</td>
			<td> <input name="validateCode"  type="text"/> &nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="usePayPassword">使用交易密码</a></td>
		</tr>
		<tr id='payPassword' style="display: none;">
			<td align="right">交易密码</td>
			<td> <input name="payPassword"  type="password"/> &nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="useValidateCode">使用手机验证码</a></td>
		</tr>
		<tr>
			<td align="right">身份证号码：</td>
			<td><input name="idNumber" id='"idNumber"' type="text" value=''></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="submit" value=" 提交  "/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>