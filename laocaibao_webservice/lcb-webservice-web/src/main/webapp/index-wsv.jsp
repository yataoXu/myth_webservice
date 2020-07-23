<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<%--
<%@ include file="/common/privi.jsp" %>
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>捞财宝前置系统</title>
</head>
<style type="text/css"> 
.tab{ margin:0; padding:0; /*合并边线*/border-collapse:collapse;} 
.tab td{ border:solid 1px #000} 
</style>
<body>
	<div>
		<table border="1" align="center" style="width: 300px;text-align: center;">
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400001.jsp" target="_blank">验证码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400003.jsp" target="_blank">用户注册接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400002.jsp" target="_blank">用户登录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400004.jsp" target="_blank">修改密码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400005.jsp" target="_blank">重置密码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400006.jsp" target="_blank">实名认证</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400007.jsp" target="_blank">邀请码验证有效性接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400008.jsp" target="_blank">注销接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400009.jsp" target="_blank">签到接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400010.jsp" target="_blank">积分详细接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400011.jsp" target="_blank">用户字段解密接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400012.jsp" target="_blank">未读信息确认接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400013.jsp" target="_blank">红包查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400014.jsp" target="_blank">注册查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400015.jsp" target="_blank">查询用户邀请好友消费记录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_400016.jsp" target="_blank">查询用户持有邀请红包接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500011.jsp" target="_blank">资产明细－收益</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500013.jsp" target="_blank">产品是否可售</a></td></tr>				
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600001.jsp" target="_blank">用户反馈接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600002.jsp" target="_blank">系统消息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600012.jsp" target="_blank">紧急公告接口</a></td></tr>
			<tr><td><font color='red'>--------------------------------<br>我是华丽的分割线，下面是原例子<br>--------------------------------</font></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_300001.jsp" target="_blank">借款申请接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_300002.jsp" target="_blank">借款查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500002.jsp" target="_blank">客户订单列表接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500003.jsp" target="_blank">下订单接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500004.jsp" target="_blank">取消订单接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500005.jsp" target="_blank">保存支付信息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500006.jsp" target="_blank">返利详细接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500007.jsp" target="_blank">返利累积金额接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500008.jsp" target="_blank">获取支付信息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600003.jsp" target="_blank">busiBanner</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600004.jsp" target="_blank">Banner</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_300005.jsp" target="_blank">初始化接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/jsp/customerAccount.jsp" target="_blank">账户管理</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600007.jsp" target="_blank">账户流水查询</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600005.jsp" target="_blank">充值支付接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600008.jsp" target="_blank">个人账账户信息</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600009.jsp" target="_blank">账户支付接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600010.jsp" target="_blank">提现接口</a></td></tr>
<%-- 			<tr><td><a href="${sessionScope.path}/jsp/companyAccount.jsp" target="_blank">公司账户管理</a></td></tr> --%>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_600011.jsp" target="_blank">是否第一次更新</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/thumb600013?method=600013" target="_blank">广告接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_700001.jsp" target="_blank">我的积分</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_700002.jsp" target="_blank">积分账户余额</a></td></tr>
			<tr><td><font color='red'>--------------------------------<br>我是华丽的分割线，下面是2.0接口<br>--------------------------------</font></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_900011.jsp" target="_blank">验证码接口</a></td></tr>
		</table>
	</div>
</body>
</html>
