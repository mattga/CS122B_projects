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
	String addId = request.getParameter("addid");
	String id = request.getParameter("id");
	String quantity = request.getParameter("quantity");
	String action = request.getParameter("action");

	// Check parameters & build URL
	String url = "cart.jsp?logic=avoid";
	if(from != null)
		url += "&from=" + from;
	if(addIndex != null)
		url += "&addindex=" + addIndex;
	if(addId != null)
		url += "&addId="+ addId;
	
	//Check session for logged in...
		User u = (User)session.getAttribute("user");
		if(u == null)
			response.sendRedirect("login.jsp?return=" + URLEncoder.encode(url,"UTF-8"));

	ShoppingCart movieCart = (ShoppingCart)session.getAttribute("movie_cart");

	if (movieCart == null)
		movieCart = new ShoppingCart();

	if(from != null && ((addIndex != null && addIndex.matches("[0-9]*")) || (addId != null && addId.matches("[0-9]*"))) ) {
		if(from.equals("browse")) {
			Movie[] mList = (Movie[])session.getAttribute("browseMovieList");
			if(mList != null) {
				int index;
				
				// If we are not gien an index, then we must have an id
				if (addIndex == null) {
					index = Integer.parseInt(addId);
					for (Movie m : mList) {
						if (m.id == index)
							movieCart.addMovie(m);
					}
				} else { // if we have an index, then we must have been given an index.
					index = Integer.parseInt(addIndex);
					movieCart.addMovie(mList[index]);	
				} 
			}
		} else if(from.equals("search")) {
			Movie[] mList = (Movie[])session.getAttribute("searchMovieList");
			if(mList != null) {
				int index = Integer.parseInt(addIndex);
				movieCart.addMovie(mList[index]);
			}
		}
	}

	if(action != null && id != null && id.matches("[0-9]*") && quantity != null && quantity.matches("[0-9]*") && action.toLowerCase().equals("update")) {
		movieCart.updateQuantity(Integer.parseInt(id), Integer.parseInt(quantity));
	} else if (action != null && id != null && id.matches("[0-9]*") && action.toLowerCase().equals("remove")) {
		movieCart.removeMovie(Integer.parseInt(id));
	}
	
	// Save Any Changes to the Cart.
	session.setAttribute("movie_cart", movieCart);
	%>
	<table class="table table-striped">
		<tbody>
			<tr>
				<th>#</th>
				<th>Movie</th>
				<th>Price</th>
				<th>Total</th>
				<th>Quantity</th>
			</tr>

			<%
			int i = 0;
			// Iterate thorugh cart and display contents.
			for(Movie m : movieCart.getCart()) {
			%>
			<tr>
				<td><%=++i%></td>
				<td><a href="movie.jsp?id=<%=m.id%>"><%=m.title%>  (<%=m.year%>)</td>
				<td style="color:#888">$9.99</td>
				<td><% out.println(m.cartQuantity * 9.99); %></td>
				<td>
					<form action="" method="GET">
						<input type="hidden" name="id" value="<%=m.id %>">
						<input type="number" name="quantity" value="<%=m.cartQuantity%>">
						<button class="btn btn-default" type="submit" name="action" value="Update"><span class="glyphicon glyphicon-refresh"></span></button>
						<button class="btn btn-default" type="submit" name="action" value="Remove"><span class="glyphicon glyphicon-remove"></span></button>
					</form>
				</td>
			</tr>
			<%
			}
			%>
			<tr>
				<th></th>
				<th>Total:</th>
				<th></th>
				<th>
				<%
					out.println(movieCart.getTotal());
				%>
				</th>
				<th>
				<%
					out.println(movieCart.getItemCount());
				%>
				</th>
			</tr>
		</tbody>
	</table>
	</div>
<%@ include file="_template/_foot.jsp" %>