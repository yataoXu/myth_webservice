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
<title>拇指贷前置系统-借款申请接口</title>
</head>
<body>
<form action="${path}/wsv/thumb300001" method="post">
	<input type="hidden" name="method" value="300001">
		<table>
			<tr>
				<td colspan="2"  align="center"><font color="red" size="5">借款申请接口</font></td>
			</tr>
			<tr>
				<td align="right">消息来源项目编号<font color="red">*</font>：</td>
				<td> 
								<select style="width: 200px" name="projectNo" id="projectSelect">
											  <option>---请选择---</option>
											  <option value="A">A</option>
											  <c:forEach items="${projectList}" var="project">
											      <option value="${project.projectNo}" <c:if test="${project.projectNo == sydGroup.projectNo}">selected</c:if> >${project.projectNo}---${project.projectName}</option>
											  </c:forEach>
								</select>
								<font color="red">(A---XXXX , 表示XXXX系统或者APP的项目编号是A)</font>
				</td>
			</tr>
			<tr>
				<td align="right">用户编号<font color="red">*</font>：</td>
				<td> <input name="userId" type="text" size="100"
					maxlength="18" value='1'>
				</td>
			</tr>
			<tr>
				<td align="right">借款金额（元）<font color="red">*</font>：</td>
				<td> <input name="amount" type="text" size="100"
					maxlength="30" value="10000">
				</td>
			</tr>
			<tr>
				<td align="right">借款期限（天）<font color="red">*</font>：</td>
				<td> <input name="term" type="text" size="100"
					maxlength="10" value='30'>
				</td>
			</tr>
			<tr>
				<td align="right">请求时间戳：</td>
				<td> <input name="reqTimestamp" type="text" size="16"
					maxlength="16" value="<%=System.currentTimeMillis()%>">
				</td>
			</tr>
			<tr>
				<td align="right">请求流水号：</td>
				<td> <input name="sn" type="text" size="16"
					maxlength="16" value="<%=System.currentTimeMillis()%>">
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