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
<title>捞财宝注册接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="400003">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">注册接口</font></td>
			</tr>
			<tr>
				<td align="right">用户手机<font color="red">*</font>：</td>
				<td> <input name="cmCellphone" id='cmCellphone' type="text" size="100"
					maxlength="11" value='15800459433'>
				</td>
			</tr>
			<tr>
				<td align="right">密码<font color="red">*</font>：</td>
				<td> <input name="cmPassword" id='cmPassword' type="text" size="100"
					maxlength="50" value='123456'>
				</td>
			</tr>
			<tr>
				<td align="right">验证码<font color="red">*</font>：</td>
				<td> <input name="validateCode" id='validateCode' type="text" size="100"
					maxlength="11" value=''>
				</td>
			</tr>
			<tr>
				<td align="right">邀请码：</td>
				<td> <input name="cmIntroduceCode" id='cmIntroduceCode' type="text" size="100"
					maxlength="11" value=''>
				</td>
			</tr>
			<tr>
				<td align="right">红包代码：</td>
				<td> <input name="accountType" id='accountType' type="text" size="100"
					 value='1'>
				</td>
			</tr>

			<tr>
				<td align="right">用户属性：</td>
				<td> <input name="redNo" id='redNo' type="text" size="100"
							value=''>
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