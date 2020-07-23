<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%--
<%@ include file="/common/privi.jsp" %>
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户相关接口</title>
</head>
<style type="text/css"> 
.tab{ margin:0; padding:0; /*合并边线*/border-collapse:collapse;} 
.tab td{ border:solid 1px #000} 
</style>
<body>
	<div>
		<table border="1" align="center" style="width: 300px;text-align: center;">
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400001.jsp" target="_blank">验证码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400003.jsp" target="_blank">用户注册接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_100002.jsp" target="_blank">用户登录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400022.jsp" target="_blank">用户验证码登录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400004.jsp" target="_blank">修改密码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400005.jsp" target="_blank">重置密码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400055.jsp" target="_blank">检验重置密码验证码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400056.jsp" target="_blank">检验验证码扩展接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/weichat/index.jsp" target="_blank">微信绑定接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400006.jsp" target="_blank">实名认证</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400007.jsp" target="_blank">邀请码验证有效性接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400008.jsp" target="_blank">注销接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400009.jsp" target="_blank">签到接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400014.jsp" target="_blank">注册查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_900011.jsp" target="_blank">验证码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400031.jsp" target="_blank">用户基本信息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/1.10.0/THUMB_700008.jsp" target="_blank">商户注册</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400011.jsp" target="_blank">用户字段解密接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400015.jsp" target="_blank">查询用户邀请好友消费记录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400016.jsp" target="_blank">查询用户持有邀请红包接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400018.jsp" target="_blank">微信注册接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600001.jsp" target="_blank">用户反馈接口</a></td></tr>

			<tr><td><a href="${sessionScope.path}/jsp/customerAccount.jsp" target="_blank">账户管理</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600008.jsp" target="_blank">个人账账户信息</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600009.jsp" target="_blank">账户支付接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_700001.jsp" target="_blank">我的积分</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_700002.jsp" target="_blank">积分账户余额</a></td></tr>

		</table>
	</div>
</body>
</html>
