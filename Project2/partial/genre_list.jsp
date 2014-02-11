<table class="table table-striped">
    <tbody>
        <tr>
            <th>Id</th>
            <th>Name</th>
        </tr>
        <%
        Genre[] genres = (new Genres()).getAllGenres();
        %>
        <% for (Genre m : genres) { %>
        <tr>
            <td>
                <%=m.genre_id%>
            </td>   
            <td>
                <a href="browse.jsp?genre=<%=m.name%>"><%=m.name%></a>
            </td> 
        </tr>
        <% } /* end for;*/ %>
    </tbody>
</table>    
