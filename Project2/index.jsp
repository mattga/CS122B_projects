
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.sql.*"%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Test Java Servlet</title>
</head>
<body>
	<form action="" method="POST">
		E-mail: <input type="text" name="email"><br>
		Password: <input type="password" name="password"><br>
		<input type="submit" value="Login">
	</form>

	<%
	String email = request.getParameter("email");
	String pass = request.getParameter("password");

	if(email.equals("") || pass.equals(""))
		out.println("Please provide both username and password.");

	// TODO: Implement with connection pooling
	try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?user=moviedb&password=tiger");
		Statement statement = conn.createStatement();

		ResultSet rs = statement.executeQuery("SELECT * FROM customers WHERE email='" + email + "' AND password='" + pass + "'");

		if(!rs.first())
			out.println("Incorrect username or password.");
		else
			out.println("Welcome " + rs.getString("first_name") + " " + rs.getString("last_name") + "!");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	%>
</body>
</html>