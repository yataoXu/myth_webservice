<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="${sessionScope.path}/js/jquery-easyui-1.3.5/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${sessionScope.path}/js/jquery-easyui-1.3.5/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${sessionScope.path}/js/jquery-easyui-1.3.5/demo/demo.css" />

<script type="text/javascript" src="${sessionScope.path}/js/jquery-easyui-1.3.5/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${sessionScope.path}/js/jquery-easyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${sessionScope.path}/js/jquery-easyui-1.3.5/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${sessionScope.path}/js/utilJs/json2.js"></script>

<!-- 添加lhgdialog的js插件 -->
<script type="text/javascript" src="${sessionScope.path}/js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="${sessionScope.path}/js/utilJs/curdtools.js"></script>


<script>
	function getAjaxCfg(){
	    return {
	            type : "POST",
	            dataType : "json",
	            cache : false,
	            async : true,
	            contentType: "application/json; charset=utf-8"
	           };
	}
</script>
	