<%@ page import="Types.Star" %>
<html lang="en">
<head>
	<jsp:useBean id="stars" class="Helpers.Stars"/>

	<meta charset="UTF-8">
	<title>Star - 
		<%
		String id = request.getParameter("id");

		Star s = null;
		if(id != null && id.matches("[0-9]*"))
			s = stars.getStar(Integer.parseInt(id));

		if(s != null)
			out.println(s.first_name + " " + s.last_name);
		%>
	</title>
</head>
<body>
	<h1>
		<%
		if(s != null) {
			out.println(s.first_name + " " + s.last_name);
			out.println("<table border=\"0\" >");
			out.println("	<tr>");
			if(s.photo_url.equals(""))
				out.println("		<td valign=\"top\"><img src=\"images/default_profile_image.jpg\" width=\"120\" height=\"120\"></td>");
			else
				out.println("		<td valign=\"top\"><img src=\"" + s.photo_url + "\" onerror=\"this.src=\'images/default_profile_image.jpg\'\" width=\"85\" height=\"120\"></td>");
			out.println("		<td>");
			out.println(" 			<table border=\"0\">");
			out.println("				<tr>");
			out.println("					<td>" + s.id + "</td>");
			out.println("					<td><b>" + s.first_name + " " + s.last_name + "</b></td>");
			out.println("				</tr>");
			out.println("				<tr>");
			out.println("					<td>Date of Birth: </td>");
			out.println("					<td>" + s.dob + "</td>");
			out.println("				</tr>");
			out.println("				<tr>");
			out.println("					<td valign=\"top\">Movies: </td>");
			String str = "";
			for(int i = 0; i < s.movies.length; i++)
				str += s.movies[i].title + "<br>";
			out.println("					<td>" + str + "</td>");
			out.println("				</tr>");	
			out.println("			</table>");
			out.println("		</td>");
			out.println("	</tr>");
			out.println("</table><br>");
		} else
			out.println("Star not found.");
		%>
	</h1>
	
</body>
</html>