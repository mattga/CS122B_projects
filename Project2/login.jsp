<%@ page import="Types.User" %>
<%@ page import="java.io.IOException" %>
<%@ include file="_template/_head.login.jsp" %>
	<div class="col-md-4 col-md-offset-4">

	<jsp:useBean id="login" class="Helpers.Login"/>

	<form action="" method="POST">
		<div class="form-group">
		    <label for="email-address">Email address</label>
		    <input type="email" class="form-control" id="email-address" name="email" placeholder="Enter email">
		</div>
		<div class="form-group">
		    <label for="password-input">Password</label>
		    <input type="password" class="form-control" id="password-input" name="password" placeholder="Password">
		</div>

  		<button type="submit" class="btn btn-primary">Log In</button>
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
	</div>
<%@ include file="_template/_foot.jsp" %>