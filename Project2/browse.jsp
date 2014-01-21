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
		// Iterate simple array object...
		for (Movie m : movies.getAllMoviesByGenre("Action")) {
			out.println("<b>"+ m.title +"</b><br>"+ m.director +"<br><br>");
		}
	%>
<body>
</html>

