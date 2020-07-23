<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${sessionScope.path}/js/img/zdico.png" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消息推送前置系统-邮件测试接口</title>
</head>
<body>
	<form action="${sessionScope.path }/wsv/psh400001" method="post">
		<input type="hidden" name="method" value="400001">
		<input name="pshType" type="hidden" value="2">
		<table>
			<tr>
				<td colspan="2" align="center"><font color="red" size="5">邮件发送接口</font></td>
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
				</select> <font color="red">(A---XXXX , 表示XXXX系统或者APP的项目编号是A)</font></td>
			</tr>
			<tr>
				<td align="right">收件人地址<font color="red">*</font>：
				</td>
				<td><input name="userEmailAddress" type="text" size="100"
					maxlength="100" value='****@qq.com'>
				</td>
			</tr>		
			<tr>
				<td align="right">邮件主题：</td>
				<td><input name="subject" type="text" size="150"
					maxlength="150" value='{测试邮件主题}'></td>
			</tr>
			<tr>
				<td align="right">邮件内容：</td>
				<!-- <td><input name="content" type="text" size="150"
					maxlength="150" value=''></td> -->
					<td><textarea name="content" type="text" rows="5" cols="150" ></textarea>
					</td>
			</tr>
			<tr>
				<td align="right">附件：</td>
				<td><input name="attachmentPaths" type="text" size="150"
					maxlength="150" value=''></td>
			</tr>
			<tr>
				<td align="right">邮件类型<2为html>：</td>
				<td><input name="emailType" type="text" size="150"
					maxlength="150" value='1'></td>
			</tr>
			<tr>
				<td align="right">请求时间戳：</td>
				<td><input name="reqTimestamp" type="text" size="16"
					maxlength="16" value="<%=System.currentTimeMillis()%>"></td>
			</tr>
			<tr>
				<td align="right">邮件发送时间：</td>
				<td><input name="sendTime" type="text" size="16" maxlength="19"
					value="2014-11-25 16:30:00"></td>
			</tr>
			<tr>
				<td align="right">请求流水号：</td>
				<td><input name="sn" type="text" size="16" maxlength="16"
					value="<%=System.currentTimeMillis()%>"></td>
			</tr>
			<tr>
				<td align="right">最大重发次数：</td>
				<td><input name="maxCount" type="text" size="5" maxlength="2"
					value="3"></td>
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