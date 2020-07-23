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
<title>检验验证码扩展接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="400056">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">检验验证码扩展接口</font></td>
			</tr>
			<tr>
				<td align="right">用户手机<font color="red">*</font>：</td>
				<td> <input name="cmCellphone" id='cmCellphone' type="text" size="100"
					maxlength="11">
				</td>
			</tr>
			<tr>
				<td align="right">验证码<font color="red">*</font>：</td>
				<td> <input name="validateCode" id='validateCode' type="text" size="100"
					maxlength="11" value=''>
				</td>
			</tr>
			<tr>
				<td align="right">验证码类型：<font color="red">*</font>：</td>
				<td><input type="text" name="type">验证码类型0,捞财宝注册，1:找回密码 3--微信注册和绑定 5--设置交易密码 6--重置交易密码，7:商户用户注册，8:欧洲杯英雄榜投票，10：充值 11.绑卡，13:访问白名单校验</td>
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