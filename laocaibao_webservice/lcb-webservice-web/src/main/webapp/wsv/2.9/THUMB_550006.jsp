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
    <input type="hidden" name="method" value="550006">
    <table align="center">
        <tr>
            <td align="center"><font color="red">查询借款详情</font></td>
        </tr>
        <tr>
            <td align="right">产品编号:</td>
            <td> <input name="productId" type="text"/></td>
        </tr>
        <tr>
            <td align="right">产品类型（默认0，1：理财产品 2，订单理财产品）:</td>
            <td> <input name="status"  type="text"/></td>
        </tr>
        <tr>
            <td align="right">id:</td>
            <td> <input name="id" type="text"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" 提交 "/></td>
        </tr>
    </table>
</form>
</body>
</html>