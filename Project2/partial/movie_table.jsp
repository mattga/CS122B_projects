<%@page import="Helpers.Movies"%>
<div class="col-md-12 text-center">
    <ul class="pagination">
    <% for (char letter : ("#ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())) { %>
        <% if (letter == '#') { %>
            <li><a href="browse.jsp?title&filter=0"><%=letter%></a></li>
        <% } else { %>
            <li><a href="browse.jsp?title&filter=<%=letter%>"><%=letter%></a></li>
        <% } %>
    <% } %>
        <li><a href="browse.jsp?title">All</a></li>
    </ul>
</div>
<table class="table table-striped">
    <tbody>
        <tr>
            <th>Id</th>
            <th>Title</th>
            <th>Year</th>
            <th>Director</th>
            <th>Genres</th>
            <th>Stars</th>
            <th>Purchase</th>
        </tr>
        <%
        if (filter != null) {
        	res = Movies.filter(filter, res);
        	moviesNum = res.length;
        }
        for (int mi = (pageNum-1)*pageSize; (mi < moviesNum && mi < pageNum*pageSize); mi++) {
            Movie m = res[mi];
            String imageSrc = m.banner_url.equals("") ? "images/default_banner.png" : m.banner_url;
        %>
        <tr>
            <td>
                <%=m.id%>
            </td>   
            <td>
                <a href="movie.jsp?id=<%=m.id%>"><%=m.title%></a>
            </td> 
            <td>
                <%=m.year%>
            </td>
            <td>
                <%=m.director%>
            </td>   
            <td>
                <%
                String s = "";
                for(int i = 0; i < m.genres.length; i++)
                s += (i!=0?", ":"") + "<a href=\"browse.jsp?genre=" + m.genres[i].name + "\">" + m.genres[i].name + "</a>";
                %>
                <%=s%>
            </td>
            <td>
                <%
                s = "";
                for(int i = 0; i < m.stars.length; i++)
                s += (i!=0?", ":"") + "<a href=\"star.jsp?id=" + m.stars[i].id + "\">" + m.stars[i].first_name + " " + m.stars[i].last_name + "</a>";
                %>
                <%=s%>
            </td>
            <td>
                <a href="cart.jsp?from=browse&addindex=<%=mi%>" class="btn btn-lg btn-primary">Add to Cart</a>
            </td>
        </tr>
        <%
        } // end for;
        %>
    </tbody>
</table>    

<%
if(url != null) {
    String setFilter = filter == null ? "" : "&filter="+filter;
%>
    <div class="row">
        <ul class="pager">
            <%
            if(pageNum > 1)
            out.println("<li class=\"previous\"><a href=\"" + url + "&reuse=1&page=" + (pageNum-1) + "&perpage=" + pageSize + setFilter + "\">&larr; Previous</a></li>");
            if(pageNum*pageSize <= moviesNum)
            out.println("<li class=\"next\"><a href=\"" + url + "&reuse=1&page=" + (pageNum+1) + "&perpage=" + pageSize + setFilter+ "\">Next &rarr;</a></li>");
            %> 
        </ul>
    </div>

    <div class="row">
        <div class="col-md-12 text-center">
            <span>Movies per page:</span><br>            
            <ul class="pagination" style="margin-top:0px;">
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum %>&perpage=8<%=setFilter%>">8</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=16<%=setFilter%>">16</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=32<%=setFilter%>">32</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=64<%=setFilter%>">64</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=128<%=setFilter%>">128</a>
                </li>
            </ul>
        </div>
    </p>
    </div>
<%
}
%>