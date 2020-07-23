<%@ page language="java" pageEncoding="UTF-8"%>
<%
String[] adminIps = new String[]{"222.66.102.163","127.0.0.1","172.16.73.158","172.16.73.146","172.16.73.63","172.16.73.216","172.16.72.40","172.16.73.52"};
String ip = request.getHeader("x-forwarded-for");
if (ip == null || ip.length() == 0
		|| "unknown".equalsIgnoreCase(ip)) {
	ip = request.getHeader("Proxy-Client-IP");
}
if (ip == null || ip.length() == 0
		|| "unknown".equalsIgnoreCase(ip)) {
	ip = request.getHeader("WL-Proxy-Client-IP");
}
if (ip == null || ip.length() == 0
		|| "unknown".equalsIgnoreCase(ip)) {
	ip = request.getRemoteAddr();
}

boolean bl = false;
for(int i=0;i<adminIps.length;i++){
	if(ip.equals(adminIps[i])){
		bl = true;
		break;
	}
}
//if(!bl){
//	response.getWriter().print("亲!别看了,此路不通,请绕道!绕道地址:"+ip+"");
//	return ;
//}
%>