<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/base.jsp"%>
<html>
<script language="javascript" type="text/javascript" src="${sessionScope.path}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

</script>
<body>
	<div>
		<p>
			<label>日历:</label>
			<input class="Wdate" type="text" onClick="WdatePicker()" value="<%=new Date()%>"> <font color=red>&lt;- 点我弹出日期控件</font>
		</p>
	</div>
</body>
</html>
