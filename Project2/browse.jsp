<%@ page import="Types.Movie" %>
<%@ page import="Types.User" %>
<%@ page import="java.net.URLEncoder" %>
<% 
	// used inside _head* jsp file
	String document_title = "Browse"; 
%>
<%@ include file="_template/_head.jsp" %>

	<div class="col-md-12">

	<h1>Movie List</h1>
	
	<% 
		// All Database Related Logic Abstracted out into Helper Class
		// Java Class is not free of HTML.
	%>
	<jsp:useBean id="movies" class="Helpers.Movies"/>
	
	<%
	Movie[] res = null;

	// Get genre value... If not provided, assign empty string to display all movies.
	String genre = request.getParameter("genre");
	String title = request.getParameter("title");
	String perpage = request.getParameter("perpage");
	String _page = request.getParameter("page");
	String reuse = request.getParameter("reuse");

	// Reuse cached movie list, otherwise pull from database. Build URL for navigating through pages
	String url = null;
	if(genre != null) {
		if(reuse != null && reuse.equals("1")) {
			res = (Movie[])session.getAttribute("browseMovieList");
		}
		else {
			res = movies.getAllMoviesByGenre(genre);
			session.setAttribute("browseMovieList", res);
		}
		url = "browse.jsp?genre=" + genre;
	} else if(title != null) {
		if(reuse != null && reuse.equals("1")) {
			res = (Movie[])session.getAttribute("browseMovieList");
		}
		else {
			res = movies.getAllMoviesByTitle(title);
			session.setAttribute("browseMovieList", res);
		}
		url = "browse.jsp?title=" + title;
	}

	// Check session for logged in...
	User u = (User)session.getAttribute("user");
	if(u == null)
		response.sendRedirect("login.jsp?return=" + URLEncoder.encode(url,"UTF-8"));

	// Error check page number and size
	if(perpage == null || !perpage.matches("5|10|25|50|100"))
		perpage = "10";
	if(_page == null || !_page.matches("[0-9]*") || Integer.parseInt(_page) < 1)
		_page = "1";

	int pageSize = Integer.parseInt(perpage);
	int pageNum = Integer.parseInt(_page);

	if(res != null) {
		int moviesNum = res.length;
		if(moviesNum == 0)
			out.println("No Movies<br>");

		// Iterate simple array object...
		for (int mi = (pageNum-1)*pageSize; mi < moviesNum && mi < pageNum*pageSize; mi++) {
			Movie m = res[mi];
			out.println("" + (mi+1) + ".<br><table border=\"0\">");
			out.println("	<tr>");
			if(m.banner_url.equals(""))
				out.println("		<td><img src=\"images/default_banner.png\" width=\"85\" height=\"120\"></td>");
			else
				out.println("		<td><img src=\"" + m.banner_url + "\" onerror=\"this.src=\'images/default_banner.png\'\" width=\"85\" height=\"120\"></td>");
			out.println("		<td>");
			out.println(" 			<table border=\"0\">");
			out.println("				<tr>");
			out.println("					<td>#" + m.id + "&nbsp;</td>");
			out.println("					<td><b><a href=\"movie.jsp?id=" + m.id + "\">" + m.title + " (" + m.year + ")</a></b></td>");
			out.println("				</tr>");
			out.println("				<tr>");
			out.println("					<td>Director:&nbsp;</td>");
			out.println("					<td>" + m.director + "</td>");
			out.println("				</tr>");
			out.println("				<tr>");
			out.println("					<td>Genres:&nbsp;</td>");
			String s = "";
			for(int i = 0; i < m.genres.length; i++)
				s += (i!=0?", ":"") + "<a href=\"browse.jsp?genre=" + m.genres[i].name + "\">" + m.genres[i].name + "</a>";
			out.println("					<td><table style=\"width: 80%\" cellspacing=\"0\"><tr><td>" + s + "</td></tr></table></td>");
			out.println("				</tr>");
			out.println("				<tr>");
			out.println("					<td>Starring:&nbsp;</td>");
			s = "";
			for(int i = 0; i < m.stars.length; i++)
				s += (i!=0?", ":"") + "<a href=\"star.jsp?id=" + m.stars[i].id + "\">" + m.stars[i].first_name + " " + m.stars[i].last_name + "</a>";
			out.println("					<td><table style=\"width: 80%\" cellspacing=\"0\"><tr><td>" + s + "</td></tr></table></td>");
			out.println("				</tr>");
			out.println("				<tr>");
			out.println("					<td>Cost:&nbsp;</td>");
			out.println("					<td>");
			out.println("						<table cellspacing=\"0\">");
			out.println("							<tr>");
			out.println("								<td>$9.99&nbsp;&nbsp;</td>");
			out.println("								<td><a href=\"cart.jsp?from=browse&addindex=" + mi + "\">Add to Cart</a></td>");
			out.println("							</tr>");
			out.println("						</table>");
			out.println("					</td>");
			out.println("				</tr>");	
			out.println("			</table>");
			out.println("		</td>");
			out.println("	</tr>");
			out.println("</table>");
			out.println("<br>");
		}
		if(url != null) {
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
	} else {
		out.println("No results<br>");
	}
	%>
	<form>
	</div>
	
<%@ include file="_template/_foot.jsp" %>
