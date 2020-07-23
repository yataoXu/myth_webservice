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
    <title>出借凭证</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
    <input type="hidden" name="method" value="420007">
    <table align="center">
        <tr>
            <td align="center"><font color="red" size="5">出借凭证申请接口</font></td>
        </tr>
        <tr>
            <td align="right">邮箱<font color="red">*</font>：</td>
            <td> <input name="email" id='email' type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">用户编号<font color="red">*</font>：</td>
            <td> <input name="customerId" id='customerId' type="text" value=''></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" 发送验证码  "/>
            </td>
        </tr>
    </table>
</form>
<form action="${path}/ApiTest/transmitRequest" method="post">
    <input type="hidden" name="method" value="420008">
    <table align="center">
        <tr>
            <td align="right">再输入一遍邮箱<font color="red">*</font>：</td>
            <td> <input name="email"  type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">邮箱验证码<font color="red">*</font>：</td>
            <td> <input name="sign" id='sign' type="text" value=''></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" 提交  "/>
            </td>
        </tr>
    </table>
</form>

<form action="${path}/huarui/withdraw" method="post">
    <table align="center">
        <tr>
            <td align="center">标的提现接口</td>
        </tr>
        <tr>
            <td align="right">cmNumber<font color="red">*</font>：</td>
            <td> <input name="customerNo" id='customerNo' type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">payAmt<font color="red">*</font>：</td>
            <td> <input name="payAmt" id='payAmt' type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">pageUrl<font color="red">*</font>：</td>
            <td> <input name="pageUrl" id='pageUrl' type="text" value=''></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" submit  "/>
            </td>
        </tr>
    </table>
</form>

<form action="${path}/huarui/recharge" method="post">
    <table align="center">
        <tr>
            <td align="center">标的充值接口</td>
        </tr>
        <tr>
            <td align="right">customerNo<font color="red">*</font>：</td>
            <td> <input name="customerNo"  type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">payAmt<font color="red">*</font>：</td>
            <td> <input name="payAmt" type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">channelType<font color="red">*</font>：</td>
            <td> <input name="channelType" type="text" value=''></td>
        </tr>
        <tr>
            <td align="right">pageUrl<font color="red">*</font>：</td>
            <td> <input name="pageUrl" type="text" value=''></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" submit  "/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
