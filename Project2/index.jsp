<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>FabFlix</title>
</head>
<body>
	<jsp:useBean id="uSession" class="Types.UserSession" scope="session"/>
	<%
	if (uSession.isLoggedIn) {
		out.println("Logged In!"); // Forward to Main Logged In Page...
	} else {
		out.println("NOT Logged In!"); // Forward to Login Page...
	}
	%>
</body>
</html>