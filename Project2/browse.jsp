<%@ page import="Types.*" %>
<%@ page import="Helpers.*" %>
<%@ page import="java.net.URLEncoder" %>
<% 
	// used inside _head* jsp file
	String document_title = "Browse"; 
%>
<%@ include file="_template/_head.jsp" %>

	<div class="col-md-12">
		<h1>Browse</h1>
	</div>

	<jsp:useBean id="movies" class="Helpers.Movies"/>	
	<%
	Movie[] res = null;

	// Get genre value... If not provided, assign empty string to display all movies.
	String genre = request.getParameter("genre");
	String title = request.getParameter("title");
	String perpage = request.getParameter("perpage");
	String _page = request.getParameter("page");
	String reuse = request.getParameter("reuse");
	String filter = request.getParameter("filter");

	// Reuse cached movie list, otherwise pull from database. Build URL for navigating through pages
	String url = "browse.jsp";
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
	if(perpage == null || !perpage.matches("8|16|32|64|128"))
		perpage = "8";
	if(_page == null || !_page.matches("[0-9]*") || Integer.parseInt(_page) < 1)
		_page = "1";

	int pageSize = Integer.parseInt(perpage);
	int pageNum = Integer.parseInt(_page);

	if(res != null) {
		int moviesNum = res.length;

		if(moviesNum == 0){
		%>
			<h3>No Movies To Display.</h3>
		<% } else { %>
			<!-- Movies Table -->
			<%@ include file="partial/movie_table.jsp" %>
		<% } %>
	<% } else { %>
		<!-- Genre List -->
		<%@ include file="partial/genre_list.jsp" %>
	<% } %>
	</div>
	
<%@ include file="_template/_foot.jsp" %>
