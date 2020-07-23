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
<title>捞财宝修改密码接口</title>
</head>
<body >
<form action="${path}/wsv/thumb400004" method="post">
	<input type="hidden" name="method" value="400004">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">修改密码接口</font></td>
			</tr>
			<tr>
				<td align="right">用户手机<font color="red">*</font>：</td>
				<td> <input name="cmCellphone" id='cmCellphone' type="text" size="100"
					maxlength="11" value='15800459433'>
				</td>
			</tr>
			<tr>
				<td align="right">旧密码<font color="red">*</font>：</td>
				<td> <input name="cmOldPassword" id='cmOldPassword' type="text" size="100"
					maxlength="50" >
				</td>
			</tr>
			<tr>
				<td align="right">新密码<font color="red">*</font>：</td>
				<td> <input name="cmNewPassword" id='cmNewPassword' type="text" size="100"
					maxlength="50" >
				</td>
			</tr>
			<tr>
				<td align="right">新密码<font color="red">*</font>：</td>
				<td> <input name="sign" id='sign' type="text" size="100"
					maxlength="50" value=''>
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