<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*" %>
<%@ page import="Types.User" %>
<jsp:useBean id="uSession" class="Types.UserSession" scope="session"/>
<%
	User u = (User)session.getAttribute("user");
	if (u == null) {
		response.sendRedirect("login.jsp");
	} 
	// Display the page below if the user is logged in....
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>FabFlix</title>
</head>
<body>
	<h1>Welcome!</h1>
	<p>Logged In As <% out.print(u == null ? "" : u.first_name); %></p>
	<ul>
	<li><a href="browse.jsp?title">Browse By Title</a></li>
	<li><a href="browse.jsp?genre">Browse By Genre</a></li>
	<li><a href="search.jsp">Search</a></li>
	<li><a href="cart.jsp">View Cart</a></li>
	<li><a href="logout.jsp">Log Out</a></li>
	</ul>
</body>
</html>