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
    <title>捞财宝产品明细接口</title>
</head>
<body >
<form action="${path}/ApiTest/transmitRequest" method="post">
    <input type="hidden" name="method" value="510003">
    <table align="center">
        <tr>
            <td align="center"><font color="red" size="5">产品列表(专区产品)</font></td>
        </tr>
        <tr>
            <td align="right">customerId：</td>
            <td>
                <input type="text" name="customerId" id="customerId"/>
            </td>
        </tr>
        <tr>
            <td align="right">productType：</td>
            <td>
                <input type="text" name="productType" id="productType"/>
            </td>
        </tr>
        <tr>
            <td align="right">页码：</td>
            <td>
                <input type="text" name="pageNo" id="pageNo"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value=" 提交"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>