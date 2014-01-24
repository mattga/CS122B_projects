<%@ page import="Types.Movie" %>
<html>
	<head>
		<title>FabFlix - Browse Movies</title>
	</head>
<body>
	<h1>Movie List</h1>
	
	<% 
		// All Database Related Logic Abstracted out into Helper Class
		// Java Class is not free of HTML.
	%>
	<jsp:useBean id="movies" class="Helpers.Movies"/>
	
	<%
	// Get genre value... If not provided, assign empty string to display all movies.
	String genre = request.getParameter("genre");
	if(genre == null)
		genre = "";

	String title = request.getParameter("title");

	Movie[] res = null;

	if(genre != "")
		res = movies.getAllMoviesByGenre(genre);
	else if(title != "")
		res = movies.getAllMoviesByTitle(title);

	if(res != null) {
		if(res.length == 0)
			out.println("No Movies");

		// Iterate simple array object...
		for (Movie m : res) {
			out.println("<table border=\"0\" >");
			out.println("	<tr>");
			if(m.banner_url.equals(""))
				out.println("		<td><img src=\"images/default_banner.png\" width=\"85\" height=\"120\"></td>");
			else
				out.println("		<td><img src=\"" + m.banner_url + "\" onerror=\"this.src=\'images/default_banner.png\'\" width=\"85\" height=\"120\"></td>");
			out.println("		<td>");
			out.println(" 			<table border=\"0\">");
			out.println("				<tr>");
			out.println("					<td>" + m.id + "</td>");
			out.println("					<td><b>" + m.title + " (" + m.year + ")</b></td>");
			out.println("				</tr>");
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
				s += (i!=0?", ":"") + m.stars[i].first_name + " " + m.stars[i].last_name;
			out.println("					<td>" + s + "</td>");
			out.println("				</tr>");	
			out.println("			</table>");
			out.println("		</td>");
			out.println("	</tr>");
			out.println("</table><br>");
		}
	}
	%>
<body>
</html>
