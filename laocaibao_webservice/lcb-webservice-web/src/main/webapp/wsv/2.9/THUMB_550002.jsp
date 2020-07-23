<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/privi.jsp"%>
<html>
<%
String path = request.getContextPath();
session.setAttribute("path", path);
%>
<head>
<link href="${sessionScope.path}/js/img/zdico.png" rel="icon" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>捞财宝app注销</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
    <input type="hidden" name="method" value="550002">
    <table align="center">
        <tr>
            <td align="center"><font color="red">更新或保存地址</font></td>
        </tr>
        <tr>
            <td align="right">Id:</td>
            <td> <input name="id" type="text"/><font color="red">此ID为空时,将进行保存操作,反之,修改</font></td>
        </tr>
        <tr>
            <td align="right">用户Id:</td>
            <td> <input name="customerId" type="text"/></td>
        </tr>
        <tr>
            <td align="right">联系人姓名:</td>
            <td> <input name="contactName" type="text"/></td>
        </tr>
        <tr>
            <td align="right">联系电话:</td>
            <td> <input name="cellPhone" type="text"/></td>
        </tr>
        <tr>
            <td align="right">省:</td>
            <td> <input name="province" type="text"/></td>
        </tr>
        <tr>
            <td align="right">市:</td>
            <td> <input name="city" type="text"/></td>
        </tr>
        <tr>
            <td align="right">区:</td>
            <td> <input name="area" type="text"/></td>
        </tr>
        <tr>
            <td align="right">街道地址:</td>
            <td> <input name="stree" type="text"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" 提交 "/></td>
        </tr>
    </table>
</form>
</body>
</html>