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
	</div>

	<div class="col-md-12 text-center">
		<ul class="pagination">
	    <% for (char letter : ("#ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())) { %>
	        <li><a href="#letter-<%=letter%>"><%=letter%></a></li>
	    <% } %>
	    </ul>
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
	if(perpage == null || !perpage.matches("8|16|32|64|128"))
		perpage = "8";
	if(_page == null || !_page.matches("[0-9]*") || Integer.parseInt(_page) < 1)
		_page = "1";

	int pageSize = Integer.parseInt(perpage);
	int pageNum = Integer.parseInt(_page);

	if(res != null) {
		int moviesNum = res.length;
		if(moviesNum == 0)
			out.println("No Movies<br>");

		int col = 0;
		// Iterate simple array object...
		for (int mi = (pageNum-1)*pageSize; mi < moviesNum && mi < pageNum*pageSize; mi++) {
			Movie m = res[mi];
			String imageSrc = m.banner_url.equals("") ? "images/default_banner.png" : m.banner_url;
			col++;
			%>			
			<% if (col % 4 == 1) { 					
			%>
				<div class="row">	
			<% } %>	
			<div class="col-sm-4 col-md-3">
			    <div class="thumbnail">
			      <img src="<%=imageSrc%>" width="94" height="140" onerror="this.src='images/default_banner.png'">
			      <div class="caption">
			        <h3 class="text-center" style="min-height:52px;"><a href="movie.jsp?id=<%=m.id%>"><%=m.title%> (<%=m.year%>)</a></h3>
			        <dl style="min-height:150px;">
						<dt>Director: </dt>
						<dd><%=m.director%></dd>
						<%
						String s = "";
						for(int i = 0; i < m.genres.length; i++)
							s += (i!=0?", ":"") + "<a href=\"browse.jsp?genre=" + m.genres[i].name + "\">" + m.genres[i].name + "</a>";
						%>
						<dt>Genres: </dt>
						<dd><%=s%></dd>
						<%
						s = "";
						for(int i = 0; i < m.stars.length; i++)
							s += (i!=0?", ":"") + "<a href=\"star.jsp?id=" + m.stars[i].id + "\">" + m.stars[i].first_name + " " + m.stars[i].last_name + "</a>";
						%>
						<dt>Starring: <dt>
						<dd><%=s%><dd>
			        </dl>
			        <h3 class="text-center">$9.99</h3>
			        <p class="text-center">
			        	<a href="cart.jsp?from=browse&addindex=<%=mi%>" class="btn btn-lg btn-primary">Add to Cart</a>
			        </p>
			      </div>
			    </div>
			  </div>
			<% if (col % 4 == 0) { 
				col = 0;
			%>
				</div>	
			<% } %>	
			<%

		} // end for;
		%>
		
		<%
		if(url != null) {
		%>
		<div class="row">
			<ul class="pager">
			<%
				if(pageNum > 1)
					out.println("<li class=\"previous\"><a href=\"" + url + "&reuse=1&page=" + (pageNum-1) + "&perpage=" + pageSize + "\">&larr; Previous</a></li>");
				if(pageNum*pageSize <= moviesNum)
					out.println("<li class=\"next\"><a href=\"" + url + "&reuse=1&page=" + (pageNum+1) + "&perpage=" + pageSize + "\">Next &rarr;</a></li>");
			%> 
			</ul>
		</div>

		<div class="row">
				<div class="col-md-12 text-center">
					<span>Movies per page:</span><br>
					<ul class="pagination" style="margin-top:0px;">
				        <li>
							<a href="<%=url%>&reuse=1&page=<%=pageNum %>&perpage=8">8</a>
				        </li>
				        <li>
				        	<a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=16">16</a>
				        </li>
				        <li>
							<a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=32">32</a>
				        </li>
				        <li>
				        	<a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=64">64</a>
				        </li>
				        <li>
				        	<a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=128">128</a>
				        </li>
				    </ul>
				</div>
			</p>
		</div>
			<%
		}
	} else {
		out.println("No results<br>");
	}
	%>
	<form>
	</div>
	
<%@ include file="_template/_foot.jsp" %>
