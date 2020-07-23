<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${sessionScope.path}/js/img/zdico.png" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消息推送前置系统-用户设备注销接口</title>
</head>
<body>
	<form action="${sessionScope.path }/wsv/psh200002" method="post">
		<input type="hidden" name="method" value="200002">
		<table>
			<tr>
				<td colspan="2" align="center"><font color="red" size="5">用户设备注销接口</font></td>
			</tr>
			<tr>
				<td align="right">消息来源项目编号<font color="red">*</font>：
				</td>
				<td><select style="width: 200px" name="projectNo"
					id="projectSelect">
						<option>---请选择---</option>
						<c:forEach items="${projectList}" var="project">
							<option value="${project.projectNo}"
								<c:if test="${project.projectNo == sydGroup.projectNo}">selected</c:if>>${project.projectNo}---${project.projectName}</option>
						</c:forEach>
				</select>
				<font color="red">(A---XXXX , 表示XXXX系统或者APP的项目编号是A)</font>
				</td>
			</tr>
			<tr>
				<td align="right">用户编号<font color="red">*</font>：
				</td>
				<td><input name="userNo" type="text" size="16" maxlength="16"
					value="zendai1"></td>
			</tr>
			<tr>
				<td align="right">设备编号：</td>
				<td><input name="deviceNo" type="text" size="16" maxlength="16"
					value="45421212dd"></td>
			</tr>
			<%-- 
			<tr>
				<td align="right">设备序列号：</td>
				<td> <input name="deviceSn" type="text" size="50"
					maxlength="50" value="userName">
				</td>
			</tr>
			<tr>
				<td align="right">设备类型：</td>
				<td> <input name="deviceType" type="text" size="16"
					maxlength="16" value="1">
				</td>
			</tr>
			--%>
			<tr>
				<td align="right">请求流水号：</td>
				<td><input name="sn" type="text" size="16" maxlength="16"
					value="<%=System.currentTimeMillis()%>"></td>
			</tr>
			<tr>
				<td align="right">请求时间戳：</td>
				<td><input name="reqTimestamp" type="text" size="16"
					maxlength="16" value="<%=System.currentTimeMillis()%>"></td>
			</tr>
			<tr>
				<td align="right">验签：</td>
				<td> <input name="sign" type="text" size="16"
					maxlength="16" value="无需填写后台计算">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit"
					value=" 提交  " /></td>
			</tr>
		</table>
	</form>
</body>
</html>