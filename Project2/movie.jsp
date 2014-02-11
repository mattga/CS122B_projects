<%@ page import="Types.Movie" %>
<%@ page import="Types.User" %>
<%@ page import="java.net.URLEncoder" %>
<jsp:useBean id="movies" class="Helpers.Movies"/>
<%
	String id = request.getParameter("id");
	Movie m = null;
	if(id != null && id.matches("[0-9]*"))
		m = movies.getMovie(Integer.parseInt(id));
	if(m != null)
		out.println(m.title);	

	// Check session for logged in...
	String url = "movie.jsp?id=" + id;
	User u = (User)session.getAttribute("user");
	if(u == null)
		response.sendRedirect("login.jsp?return=" + URLEncoder.encode(url,"UTF-8"));
	
	// Determine Page title
	String document_title = "Movie Not Found";
	if(m != null) {
		document_title += m.title + " - " + m.year;
	} 
%>
<%@ include file="_template/_head.jsp" %>
    <div class="col-md-12">
	<h1>
		<%
			out.println( m == null ? "No Movie Not Found" : m.title + " (" + m.year + ")");
		%>
	</h1>
	<%
	if(m != null) {
		String imageSrc = m.banner_url.equals("") ? "images/default_banner.png" : m.banner_url;
	%>
		<div class="col-sm-12 col-md-12">
			    <div class="thumbnail">
			      <img src="<%=imageSrc%>" width="94" height="140" onerror="this.src='images/default_banner.png'">
			      <div class="caption">
			        <!--h3 class="text-center" style="min-height:52px;"><a href="movie.jsp?id=<%=m.id%>"><%=m.title%> (<%=m.year%>)</a></h3-->
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
						<dt>Trailer: <dt>
						<dd><%
							if(!m.trailer_url.equals(""))
								out.println("<a href=\""+ m.trailer_url +"\">Watch Trailer</a>");
							else 
								out.println("No Trailer Available");
							%>
						<dd>
			        </dl>
			        <h3 class="text-center">$9.99</h3>
			        <p class="text-center">
			        	<a href="cart.jsp?from=browse&addid=<%=m.id%>" class="btn btn-lg btn-primary">Add to Cart</a>
			        </p>
			      </div>
			    </div>
			  </div>
		<%
	} else
		out.println("Movie not found.");
	%>
    </div>
<%@ include file="_template/_foot.jsp" %>


