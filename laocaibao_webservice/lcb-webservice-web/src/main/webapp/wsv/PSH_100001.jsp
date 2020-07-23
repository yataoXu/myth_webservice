<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${sessionScope.path}/js/img/zdico.png" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消息推送前置系统-推送任务接口</title>
</head>
<body>
<form action="${sessionScope.path}/wsv/psh100001" method="post">
	<input type="hidden" name="method" value="100001">
	<table>
		<tr>
			<td colspan="2"  align="center"><font color="red" size="5">推送任务新增接口</font></td>
		</tr>
		<tr>
			<td align="right">通知标题：</td>
			<td> <input name="smsTitle" type="text" size="16"
				maxlength="30" value="标题">
			</td>
		</tr>
		<tr>
			<td align="right">推送方式<font color="red">*</font>：</td>
			<td> <input name="sendType" type="text" size="5"
				maxlength="1" value="1"><font color="red">(推送方式类型为1代表通知，2为消息透传)
			</td>
		</tr>
		<tr>
			<td align="right">发送类型<font color="red">*</font>：</td>
			<td> <input name="pshType" type="text" size="5"
				maxlength="1" value="1"><font color="red">(推送方式类型为1代表消息，2为邮件)
			</td>
		</tr>
		<tr>
			<td align="right">通知内容：</td>
			<td> <input name="smsTxt" type="text" size="50"
				maxlength="500" value="通知内容"><font color="red">(推送方式类型为1时候，该项必填)</font>
			</td>
		</tr>
		<tr>
			<td align="right">透传内容：</td>
			<td> <input name="smsTranscontent" type="text" size="50"
				maxlength="500" value="透传内容"><font color="red">(推送方式类型为2时候，该项必填)</font>
			</td>
		</tr>
		<tr>
			<td align="right">消息来源项目编号<font color="red">*</font>：</td>
			<td> 
								 	<select style="width: 200px" name="projectNo" id="projectSelect">
											  <option>---请选择---</option>
											  <c:forEach items="${projectList}" var="project">
											      <option value="${project.projectNo}" <c:if test="${project.projectNo == sydGroup.projectNo}">selected</c:if> >${project.projectNo}---${project.projectName}</option>
											  </c:forEach>
								</select>
			<font color="red">(A---XXXX , 表示XXXX系统或者APP的项目编号是A)</font>					
			</td>
		
		</tr>
		<tr>
			<td align="right">推送目标<font color="red">*</font>：</td>
			<td> <input name="target" type="text" size="150"
				maxlength="4000" value='[{"pn":"A","t":"14","no":"1,2,3" },{"pn": "B" , "t": "11"}]'>  <!-- [{"pn": "A" , "t": "11"}] --> <!-- [{"pn":"A","t":"14","no":"1,2,3" },{"pn": "B" , "t": "11"}]  -->
				<font color="red">json样式：[{...},{...}],pn:项目编号 <br/>t发送类型：1为发送给该项目下全部IOS，2为发送给该项目下全部安卓，11为全发，12为发送给用户组，13为发送给用户，14为发送给设备，15为发送给未登记在消息推送系统的设备</font>		
			</td>
		</tr>
		
		<tr>
			<td align="right">时间戳<font color="red">*</font>：</td>
			<td> <input name="reqTimestamp" type="text" size="16"
				maxlength="13" value="<%=new java.util.Date().getTime() %>"> 
			</td>
		</tr>
		<tr>
			<td align="right">消息发送时间：</td>
			<td> <input name="sendTime" type="text" size="16"
				maxlength="19" value="2014-11-11 16:30:00">
			</td>
		</tr>
		<tr>
			<td align="right">消息类型：</td>
			<td> <input name="smsType" type="text" size="5"
				maxlength="1" value="1">
				<font color="red">0表示其它,1表示营销,2表示广告</font>		
			</td>
		</tr>
		<tr>
			<td align="right">流水号<font color="red">*</font>：</td>
			<td> <input name="sn" type="text" size="50"
				maxlength="32" value="<%=new java.util.Date().getTime() %>">
			</td>
		</tr>
		<tr>
			<td align="right">优先级：</td>
			<td> <input name="priority" type="text" size="5"
				maxlength="1" value="5">
			<font color="red">优先级1-5，1最高，5最低，不填时默认为5</font>			
			</td>
		</tr>
		<tr>
			<td align="right">最大重发次数：</td>
			<td> <input name="maxCount" type="text" size="5"
				maxlength="2" value="3">
			</td>
		</tr>
		<tr>
			<td align="right">备注：</td>
			<td> <input name="remark" type="text" size="200"
				maxlength="1000" value="">
			</td>
		</tr>
	<tr>
			<td align="right">校验码：</td>
			<td> <input name="sign" type="text" size="50"
				maxlength="50" value="">不填写则后台自动计算校验码
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="submit" value=" 提交  " class="btn btn-primary"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
