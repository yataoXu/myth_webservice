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
<title>拇指贷前置系统-用户反馈接口</title>
</head>
<body>
  <form action="${path}/wsv/thumb600001" method="post" name="model_600001">
	<input type="hidden" name="method" value="600001">
		<table>
			<tr>
				<td colspan="6"  align="center"><font color="red">用户反馈接口</font></td>
			</tr>
			<tr>
				<td align="right">反馈内容<font color="red">*</font>：</td>
				<td colspan="5"> <textarea rows="20" size="300" name="content" maxlength="500"></textarea>
				</td>
			</tr>
			<tr>
				<td align="right">联系方式：</td>
				<td colspan="5"> <input name="contactWay" type="text" size="100" 
					maxlength="30" value="">
				</td>
			</tr>
			<tr>
				<td align="right">客户Id：</td>
				<td colspan="5"> <input name="customerId" type="text" size="100" 
					maxlength="30" value="">
				</td>
			</tr>
			<tr>
				<td colspan="6" align="center"><input type="submit" value=" 提交  "/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>