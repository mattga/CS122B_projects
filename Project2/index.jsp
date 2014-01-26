<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="Types.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>FabFlix</title>
</head>
<body>
	<jsp:useBean id="uSession" class="Types.UserSession" scope="session"/>
	<%
	User u = (User)session.getAttribute("user");

	if (u != null) {
		out.println("Welcome " + u.first_name + " " + u.last_name + "<br><br>Display browse options, search inputs, checkout, etc...");
	} else {
		// Not logged in...forward to login page.
		response.sendRedirect("login.jsp");
	}
	%>
</body>
</html>