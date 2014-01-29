<%@ page import="Types.ShoppingCart" %>
<%@ page import="Types.User" %>
<%@ page import="Types.Movie" %>
<%@ page import="java.net.URLEncoder" %>
<% 
	// used inside _head* jsp file
	String document_title = "Cart"; 
%>
<%@ include file="_template/_head.jsp" %>
	<div class="col-md-12">

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
		
	%>
	<table class="table table-striped">
		<tbody>
			<tr>
				<th>Movie</th>
				<th>Price</th>
				<th>Quantity</th>
			</tr>

			<%
			// Iterate thorugh cart and display contents.
			for(Movie m : movieCart.getCart()) {
			%>
			<tr>
				<td><a href="movie.jsp?id=<%=m.id%>"><%=m.title%>  (<%=m.year%>)</td>
				<td>$9.99</td>
				<td>
					<form action="" method="GET">
						<input type="hidden" name="id" value="<%=m.id %>">
						<input type="number" name="quantity" value="<%=m.cartQuantity%>">
						<input tpye="submit" name="action" value="Update">
						<input tpye="submit" name="action" value="Remove">
					</form>
				</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table><br>
	<%
	out.println("Total: " + movieCart.getTotal());
	%>

	</div>
<%@ include file="_template/_foot.jsp" %>