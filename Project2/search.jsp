<%@ page import="Types.Movie" %>
<html>
<head>
	         <title>FabFlix - Search Movies         </title>
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
	String title = request.getParameter("title");
	String year = request.getParameter("year");
	String director = request.getParameter("director");
	String star_fn = request.getParameter("starfn");
	String star_ln = request.getParameter("starln");
	String perpage = request.getParameter("perpage");
	String _page = request.getParameter("page");
	String reuse = request.getParameter("reuse");

	Movie[] res = null;

	// Reuse cached movie list, otherwise pull from database
	if(reuse != null && reuse.equals("1")) {
		res = (Movie[])session.getAttribute("searchMovieList");
	}
	else {
		res = movies.getAllMoviesBySearch(title,year,director,star_fn,star_ln);
		session.setAttribute("searchMovieList", res);
	}

	// Error check page number and size
	if(perpage == null || !perpage.matches("5|10|25|50|100"))
		perpage = "10";
	if(_page == null || !_page.matches("[0-9]*") || Integer.parseInt(_page) < 1)
		_page = "1";

	// Build url for navigating through pages. Probably a much easier way to do this...
	String url = "search.jsp?logic=avoid"
					+ (title != null ? "&title=" + title : "")
					+ (year != null ? "&year=" + year : "")
					+ (director != null ? "&director=" + director : "")
					+ (star_fn != null ? "&starfn=" + star_fn : "")
					+ (star_ln != null ? "&starln=" + star_ln : "");

	int pageSize = Integer.parseInt(perpage);
	int pageNum = Integer.parseInt(_page);

	if(res != null) {
		int moviesNum = res.length;
		if(moviesNum == 0)
			out.println("No Movies");

		// Iterate simple array object...
		for (int mi = (pageNum-1)*pageSize; mi < moviesNum && mi < pageNum*pageSize; mi++) {
			Movie m = res[mi];
			out.println("" + (mi+1) + ".<br><table border=\"0\" >");
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
				s += (i!=0?", ":"") + "<a href=\"star.jsp?id=" + m.stars[i].id + "\">" + m.stars[i].first_name + " " + m.stars[i].last_name + "</a>";
			out.println("					<td>" + s + "</td>");
			out.println("				</tr>");	
			out.println("			</table>");
			out.println("		</td>");
			out.println("	</tr>");
			out.println("</table><br>");
		}
		if(pageNum > 1)
			out.println("<a href=\"" + url + "&reuse=1&page=" + (pageNum-1) + "&perpage=" + pageSize + "\">&lt;</a>");
		if(pageNum*pageSize <= moviesNum)
			out.println("<a href=\"" + url + "&reuse=1&page=" + (pageNum+1) + "&perpage=" + pageSize + "\">&gt;</a>");
		out.print("    Movies per page: <a href=\"" + url + "&reuse=1&page=" + pageNum + "&perpage=5\">5</a> | ");
		out.print("<a href=\"" + url + "&reuse=1&page=" + pageNum + "&perpage=10\">10</a> | ");
		out.print("<a href=\"" + url + "&reuse=1&page=" + pageNum + "&perpage=25\">25</a> | ");
		out.print("<a href=\"" + url + "&reuse=1&page=" + pageNum + "&perpage=50\">50</a> | ");
		out.println("<a href=\"" + url + "&reuse=1&page=" + pageNum + "&perpage=100\">100</a>");
	}
	%>
	<body>
</html>
