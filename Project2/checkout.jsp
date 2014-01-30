<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="Helpers.MySQL"%>
<%@ page import="Types.ShoppingCart" %>
<%@ page import="Types.User" %>
<%@ page import="Types.Movie" %>
<%@ page import="java.net.URLEncoder" %>
<%      
    //Check session for logged in...
    User u = (User)session.getAttribute("user");
    if(u == null)
        response.sendRedirect("login.jsp");

    // No Shopping Cart, just send the user to login screen.
    ShoppingCart movieCart = (ShoppingCart)session.getAttribute("movie_cart");
    if(movieCart == null)
        response.sendRedirect("index.jsp");

    // used inside _head* jsp file
    String document_title = "Cart"; 
%>
<%@ include file="_template/_head.jsp" %>
    <div class="col-md-12">
        <h1>Checkout</h1>
        <% if (request.getParameter("submitted") != null) { %>
            <%
            String cc = request.getParameter("creditcard");
            String ex = request.getParameter("expiration");
            String fn = request.getParameter("first_name");
            String ln = request.getParameter("last_name");
			
            PreparedStatement ps = MySQL.getInstance().getConnection().prepareStatement("SELECT * FROM `creditcards` WHERE `id` = ? AND `first_name` = ? AND `last_name` = ? AND `expiration` = ?");
            ps.setString(1, cc);
            ps.setString(2, fn);
            ps.setString(3, ln);
            ps.setDate(4, java.sql.Date.valueOf(ex));
            
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
            	%>
                <h3>Success</h3>
                <p>Your Card Has been charched for <%=movieCart.getItemCount()%> Movies a total of <%=movieCart.getTotal()%></p>
                <%
        		Calendar calendar = new GregorianCalendar();
                String date = calendar.get(Calendar.YEAR) +"-" + (calendar.get(Calendar.MONTH) + 1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH);
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO  `moviedb`.`sales` (`id` ,`customer_id` ,`movie_id` ,`sale_date`) VALUES ");
                for (Movie m : movieCart.getCart())
                    query.append(String.format("(NULL ,  '%d',  '%d',  '%s'),", (((User)session.getAttribute("user")).id), m.id, date));
				
                // Eliminate the extra comma at the end of the query.                
                if (MySQL.getInstance().getConnection().createStatement().executeUpdate(query.substring(0, query.length()-1)) > 0)
                	out.println("<span class=\"alert alert-success\">Successfully Saved Your Transaction.</span>");
                else
                	out.println("<span class=\"alert alert-danger\">Error --Coult Not Save Your Transaction</span>");
                // Clear the cart.
                session.setAttribute("movie_cart", new ShoppingCart());
            } else { 
            	out.println("<span class=\"alert alert-danger\">Error -- Card Mismatch! Use the back button on your browser to return tot he previous screen and edit your information.</span>");
            }
            %>
        <% } else { %>
            <%@ include file="partial/checkout_information.jsp" %>
        <% } %>
    </div>
<%@ include file="_template/_foot.jsp" %>