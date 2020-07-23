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
			<tr><td><a href="${sessionScope.path}/wsv/1.11/index.jsp" target="_blank">1.11接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/1.10.0/index.jsp" target="_blank">1.10.0接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/190/index.jsp" target="_blank">1.9.0接口</a></td></tr>
            <tr><td><a href="${sessionScope.path}/wsv/2.0/index.jsp" target="_blank">2.0接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.1/index.jsp" target="_blank">2.1接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.2/index.jsp" target="_blank">2.2接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.5/index.jsp" target="_blank">2.5接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.6/index.jsp" target="_blank">2.6接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.7/index.jsp" target="_blank">2.7接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/2.8/index.jsp" target="_blank">2.8接口</a></td></tr>
            <tr><td><a href="${sessionScope.path}/wsv/2.9/index.jsp" target="_blank">2.9接口</a></td></tr>
            <tr><td><a href="${sessionScope.path}/wsv/3.0/index.jsp" target="_blank">3.0接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/3.2/index.jsp" target="_blank">3.2接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/3.5/index.jsp" target="_blank">3.5接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/4.1/index.jsp" target="_blank">4.1接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/fuiou/index.jsp" target="_blank">富友存管接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/user/index.jsp" target="_blank">用户相关接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_510003.jsp" target="_blank">产品列表(专区产品)</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_520020.jsp" target="_blank">获取平台运营数据</a></td></tr>
            <tr><td><a href="${sessionScope.path}/wsv/personalLoan/index.jsp" target="_blank">个贷接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/deposit/index.jsp" target="_blank">存管接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/euroCup/index.jsp" target="_blank">欧洲杯接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/tencent/index.jsp" target="_blank">腾讯活动接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/activity/index.jsp" target="_blank">活动汇总接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/weichat/index.jsp" target="_blank">微信绑定接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400001.jsp" target="_blank">验证码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400003.jsp" target="_blank">用户注册接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_750001.jsp" target="_blank">用户注册---返利网</a></td></tr>

			<tr><td><a href="${sessionScope.path}/wsv/edu/index.jsp" target="_blank">投资者教育</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_100002.jsp" target="_blank">用户登录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400022.jsp" target="_blank">用户验证码登录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400004.jsp" target="_blank">修改密码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400005.jsp" target="_blank">重置密码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400055.jsp" target="_blank">检验重置密码验证码接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400056.jsp" target="_blank">检验验证码扩展接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_420005.jsp" target="_blank">借款意向收集</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_420006.jsp" target="_blank">授权交割日</a></td></tr>


			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400006.jsp" target="_blank">实名认证</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400007.jsp" target="_blank">邀请码验证有效性接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400008.jsp" target="_blank">注销接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400010.jsp" target="_blank">积分详细接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400011.jsp" target="_blank">用户字段解密接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400012.jsp" target="_blank">未读信息确认接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400013.jsp" target="_blank">红包查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400014.jsp" target="_blank">注册查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400015.jsp" target="_blank">查询用户邀请好友消费记录接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400016.jsp" target="_blank">查询用户持有邀请红包接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_400018.jsp" target="_blank">微信注册接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500011.jsp" target="_blank">资产明细－收益</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500017.jsp" target="_blank">查询投资协议</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500018.jsp" target="_blank">查询债权协议</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_500023.jsp" target="_blank">标的协议模板</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500013.jsp" target="_blank">产品是否可售</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600002.jsp" target="_blank">系统消息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600012.jsp" target="_blank">紧急公告接口</a></td></tr>
			<tr><td><font color='red'>--------------------------------<br>我是华丽的分割线，下面是原例子<br>--------------------------------</font></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_300001.jsp" target="_blank">借款申请接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_300002.jsp" target="_blank">借款查询接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500002.jsp" target="_blank">客户订单列表接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500003.jsp" target="_blank">下订单接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500004.jsp" target="_blank">取消订单接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500005.jsp" target="_blank">保存支付信息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500006.jsp" target="_blank">返利详细接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500007.jsp" target="_blank">返利累积金额接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500008.jsp" target="_blank">获取支付信息接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_500009.jsp" target="_blank">获取充值状态接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600003.jsp" target="_blank">busiBanner</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600004.jsp" target="_blank">Banner</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_300005.jsp" target="_blank">初始化接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600011.jsp" target="_blank">是否第一次更新</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/thumb600013?method=600013" target="_blank">广告接口</a></td></tr>
			<tr><td><a href="${sessionScope.path}/httpwsv/THUMB_600014.jsp" target="_blank">债权信息内容</a></td></tr>
			<tr><td><font color='red'>--------------------------------<br>我是华丽的分割线，下面是2.0接口<br>--------------------------------</font></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_420007.jsp" target="_blank">出借凭证</a></td></tr>
			<tr><td><a href="${sessionScope.path}/wsv/THUMB_908001.jsp" target="_blank">现金福利查询接口</a></td></tr>

			<tr><td><a href="${sessionScope.path}/wsv/wacai/index.jsp" target="_blank">挖财接口</a></td></tr>
		</table>
	</div>
</body>
</html>
