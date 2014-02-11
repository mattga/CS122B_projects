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
<%
    
    String setFilter = (filter == null) ? "" : ("&filter="+ filter);

    String titleSort = "";
    String yearSort = "";
    String sortField = request.getParameter("sortBy");
    int sortDirection = request.getParameter("sortDirection") == null ? 1 : Integer.parseInt(request.getParameter("sortDirection"));

    String currentSort = "&sortBy="+ sortField +"&sortDirection="+sortDirection;

    if(sortField != null) {
        if (sortField.equals("title"))
            titleSort =  url + "&reuse=1&page=" + (pageNum) + "&perpage=" + pageSize + setFilter+ "&sortBy=title&sortDirection="+ (sortDirection * -1);
        else 
            titleSort = url + "&reuse=1&page=" + (pageNum) + "&perpage=" + pageSize + setFilter+ "&sortBy=title&sortDirection=1";
        
        if (sortField.equals("year"))
            yearSort = url + "&reuse=1&page=" + (pageNum) + "&perpage=" + pageSize + setFilter+ "&sortBy=year&sortDirection="+ (sortDirection * -1);
        else 
            yearSort = url + "&reuse=1&page=" + (pageNum) + "&perpage=" + pageSize + setFilter+ "&sortBy=year&sortDirection=1";
    } else {
        titleSort = url + "&reuse=1&page=" + (pageNum) + "&perpage=" + pageSize + setFilter+ "&sortBy=title&sortDirection=1";
        yearSort = url + "&reuse=1&page=" + (pageNum) + "&perpage=" + pageSize + setFilter+ "&sortBy=year&sortDirection=1";
    }   
%>
<table class="table table-striped">
    <tbody>
        <tr>
            <th>Id</th>
            <th><a href="<%=titleSort%>">Title</a></th>
            <th><a href="<%=yearSort%>">Year</a></th>
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
        
        if (sortField != null && sortField.equals("title"))
            res = Movies.sortByTitle(res, sortDirection);
        else if (sortField != null && sortField.equals("year"))
            res = Movies.sortByYear(res, sortDirection);
        
        

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
                <a href="cart.jsp?from=browse&addid=<%=m.id%>" class="btn btn-lg btn-primary">Add to Cart</a>
            </td>
        </tr>
        <%
        } // end for;
        %>
    </tbody>
</table>    

<%
if(url != null) {
%>
    <div class="row">
        <ul class="pager">
            <%
            if(pageNum > 1)
            out.println("<li class=\"previous\"><a href=\"" + url + "&reuse=1&page=" + (pageNum-1) + "&perpage=" + pageSize + setFilter + currentSort +"\">&larr; Previous</a></li>");
            if(pageNum*pageSize <= moviesNum)
            out.println("<li class=\"next\"><a href=\"" + url + "&reuse=1&page=" + (pageNum+1) + "&perpage=" + pageSize + setFilter+ currentSort +"\">Next &rarr;</a></li>");
            %> 
        </ul>
    </div>

    <div class="row">
        <div class="col-md-12 text-center">
            <span>Movies per page:</span><br>            
            <ul class="pagination" style="margin-top:0px;">
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum %>&perpage=8<%=setFilter%><%=currentSort%>">8</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=16<%=setFilter%><%=currentSort%>">16</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=32<%=setFilter%><%=currentSort%>">32</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=64<%=setFilter%><%=currentSort%>">64</a>
                </li>
                <li>
                    <a href="<%=url%>&reuse=1&page=<%=pageNum%>&perpage=128<%=setFilter%><%=currentSort%>">128</a>
                </li>
            </ul>
        </div>
    </p>
    </div>
<%
}
%>