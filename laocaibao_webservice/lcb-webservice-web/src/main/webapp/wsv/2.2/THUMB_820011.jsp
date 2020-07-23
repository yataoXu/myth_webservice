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
<title>土豪列表接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="820011">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">土豪列表接口</font></td>
			</tr>
			<tr>
				<td>用户编号：<input type="text" name="customerId"></td>
			</tr>
			<tr>
				<td>查询条件：<input type="text" name="seachStr"></td>
			</tr>
			<tr>
				<td>查询类型：
					<select name ="seachType">
						 <option value="1">全部</option>
						 <option value="2">可加入</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>页数：<input name="pageNo" id='"pageNo"' type="text" value='1'></td>
			</tr>
			<tr>
				<td>每页大小：<input name="pageSize" id="pageSize" type="text" value='20'></td>
			</tr>


			<tr>
				<td align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>