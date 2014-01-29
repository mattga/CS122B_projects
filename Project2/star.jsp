<%@ page import="Types.Star" %>
<%@ page import="Types.User" %>
<%@ page import="Helpers.Stars" %>
<%@ page import="java.net.URLEncoder" %>
<%
	// Check session for logged in...
	String id = request.getParameter("id");
	String url = "star.jsp?id=" + id;
	User u = (User)session.getAttribute("user");
	Stars stars = new Stars();

	if(u == null)
		response.sendRedirect("login.jsp?return=" + URLEncoder.encode(url,"UTF-8"));

	// used inside _head* jsp file
	String document_title = "Bio: "; 

	Star s = null;
	if(id != null && id.matches("[0-9]*"))
		s = stars.getStar(Integer.parseInt(id));

	// Determine Page title
	if(s != null) {
		document_title += s.first_name + " " + s.last_name;
	} else {	
		document_title = "Unknown Bio";
	}
%>
<%@ include file="_template/_head.jsp" %>
	<div class="col-md-12">

	<h1><% out.println( s != null ? s.first_name + " " + s.last_name : "Unknown"); %></h1>
	<%
	if (s != null) {
	%>
		<table border="0" >
			<tr>
				<%
				if (s.photo_url.equals("")) {
					out.println("<td valign=\"top\"><img src=\"images/default_profile_image.jpg\" width=\"105\" height=\"120\"></td>");
				
				} else {
					out.println("<td valign=\"top\"><img src=\""+s.photo_url+"\" onerror=\"this.src='images/default_profile_image.jpg'\" width=\"105\" height=\"120\"></td>");
				}
				%>
				<td>
		 			<table border="0">
						<tr>
							<td>Date of Birth: </td>
							<td><%=s.dob%></td>
						</tr>
						<tr>
							<td valign="top">Movies: </td>
								<%
								String str = "";
								for(int i = 0; i < s.movies.length; i++)
									str += "<a href=\"movie.jsp?id=" + s.movies[i].id + "\">" + s.movies[i].title + "</a><br>";
								%>
							<td><%=str%></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	<%
	} else { %>
		<p class="alert alert-danger">
			<b>Sorry.</b> The Star could not be found.
		</p>
	<%
	}
	%>
	
</div>
<%@ include file="_template/_foot.jsp" %>
