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
<title>捞财宝注册码发送接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="400001">
		<table align="center">
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">注册码发送接口</font></td>
			</tr>
			<tr>
				<td align="right">用户手机<font color="red">*</font>：</td>
				<td> <input name="cvMobile" id='cvMobile' type="text" size="100"
					maxlength="11" value=''>
				</td>
			</tr>
			<tr>
				<td align="right">业务类型<font color="red">*</font>：</td>
				<td> 
								<select style="width: 200px" name="cvType" id="cvType">
											  <option value="0">注册</option>
											  <option value="1">修改密码</option>
								</select>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">&nbsp;<font color='red'>点击请慎重，它是真的发短信，花钱的。。。</font>
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