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

<% 
	// used inside _head* jsp file
	String document_title = "Welcome"; 
%>
<%@ include file="_template/_head.jsp" %>
	<div class="col-md-12">
	
	<h1>Welcome <% out.print(u == null ? "" : u.first_name); %>!</h1>
	<p>Please take a look around, buy some movies, and pay us lots of money.</p>
	<p>Use the navigation menu above to make your way through the site.</p>

	</div>
<%@ include file="_template/_foot.jsp" %>