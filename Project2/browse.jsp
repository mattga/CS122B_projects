<%@ page import="Types.Movie" %>
<html>
	<head>
		<title>Browse by Genre</title>
	</head>
<body>
	<h1>Some Action Movies</h1>
	
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

		if(res != null)
			if(res.length == 0)
				out.println("No Movies");
			// Iterate simple array object...
			for (Movie m : res) {
				// out.println("<b>"+ m.title +"</b><br>"+ m.director +"<br><br>");
		
				out.println("<table border=\"0\">");
				out.println("	<tr>");
				out.println("		<td>ID</td>");
				out.println("		<td>" + m.id + "</td>");
				out.println("	</tr>");
				out.println("	<tr>");
				out.println("		<td>Title</td>");
				out.println("		<td>" + m.title + "</td>");
				out.println("	</tr>");
				out.println("	<tr>");
				out.println("		<td>Year</td>");
				out.println("		<td>" + m.year + "</td>");
				out.println("	</tr>");
				out.println("	<tr>");
				out.println("		<td>Director</td>");
				out.println("		<td>" + m.director + "</td>");
				out.println("	</tr>");
				out.println("	<tr>");
				out.println("		<td>Genres</td>");
				String s = "";
				for(int i = 0; i < m.genres.length; i++)
					s += (i!=0?", ":"") + m.genres[i];
				out.println("		<td>" + s + "</td>");
				out.println("	</tr>");
				out.println("	<tr>");
				out.println("		<td>Stars</td>");
				s = "";
				for(int i = 0; i < m.stars.length; i++)
					s += (i!=0?", ":"") + m.stars[i].first_name + " " + m.stars[i].last_name;
				out.println("		<td>" + s + "</td>");
				out.println("	</tr>");	
				out.println("</table><br>");
			}
	%>
<body>
</html>

