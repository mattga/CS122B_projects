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

<div class="col-md-4 col-md-offset-4">
    <form action="checkout.jsp" method="POST">
        <input type="hidden" name="submitted" value="true"/>
        <div class="form-group">
            <label for="creditcard">Credit Card Number</label>
            <input type="text" class="form-control" id="creditcard" name="creditcard" placeholder="XXXXXX"/>
        </div>
        <div class="form-group">
            <label for="expiration">Expiration</label>
            <input type="text" class="form-control" id="expiration" name="expiration" placeholder="1999-12-31"/>
        </div>
        <div class="form-group">
            <label for="first_name">First Name</label>
            <input type="text" class="form-control" id="first_name" name="first_name" placeholder="First Name"/>
        </div>
        <div class="form-group">
            <label for="last_name">Last Name</label>
            <input type="text" class="form-control" id="last_name" name="last_name" placeholder="Last name"/>
        </div>
        <button type="submit" id="submit" class="btn btn-primary">Finish &amp; Pay</button>
    </form>
</div>
