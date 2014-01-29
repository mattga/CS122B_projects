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
<%@ include file="_template/_head.jsp" %>
	<div class="col-md-12">
	
	<h1>Welcome!</h1>
	<p>Logged In As <% out.print(u == null ? "" : u.first_name); %></p>
	
	</div>
<%@ include file="_template/_foot.jsp" %>