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
<title>捞财宝产品明细接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
	<input type="hidden" name="method" value="510002">
		<table align="center">
			<tr>
				<td align="center"><font color="red" size="5">产品列表-更多(6周年)</font></td>
			</tr>
			<tr>
				<td align="right">产品类型：</td>
				<td> 
					<select name="productType" id="productType">
						<option value ="">请选择</option>
						<option value ="1">定期</option>
						<option value ="2">个贷</option>
						<option value ="3">理财计划</option>
						<option value ="4">转让产品</option>
						<option value ="5">PC全部</option>
						<option value ="6">新手标</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">pageNo：</td>
				<td> <input name="pageNo" id='pageNo' type="text" value=''></td>
			</tr>
			<tr>
				<td align="right">customerId：</td>
				<td>
					<input type="text" name="customerId" id="customerId"/>
				</td>
			</tr>
			<tr>
				<td align="right">收益率排序：</td>
				<td> <input name="rateSort" id='rateSort' type="text" placeholder="0: 降序 1: 升序"></td>
			</tr>
			<tr>
				<td align="right">封闭期排序：</td>
				<td> <input name="termSort" id='termSort' type="text" placeholder="0: 降序 1: 升序"></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>