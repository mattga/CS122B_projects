<%@ page import="Types.ShoppingCart" %>
<%@ page import="Types.User" %>
<%@ page import="Types.Movie" %>
<%@ page import="java.net.URLEncoder" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>FabFlix - Shopping Cart
	</title>
</head>
<body>
	<h1>Shopping Cart</h1>
	<%
	// Get parameters
	String from = request.getParameter("from");
	String addIndex = request.getParameter("addindex");
	String id = request.getParameter("id");
	String quantity = request.getParameter("quantity");
	String action = request.getParameter("action");

	// Check parameters & build URL
	String url = "cart.jsp?logic=avoid";
	if(from != null)
		url += "&from=" + from;
	if(addIndex != null)
		url += "&addindex=" + addIndex;

	// Check session for logged in...
	User u = (User)session.getAttribute("user");
	if(u == null)
		response.sendRedirect("login.jsp?return=" + URLEncoder.encode(url,"UTF-8"));

	ShoppingCart movieCart = (ShoppingCart)session.getAttribute("movie_cart");

	if (movieCart == null)
		movieCart = new ShoppingCart();

	if(from != null && addIndex != null && addIndex.matches("[0-9]*")) {
		if(from.equals("browse")) {
			Movie[] mList = (Movie[])session.getAttribute("browseMovieList");
			if(mList != null) {
				int index = Integer.parseInt(addIndex);
				movieCart.addMovie(mList[index]);
			}
		} else if(from.equals("search")) {
			Movie[] mList = (Movie[])session.getAttribute("searchMovieList");
			if(mList != null) {
				int index = Integer.parseInt(addIndex);
				movieCart.addMovie(mList[index]);
			}
		}
	}

	if(action != null && id != null && id.matches("[0-9]*") && quantity != null && quantity.matches("[0-9]*") && action.equals("update"))
		movieCart.updateQuantity(Integer.parseInt(id), Integer.parseInt(quantity));
	else if (action != null && id != null && id.matches("[0-9]*") && action.equals("remove"))
		movieCart.removeMovie(Integer.parseInt(id));
		
	out.println("<table>");
	out.println("	<tr>");
	out.println("		<td><b>Movie</b></td>");
	out.println("		<td><b>Price</b></td>");
	out.println("		<td><b>Quantity</b></td>");
	out.println("	</tr>");
	for(Movie m : movieCart.getCart()) {
		out.println("	<tr>");
		out.println("		<td><a href=\"movie.jsp?id=" + m.id + ">" + m.title + " (" + m.year + ")</td>");
		out.println("		<td>$9.99</td>");
		out.println("		<td>");
		out.println("			<form action=\"\" method=\"GET\">");
		out.println("				<input type=\"hidden\" name=\"id\" value=\"" + m.id + "\">");
		out.println("				<input type=\"number\" name=\"quantity\" value=\"" + m.cartQuantity + "\">");
		out.println("				<input tpye=\"submit\" name=\"action\" value=\"Update\">");
		out.println("				<input tpye=\"submit\" name=\"action\" value=\"Remove\">");
		out.println("			</form>");
		out.println("		</td>");
		out.println("	</tr>");
	}
	out.println("</table><br>");
	out.println("Total: " + movieCart.getTotal());
	%>
</body>
</html>