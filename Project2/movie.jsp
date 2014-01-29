<%@ page import="Types.Movie" %>
<%@ page import="Types.User" %>
<%@ page import="java.net.URLEncoder" %>
<html lang="en">
<head>
	<jsp:useBean id="movies" class="Helpers.Movies"/>

	<meta charset="UTF-8">
	<title>FabFlix - Movie: 
		<%
		String id = request.getParameter("id");

		Movie m = null;
		if(id != null && id.matches("[0-9]*"))
			m = movies.getMovie(Integer.parseInt(id));

		if(m != null)
			out.println(m.title);
		
		// Check session for logged in...
		String url = "star.jsp?id=" + id;
		User u = (User)session.getAttribute("user");
		if(u == null)
			response.sendRedirect("login.jsp?return=" + URLEncoder.encode(url,"UTF-8"));

		%>
	</title>
</head>
<body>
	<h1>
		<%
		if(m != null)
			out.println(m.title + " (" + m.year + ")");
		%>
	</h1>
	<%
	if(m != null) {
		out.println("<table border=\"0\" >");
		out.println("	<tr>");
		if(m.banner_url.equals(""))
			out.println("		<td><img src=\"images/default_banner.png\" width=\"85\" height=\"120\"></td>");
		else
			out.println("		<td><img src=\"" + m.banner_url + "\" onerror=\"this.src=\'images/default_banner.png\'\" width=\"85\" height=\"120\"></td>");
		out.println("		<td>");
		out.println(" 			<table border=\"0\">");
		out.println("				<tr>");
		out.println("					<td>Director: </td>");
		out.println("					<td>" + m.director + "</td>");
		out.println("				</tr>");
		out.println("				<tr>");
		out.println("					<td>Genres: </td>");
		String s = "";
		for(int i = 0; i < m.genres.length; i++)
			s += (i!=0?", ":"") + m.genres[i].name;
		out.println("					<td>" + s + "</td>");
		out.println("				</tr>");
		out.println("				<tr>");
		out.println("					<td>Starring: </td>");
		s = "";
		for(int i = 0; i < m.stars.length; i++)
			s += (i!=0?", ":"") + "<a href=\"star.jsp?id=" + m.stars[i].id + "\">" + m.stars[i].first_name + " " + m.stars[i].last_name + "</a>";
		out.println("					<td>" + s + "</td>");
		out.println("				</tr>");
		out.println("				<tr>");
		out.println("					<td>Trailer: </td>");
		if(!m.trailer_url.equals(""))
			out.println("					<td><a href=\"" + m.trailer_url + "\">Watch</a></td>");
		else
			out.println("					<td>N/A</td>");
		out.println("				</tr>");
		out.println("			</table>");
		out.println("		</td>");
		out.println("	</tr>");
		out.println("</table><br>");
	} else
		out.println("Movie not found.");
	%>
	
</body>
</html>