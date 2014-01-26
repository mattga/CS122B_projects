<%@ page import="Types.User" %>
<%@ page import="java.io.IOException" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FabFlix - Login</title>
</head>
<body>
	<jsp:useBean id="login" class="Helpers.Login"/>

	<form action="" method="POST">
		Email: <input type="text" name="email"><br>
		Password: <input type="password" name="password"><br>
		<input type="submit" value="Login">
	</form>

	<%
	// Get login info
	String email = request.getParameter("email");
	String pass = request.getParameter("password");

	// Return parameter is a URL provided to redirect to after login
	String _return = request.getParameter("return");

	if(email != null && pass != null) {
		// Complain if insufficient info
		if(email.equals("") || pass.equals(""))
			out.println("Please provide both username and password.");
		else {
			// Send info to Login.java to query database
			User u = login.userLogin(email, pass);
			// Complain if incorrect credentials
			if(u == null) {
				out.println("Incorrect username or password.");
			}
			else {
				// Cache user in the session
				session.setAttribute("user", u);
				try {
					// Go to index if no return URL found
					if(_return == null)
						response.sendRedirect("index.jsp");
					else
						response.sendRedirect(_return);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	%>
</body>
</html>